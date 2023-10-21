package com.application.cardify;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.nio.charset.StandardCharsets;

public class CardActivity extends AppCompatActivity {

    //create a fake card
    private Card card = new Card("John Doe", "ĀČĒĢĪĶĻŅŠŪŽ Company", "2222222", "test@test.com", "1234 Example Street, City, State, 12345", "https://www.example.com", "https://www.example.com/image.png", "https://www.example.com/logo.png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card);

        Button backBtn = findViewById(R.id.view_btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        TextView companyNameField = findViewById(R.id.cardCompanyName_viewCard);
        TextView phoneField = findViewById(R.id.phone_viewCard);
        TextView emailField = findViewById(R.id.email_viewCard);
        TextView websiteField = findViewById(R.id.website_viewCard);
        TextView addressField = findViewById(R.id.address_viewCard);
        ImageView logoField = findViewById(R.id.companyLogo_viewCard);
        ImageView qrCodeField = findViewById(R.id.cardQrCode_viewCard);


        // Get the Card object from the intent
//        card = (Card) getIntent().getSerializableExtra("CardObject");

        // Set the text for your TextViews from the Card object
        companyNameField.setText(card.getCompanyName());
        phoneField.setText(card.getPhoneNumber());
        emailField.setText(card.getEmail());
        websiteField.setText(card.getWebsite());
        addressField.setText(card.getAddress());
//        logoField.setText(card.getLogoLink());

        // Generate and display the QR code
        generateAndDisplayQRCode(qrCodeField);
    }

    private void generateAndDisplayQRCode(ImageView qrCodeField) {
        try {
            String name = "Name: " + card.getNameSurname();
            String companyName = "Company: " + card.getCompanyName();
            String email = "Email: " + card.getEmail();
            String address = "Address: " + card.getAddress();
            String phone = "Phone: " + card.getPhoneNumber();
            String website = "Website: " + card.getWebsite();
            String image = "Image: " + card.getImageLink();
            String logo = "Logo: " + card.getLogoLink();

            // Combine the individual data strings
            String data = name + "\n" + companyName + "\n" + email + "\n" + address
                    + "\n" + phone + "\n" + website + "\n" + image + "\n" + logo;

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

}
