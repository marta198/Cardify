package com.example.cardify;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.cardify.Card;

public class AddNew extends AppCompatActivity {

    EditText nameEditText;
    EditText companyNameEditText;
    EditText phoneField;
    EditText emailField;
    EditText websiteField;
    EditText addressField;
    ImageView logoImageView;
    ImageView qrCodeImageView;
    ImageView backgroundImage;

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
        qrCodeImageView = findViewById(R.id.qrCode_addCard);
        backgroundImage = findViewById(R.id.backgroundImage_addCard);

        Button saveBtn = findViewById(R.id.saveBtn_addCard);
        saveBtn.setOnClickListener(view -> saveCard());
    }

    private void saveCard(){
        String fullName = nameEditText.getText().toString();
        String company = companyNameEditText.getText().toString();
        String phone = phoneField.getText().toString();
        String email = emailField.getText().toString();
        String website = websiteField.getText().toString();
        String address = addressField.getText().toString();

//        Card c = new Card(fullName, company, phone, email, website, address);
//        Card.addMyCard(c, this);
    }
}
