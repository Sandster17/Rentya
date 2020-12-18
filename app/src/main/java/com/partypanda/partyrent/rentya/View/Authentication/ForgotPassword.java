package com.partypanda.partyrent.rentya.View.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.partypanda.partyrent.rentya.R;
import com.partypanda.partyrent.rentya.View.Preview.Preview;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button send;
    private EditText emailInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        send = findViewById(R.id.confirm);
        auth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailInput.getText()!=null||!emailInput.getText().equals("")){
                    auth.sendPasswordResetEmail(emailInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this,"Please check your email inbox and click the link to change your password",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(ForgotPassword.this,"Please enter a valid email address.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}