package com.application.cardify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class AddNew extends AppCompatActivity {

    EditText nameEditText;
    EditText companyNameEditText;
    EditText phoneField;
    EditText emailField;
    EditText websiteField;
    EditText addressField;
    ImageView logoImageView;
    ImageView backgroundImage;
    Button chooseBg;
    String selectedBackgroundImageUrl;
    String companyLogoUrl;

    // Firebase variables
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    DatabaseReference cardsRef = databaseReference.child("cards");
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        // Initialize your UI elements
        nameEditText = findViewById(R.id.nameEditText_addCard);
        companyNameEditText = findViewById(R.id.companyName_addCard);
        phoneField = findViewById(R.id.phone_addCard);
        emailField = findViewById(R.id.email_addCard);
        websiteField = findViewById(R.id.website_addCard);
        addressField = findViewById(R.id.address_addCard);
        logoImageView = findViewById(R.id.companyLogo_addCard);
        backgroundImage = findViewById(R.id.backgroundImage_addCard);
        chooseBg = findViewById(R.id.choosebg_addCard);

        Button saveBtn = findViewById(R.id.saveBtn_addCard);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard();
            }
        });

        // Set up the company logo click listener
        logoImageView.setOnClickListener(view -> showImageUploadDialog(companyLogoUrl, "Company Logo Image URL"));

        // Set up the chooseBg button click listener
        chooseBg.setOnClickListener(view -> showImageUploadDialog(selectedBackgroundImageUrl, "Background Image URL"));

        Button cancelBtn = findViewById(R.id.cancelBtn_addCard);
        cancelBtn.setOnClickListener(view -> {
            nameEditText.setText("");
            companyNameEditText.setText("");
            phoneField.setText("");
            emailField.setText("");
            websiteField.setText("");
            addressField.setText("");
            logoImageView.setImageResource(0);
            backgroundImage.setImageResource(0);
            finish();
        });
    }

    private void saveCard() {
        String fullName = nameEditText.getText().toString();
        String company = companyNameEditText.getText().toString();
        String phone = phoneField.getText().toString();
        String email = emailField.getText().toString();
        String website = websiteField.getText().toString();
        String address = addressField.getText().toString();

        // Check if all inputs are entered
        if (fullName.isEmpty() || company.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                website.isEmpty() || address.isEmpty()) {
            showAlertDialog("Please fill in all required fields.");
        } else if (selectedBackgroundImageUrl == null || selectedBackgroundImageUrl.isEmpty()) {
            showAlertDialog("Please provide a background image URL.");
        } else if (companyLogoUrl == null || companyLogoUrl.isEmpty()) {
            showAlertDialog("Please provide a company logo URL.");
        } else {
            String user = currentUser.getEmail();

            DatabaseReference newCardRef = cardsRef.push();
            String cardKey = newCardRef.getKey();
            Card newCard = new Card(user, fullName, company, phone, email, website, address, selectedBackgroundImageUrl, companyLogoUrl, "Important", true);
            newCard.setKey(cardKey);
            newCardRef.setValue(newCard);
            Toast.makeText(AddNew.this, "Card created", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showImageUploadDialog(String imageUrl, String dialogTitle) {
        // Create an AlertDialog to allow the user to input the image URL
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(dialogTitle);

        final EditText input = new EditText(this);
        input.setHint("Enter Image URL");
        input.setText(imageUrl); // Pre-fill the current URL if available
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredUrl = input.getText().toString();
                if (isValidUrl(enteredUrl)) {
                    if (dialogTitle.equals("Company Logo Image URL")) {
                        companyLogoUrl = enteredUrl;
                        loadImageFromUrl(companyLogoUrl, logoImageView);
                    } else if (dialogTitle.equals("Background Image URL")) {
                        selectedBackgroundImageUrl = enteredUrl;
                        loadImageFromUrl(selectedBackgroundImageUrl, backgroundImage);
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

    // Check if a URL is valid
    private boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }

    // Load an image from a URL into an ImageView
    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        try {
            Picasso.get().load(imageUrl).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Incomplete Information");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
