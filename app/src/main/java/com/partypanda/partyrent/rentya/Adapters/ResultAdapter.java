package com.partypanda.partyrent.rentya.Adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Model.Rating;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.Model.SavedAd;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Authentication.LogIn;
import com.partypanda.partyrent.rentya.View.Preview.Preview;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ResultAdapter extends RecyclerView.Adapter <ResultAdapter.ViewHolderClass> {

    private Context context;
    private FirebaseFirestore resultDB;
    private String userId;
    private FirebaseAuth auth;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private FirebaseUser currentUser;
    private FirebaseFirestore rentalsDB;
    private int category;
    private char screen;

    public ResultAdapter(Context context, ArrayList<Rental> rentalArrayList,ArrayList<String> rentalIdList,int category,char screen){
        this.rentalArrayList = rentalArrayList;
        this.rentalIdList = rentalIdList;
        this.context = context;
        rentalsDB = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        this.category = category;
        this.screen = screen;
        if(currentUser!=null){
            userId = auth.getCurrentUser().getUid();
        }
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent , int viewType)
    {
        return new ViewHolderClass(LayoutInflater.from(context).inflate(R.layout.rental_card,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass viewHolder,int position){
        viewHolder.type.setText(rentalArrayList.get(position).getType());
        viewHolder.title.setText(rentalArrayList.get(position).getTitle());
        viewHolder.description.setText(rentalArrayList.get(position).getGuests() + " guests-" +
                rentalArrayList.get(position).getRooms() + " Rooms- " + rentalArrayList.get(position).getBathrooms() + " baths");
        viewHolder.loadImageLink(position);
        viewHolder.clickCard(position);
        if(currentUser!=null){
            viewHolder.isFavourite(position);
        }
        viewHolder.setRating(position);

        viewHolder.clickFavourite(position);

    }

    @Override
    public int getItemCount()
    {
        return rentalArrayList.size();
    }

    class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView type;
        TextView title;
        TextView description;
        TextView stars;
        TextView favourite;
        ImageView profile;
        private CardView preview;
        private int count;
        private FirebaseFirestore savedAdsDB;
        private FirebaseAuth auth;
        private FirebaseFirestore ratingDB;



        public ViewHolderClass(View view){
            super(view);
            type = view.findViewById(R.id.apartment_type);
            title = view.findViewById(R.id.apartment_title);
            description = view.findViewById(R.id.apartment_description);
            stars = view.findViewById(R.id.apartment_stars);
            favourite = view.findViewById(R.id.apartment_favourite);
            savedAdsDB = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();
            profile = view.findViewById(R.id.apartment_profile);
            preview = view.findViewById(R.id.card_view);

            ratingDB = FirebaseFirestore.getInstance();
            count = 0;
        }

        public void clickFavourite(final int position){
             favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(currentUser!=null) {
                            Drawable drawableSelected = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_favorite_24, null);
                            Drawable drawableUnselected = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_baseline_favorite_border_24, null);
                        if (count % 2 == 0) {
                            favourite.setBackground(drawableSelected);
                            saveAd(position);
                        } else {
                            favourite.setBackground(drawableUnselected);
                            deleteAd(position);
                        }
                        count++;
                    }
                    else
                    {
                        Intent intent = new Intent(context.getApplicationContext(), LogIn.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void isFavourite(final int position){

            if(currentUser!=null) {
            savedAdsDB.collection("SavedRentals").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            SavedAd savedAd = d.toObject(SavedAd.class);
                            if(savedAd.getRentalId().equals(rentalIdList.get(position))&&userId.equals(savedAd.getUserId())){

                                Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_favorite_24,null);
                                favourite.setBackground(drawable);
                                count++;
                            }
                        }
                    }
                }
            });
        }
            else
            {
                Intent intent = new Intent(context.getApplicationContext(), LogIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

        }

        private void saveAd(int position){
            SavedAd savedAd = new SavedAd(userId, rentalIdList.get(position));
            savedAdsDB.collection("SavedRentals").document().set(savedAd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(context.getApplicationContext(), "Ad has been saved successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void clickCard(final int position){
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (currentUser != null) {

                        if (auth.getCurrentUser() != null) {
                            Intent intent = new Intent(view.getContext(), Preview.class);
                            intent.putExtra("rentalId", rentalIdList.get(position));
                            intent.putExtra("themePos",category);
                            intent.putExtra("KEY",screen);
                            view.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(view.getContext(), LogIn.class);
                            view.getContext().startActivity(intent);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(context.getApplicationContext(), LogIn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }

        private void deleteAd(final int position){

            savedAdsDB.collection("SavedRentals").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            SavedAd savedAd = d.toObject(SavedAd.class);
                            if(savedAd.getRentalId().equals(rentalIdList.get(position))&&savedAd.getUserId().equals(userId)){
                                DocumentReference deleteRef = FirebaseFirestore.getInstance().collection("SavedRentals").document(d.getId());
                                deleteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context.getApplicationContext(), "Removed from saved rentals", Toast.LENGTH_SHORT).show();
                                        }else{
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

        private void loadImageLink(final int position){
            rentalsDB.collection("ListRentalImages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        boolean add = false;
                        HashMap<String, Object>  map = null;
                        for(DocumentSnapshot d:list){
                            if(rentalIdList.get(position).equals(d.getId())) {
                                add = true;
                                map = (HashMap<String, Object>) d.getData();
                            }
                        }
                        if(add){

                            ArrayList<String> imageLinks = new ArrayList<>();
                            for (Object value : map.values()) {
                                imageLinks.add(value.toString());
                            }
                            if (!imageLinks.isEmpty()) {
                                Picasso.get().load(imageLinks.get(0)).resize(250, 250)
                                        .centerCrop().into(profile);
                            } else {
                                int defaultImage = R.mipmap.apartment;
                                Picasso.get().load(defaultImage).resize(250, 250)
                                        .centerCrop().into(profile);
                            }
                            imageLinks.clear();
                        }
                        else{

                            int defaultImage = R.mipmap.apartment;
                            Picasso.get().load(defaultImage).resize(250, 250)
                                    .centerCrop().into(profile);
                        }
                    }
                }
            });
        }
        private void setRating(final int position){
            ratingDB.collection("RentalReview").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        float total = 0;
                        int count = 0;
                        for(DocumentSnapshot d:list){
                            Rating rating = d.toObject(Rating.class);
                            if(rating.getRentalID().equals(rentalIdList.get(position))){
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

