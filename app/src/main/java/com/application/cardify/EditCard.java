package com.application.cardify;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    private String newLogoImageUrl;
    private String newBackgroundImageUrl;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        nameEditText = findViewById(R.id.nameEditText_editCard);
        companyNameEditText = findViewById(R.id.companyName_editCard);
        phoneField = findViewById(R.id.phone_editCard);
        emailField = findViewById(R.id.email_editCard);
        websiteField = findViewById(R.id.website_editCard);
        addressField = findViewById(R.id.address_editCard);
        logoImageView = findViewById(R.id.companyLogo_editCard);
        backgroundImage = findViewById(R.id.backgroundImage_editCard);
        chooseBg = findViewById(R.id.choosebg_editCard);

        String jsonCard = getIntent().getStringExtra("jsonCard");
        String nameSurname = getIntent().getStringExtra("nameSurname");
        String companyName = getIntent().getStringExtra("companyName");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String email = getIntent().getStringExtra("email");
        String website = getIntent().getStringExtra("website");
        String address = getIntent().getStringExtra("address");
        String bgImage = getIntent().getStringExtra("bgImage");
        String logo = getIntent().getStringExtra("image");
        String key = getIntent().getStringExtra("cardKey");

        nameEditText.setText(nameSurname);
        companyNameEditText.setText(companyName);
        phoneField.setText(phoneNumber);
        emailField.setText(email);
        websiteField.setText(website);
        addressField.setText(address);

        loadImageFromUrl(logo, logoImageView);
        loadImageFromUrl(bgImage, backgroundImage);

        chooseBg.setOnClickListener(view -> showImageUploadDialog("Background Image URL"));
        logoImageView.setOnClickListener(view -> showImageUploadDialog("Company Logo Image URL"));

        Button saveBtn = findViewById(R.id.saveBtn_editCard);
        saveBtn.setOnClickListener(view -> saveEditedCard(key, bgImage, logo));

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
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
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
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(jsonCard, Card.class);
    }

    private void saveEditedCard(String key, String bgImage, String logo) {
        String fullName = nameEditText.getText().toString();
        String company = companyNameEditText.getText().toString();
        String phone = phoneField.getText().toString();
        String email = emailField.getText().toString();
        String website = websiteField.getText().toString();
        String address = addressField.getText().toString();

        Card existingCard = new Card(
                currentUser.getEmail(),
                fullName,
                company,
                phone,
                email,
                website,
                address,
                bgImage,
                logo,
                "Important",
                true
        );

        if (existingCard != null) {
            existingCard.setNameSurname(fullName);
            existingCard.setCompanyName(company);
            existingCard.setPhoneNumber(phone);
            existingCard.setEmail(email);
            existingCard.setWebsite(website);
            existingCard.setAddress(address);

            if (newLogoImageUrl != null) {
                existingCard.setImage(newLogoImageUrl);
            }

            if (newBackgroundImageUrl != null) {
                existingCard.setBgImage(newBackgroundImageUrl);
            }

            DatabaseReference cardRef = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference("cards");
            cardRef.child(key).setValue(existingCard);

            Toast.makeText(this, "Card saved", Toast.LENGTH_SHORT).show();

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
                        newBackgroundImageUrl = enteredUrl;
                        loadImageFromUrl(newBackgroundImageUrl, backgroundImage);
                    } else if (dialogTitle.equals("Company Logo Image URL")) {
                        newLogoImageUrl = enteredUrl;
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
}
