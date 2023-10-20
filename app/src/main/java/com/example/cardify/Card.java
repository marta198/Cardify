package com.example.cardify;

import android.app.Activity;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class Card {

    private String nameSurname;
    private String companyName;
    private String phoneNumber;
    private String email;
    private String website;
    private String address;
    private String imageLink;
    private String logoLink;

    public Card(String nameSurname, String companyName, String phoneNumber, String email, String website, String address, String imageLink, String logoLink) {
        this.nameSurname = nameSurname;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.address = address;
        this.imageLink = imageLink;
        this.logoLink = logoLink;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public static void addMyCard(Card item, Activity activity) {
        SharedPreferences mPrefs = activity.getSharedPreferences("cardifyPrefs", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String result = gson.toJson(item);
        Set<String> set = mPrefs.getStringSet("MyCards", new HashSet<String>());
        set.add(result);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putStringSet("MyCards", set);
        prefsEditor.apply();
    }

    public static Set<Card> getMyCards(Activity activity) {
        SharedPreferences mPrefs = activity.getSharedPreferences("cardifyPrefs", Activity.MODE_PRIVATE);
        Set<Card> cardSet = new HashSet<>();
        Set<String> stringSet = mPrefs.getStringSet("MyCards", new HashSet<String>());
        Gson gson = new Gson();

        for (String item : stringSet) {
            Card obj = gson.fromJson(item, Card.class);
            cardSet.add(obj);
        }
        return cardSet;
    }
}
