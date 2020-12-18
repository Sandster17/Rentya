package com.partypanda.partyrent.rentya.View.Search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.partypanda.partyrent.rentya.MainActivity;
import com.partypanda.partyrent.rentya.R;

public class EmptyResultList extends AppCompatActivity {

    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_result_list);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmptyResultList.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}