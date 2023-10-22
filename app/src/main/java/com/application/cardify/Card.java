package com.application.cardify;

import android.app.Activity;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import java.util.HashSet;
import java.util.Set;

public class Card {

    private String user; // New variable for user's email
    private String nameSurname;
    private String companyName;
    private String phoneNumber;
    private String email;
    private String website;
    private String address;
    private String bgImage; // Variable for background image
    private String image; // Variable for the image
    private String importance; // Variable for importance
    private boolean isOwner; // Variable to indicate if the user is the owner

    public Card(String user, String nameSurname, String companyName, String phoneNumber, String email, String website, String address, String bgImage, String image, String importance, boolean isOwner) {
        this.user = user;
        this.nameSurname = nameSurname;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.website = website;
        this.address = address;
        this.bgImage = bgImage;
        this.image = image;
        this.importance = importance;
        this.isOwner = isOwner;
    }

    public String getUser() {
        return user;
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

    public String getBgImage() {
        return bgImage;
    }

    public String getImage() {
        return image;
    }

    public String getImportance() {
        return importance;
    }

    public boolean getIsOwner() { return isOwner; }

    public void setUser(String user) {
        this.user = user;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
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

    public static void addTradedCard(Card item, Activity activity) {
        SharedPreferences mPrefs = activity.getSharedPreferences("cardifyPrefs", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String result = gson.toJson(item);
        Set<String> set = mPrefs.getStringSet("TradedCards", new HashSet<String>());
        set.add(result);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putStringSet("TradedCards", set);
        prefsEditor.apply();
    }

    public static Set<Card> getTradedCards(Activity activity) {
        SharedPreferences mPrefs = activity.getSharedPreferences("cardifyPrefs", Activity.MODE_PRIVATE);
        Set<Card> cardSet = new HashSet<>();
        Set<String> stringSet = mPrefs.getStringSet("TradedCards", new HashSet<String>());
        Gson gson = new Gson();

        for (String item : stringSet) {
            Card obj = gson.fromJson(item, Card.class);
            cardSet.add(obj);
        }
        return cardSet;
    }
}
