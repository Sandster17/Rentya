package com.partypanda.partyrent.rentya.View.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.LoadingScreen;
import com.partypanda.partyrent.rentya.Adapters.ResultAdapter;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.R;

import java.util.ArrayList;
import java.util.List;

public class ResultList extends AppCompatActivity {

    private TextView back;
    private RecyclerView resultLists;
    private FirebaseFirestore rentalsDB;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private ResultAdapter resultAdapter;
    private String type;
    private int people;
    private String term;
    private String uni;
    private String metro;
    final private char KEY = 'S';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);
        back = findViewById(R.id.back);
        rentalArrayList = new ArrayList<>();
        rentalIdList = new ArrayList<>();
        resultLists = findViewById(R.id.resultlist_recycleview);
        resultLists.setLayoutManager(new LinearLayoutManager(this));
        rentalsDB = FirebaseFirestore.getInstance();
        //-Select nearest university
        //-Select nearest metro-do not allow this option to be selected
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultList.this, MainActivity.class);
                startActivity(intent);
            }
        });
        uploadResults();
    }
    private boolean verifySearch(Rental rental){
        boolean verified = true;
        type = getIntent().getStringExtra("apartment_type");
        people = getIntent().getIntExtra("apartment_people",0);
        term = getIntent().getStringExtra("apartment_term");
        uni = getIntent().getStringExtra("apartment_uni");
        metro = getIntent().getStringExtra("apartment_metro");

        double max = getIntent().getDoubleExtra("apartment_max",0);
        double min = getIntent().getDoubleExtra("apartment_min",0);

        if(!type.equals(rental.getType())){
            verified = false;
        }
        if(people!=0){
            if(people!=rental.getGuests()){
                verified = false;
            }
        }
        if(!term.equals(rental.getTerm())){
            verified = false;
        }
        if(!uni.equals("Select nearest university")){

            if(!uni.equals(rental.getNearestUniversity())){
                verified = false;
            }
        }
        if(!metro.equals("Select nearest metro")) {

            if (!metro.equals(rental.getNearestMetro())) {
                verified = false;
            }
        }
        if(max!=0&&min!=0){
            if(max<rental.getFee()&&min>rental.getFee()){
                verified = false;
            }
        }
        return verified;
    }

    private void uploadResults(){

            final LoadingScreen loadingScreen = new LoadingScreen(ResultList.this);
            loadingScreen.startLoadingDialog();

        rentalsDB.collection("ListRental").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        Rental rental = d.toObject(Rental.class);
                        if(verifySearch(rental)){
                            String id = d.getId();
                            rentalIdList.add(id);
                            rentalArrayList.add(rental);
                        }
                    }
                    loadingScreen.dismiss();
                    if(rentalArrayList.size()!=0){
                        resultAdapter = new ResultAdapter(getApplicationContext(),rentalArrayList,rentalIdList,-1,KEY);
                        resultLists.setAdapter(resultAdapter);
                    }else{
                        Intent intent = new Intent(ResultList.this, EmptyResultList.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}