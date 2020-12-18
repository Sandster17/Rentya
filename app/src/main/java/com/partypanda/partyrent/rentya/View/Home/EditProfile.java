package com.partypanda.partyrent.rentya.View.Home;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.Model.User;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Authentication.SetUpAccount;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.List;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth auth;
    private String userId;
    private EditText firstnameInput;
    private EditText lastnameInput;
    private EditText phoneNo;
    private Button update;
    private Button changePassword;
    private User user;
    private FirebaseFirestore userDB;
    private CircleImageView profileImage;
    private static final int IMAGE_REQUEST = 1;
    private StorageReference userStorageReference;
    private String profileImageUrl;
    private TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        firstnameInput = findViewById(R.id.firstname);
        lastnameInput = findViewById(R.id.lastname);
        phoneNo = findViewById(R.id.phone_number);
        update = findViewById(R.id.update);
        changePassword = findViewById(R.id.change_password);
        profileImage = findViewById(R.id.circleImageView);
        userStorageReference = FirebaseStorage.getInstance().getReference("Profile Images");
        userDB = FirebaseFirestore.getInstance();
        back = findViewById(R.id.back);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST);
            }
        });


        final String fullname = getIntent().getStringExtra("fullname");
        final String photoUrl = getIntent().getStringExtra("photoUrl");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this,UserAccount.class  );
                intent.putExtra("fullname",fullname);
                intent.putExtra("photoUrl",photoUrl);
                startActivity(intent);
            }
        });
        uploadData();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = firstnameInput.getText().toString();
                String lastname = lastnameInput.getText().toString();
                String phone = phoneNo.getText().toString();
                User user = new User(firstname, lastname, profileImageUrl,phone, false, false);
                userDB.collection("Users").document(userId).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            uploadData();
                            Toast.makeText(EditProfile.this, "Data has been updated successfully successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(EditProfile.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

                if(resultUri!=null) {
                    final StorageReference filePath = userStorageReference.child(userId + ".jpg");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(EditProfile.this, "Profile Image " +
                                        "has been added successfully", Toast.LENGTH_SHORT).show();
                                filePath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri download = task.getResult();
                                        final String downloadedUrl = download.toString();
                                        profileImageUrl = downloadedUrl;
                                        if (downloadedUrl != null) {
                                            Picasso.get().load(downloadedUrl).into(profileImage);
                                        }
                                        else{
                                            Toast.makeText(EditProfile.this, "No photo was selected.", Toast.LENGTH_SHORT);
                                        }
                                    }
                                });


                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(EditProfile.this, message, Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        }

    }

    private void uploadData() {

        userDB.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        if(userId.equals(d.getId())) {
                            user = d.toObject(User.class);
                            if (user.getFirstname() != null) {
                                firstnameInput.setText(user.getFirstname());
                            } else {
                                firstnameInput.setText("No first name available");
                            }
                            if (user.getLastname() != null) {
                                lastnameInput.setText(user.getLastname());
                            } else {
                                firstnameInput.setText("No last name available");
                            }
                            if (user.getPhoneNo() != null) {
                                phoneNo.setText(user.getPhoneNo());
                            } else {
                                phoneNo.setText("No phone number exists");
                            }
                            if (user.getProfileUrl() != null) {
                                Picasso.get().load(user.getProfileUrl()).into(profileImage);
                            }
                        }
                    }
                }
            }
        });
    }
}


