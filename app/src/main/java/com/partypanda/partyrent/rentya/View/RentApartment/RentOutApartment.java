package com.partypanda.partyrent.rentya.View.RentApartment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Home.UserAccount;

public class RentOutApartment extends AppCompatActivity {

    private Spinner apartmentType;
    private Spinner apartmentPeople;
    private Spinner apartmentTerm;
    private Spinner apartmentCity;
    private TextView back;
    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_out_apartment);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        final String fullname = getIntent().getStringExtra("fullname");
        final String photoUrl = getIntent().getStringExtra("photoUrl");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentOutApartment.this, UserAccount.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("photoUrl",photoUrl);
                startActivity(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RentOutApartment.this, ListApartmentDescription.class);
                String type = apartmentType.getSelectedItem().toString();
                int guests = apartmentPeople.getSelectedItemPosition() + 1;
                String term = apartmentTerm.getSelectedItem().toString();
                String region = apartmentCity.getSelectedItem().toString();
                intent.putExtra("listing_type",type);
                intent.putExtra("listing_guests",guests);
                intent.putExtra("listing_term",term);
                intent.putExtra("listing_region",region);
                startActivity(intent);
            }
        });
        apartmentType = findViewById(R.id.apartment_type);
        apartmentPeople = findViewById(R.id.apartment_people);
        apartmentTerm = findViewById(R.id.apartment_term);
        apartmentCity = findViewById(R.id.apartment_city);

        //Adapter_type
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this,R.array.
                apartment_type,R.layout.color_spinner_layout);
        adapterType.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        apartmentType.setAdapter(adapterType);

        //Adapter_People
        ArrayAdapter<CharSequence> adapterPeople = ArrayAdapter.createFromResource(this,R.array.
                apartment_people,R.layout.color_spinner_layout);
        adapterPeople.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        apartmentPeople.setAdapter(adapterPeople);

        //Adapter_Term
        ArrayAdapter<CharSequence> adapterTerm = ArrayAdapter.createFromResource(this,R.array.
                apartment_term,R.layout.color_spinner_layout);
        adapterPeople.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        apartmentTerm.setAdapter(adapterTerm);

        //Adapter_City
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(this,R.array.
                apartment_location,R.layout.color_spinner_layout);
        adapterCity.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        apartmentCity.setAdapter(adapterCity);
    }
}