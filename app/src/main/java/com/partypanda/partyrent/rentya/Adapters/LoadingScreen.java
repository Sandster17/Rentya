package com.partypanda.partyrent.rentya.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.partypanda.partyrent.rentya.R;

public class LoadingScreen {

    Activity activity;
    AlertDialog alertDialog;

    public LoadingScreen(Activity activity){
        this.activity = activity;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_screen,null));
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void dismiss(){
        alertDialog.dismiss();
    }

}
