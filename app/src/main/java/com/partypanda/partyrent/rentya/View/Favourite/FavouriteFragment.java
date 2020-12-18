package com.partypanda.partyrent.rentya.View.Favourite;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.LoadingScreen;
import com.partypanda.partyrent.rentya.Adapters.ResultAdapter;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.Model.SavedAd;
import com.partypanda.partyrent.rentya.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    private RecyclerView savedLists;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private FirebaseFirestore savedAdDB;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore rentalsDB;
    private ResultAdapter resultAdapter;
    private View view;
    final char KEY  = 'F';

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        this.view = view;
        rentalArrayList = new ArrayList<>();
        rentalIdList = new ArrayList<>();
        savedLists = view.findViewById(R.id.favourite_recycleview);
        savedLists.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rentalsDB = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        savedAdDB = FirebaseFirestore.getInstance();
        verifySavedAd();
        return view;

    }

    private void verifySavedAd(){
        savedAdDB.collection("SavedRentals").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){

                        SavedAd savedAd = d.toObject(SavedAd.class);
                        if(savedAd.getUserId().equals(userId)){
                            uploadSavedRentals(savedAd.getRentalId());
                        }

                    }
                }
            }
        });
    }
    private void uploadSavedRentals(final String rentalId){

        Activity activity = (Activity) view.getContext();
        final LoadingScreen loadingScreen = new LoadingScreen(activity);
        loadingScreen.startLoadingDialog();
        rentalsDB.collection("ListRental").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        String id = d.getId();
                        if(id.equals(rentalId)){
                            rentalIdList.add(id);
                            Rental rental = d.toObject(Rental.class);
                            rentalArrayList.add(rental);
                        }
                    }
                    loadingScreen.dismiss();
                    resultAdapter = new ResultAdapter(view.getContext(),rentalArrayList,rentalIdList,-1,KEY);
                    savedLists.setAdapter(resultAdapter);
                }
            }
        });
    }

}