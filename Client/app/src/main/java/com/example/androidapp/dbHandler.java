package com.example.androidapp;

import android.content.Context;
import io.realm.Realm;
import io.realm.mongodb.App;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import org.bson.Document;


public class dbHandler {

    // should probably hide this later :)
    private final String APP_ID = "seeedsentinelrealm-nfhhn";
    private App app;
    private static User user;
    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private CurrentUserData currentUserData;

    static MongoCollection<Document> mongoCollection;

    public dbHandler(Context context) {
        Realm.init(context);
        app = new App(new AppConfiguration.Builder(APP_ID).build());



    }

    public App getApp() {
        return app;
    }


    // maybe this is a bad idea since we can just use currentUser().getCustomData()
    public CurrentUserData getCurrentUserData() {
        return currentUserData;
    }

    //method to update users pattern
    public static void updateUserPattern(User user, String pattern) {
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("SeeedDB");
        mongoCollection = mongoDatabase.getCollection("UserData");
        Document queryFilter = new Document().append("user-id", user.getId());
        Document updateDocument = new Document().append("$set", new Document().append("pattern", pattern));
        mongoCollection.updateOne(queryFilter, updateDocument).getAsync(result -> {
            if (result.isSuccess()) {
                System.out.println("Successfully updated document.");
                // invalidate the user object to refresh
                user.refreshCustomData();
            } else {
                System.err.println("Failed to update document with: " + result.getError().getErrorMessage());
            }
        });

    }


}










