package com.partypanda.partyrent.rentya.View.RentApartment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.partypanda.partyrent.rentya.Adapters.LoadingScreen;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListApartmentPhotos extends AppCompatActivity {

    private TextView addPhotos;
    private Uri uri;
    private static final int PICK_IMAGE = 1;
    private TextView uploadImages;
    private ArrayList<Uri> imageList = new ArrayList<>();
    private String rentalId;
    private TextView later;
    private TextView imagesUploaded;
    private FirebaseFirestore listingImageDB;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.partypanda.partyrent.rentya.R.layout.activity_list_apartment_photos);
        listingImageDB = FirebaseFirestore.getInstance();
        later = findViewById(R.id.upload);
        addPhotos = findViewById(R.id.add_photos);
        uploadImages = findViewById(R.id.upload);
        imagesUploaded = findViewById(R.id.images_uploaded);
        rentalId = getIntent().getStringExtra("rentalID");
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListApartmentPhotos.this, MainActivity.class);
                startActivity(intent);

            }
        });

        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoadingScreen loadingScreen = new LoadingScreen(ListApartmentPhotos.this);
                loadingScreen.startLoadingDialog();
                final ArrayList<String> urlList = new ArrayList<>();

                loadingScreen.startLoadingDialog();
                final StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("RentalListImages").child(rentalId);
                        imageFolder.child(rentalId);
                for(int upload_count = 0;upload_count   < imageList.size();upload_count++){
                            Uri temp = imageList.get(upload_count);

                            final StorageReference imageName  = imageFolder.child(rentalId + (upload_count+1));
                            imageName.putFile(temp).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()){
                                       imageName.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                String downloadableUrl = task.getResult().toString();
                                                urlList.add(downloadableUrl);
                                            }
                                        });
                                        Toast.makeText(ListApartmentPhotos.this, "Image Selected Successfully", Toast.LENGTH_LONG).show();
                                        saveImageToFirestore(urlList);
                                        counter++;
                                    }
                                    else
                                    {
                                        Toast.makeText(ListApartmentPhotos.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                loadingScreen.dismiss();
                Intent intent = new Intent(ListApartmentPhotos.this, MainActivity.class);
                startActivity(intent);
                if(counter==imageList.size()){
                }
            }
        });

    }
    private void saveImageToFirestore(final ArrayList<String> urlList){

        FirebaseFirestore rentalImagesDB = FirebaseFirestore.getInstance();
        HashMap<String , String> imageList = new HashMap<String, String>();
        int pos = 0;
        for(String url:urlList){
            imageList.put("" + pos,urlList.get(pos));
            pos++;
        }

        rentalImagesDB.collection("ListRentalImages").document(rentalId).set(imageList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ListApartmentPhotos.this, "Task was successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(ListApartmentPhotos.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== PICK_IMAGE&&resultCode==RESULT_OK){

                if(data.getClipData()!=null) {
                    int countClipData = data.getClipData().getItemCount();
                    imagesUploaded.setText("  " + countClipData + " Photo are ready to be uploaded");
                    for(int currentImageSelect =0;currentImageSelect < countClipData;currentImageSelect++){
                        uri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        imageList.add(uri);
                    }

                }
                else{
                    Toast.makeText(ListApartmentPhotos.this, "No image has been selected", Toast.LENGTH_LONG).show();
                }
            }
        else{
            Toast.makeText(ListApartmentPhotos.this, "Error unable to access storage", Toast.LENGTH_LONG).show();
        }
    }
}