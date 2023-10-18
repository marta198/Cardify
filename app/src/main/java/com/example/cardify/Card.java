package com.example.cardify;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class Card {

    String NameSurname;
    String Description;
    String Email;
    String PhoneNumber;
    public Card(String nameSurname, String description, String email, String phoneNumber) {
        this.NameSurname = nameSurname;
        this.Description = description;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }

    @NonNull
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public  static void addMyCard(Card item,Activity activity){
        SharedPreferences mPrefs = activity.getSharedPreferences("cardifyPrefs",MODE_PRIVATE);
        Gson gson = new Gson();
        String result = gson.toJson(item);
        Set<String> set = mPrefs.getStringSet("MyCards", new HashSet<String>());
        Log.d("testData", set.toString());
        set.add(result);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.clear();

        Log.d("testData", set.toString());
        prefsEditor.putStringSet("MyCards",set);
        prefsEditor.commit();
        set = mPrefs.getStringSet("MyCards", new HashSet<String>());
        Log.d("testData", set.toString());
    }

    public static Set<Card> getMyCards(Activity activity) {

        SharedPreferences mPrefs = activity.getSharedPreferences("cardifyPrefs",MODE_PRIVATE);
        Set<Card> cardSet = new HashSet<Card>();
        Set<String> stringSet;
        stringSet = mPrefs.getStringSet("MyCards", new HashSet<String>());
        Log.d("testData", stringSet.toString());
        Gson gson = new Gson();

        for (String item:stringSet) {
        Card obj = gson.fromJson(item, Card.class);
        cardSet.add(obj);
        }
        return cardSet;
    }


}
