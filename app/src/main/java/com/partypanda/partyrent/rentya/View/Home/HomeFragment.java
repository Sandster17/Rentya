package com.partypanda.partyrent.rentya.View.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.partypanda.partyrent.rentya.Adapters.ThemeAdapter;
import com.partypanda.partyrent.rentya.Model.User;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Authentication.LogIn;
import com.partypanda.partyrent.rentya.View.Authentication.SetUpAccount;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private RecyclerView themesView;
    private FirebaseAuth auth;
    private TextView seeAll;
    private CircleImageView profileImage;
    private FirebaseFirestore userDB;
    private String userId;
    private CardView kyivCard;
    private CardView sofiaCard;
    private View view;
    private User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        themesView = view.findViewById(R.id.themes_recycleview);
        auth = FirebaseAuth.getInstance();
        user = null;
        this.view= view;
        if(auth.getCurrentUser()!=null){
           userId = auth.getCurrentUser().getUid();
        }
        sofiaCard = view.findViewById(R.id.sofia_card);
        kyivCard = view.findViewById(R.id.kyiv_card);
        profileImage = view.findViewById(R.id.user_profile);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false);
        themesView.setLayoutManager(layoutManager);
        ArrayList<Integer> themeIcon = new ArrayList<>();
        ArrayList<String> theme = new ArrayList<>();
        theme.add("Long term\ncontracts");
        theme.add("Short term\ncontracts");
        themeIcon.add(R.mipmap.apartment);
        themeIcon.add(R.mipmap.shorttermrentals);
        seeAll = view.findViewById(R.id.see_all);
        ThemeAdapter adapter = new ThemeAdapter(themeIcon,theme,view.getContext());
        themesView.setAdapter(adapter);
        userDB = FirebaseFirestore.getInstance();
        theme.add("Apartment/House");
        theme.add("Room in Apartment");
        theme.add("Hostel");
        themeIcon.add(R.mipmap.house);
        themeIcon.add(R.mipmap.tophostels);
        themeIcon.add(R.mipmap.room);
        sofiaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ApartmentList.class );
                intent.putExtra("area","sofia");
                view.getContext().startActivity(intent);
            }
        });
        kyivCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),ApartmentList.class );
                intent.putExtra("area","kyiv");
                view.getContext().startActivity(intent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser()!=null){
                    Intent intent = new Intent(view.getContext(), UserAccount.class);
                    if(user!=null){
                        intent.putExtra("fullname",user.getFirstname()+  " " + user.getLastname());
                        intent.putExtra("photoUrl",user.getProfileUrl());
                        view.getContext().startActivity(intent);
                    }
                    else{
                        intent = new Intent(view.getContext(), SetUpAccount.class);
                        startActivity(intent);
                    }
                }
                else{
                    Intent intent = new Intent(view.getContext(), LogIn.class);
                    view.getContext().startActivity(intent);
                }
            }
        });

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ApartmentList.class);
                intent.putExtra("themePos",-1);
                view.getContext().startActivity(intent);
            }
        });

        if(auth.getCurrentUser()!=null){
            loadData();
        }


        return view;
    }

    private void loadData(){

        userDB.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                    if(userId.equals(d.getId())){
                        user = d.toObject(User.class);
                        if(user.getProfileUrl()!=null){

                            Picasso.get().load(user.getProfileUrl()).into(profileImage);

                        }
                    }
                    }
                }
            }
        });
    }


}