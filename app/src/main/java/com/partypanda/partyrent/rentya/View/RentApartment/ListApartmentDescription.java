package com.partypanda.partyrent.rentya.View.RentApartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.R;

public class ListApartmentDescription extends AppCompatActivity {


    private EditText title;
    private EditText description;
    private EditText rooms;
    private EditText bathrooms;
    private CheckBox renovated;
    private Spinner university;
    private Spinner metrostation;
    private TextView back;
    private TextView addRooms;
    private TextView addBathrooms;
    private TextView removeRooms;
    private TextView removeBathrooms;
    private int num_rooms = 0;
    private int num_bathorooms = 0;
    private Button next;
    private TextView errorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_apartment_description);
        back = findViewById(R.id.back);
        title = findViewById(R.id.listing_title);
        description = findViewById(R.id.listing_description);
        rooms = findViewById(R.id.listing_rooms);
        bathrooms = findViewById(R.id.listing_bathrooms);
        renovated = findViewById(R.id.listing_renovated);
        university = findViewById(R.id.listing_University);
        metrostation = findViewById(R.id.listing_metro_station);
        addRooms = findViewById(R.id.add_rooms);
        removeRooms = findViewById(R.id.remove_rooms);
        addBathrooms = findViewById(R.id.add_bathrooms);
        removeBathrooms = findViewById(R.id.remove_rooms);
        next = findViewById(R.id.next);
        errorMessage = findViewById(R.id.error_message);
        addBathrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num_bathorooms++;
                bathrooms.setText("" + num_bathorooms + " Bathrooms");
            }
        });

        removeBathrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(num_bathorooms>0){
                    num_bathorooms--;
                    bathrooms.setText("" + num_bathorooms + " Bathrooms");
                }
            }
        });

        addRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num_rooms++;
                rooms.setText("" + num_rooms +  " rooms");
            }
        });

        removeRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num_rooms>0){
                    num_rooms--;
                    rooms.setText("" + num_rooms + " rooms");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isFieldComplete = true;

                String type = getIntent().getStringExtra("listing_type");
                String term = getIntent().getStringExtra("listing_term");
                int guests = getIntent().getIntExtra("listing_guests",1);

                //Current Activity
                String listingTitle = title.getText().toString();
                String listingDescription = description.getText().toString();
                int listingRooms = 0;
                int listingBathrooms = 0;
                try{
                    listingRooms = Integer.parseInt("" + rooms.getText().toString().charAt(0))+1;
                    listingBathrooms = Integer.parseInt("" + bathrooms.getText().toString().charAt(0))+1;
                }
                catch(Exception e){
                    isFieldComplete = false;
                }
                boolean listingRenovated = renovated.isSelected();
                String nearestUniversity = university.getSelectedItem().toString();
                String nearestMetro = metrostation.getSelectedItem().toString();

                if(listingTitle.length()<1||listingTitle==null)
                {
                    isFieldComplete = false;
                }

                if(listingDescription.length()<1||listingDescription==null)
                {
                    isFieldComplete = false;
                }


                if(isFieldComplete){

                    //New push to next activity
                    Intent intent = new Intent(ListApartmentDescription.this, ListFeeAndLocation.class);
                    intent.putExtra("listing_type",type);
                    intent.putExtra("listing_guests",guests);
                    intent.putExtra("listing_term",term);
                    intent.putExtra("listing_title",listingTitle);
                    intent.putExtra("listing_description",listingDescription);
                    intent.putExtra("listing_rooms",listingRooms);
                    intent.putExtra("listing_bathrooms",listingBathrooms);
                    intent.putExtra("listing_renovated",listingRenovated);
                    intent.putExtra("listing_university",nearestUniversity);
                    intent.putExtra("listing_metro",nearestMetro);
                    startActivity(intent);
                }
                else
                {
                    errorMessage.setVisibility(View.VISIBLE);
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListApartmentDescription.this, RentOutApartment.class);
                startActivity(intent);
            }
        });

        //University Spinner
        ArrayAdapter<CharSequence> universityAdapter = ArrayAdapter.createFromResource(this,R.array.
                university_kyiv,R.layout.color_spinner_layout);
        universityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        university.setAdapter(universityAdapter);


        ArrayAdapter<CharSequence> metroAdapter = ArrayAdapter.createFromResource(this,R.array.
                metrostation_kyiv,R.layout.color_spinner_layout);
        metroAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        metrostation.setAdapter(metroAdapter);



    }
}