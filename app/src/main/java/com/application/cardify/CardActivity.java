package com.application.cardify;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;

public class CardActivity extends AppCompatActivity {

    Card card; // Store the Card object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card);

        Button backBtn = findViewById(R.id.view_btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            card = gson.fromJson(extras.getString("jsonCard"), Card.class);
        }

        // Initialize your TextViews and other views
        TextView nameField = findViewById(R.id.cardNameSurname_viewCard);
        TextView companyNameField = findViewById(R.id.cardCompanyName_viewCard);
        TextView phoneField = findViewById(R.id.phone_viewCard);
        TextView emailField = findViewById(R.id.email_viewCard);
        TextView websiteField = findViewById(R.id.website_viewCard);
        TextView addressField = findViewById(R.id.address_viewCard);
        ImageView logoField = findViewById(R.id.companyLogo_viewCard);
        ImageView bgField = findViewById(R.id.backgroundImage_viewCard);
        ImageView qrCodeField = findViewById(R.id.cardQrCode_viewCard);

        // Set the text for your TextViews from the Card object
        nameField.setText(card.getNameSurname());
        companyNameField.setText(card.getCompanyName());
        phoneField.setText(card.getPhoneNumber());
        emailField.setText(card.getEmail());
        websiteField.setText(card.getWebsite());
        addressField.setText(card.getAddress());

        loadImageFromUrl(card.getImage(), logoField);
        loadImageFromUrl(card.getBgImage(), bgField);
        // Generate and display the QR code
        generateAndDisplayQRCode(qrCodeField);
    }

    private void generateAndDisplayQRCode(ImageView qrCodeField) {
        try {
            String fullName = card.getNameSurname();
            String companyName = card.getCompanyName();
            String email = card.getEmail();
            String address = card.getAddress();
            String phone = card.getPhoneNumber();
            String website = card.getWebsite();
            String bgImage = card.getBgImage();
            String logo = card.getImage();

            // Combine the individual data strings
            String data = fullName + "\n" + companyName + "\n" + email + "\n" + address
                    + "\n" + phone + "\n" + website + "\n" + bgImage + "\n" + logo;

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            int qrCodeSize = 400;

            // Encode the data using UTF-8 character encoding
            BitMatrix bitMatrix = multiFormatWriter.encode(new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeField.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromUrl(String imageUrl, ImageView imageView) {
        try {
            Picasso.get().load(imageUrl).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
