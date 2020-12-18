package com.partypanda.partyrent.rentya;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Model.SavedAd;
import com.partypanda.partyrent.rentya.View.Authentication.LogIn;
import com.partypanda.partyrent.rentya.View.Authentication.SetUpAccount;
import com.partypanda.partyrent.rentya.View.Bookings.BookingsFragment;
import com.partypanda.partyrent.rentya.View.DefaultFragment.DefaultFragment;
import com.partypanda.partyrent.rentya.View.Favourite.FavouriteFragment;
import com.partypanda.partyrent.rentya.View.Home.History;
import com.partypanda.partyrent.rentya.View.Home.HomeFragment;
import com.partypanda.partyrent.rentya.View.Search.SearchFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FirebaseAuth auth;
    private FirebaseFirestore userDB;
    private  Fragment selectedFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_nav);
        auth = FirebaseAuth.getInstance();
        userDB = FirebaseFirestore.getInstance();
        final char KEY  = getIntent().getCharExtra("KEY",'M');

        if (KEY == 'M') {
            selectedFragment = new HomeFragment();
        }
        if (KEY == 'S') {
            selectedFragment = new SearchFragment();
        }
        if (KEY == 'B') {
            selectedFragment = new BookingsFragment();
        }
        if (KEY == 'F') {
            selectedFragment = new FavouriteFragment();
        }

        if (KEY == 'H') {
            selectedFragment = new HomeFragment();
        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
        bottomNav.setOnNavigationItemSelectedListener(selectedListener);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser==null)
        {
            Intent setupIntent = new Intent(MainActivity.this, LogIn.class);
            setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(setupIntent);
            finish();
        }
        else{

            userDB.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        boolean isRegistered = false;
                        for(DocumentSnapshot d:list){
                            if(d.getId().equals(auth.getCurrentUser().getUid())){
                                isRegistered = true;
                            }
                        }
                        if(!isRegistered){
                            Intent intent = new Intent(MainActivity.this, LogIn.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                    else{
                        if(currentUser!=null){
                            Intent intent = new Intent(MainActivity.this, SetUpAccount.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }

                }
            });
        }
    }*/


    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId())
            {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_bookings:
                    if(auth.getCurrentUser()!=null){
                        selectedFragment = new BookingsFragment();
                    }
                    else{
                        selectedFragment = new DefaultFragment();
                    }
                    break;
                case R.id.nav_favourite:
                    if(auth.getCurrentUser()!=null){
                        selectedFragment = new FavouriteFragment();
                    }
                    else{
                        selectedFragment = new DefaultFragment();
                    }

                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };
}