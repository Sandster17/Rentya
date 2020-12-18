package com.partypanda.partyrent.rentya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partypanda.partyrent.rentya.View.Home.ApartmentList;
import com.squareup.picasso.Picasso;
import com.partypanda.partyrent.rentya.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ThemeAdapter extends  RecyclerView.Adapter<ThemeAdapter.ViewTheme>{


     ArrayList<Integer> themeIcon;
     ArrayList<String> theme;
     Context context;
     int position;
    public ThemeAdapter(ArrayList<Integer> themeIcon, ArrayList<String> theme, Context context){
        this.themeIcon = themeIcon;
        this.theme = theme;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewTheme onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theme_card,parent,false);
        return new ViewTheme(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTheme holder, int position) {
        holder.theme.setText(theme.get(position));
        Picasso.get().load(themeIcon.get(position)).resize(150, 150)
                .centerCrop().into(holder.themeIcon);
        holder.onClick(position);
    }

    @Override
    public int getItemCount() {
        return themeIcon.size();
    }

    public class ViewTheme extends RecyclerView.ViewHolder{
        ImageView themeIcon;
        TextView theme;
        View viewItem;
        public ViewTheme(View viewItem)
        {
            super(viewItem);
            this.viewItem = viewItem;
            themeIcon = viewItem.findViewById(R.id.theme_icon);
            theme = viewItem.findViewById(R.id.theme);

        }

        public void onClick(final int position){

            themeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.getApplicationContext(), ApartmentList.class);
                    intent.putExtra("themePos",position);
                    context.startActivity(intent);
                }
            });

        }
    }


}
