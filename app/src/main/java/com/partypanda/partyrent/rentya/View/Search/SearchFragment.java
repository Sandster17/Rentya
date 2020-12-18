package com.partypanda.partyrent.rentya.View.Search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.partypanda.partyrent.rentya.R;

import javax.xml.transform.Result;

public class SearchFragment extends Fragment {

    private Spinner apartmentType;
    private Spinner apartmentPeople;
    private Spinner apartmentTerm;
    private Button search;
    private Spinner university;
    private Spinner metrostation;
    private EditText max;
    private EditText min;
    final private char KEY = 'S';


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        apartmentType = view.findViewById(R.id.apartment_type);
        apartmentPeople = view.findViewById(R.id.apartment_people);
        apartmentTerm = view.findViewById(R.id.apartment_term);
        university = view.findViewById(R.id.listing_University);
        metrostation = view.findViewById(R.id.listing_metro_station);
        search = view.findViewById(R.id.search_home);
        max = view.findViewById(R.id.apartment_max);
        min = view.findViewById(R.id.apartment_min);

        //Adapter_type
    ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(view.getContext(),R.array.
            apartment_type,R.layout.color_spinner_layout);
    adapterType.setDropDownViewResource(R.layout.spinner_dropdown_layout);
    apartmentType.setAdapter(adapterType);

    //Adapter_People
    ArrayAdapter<CharSequence> adapterPeople = ArrayAdapter.createFromResource(view.getContext(),R.array.
            apartment_people,R.layout.color_spinner_layout);
    adapterPeople.setDropDownViewResource(R.layout.spinner_dropdown_layout);
    apartmentPeople.setAdapter(adapterPeople);

    //Adapter_Term
    ArrayAdapter<CharSequence> adapterTerm = ArrayAdapter.createFromResource(view.getContext(),R.array.
            apartment_term,R.layout.color_spinner_layout);
    adapterPeople.setDropDownViewResource(R.layout.spinner_dropdown_layout);
    apartmentTerm.setAdapter(adapterTerm);

    //Adapter_university
        final ArrayAdapter<CharSequence> universityAdapter = ArrayAdapter.createFromResource(view.getContext(),R.array.
                university_kyiv,R.layout.color_spinner_layout);
        universityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        university.setAdapter(universityAdapter);

        //Adapter_metrostation
        ArrayAdapter<CharSequence> metroAdapter = ArrayAdapter.createFromResource(view.getContext(),R.array.
                metrostation_kyiv,R.layout.color_spinner_layout);
        metroAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        metrostation.setAdapter(metroAdapter);

    search.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), ResultList.class);
            String type = apartmentType.getSelectedItem().toString();
            int people = apartmentPeople.getSelectedItemPosition() + 1;
            String term = apartmentTerm.getSelectedItem().toString();
            String uni = university.getSelectedItem().toString();
            String metro = metrostation.getSelectedItem().toString();

            double minValue = 0, maxValue = 0;
            try{

                minValue = Double.parseDouble(min.getText().toString());
                maxValue = Double.parseDouble(max.getText().toString());
            }
            catch(Exception e){
                Toast.makeText(view.getContext(),
                        "Unable to convert values entered.", Toast.LENGTH_LONG).show();
            }
            //Search Input
            intent.putExtra("apartment_type",type);
            intent.putExtra("apartment_people",people);
            intent.putExtra("apartment_term",term);
            intent.putExtra("apartment_uni",uni);
            intent.putExtra("apartment_metro",metro);
            intent.putExtra("apartment_min",minValue);
            intent.putExtra("apartment_max",maxValue);
            intent.putExtra("KEY",KEY);
            startActivity(intent);



        }
    });


    return view;
    }
}