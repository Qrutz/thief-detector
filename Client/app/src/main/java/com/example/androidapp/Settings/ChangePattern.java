package com.example.androidapp.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.androidapp.MQTT.MqttHandler;
import com.example.androidapp.R;
import com.example.androidapp.dbHandler;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ChangePattern extends AppCompatActivity {


    // Adapted from this library : https://github.com/aritraroy/PatternLockView
    private PatternLockView mPatternLockView;
    private static final String BROKER_URL = "tcp://10.0.2.2:1883";
    private static final String CLIENT_ID = "SentinelApp";
    private MqttHandler mqttHandler;
    ImageView backArrow;
    Button SubmitButton;
    String StoredPattern;
    List<PatternLockView.Dot> convertPattern;


    // pattern listener
    private final PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
            // if pattern is greater than 3, make it green ie its long enough for our purposes IMO :=)
            if (progressPattern.size() >= 3) {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
            } else {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
            }
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));

        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpattern_layout);

        dbHandler db = new dbHandler(getApplicationContext());
        App app = db.getApp();
        User user = app.currentUser();


          // get the currently stored pattern
        StoredPattern = user.getCustomData().get("pattern").toString();

        // convert our string pattern to list of dots
        convertPattern = new ArrayList<>();
        for (int i = 0; i < StoredPattern.length(); i++) {
            int dotId = Character.getNumericValue(StoredPattern.charAt(i));
            PatternLockView.Dot dot = PatternLockView.Dot.of(dotId);
            convertPattern.add(dot);
        }






        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        // return to settings page
        backArrow = findViewById(R.id.back_button_setPatternscreen);
        backArrow.setOnClickListener(view -> finish());

        // pattern listener
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        // set the starting pattern to the currently stored pattern
        mPatternLockView.setPattern(mPatternLockView.getPatternViewMode(), convertPattern);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);






        // submit pattern button
        SubmitButton = findViewById(R.id.submitButton);
        SubmitButton.setOnClickListener(view -> {

            String pattern = PatternLockUtils.patternToString(mPatternLockView, mPatternLockView.getPattern());

            // check if pattern is greater than 3, else return
            if (pattern.length() < 3) {
                Toast.makeText(getApplicationContext(), "Pattern needs to be => 3", Toast.LENGTH_SHORT).show();
                return;
            }

            // publish to broker ie save it
            String topic = "SentinelApp/pattern";
            publishPattern(topic, pattern);

            // update db
            MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
            MongoDatabase mongoDatabase = mongoClient.getDatabase("SeeedDB");
            MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("UserData");
            Document queryFilter = new Document().append("user-id", user.getId());
            Document updateDocument = new Document().append("$set", new Document().append("pattern", pattern));
            mongoCollection.updateOne(queryFilter, updateDocument).getAsync(result -> {
                if (result.isSuccess()) {
                    Log.v("EXAMPLE", "Updated document");
                    // invalidate the user object to refresh
                    user.refreshCustomData();
                    // go back to settings page
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("EXAMPLE", "Unable to update document. Error: " + result.getError());
                }
            });


            // clear pattern
            mPatternLockView.clearPattern();
        });


    }

    // publish pattern to broker
    private void publishPattern(String topic, String message) {
        Toast.makeText(getApplicationContext(), "New pattern is saved :)", Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic, message);
    }





}
