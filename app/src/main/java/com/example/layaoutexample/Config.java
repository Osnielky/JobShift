package com.example.layaoutexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Config extends AppCompatActivity {
    EditText textName, TextEmployerName, textHourRate;
    Button btnSave, btnExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        textName = findViewById(R.id.textName);
        TextEmployerName = findViewById(R.id.textHourRate);
        textHourRate = findViewById(R.id.textHourRate);
        btnSave = findViewById(R.id.saveConfig);
        btnExit = findViewById(R.id.exitconfigButton);


        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Config.this, MainActivity.class);
                startActivity(intent);


            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {








            }
        });



    }
}