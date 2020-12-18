package com.partypanda.partyrent.rentya.View.Authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.Model.User;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.RentApartment.ListApartmentPhotos;
import com.partypanda.partyrent.rentya.View.RentApartment.ListFeeAndLocation;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetUpAccount extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private CircleImageView profile;
    private Button complete;
    private FirebaseAuth auth;
    private static final int IMAGE_REQUEST = 1;
    private StorageReference userStorageReference;
    private String userID;
    private String profileImageUrl;
    private FirebaseFirestore userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_account);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        profile = findViewById(R.id.circleImageView);
        complete = findViewById(R.id.complete);
        auth = FirebaseAuth.getInstance();
        userID  = auth.getCurrentUser().getUid();
        userDB = FirebaseFirestore.getInstance().getInstance();
        userStorageReference = FirebaseStorage.getInstance().getReference("Profile Images");
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST);
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstnameInput = firstname.getText().toString();
                String lastnameInput = lastname.getText().toString();
                String phone = getIntent().getStringExtra("phone");
                User user = new User(firstnameInput, lastnameInput, profileImageUrl, phone, false,false);
                userDB.collection("Users").document(userID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SetUpAccount.this,"User has been added successfully",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SetUpAccount.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(SetUpAccount.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1).start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();

                final StorageReference filePath = userStorageReference.child(userID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(SetUpAccount.this, "Profile Image " +
                                    "has been added successfully", Toast.LENGTH_SHORT).show();
                            filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri download = task.getResult();
                                    final String downloadedUrl = download.toString();
                                    profileImageUrl = downloadedUrl;
                                    Picasso.get().load(downloadedUrl).into(profile);
                                }
                            });


                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SetUpAccount.this, message, Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        }


    }
}