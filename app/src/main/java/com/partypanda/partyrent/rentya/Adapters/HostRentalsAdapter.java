package com.partypanda.partyrent.rentya.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.partypanda.partyrent.rentya.Model.Rental;
import com.partypanda.partyrent.rentya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HostRentalsAdapter extends RecyclerView.Adapter <HostRentalsAdapter.ViewRentalClass>{
    private Context context;
    private FirebaseFirestore resultDB;
    private String userId;
    private FirebaseAuth auth;
    private ArrayList<Rental> rentalArrayList;
    private ArrayList<String> rentalIdList;
    private FirebaseUser currentUser;

    public HostRentalsAdapter(Context context,ArrayList<Rental> rentalArrayList,ArrayList<String> rentalIdList){
        this.rentalArrayList = rentalArrayList;
        this.rentalIdList = rentalIdList;
        this.context = context;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if(currentUser!=null){
            userId = currentUser.getUid();
        }
    }

    @NonNull
    @Override
    public ViewRentalClass onCreateViewHolder(@NonNull ViewGroup parent , int viewType)
    {
        return new ViewRentalClass(LayoutInflater.from(context).inflate(R.layout.host_rental,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull HostRentalsAdapter.ViewRentalClass viewHolder, int position){
        viewHolder.type.setText(rentalArrayList.get(position).getType());
        viewHolder.title.setText(rentalArrayList.get(position).getTitle());
        viewHolder.description.setText(rentalArrayList.get(position).getGuests() + " guests-" +
                rentalArrayList.get(position).getRooms() + " Rooms- " + rentalArrayList.get(position).getBathrooms() + " baths");
        Picasso.get().load(R.mipmap.apartment).resize(150, 150)
                .centerCrop().into(viewHolder.profile);

    }

    @Override
    public int getItemCount()
    {
        return rentalArrayList.size();
    }

    class ViewRentalClass extends RecyclerView.ViewHolder {

        TextView type;
        TextView title;
        TextView description;
        TextView stars;
        TextView favourite;
        ImageView profile;
        TextView edit;
        TextView delete;
        private CardView preview;
        private int count;
        private FirebaseFirestore savedAdsDB;
        private FirebaseAuth auth;

        public ViewRentalClass(View view) {
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
            edit = view.findViewById(R.id.apartment_edit);
            delete = view.findViewById(R.id.apartment_delete);
            count = 0;
        }
    }
}
