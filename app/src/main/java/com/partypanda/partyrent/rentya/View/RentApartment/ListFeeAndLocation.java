package com.partypanda.partyrent.rentya.View.RentApartment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.R;

import java.util.Arrays;
import java.util.List;

public class ListFeeAndLocation extends AppCompatActivity {

    private EditText search;
    private EditText fee;
    private Button next;
    private TextView back;
    private CheckBox negotiable;
    private FirebaseFirestore listRentalDB;
    private FirebaseAuth auth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fee_and_location);
        listRentalDB = FirebaseFirestore.getInstance();
        search = findViewById(R.id.search_address);
        fee = findViewById(R.id.apartment_fee);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        negotiable = findViewById(R.id.apartment_negotiable);
        search.setFocusable(false);
        Places.initialize(getApplicationContext(),"AIzaSyA6veVPCCzE808LtgO0tlmY9J4CNP7iXiI" );
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid().toString();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1st step data.
                String type = getIntent().getStringExtra("listing_type");
                String term = getIntent().getStringExtra("listing_term");
                int guests = getIntent().getIntExtra("listing_guests",1);
                String region = getIntent().getStringExtra("listing_region");

                //2nd Step data
                String listingTitle = getIntent().getStringExtra("listing_title");
                String listingDescription = getIntent().getStringExtra("listing_description");
                int listingRooms = getIntent().getIntExtra("listing_rooms",1);
                int listingBathrooms =  getIntent().getIntExtra("listing_rooms",1);
                boolean listingRenovated = getIntent().getBooleanExtra("listing_renovated",false);
                String nearestUniversity = getIntent().getStringExtra("listing_university");
                String nearestMetro = getIntent().getStringExtra("listing_metro");

                //Current Activity.
                String selectedSearch = search.getText().toString();
                double apartmentFee = 0.0;
                try{
                        apartmentFee = Double.parseDouble(fee.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(ListFeeAndLocation.this, "Please enter an apartment fee greater than 0",Toast.LENGTH_LONG).show();
                }

                boolean isNegotiable = negotiable.isSelected();
                String status = "pending";
                //Create Reference in a database.
                // public Rental(String type, String guests, String term, String region, String title, String description, int rooms, int bathrooms, boolean renovated, String nearestUniversity, String nearestMetro, double fee, boolean negotiable, String address) {
                Rental rental = new Rental(type, guests, term ,region,listingTitle, listingDescription, listingRooms, listingBathrooms, listingRenovated,nearestUniversity, nearestMetro, apartmentFee, isNegotiable, selectedSearch,status,userId);
                listRentalDB.collection("ListRental").add(rental)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ListFeeAndLocation.this,"Application has been sent successfully!!!",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(ListFeeAndLocation.this, ListApartmentPhotos.class);
                                    DocumentReference documentReference = task.getResult();
                                    String id = documentReference.getId();
                                    intent.putExtra("rentalID",id);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(ListFeeAndLocation.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent = new Autocomplete.
                        IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(ListFeeAndLocation.this);
                startActivityForResult(intent, 100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK)
        {
            final Place place = Autocomplete.getPlaceFromIntent(data);
            String temp = place.getAddress();
            search.setText( temp);
            Toast.makeText(getApplicationContext(),"Place" + place.getAddress(),Toast.LENGTH_SHORT);
        }
        else
        if(resultCode== AutocompleteActivity.RESULT_ERROR)
        {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(),status.getStatusMessage(),Toast.LENGTH_SHORT);
        }
    }

}