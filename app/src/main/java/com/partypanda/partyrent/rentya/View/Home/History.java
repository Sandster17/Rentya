package com.partypanda.partyrent.rentya.View.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.LoadingScreen;
import com.partypanda.partyrent.rentya.Adapters.ResultAdapter;
import com.partypanda.partyrent.rentya.Model.AdClicked;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.R;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private FirebaseFirestore historyDB;
    private FirebaseFirestore rentalDB;
    private String userId;
    private FirebaseAuth auth;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private ResultAdapter resultAdapter;
    private RecyclerView historyList;
    private TextView back;
    final char KEY  = 'H';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyDB = FirebaseFirestore.getInstance();
        historyList = findViewById(R.id.history_recycleview);
        historyList.setLayoutManager(new LinearLayoutManager(this));
        rentalDB = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        rentalArrayList = new ArrayList<>();
        rentalIdList = new ArrayList<>();
        back = findViewById(R.id.back);

        final String fullname = getIntent().getStringExtra("fullname");
        final String photoUrl = getIntent().getStringExtra("photoUrl");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(History.this, UserAccount.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("photoUrl",photoUrl);
                startActivity(intent);
            }
        });
        if(auth.getCurrentUser()!=null){
            userId = auth.getCurrentUser().getUid();
            uploadData();
        }

    }
    private void uploadData(){
        final LoadingScreen loadingScreen = new LoadingScreen(this);
        loadingScreen.startLoadingDialog();
        historyDB.collection("UserHistory").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        AdClicked adClicked = d.toObject(AdClicked.class);
                        if(adClicked!=null&&adClicked.getUserId()!=null) {
                            if (adClicked.getUserId().equals(userId)) {
                                uploadRental(adClicked.getRentalId());
                            }
                        }
                    }
                    loadingScreen.dismiss();
                }
            }
        });
    }
    private void uploadRental(final String rentalId){
        rentalDB.collection("ListRental").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        String id = d.getId();
                        Rental rental = d.toObject(Rental.class);
                        if(id.equals(rentalId)){
                            rentalIdList.add(id);
                            rentalArrayList.add(rental);
                        }
                    }
                    resultAdapter = new ResultAdapter(getApplicationContext(),rentalArrayList,rentalIdList,-1,KEY);
                    historyList.setAdapter(resultAdapter);
                }
            }
        });
    }
}