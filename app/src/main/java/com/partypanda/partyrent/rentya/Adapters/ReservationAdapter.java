package com.partypanda.partyrent.rentya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Model.Rating;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.Model.Reservation;
import com.partypanda.partyrent.rentya.Model.SavedAd;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Authentication.LogIn;
import com.partypanda.partyrent.rentya.View.Preview.Preview;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ReservationAdapter extends RecyclerView.Adapter <ReservationAdapter.ViewHolderClass> {

    private Context context;
    private FirebaseFirestore resultDB;
    private String userId;
    private FirebaseAuth auth;
    private ArrayList<Reservation> reservationArrayList;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private FirebaseAuth user;
    private FirebaseFirestore reservationDB;
    private FirebaseFirestore cancelReservation;


    public ReservationAdapter(Context context,ArrayList<Reservation> reservationArrayList,String userId) {
        this.rentalArrayList = new ArrayList<>();
        this.reservationArrayList = reservationArrayList;
        this.context = context;
        auth = FirebaseAuth.getInstance();
        reservationDB = FirebaseFirestore.getInstance();
        cancelReservation = FirebaseFirestore.getInstance();
        this.userId = userId;


    }
    private void setView(@NonNull ReservationAdapter.ViewHolderClass viewHolder, Rental rental, String rentalId,int position){

        viewHolder.type.setText(rental.getType());
        viewHolder.title.setText(rental.getTitle());
        viewHolder.description.setText(rental.getGuests() + " guests-" +
                rental.getRooms() + " Rooms- " + rental.getBathrooms() + " baths");
        Picasso.get().load(R.mipmap.apartment).resize(150, 150)
                .centerCrop().into(viewHolder.profile);
        viewHolder.clickCard(rentalId);
        viewHolder.isFavourite(rentalId);
        viewHolder.clickFavourite(rentalId);
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try{
            Date reservationDate = sdf.parse(reservationArrayList.get(position).getDate());
            if(currentDate.after(reservationDate)){
                viewHolder.preview.setText("Review");
                viewHolder.cancel.setVisibility(View.INVISIBLE);
            }
        }
        catch(Exception ex){
            Toast.makeText(context, "System error. Please contact admin" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }


            String day = "", month = "", year = "";
        viewHolder.setRating(rentalId);
        day = reservationArrayList.get(position).getDate().substring(0,2);
        if(reservationArrayList.get(position).getDate().length()==9){

            month =  reservationArrayList.get(position).getDate().substring(3,4);
            year = reservationArrayList.get(position).getDate().substring(5);
        }
        else
        if(reservationArrayList.get(position).getDate().length()==10){
            month =  reservationArrayList.get(position).getDate().substring(3,5);
            year = reservationArrayList.get(position).getDate().substring(6);

        }

        viewHolder.reservationDate.setText("On " + toDay(convert(day)) + "," + toMonth(convert(month)) + " " + day + " ," + year );
        viewHolder.cancel(position);
    }
    @NonNull
    @Override
    public ReservationAdapter.ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReservationAdapter.ViewHolderClass(LayoutInflater.from(context).inflate(R.layout.reservation_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationAdapter.ViewHolderClass viewHolder, final int position) {

        reservationDB.collection("ListRental").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {

                        if(d.getId().equals(reservationArrayList.get(position).getRentalId())){
                            Rental rental = d.toObject(Rental.class);
                            rentalArrayList.add(rental);
                            rentalIdList.add(d.getId());
                            setView(viewHolder,rental,d.getId(),position);
                        }
                    }
                }
            }
        });

        this.rentalIdList = new ArrayList<>();
    }
    private int convert(String data){
        try{
            return Integer.parseInt(data);
        }
        catch (Exception e){
            return 0;
        }
    }
    private String toDay(int day){

        String days[] = {"Mon","Tues","Wed","Thur","Fri","Sat","Sun"};
            int temp =0;
            for(int i =0;i < day;i++){
                if(temp==6){
                    temp =0;
                }
                temp++;
            }
            return days[temp];

    }
    private String toMonth(int month){

        String months[] = {"January" , "February" , "March" , "April", "May",
                        "June", "July", "August", "September", "October",
                        "November", "December"};
        return months[month];
    }

    @Override
    public int getItemCount() {
        return reservationArrayList.size();
    }

    class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView type;
        TextView title;
        TextView description;
        TextView stars;
        TextView favourite;
        ImageView profile;
        private FirebaseFirestore ratingDB;
        private int count;
        private FirebaseFirestore savedAdsDB;
        private FirebaseAuth auth;
        private Button preview;
        private TextView cancel;
        TextView reservationDate;


        public ViewHolderClass(View view) {
            super(view);
            type = view.findViewById(R.id.apartment_type);
            title = view.findViewById(R.id.apartment_title);
            description = view.findViewById(R.id.apartment_description);
            stars = view.findViewById(R.id.apartment_stars);
            favourite = view.findViewById(R.id.apartment_favourite);
            savedAdsDB = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            profile = view.findViewById(R.id.apartment_profile);
            cancel = view.findViewById(R.id.cancel);
            preview = view.findViewById(R.id.preview);
            reservationDate = view.findViewById(R.id.reservation_date);
            ratingDB = FirebaseFirestore.getInstance();
            count = 0;
                    }

        public void clickFavourite(final String rentalId) {
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Drawable drawableSelected = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_favorite_24, null);
                    Drawable drawableUnselected = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_favorite_border_24, null);
                    if (count % 2 == 0) {
                        favourite.setBackground(drawableSelected);
                        saveAd(rentalId);
                    } else {
                        favourite.setBackground(drawableUnselected);
                        deleteAd(rentalId);
                    }
                    count++;
                }
            });
        }

        public void isFavourite(final String rentalId) {

            savedAdsDB.collection("SavedRentals").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            SavedAd savedAd = d.toObject(SavedAd.class);
                            if (savedAd.getRentalId().equals(rentalId) && userId.equals(savedAd.getUserId())) {

                                Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_baseline_favorite_24, null);
                                favourite.setBackground(drawable);
                                count++;
                            }
                        }
                    }
                }
            });
        }

        private void saveAd(String rentalId) {
            SavedAd savedAd = new SavedAd(userId,rentalId);
            savedAdsDB.collection("SavedRentals").document().set(savedAd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context.getApplicationContext(), "Ad has been saved successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        public void clickCard(final String rentalId) {
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (auth.getCurrentUser() != null) {
                        Intent intent = new Intent(view.getContext(), Preview.class);
                        intent.putExtra("rentalId",rentalId);
                        intent.putExtra("KEY",'B');
                        view.getContext().startActivity(intent);
                    } else {
                        Intent intent = new Intent(view.getContext(), LogIn.class);
                        view.getContext().startActivity(intent);
                    }

                }
            });
        }

        private void deleteAd(final String rentalId) {

            savedAdsDB.collection("SavedRentals").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            SavedAd savedAd = d.toObject(SavedAd.class);
                            if (savedAd.getRentalId().equals(rentalId) && savedAd.getUserId().equals(userId)) {
                                DocumentReference deleteRef = FirebaseFirestore.getInstance().collection("SavedRentals").document(d.getId());
                                deleteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context.getApplicationContext(), "Removed from saved rentals", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
        private void cancel(final int position){
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cancelReservation.collection("Reservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Reservation reservation = d.toObject(Reservation.class);
                                    if(reservation.getRentalId().equals(rentalIdList.get(position))) {
                                        DocumentReference deleteRef = FirebaseFirestore.getInstance().collection("Reservations").document(d.getId());
                                        deleteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context.getApplicationContext(), "Reservation has been cancelled.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
        private void setRating(final String rentalId){
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
}