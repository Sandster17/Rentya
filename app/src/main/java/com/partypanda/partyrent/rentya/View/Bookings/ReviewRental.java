package com.partypanda.partyrent.rentya.View.Bookings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.Model.Rating;
import com.partypanda.partyrent.rentya.R;

public class ReviewRental extends AppCompatActivity {


    private TextView back;
    private RatingBar ratingBar;
    private float rating;
    private String rentalID;
    private String userID;
    private Button confirm;
    private FirebaseFirestore reviewDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_rental);
        back = findViewById(R.id.back);
        ratingBar = findViewById(R.id.rental_rating);
        rating = ratingBar.getRating();
        reviewDB = FirebaseFirestore.getInstance();
        rentalID = getIntent().getStringExtra("rentalID");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        confirm = findViewById(R.id.confirm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewRental.this, MainActivity.class);
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadReview();
                Intent intent = new Intent(ReviewRental.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadReview(){
        Rating review = new Rating(rentalID,userID,rating);
        reviewDB.collection("RentalReview").add(review).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ReviewRental.this, "Your Review has been submitted successfully. Thank you:) ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ReviewRental.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ReviewRental.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}