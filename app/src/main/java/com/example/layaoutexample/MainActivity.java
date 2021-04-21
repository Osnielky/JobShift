package com.example.layaoutexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Switch switchOne;
    Chronometer chronometer;
    ImageButton btStart, btStop, btSave, buttonSaveAdjust, infoImageButton,buttonSettings;
    //-------------------------------
    private boolean isResume;
    Handler handler;
    long tMilliSec, tStart, tBuff, tUpdate = 0L;
    int sec, min, milliSec;
    //-------------------------------
//database
    DataBaseHelper mDataBaseHelper;
    ConstraintLayout constraintLayoutsecund;
    Button timeButton, timeButtonfinish;
    int hourstart, minutestart, hourFinish, minuteFinish;
    String startTimeString = "START :   ";
    String finishTimeString = "END :  ";
    TextView actualDate, timeRunning, textTimeadjust;
    int timeToSave;     //this variable will store de daily time in hours

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_main);




//*********************----Navigation code----*****************************************


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    toastMessage("Home");

                    Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                    startActivity(intent);

                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                toastMessage("Home");
                return true;

            }
        });


//******************************************************************************
        chronometer = findViewById(R.id.chronometer);
        btStart = findViewById(R.id.bt_start);
        btStop = findViewById(R.id.bt_stop);
        btSave = findViewById(R.id.bt_save);
        buttonSaveAdjust = findViewById(R.id.buttonSaveAdjust);
        hourstart = 0;
        minutestart = 0;
        hourFinish = 0;
        minuteFinish = 0;
        timeButton = findViewById(R.id.timeButton);
        timeButtonfinish = findViewById(R.id.timeButtonfinish);
        timeButton.setText(startTimeString + String.format(Locale.getDefault(), "%02d:%02d", 8, 00));
        timeButtonfinish.setText(finishTimeString + String.format(Locale.getDefault(), "%02d:%02d", 5, 00));
        actualDate = findViewById(R.id.actualDate);
        textTimeadjust = findViewById(R.id.textTimeadjust);
        switchOne = findViewById(R.id.switch1);
        infoImageButton = findViewById(R.id.infoImageButton);
        buttonSettings = findViewById(R.id.buttonSettings);
        constraintLayoutsecund = findViewById(R.id.constraintLayoutsecund);
        getDate();
        mDataBaseHelper = new DataBaseHelper(this);

        setTitle("Home");
        buttonSaveAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("this", "saving the data");
                passDatatoSave();
            }
        });


        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Config.class);
                startActivity(intent);


            }
        });



        infoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);


            }
        });


// this button is going to save the time worked on the dataBase in the first Step
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                tMilliSec = 0L;
                tStart = 0L;
                tBuff = 0L;
                tUpdate = 0L;
                sec = 0;
                min = 0;
                milliSec = 0;
                chronometer.setText("00:00:00");
                passDatatoSave();


            }
        });


        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isResume) {
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    chronometer.start();
                    isResume = true;
                    btStop.setVisibility(View.GONE);
                    btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                } else {

                    tBuff += tMilliSec;
                    handler.removeCallbacks(runnable);
                    chronometer.stop();
                    isResume = false;
                    btStop.setVisibility(View.VISIBLE);
                    btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));

                }
            }
        });


        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isResume) {
                    btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    tMilliSec = 0L;
                    tStart = 0L;
                    tBuff = 0L;
                    tUpdate = 0L;
                    sec = 0;
                    min = 0;
                    milliSec = 0;
                    chronometer.setText("00:00:00");
                }
            }
        });


        handler = new Handler();
        enableSecundPart();
        switchOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("switch", String.valueOf(switchOne.isChecked()));
                enableSecundPart();
            }
        });
    }


    public void passDatatoSave() {

        String actualDate = getDate();

        AddData(actualDate, timeToSave);

    }


    public void AddData(String newEntry, int value) {

        boolean insertData = mDataBaseHelper.addData(newEntry, value);
        if (insertData) {
            toastMessage("Data Successfully Inserted");

        } else {
            toastMessage("Something went wrong!");

        }


    }


    //method shows the timer on the screen
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {

            int Myhours;

            tMilliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMilliSec;
            timeToSave = (int) (tUpdate / 3.6e+6);
            sec = (int) (tUpdate / 1000);
            min = sec / 60;
            Myhours = min / 60;
            sec = sec % 60;
            milliSec = (int) (tUpdate % 100);
            chronometer.setText(String.format("%02d", Myhours) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
            handler.postDelayed(this, 60);

        }
    };


    public void enableSecundPart() {

        Log.i("-------------", String.valueOf(timeToSave));


        if (switchOne.isChecked() == false) {

            constraintLayoutsecund.setEnabled(false);

            for (int i = 0; i < constraintLayoutsecund.getChildCount(); i++) {
                View child = constraintLayoutsecund.getChildAt(i);
                child.setEnabled(false);
            }
        } else {

            for (int i = 0; i < constraintLayoutsecund.getChildCount(); i++) {
                View child = constraintLayoutsecund.getChildAt(i);
                child.setEnabled(true);
            }
        }
    }

    public void calculateTimeAdjust() { //this method will calculate the time and store it in the time to save variable

        int total = (hourFinish - minuteFinish) - (hourstart + minutestart);
        timeToSave = total;
        if (timeToSave > 0) {
            textTimeadjust.setText(String.valueOf(timeToSave) + " H");

        } else {
            timeToSave = 0;
            Toast.makeText(getApplicationContext(), "Please, You need to review the Hours ", Toast.LENGTH_LONG).show();


        }


    }

    public String getDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM-dd-yyyy");
        String datetime = dateformat.format(c.getTime());
        actualDate.setText(datetime);
        return datetime;
    }

    public void popTimePicker(View view) {

        int style = AlertDialog.THEME_HOLO_DARK;

        if (view.getId() == R.id.timeButton) {

            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hourstart = selectedHour;
                    minutestart = selectedMinute;
                    timeButton.setText(startTimeString + String.format(Locale.getDefault(), "%02d:%02d", hourstart, minutestart));
                }
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hourstart, minutestart, true);
            timePickerDialog.setTitle("Select Start");
            timePickerDialog.show();
        }

        if (view.getId() == R.id.timeButtonfinish) {

            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                    hourFinish = selectedHour;
                    minuteFinish = selectedMinute;
                    timeButtonfinish.setText(finishTimeString + String.format(Locale.getDefault(), "%02d:%02d", hourFinish, minuteFinish));
                    calculateTimeAdjust();
                }
            };


            TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hourFinish, minuteFinish, true);
            timePickerDialog.setTitle("Select finish");
            timePickerDialog.show();


        }


    }


    private void toastMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


}