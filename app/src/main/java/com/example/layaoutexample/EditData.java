package com.example.layaoutexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditData extends AppCompatActivity {


    private static final String TAG = "EditDataActivity";
    private Button btnSAve, btnDelete;
    private EditText editableitem;
    DataBaseHelper mDatabaseHelper;
    String date, hours;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        btnSAve = (Button) findViewById(R.id.updateButton);
        btnDelete = (Button) findViewById(R.id.deleteButton);
        editableitem = findViewById(R.id.editData);
        mDatabaseHelper = new DataBaseHelper(this);
        setTitle("Config");

        //get Intent extra from the list activity
        Intent recive = getIntent();
        hours = recive.getStringExtra("hours");
        id = Integer.parseInt(recive.getStringExtra("id"));

        hours = hours.substring(hours.indexOf("(") + 1, hours.indexOf(")"));

        editableitem.setText(hours);


        btnSAve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hoursToSave = Integer.parseInt(String.valueOf(editableitem.getText()));

                if (!(hoursToSave == 0)) {
                    mDatabaseHelper.updateHours(hoursToSave, id, 0);

                    Intent intent = new Intent(EditData.this, ListDataActivity.class);
                    startActivity(intent);


                } else {

                    toastMessage("Yo most enter a valid Data");

                }


            }
        });





    }


    private void toastMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}