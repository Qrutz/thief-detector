package com.example.androidapp;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.example.androidapp.History.HistoryActivity;
import com.example.androidapp.Settings.SettingsActivity;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import io.realm.mongodb.App;

public class MainActivity extends AppCompatActivity {

    dbHandler db;
    App app;

    LinearLayout homeButton;
    LinearLayout settingsButton;
    LinearLayout historyButton;
    LinearLayout placeHolderbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
       userViewModel.editPasscode("1234");
       Toast.makeText(getApplicationContext(), "Successfully updated passcode", Toast.LENGTH_SHORT).show(
         );});

    }
}
