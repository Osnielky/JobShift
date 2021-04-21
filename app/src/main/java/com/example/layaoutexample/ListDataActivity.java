package com.example.layaoutexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    DataBaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_listdata_activity);
        mListView = (ListView) findViewById(R.id.mListView);
        mDatabaseHelper = new DataBaseHelper(this);
        setTitle("My records");
        populateListView();
    }

    private void populateListView() {

        Cursor data = mDatabaseHelper.getData();

        ArrayList<String> listData = new ArrayList<>();

        while (data.moveToNext()) {
            // get the value from the database in column one
            listData.add(data.getString(0) + "     " + data.getString(1) + "--" + "     (" + data.getString(2) + ") h");


        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String var = parent.getItemAtPosition(position).toString();


                toastMessage(String.valueOf(position));
                Log.i("--------", String.valueOf(position));

                Intent editScreenIntent = new Intent (ListDataActivity.this, EditData.class);
                editScreenIntent.putExtra("id",String.valueOf((position+1)));
                editScreenIntent.putExtra("hours",(var));
                startActivity(editScreenIntent);


            }
        });


    }

    private void toastMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

}