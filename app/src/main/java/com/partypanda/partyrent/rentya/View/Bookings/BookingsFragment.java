package com.partypanda.partyrent.rentya.View.Bookings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.LoadingScreen;
import com.partypanda.partyrent.rentya.Adapters.ReservationAdapter;
import com.partypanda.partyrent.rentya.Model.Reservation;
import com.partypanda.partyrent.rentya.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingsFragment extends Fragment implements DatePickerDialog.OnDateSetListener  {

    private RecyclerView reservationRecycleview;
    private FirebaseAuth auth;
    private String userId;
    private FirebaseFirestore reservationsDB;
    private ArrayList<Reservation>  reservationArrayList;
    private View view;
    private TextView reservations;
    private EditText search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookings, container, false);
        this.view = view;
        reservationRecycleview = view.findViewById(R.id.reservation_recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        reservationRecycleview.setLayoutManager(layoutManager);
        auth = FirebaseAuth.getInstance();
        reservationsDB = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        reservationArrayList = new ArrayList<>();
        reservations = view.findViewById(R.id.num_reservations);
        search = view.findViewById(R.id.search_date);
        loadData();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        return view;
    }
    private void loadData(){

        Activity activity = (Activity) view.getContext();

        final LoadingScreen loadingScreen = new LoadingScreen(activity);
        loadingScreen.startLoadingDialog();

        reservationsDB.collection("Reservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        Reservation reservation = d.toObject(Reservation.class);
                        if(userId.equals(reservation.getUserId())){

                            reservationArrayList.add(reservation);
                        }
                    }
                    loadingScreen.dismiss();
                     ReservationAdapter adapter = new ReservationAdapter(view.getContext(),reservationArrayList,userId);
                    reservations.setText("" + adapter.getItemCount());
                    reservationRecycleview.setAdapter(adapter);
                }
            }
        });
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),R.style.MyTimePickerDialogTheme,this, Calendar.getInstance()
                .get(Calendar.YEAR),Calendar.getInstance()
                .get(Calendar.MONTH),Calendar.getInstance()
                .get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        search.setText(date);
    }
}