package com.example.androidapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.example.androidapp.History.HistoryActivity;
import com.example.androidapp.Settings.SettingsActivity;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.mongodb.App;

import java.security.Permission;
import java.util.Date;
import android.Manifest;




public class MainActivity extends AppCompatActivity {

    dbHandler db;
    App app;

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;
    LinearLayout placeHolderbutton;

    EditText telNum;  //d
    FloatingActionButton telBtn;  //d
    static int code= 100; //d
    private static final int PERMISSION_CODE = 100;//d





    @Override
    protected void onCreate(Bundle savedInstanceState) {   //d
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText phoneEditText = findViewById(R.id.editTextPhone); //d
        telBtn = findViewById(R.id.callbtn); //d


        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }

        telBtn.setOnClickListener(new View.OnClickListener() { //d
            @Override
            public void onClick(View view) {

                String telNum =phoneEditText.getText().toString();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData (Uri.parse("tel:"+telNum));
                startActivity(i);
            }
        });






        db = new dbHandler(getApplicationContext());
        app = db.getApp();

        if (app.currentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), StarterPage.class);
            startActivity(intent);
            finish();
        }

        UserViewModel userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);



        // HOME BUTTON SETTINGS
        homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(view -> {
            // start alarm status activity
            Intent intent = new Intent(MainActivity.this, AlarmStatusActivity.class);
            startActivity(intent);
        });

        // SETTINGS BUTTON SETTINGS
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> {
            // start settings activity
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
        );

        // HISTORY BUTTON SETTINGS
        historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(view -> {
            // start history activity
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivity(intent);
        });

        // PLACEHOLDER BUTTON SETTINGS
        placeHolderbutton = findViewById(R.id.placeholder_button);
        placeHolderbutton.setOnClickListener(view -> {
            //userViewModel.getUser().getValue().getBreakins().forEach(breakin -> {
            //Log.v(breakin.get("location").toString(), breakin.get("date").toString());
        //});
         });

    }
}
