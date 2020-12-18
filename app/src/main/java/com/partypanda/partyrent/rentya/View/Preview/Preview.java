package com.partypanda.partyrent.rentya.View.Preview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.ListingImagesView;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.Model.AdClicked;
import com.partypanda.partyrent.rentya.Model.AdViewed;
import com.partypanda.partyrent.rentya.Model.Rating;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.Model.Reservation;
import com.partypanda.partyrent.rentya.Model.User;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Bookings.ReviewRental;
import com.partypanda.partyrent.rentya.View.Home.ApartmentList;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Preview extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    private TextView cancel;
    private String rentalId;
    private FirebaseFirestore rentalsDB;
    private TextView listingType;
    private TextView listingTitle;
    private TextView listingDescription;
    private TextView listingStation;
    private TextView listingAbout;
    private TextView listingFee;
    private EditText hostFullname;
    private CircleImageView hostProfileImage;
    private EditText hostPhone;
    private Button callHost;
    private Button emailHost;
    private FirebaseFirestore storageDB;
    private FirebaseFirestore storageImagesDB;
    private RecyclerView imageRecycleView;
    private Button reserve;
    private EditText eventDate;
    private EditText eventTime;
    private Button confirmReservation;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore reservationDB;
    private CircleImageView profile;
    private TextView viewHost;
    private FirebaseFirestore userDB;
    private User user;
    private FirebaseFirestore historyDB;
    private int category;
    private char key;
    private Button googleMapDirect;
    private Rental rental;
    private TextView listingAddress;
    private FirebaseFirestore adViewDB;
    private TextView stars;
    private FirebaseFirestore ratingDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        storageImagesDB = FirebaseFirestore.getInstance();
        userDB = FirebaseFirestore.getInstance();
        cancel = findViewById(R.id.cancel);
        googleMapDirect = findViewById(R.id.google_map_direct);
        rentalsDB = FirebaseFirestore.getInstance();
        profile = findViewById(R.id.host_profile);
        viewHost = findViewById(R.id.view_profile);
        adViewDB = FirebaseFirestore.getInstance();
        rentalId = getIntent().getStringExtra("rentalId");
        category = getIntent().getIntExtra("category", 0);
        listingType = findViewById(R.id.apartment_type);
        listingTitle = findViewById(R.id.apartment_title);
        listingDescription = findViewById(R.id.apartment_description);
        listingStation = findViewById(R.id.apartment_location);
        listingAbout = findViewById(R.id.listing_about);
        listingFee = findViewById(R.id.listing_fee);
        listingAddress = findViewById(R.id.apartment_address);
        imageRecycleView = findViewById(R.id.listing_images);
        reserve = findViewById(R.id.reserve);
        stars = findViewById(R.id.apartment_stars);
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        reservationDB = FirebaseFirestore.getInstance();
        historyDB = FirebaseFirestore.getInstance();
        ratingDB = FirebaseFirestore.getInstance();
        setRating();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        imageRecycleView.setLayoutManager(layoutManager);

        loadData();
        isReserved();
        final int type = getIntent().getIntExtra("themePos", 0);
        final char KEY = getIntent().getCharExtra("KEY", 'M');
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if (KEY == 'M') {
                    intent = new Intent(Preview.this, ApartmentList.class);
                    intent.putExtra("KEY", KEY);
                    intent.putExtra("themePos", type);
                    view.getContext().startActivity(intent);
                } else {
                    intent = new Intent(Preview.this, MainActivity.class);
                    intent.putExtra("KEY", KEY);
                    view.getContext().startActivity(intent);
                }


            }
        });
        viewHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rental.getHostId()!=null){
                    viewHostProfile();
                }else{
                    Toast.makeText(Preview.this, "There is not existing host for this rental!", Toast.LENGTH_LONG).show();
                }

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(rental.getHostId()!=null){
                    viewHostProfile();
                }else{
                    Toast.makeText(Preview.this, "There is not existing host for this rental!", Toast.LENGTH_LONG).show();
                }

            }
        });

        loadRecycleViewImages();
        setPreview();
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Preview.this, R.style.BottomSheetDiaglogTheme);
                View bottomSheet = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet, (ConstraintLayout) findViewById(R.id.reservation_container));
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();

                eventDate = bottomSheet.findViewById(R.id.event_date);
                eventTime = bottomSheet.findViewById(R.id.event_time);
                confirmReservation = bottomSheet.findViewById(R.id.confirm_reservation);
                eventDate.setInputType(InputType.TYPE_NULL);
                eventTime.setInputType(InputType.TYPE_NULL);
                eventDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog();

                    }
                });
                eventTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePickerDialog(eventTime);
                    }
                });
                confirmReservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(eventDate.getText().toString()) || TextUtils.isEmpty(eventTime.getText().toString())) {
                            Toast.makeText(Preview.this, "Please select a date and time you would like to see this apartment", Toast.LENGTH_LONG).show();
                        } else {
                            String date = eventDate.getText().toString();
                            String time = eventTime.getText().toString();
                            Reservation reservation = new Reservation(rentalId, userId, date, time,rental.getHostId());
                            setReservation(reservation);

                        }
                    }
                });
            }
        });
        googleMapDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + rental.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    private void setPreview() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String formatDate = formatter.format(date);
        AdClicked adClicked = new AdClicked(formatDate, rentalId, userId);
        reservationDB.collection("UserHistory").document().set(adClicked).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    private void viewHostProfile() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Preview.this, R.style.BottomSheetDiaglogTheme);
        View bottomSheet = LayoutInflater.from(getApplicationContext()).inflate(R.layout.host_account, (ConstraintLayout) findViewById(R.id.host_container));
        bottomSheetDialog.setContentView(bottomSheet);
        bottomSheetDialog.show();
        hostFullname = bottomSheet.findViewById(R.id.fullname);
        hostPhone = bottomSheet.findViewById(R.id.phone_number);
        hostPhone.setEnabled(false);
        hostFullname.setEnabled(false);
        callHost = bottomSheet.findViewById(R.id.call);
        emailHost = bottomSheet.findViewById(R.id.email);
        hostProfileImage = bottomSheet.findViewById(R.id.host_profile);
        hostFullname.setText(user.getFirstname() + " " + user.getLastname());
        adViewed();

        if (user.getProfileUrl() != null) {

            Picasso.get().load(user.getProfileUrl()).resize(150, 150)
                    .centerCrop().into(hostProfileImage);
        }

        if (user.getPhoneNo() != null) {

            hostPhone.setText("" + user.getPhoneNo());
        } else {
            hostPhone.setText("No phone number available");
        }

        callHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getPhoneNo() != null) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    String tel = "tel:" + user.getPhoneNo();
                    intent.setData(Uri.parse(tel));
                     if (ActivityCompat.checkSelfPermission(Preview.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                         startActivity(intent);
                         // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }
                     else {
                         Toast.makeText(Preview.this,"We do not have permission to make calls from this application.Please check settings and grant access if you would like to use this function",Toast.LENGTH_LONG).show();
                     }

                }
            }
        });
        emailHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,auth.getCurrentUser().getEmail());
                intent.putExtra(Intent.EXTRA_SUBJECT,"Interest in rental '" + rental.getTitle() +"'");
                intent.putExtra(Intent.EXTRA_TEXT,"Good day " + user.getFirstname() + " " + user.getLastname());
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Choose an email client."));
            }
        });

    }

    //Adds Reservation to secondary storage(cloud Firestore)
    private void setReservation(Reservation reservation){
        reservationDB.collection("Reservations").document().set(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Preview.this,"Reservation has been made successfully",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Preview.this, ReviewRental.class);
                    intent.putExtra("rentalID", rentalId);
                    startActivity(intent);
                }
                else{
                        Toast.makeText(Preview.this,"Reservation has not been made successfully",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    private void isReserved(){

        reservationDB.collection("Reservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        Reservation reservation = d.toObject(Reservation.class);
                        if(reservation.getUserId().equals(userId)&&rentalId.equals(reservation.getRentalId())){
                            reserve.setText("RESERVED");
                            reserve.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    private void loadData(){
        rentalsDB.collection("ListRental").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list){
                        String id = d.getId();
                        if(d.getId().equals(rentalId)){
                            rental = d.toObject(Rental.class);
                            setFields(rental);
                        }
                    }
                }
            }
        });
    }

    private void setValues(){
        String name = "";

    }

    private void loadRecycleViewImages(){
        rentalsDB.collection("ListRentalImages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    ArrayList<String> imageLinks = new ArrayList<>();
                    for(DocumentSnapshot d:list){
                        String id = d.getId();
                        if(d.getId().equals(rentalId)){
                            HashMap<String,Object> map =  (HashMap<String,Object>)d.getData();
                            for (Object value:map.values()){
                                imageLinks.add(value.toString());
                            }
                        }
                        ListingImagesView adapter = new ListingImagesView(imageLinks,getApplicationContext());
                        imageRecycleView.setAdapter(adapter);
                    }
                }
            }
        });
    }

    private void adViewed(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String formatDate = formatter.format(date);
        AdViewed adViewed = new AdViewed(formatDate, rentalId, userId);
        adViewDB.collection("AdViewed").document().set(adViewed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    private void setFields(Rental rental){
        listingType.setText(rental.getType());
        listingTitle.setText(rental.getTitle());
        listingDescription.setText(rental.getGuests() + " guests-" +
                rental.getRooms() + " Rooms- " + rental.getBathrooms() + " baths");
        listingStation.setText("metro " + rental.getNearestMetro() + " - University " + rental.getNearestUniversity());
        listingAbout.setText(rental.getDescription());
        listingAddress.setText(rental.getAddress());
        if(rental.getTerm().equals("Short-term contract")){
            listingFee.setText(rental.getFee() + "/month");
        }
        else
        if(rental.getTerm().equals("Long-term contract")){
            listingFee.setText(rental.getFee() + "/day");
        }
        else{
            listingFee.setText(rental.getFee() + "");
        }
        if(rental.getHostId()!=null){
            loadHostProfileImage(rental.getHostId());
        }
    }

    private void loadHostProfileImage(final String userId){
        userDB.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                    if(userId.equals(d.getId())){
                            user = d.toObject(User.class);
                            if(user.getProfileUrl()!=null){
                                Picasso.get().load(user.getProfileUrl()).resize(150, 150)
                                        .centerCrop().into(profile);
                            }
                        }
                    }
                }
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.MyTimePickerDialogTheme,this, Calendar.getInstance()
                .get(Calendar.YEAR),Calendar.getInstance()
                .get(Calendar.MONTH),Calendar.getInstance()
                .get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        eventDate.setText(date);
    }

    private void showTimePickerDialog(final EditText temp){

        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timePickerDialog = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                temp.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new TimePickerDialog(Preview.this, R.style.MyTimePickerDialogTheme,timePickerDialog,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }
    private void setRating(){
        ratingDB.collection("RentalReview").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    float total = 0;
                    int count = 0;
                    for(DocumentSnapshot d:list){
                        Rating rating = d.toObject(Rating.class);
                        if(rating.getRentalID().equals(rentalId)){
                            total+= rating.getRating();
                            count++;
                        }
                        float temp = total/count;
                        stars.setText(temp + "(" + count + ")");
                    }
                }
            }
        });
    }




}