package com.partypanda.partyrent.rentya.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.partypanda.partyrent.rentya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListingImagesView extends  RecyclerView.Adapter<ListingImagesView.ViewImages>{

    private ArrayList<String> imageLinks;
    private Context context;
    public ListingImagesView(ArrayList<String> imageLinks,Context context){
        this.imageLinks = imageLinks;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewImages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_images,parent,false);
        return new ListingImagesView.ViewImages(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewImages holder, int position) {
        if(!imageLinks.isEmpty()){
            Picasso.get().load(imageLinks.get(position)).resize(250, 250)
                    .centerCrop().into(holder.listingImage);

        }else
        {
            int defaultImage = R.mipmap.apartment;
            Picasso.get().load(defaultImage).resize(250, 250)
                    .centerCrop().into(holder.listingImage);
            }
    }
    @Override
    public int getItemCount() {
        if(imageLinks.isEmpty())
        {
            return 5;
        }
        else{
            return imageLinks.size();
        }

    }
    public class ViewImages extends RecyclerView.ViewHolder{
        ImageView listingImage;
        public ViewImages(View view){
            super(view);
            listingImage = view.findViewById(R.id.listing_icon);
        }
    }
}
