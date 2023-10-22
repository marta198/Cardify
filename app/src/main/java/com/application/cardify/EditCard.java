package com.application.cardify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.application.cardify.Card;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class EditCard extends AppCompatActivity {

    EditText nameEditText;
    EditText companyNameEditText;
    EditText phoneField;
    EditText emailField;
    EditText websiteField;
    EditText addressField;
    ImageView logoImageView;
    ImageView backgroundImage;
    Button chooseBg;

    private String newLogoImageUrl;
    private String newBackgroundImageUrl;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        // Initialize your UI elements
        nameEditText = findViewById(R.id.nameEditText_editCard);
        companyNameEditText = findViewById(R.id.companyName_editCard);
        phoneField = findViewById(R.id.phone_editCard);
        emailField = findViewById(R.id.email_editCard);
        websiteField = findViewById(R.id.website_editCard);
        addressField = findViewById(R.id.address_editCard);
        logoImageView = findViewById(R.id.companyLogo_editCard);
        backgroundImage = findViewById(R.id.backgroundImage_editCard);
        chooseBg = findViewById(R.id.choosebg_editCard);

        // Retrieve the card data from the extras
        String jsonCard = getIntent().getStringExtra("jsonCard");
        String cardName = getIntent().getStringExtra("cardName");
        String companyName = getIntent().getStringExtra("companyName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String email = getIntent().getStringExtra("email");
        String website = getIntent().getStringExtra("website");
        String address = getIntent().getStringExtra("address");

        // Update the UI elements with the retrieved data
        nameEditText.setText(cardName);
        companyNameEditText.setText(companyName);
        phoneField.setText(phoneNumber);
        emailField.setText(email);
        websiteField.setText(website);
        addressField.setText(address);

        // Load the existing background and company logo images
        loadImageFromUrl(jsonCard, logoImageView);
        loadImageFromUrl(jsonCard, backgroundImage);

        chooseBg.setOnClickListener(view -> showImageUploadDialog("Background Image URL"));

        Button saveBtn = findViewById(R.id.saveBtn_editCard);
        saveBtn.setOnClickListener(view -> saveEditedCard());

        Button cancelBtn = findViewById(R.id.cancelBtn_editCard);
        cancelBtn.setOnClickListener(view -> finish());
    }

    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        if (isValidUrl(imageUrl)) {
            try {
                Picasso.get().load(imageUrl).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case where the URL is invalid
            // You can show an error message or provide default image
        }
    }

    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    private Card parseJsonToCard(String jsonCard) {
        // Use Gson or your preferred JSON library to parse the JSON string back to a Card object
        // Example:
        // Gson gson = new Gson();
        // return gson.fromJson(jsonCard, Card.class);
        return null; // Replace with your actual code
    }

    private void saveEditedCard() {
        // Get the edited data from the EditText fields
        String fullName = nameEditText.getText().toString();
        String company = companyNameEditText.getText().toString();
        String phone = phoneField.getText().toString();
        String email = emailField.getText().toString();
        String website = websiteField.getText().toString();
        String address = addressField.getText().toString();

        // Retrieve the existing card data from the intent
        String jsonCard = getIntent().getStringExtra("jsonCard");
        Card existingCard = parseJsonToCard(jsonCard);

        if (existingCard != null) {
            // Update the existing card with the edited data
            existingCard.setNameSurname(fullName);
            existingCard.setCompanyName(company);
            existingCard.setPhoneNumber(phone);
            existingCard.setEmail(email);
            existingCard.setWebsite(website);
            existingCard.setAddress(address);
            existingCard.setBgImage(newBackgroundImageUrl);
            existingCard.setImage(newLogoImageUrl);

            // Save the edited card back to the database or perform any necessary updates
            // You may use Firebase Realtime Database or your preferred method
            updateCardInDatabase(existingCard);

            // Finish the activity to return to the previous screen
            finish();
        } else {
            showAlertDialog("Failed to load card data for editing.");
        }
    }
    private void showImageUploadDialog(String dialogTitle) {
        // Create an AlertDialog to allow the user to input the image URL
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(dialogTitle);

        final EditText input = new EditText(this);
        input.setHint("Enter Image URL");
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredUrl = input.getText().toString();
                if (isValidUrl(enteredUrl)) {
                    if (dialogTitle.equals("Background Image URL")) {
                        newBackgroundImageUrl = enteredUrl; // Update the new background image URL
                        loadImageFromUrl(newBackgroundImageUrl, backgroundImage);
                    } else if (dialogTitle.equals("Company Logo Image URL")) {
                        newLogoImageUrl = enteredUrl; // Update the new company logo image URL
                        loadImageFromUrl(newLogoImageUrl, logoImageView);
                    }
                } else {
                    showAlertDialog("Invalid URL. Please provide a valid image URL.");
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }


    private boolean isValidData(String fullName, String company, String phone, String email, String website, String address) {
        // Check if all required fields are filled
        return !fullName.isEmpty() && !company.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !website.isEmpty() && !address.isEmpty();
    }

    private void showAlertDialog(String message) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle("Incomplete Information");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void updateCardInDatabase(Card card) {
        // Perform database update here
        // Example: Save the edited card data back to Firebase Realtime Database
        // DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference("your_card_data");
        // cardRef.child(card.getId()).setValue(card);
    }
}
