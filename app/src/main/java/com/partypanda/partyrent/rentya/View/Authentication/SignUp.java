package com.partypanda.partyrent.rentya.View.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.partypanda.partyrent.rentya.R;

public class SignUp extends AppCompatActivity {

    private Button next;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText verifyPassword;
    private TextView back;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        next = findViewById(R.id.next);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone_number);
        password = findViewById(R.id.password);
        verifyPassword = findViewById(R.id.verify_password);
        back = findViewById(R.id.back);
        auth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount()
    {
        String emailInput = email.getText().toString();
        String  passwordInput = password.getText().toString();
        final String phoneInput = phone.getText().toString();
        String confirmPasswordInput = verifyPassword.getText().toString();
        boolean newAccount = true;

        if(TextUtils.isEmpty(emailInput)||emailInput==null)
        {
            Toast.makeText(this,"Please enter your valid email address",Toast.LENGTH_SHORT).show();
            newAccount = false;
        }
        if(TextUtils.isEmpty(passwordInput))
        {
            Toast.makeText(this,"Please enter a password with 6 or more characters " +
                    "and with at least 6 digits",Toast.LENGTH_SHORT).show();
            newAccount = false;
        }
        else
        {
            if(!isPasswordValid(passwordInput))
            {
                newAccount = false;
                Toast.makeText(this,"Password is not valid, Please ensure that password is more that 6" +
                        " characters long and has at least 1 digit.",Toast.LENGTH_SHORT).show();
            }
        }

        if(TextUtils.isEmpty(confirmPasswordInput))
        {
            Toast.makeText(this,"Please confirm your password",Toast.LENGTH_SHORT).show();
            newAccount = false;

        }

        if(!passwordInput.equals(confirmPasswordInput))
        {
            newAccount = false;
            Toast.makeText(this,"Password is not the same",Toast.LENGTH_SHORT).show();
        }

            if(newAccount==true)
        {
            auth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SignUp.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, SetUpAccount.class);
                        intent.putExtra("phone",phoneInput);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(SignUp.this, "Error with creating " +
                                "account.Please try again." + task.getException(), Toast.LENGTH_SHORT).show();

                    /*    error.setText(task.getException().getMessage());
                        error.setVisibility(View.VISIBLE);
                    */
                    }
                }
            });
        }
        else
        {
        /*    error.setText("There has been an error.Please make sure that you have entered a valid email and password");
            error.setVisibility(View.VISIBLE);
        */
        }
    }
    private boolean isPasswordValid(String password)
    {
        boolean valid = false;
        int digits = 0;
        int letters = 0;
        if(password.length()>=6)
        {
            for(int i =0;i < password.length();i++)
            {
                if(Character.isDigit(password.charAt(i)))
                {
                    digits++;
                }
                if(Character.isLetter(password.charAt(i)))
                {
                    letters++;
                }
            }
            if(letters<password.length()&&digits>0&&digits<password.length())
            {
                valid = true;
            }
        }
        return valid;
    }





}