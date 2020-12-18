package com.partypanda.partyrent.rentya.View.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.RentApartment.RentOutApartment;
import com.partypanda.partyrent.rentya.View.Splash;
import com.squareup.picasso.Picasso;

public class UserAccount extends AppCompatActivity {

    private TextView back;
    private ConstraintLayout profile;
    private ConstraintLayout history;
        private ConstraintLayout rentSpace;
        private ConstraintLayout privacyPolicy;
        private CircleImageView profileImage;
        private TextView userFullname;
        private TextView signOut;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_account);
            back = findViewById(R.id.back);
            profile = findViewById(R.id.profile);
            history = findViewById(R.id.history);
            rentSpace = findViewById(R.id.rent);
            privacyPolicy = findViewById(R.id.policy);
            profileImage = findViewById(R.id.circleImageView);
            userFullname = findViewById(R.id.fullname);
            signOut = findViewById(R.id.sign_out);

            final String fullname = getIntent().getStringExtra("fullname");
            final String photoUrl = getIntent().getStringExtra("photoUrl");
            if(photoUrl!=null){
                Picasso.get().load(photoUrl).resize(150, 150)
                        .centerCrop().into(profileImage);
            }
            if(fullname!=null){
                userFullname.setText(fullname);
            }

            rentSpace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
/*
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth.getCurrentUser()!=null){
                    if(auth.getCurrentUser().isEmailVerified()){
                        Intent intent = new Intent(UserAccount.this, RentOutApartment.class);
                        intent.putExtra("fullname",fullname);
                        intent.putExtra("photoUrl",photoUrl);
                        startActivity(intent);
                    }{
                        Intent intent = new Intent(UserAccount.this, SignAsHost.class);
                        intent.putExtra("fullname",fullname);
                        intent.putExtra("photoUrl",photoUrl);
                        startActivity(intent);

                    }
                }*/
                }
            });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAccount.this, EditProfile.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("photoUrl",photoUrl);

                startActivity(intent);

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAccount.this, History.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("photoUrl",photoUrl);
                startActivity(intent);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserAccount.this, MainActivity.class);
                startActivity(intent);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                finish();
                Intent intent = new Intent(UserAccount.this, Splash.class);
                startActivity(intent);
            }
        });
    }
}