package com.example.cardify;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
        String data = "Name: " + card.getNameSurname() + "\nCompany: " + card.getCompanyName()
                + "\nEmail: " + card.getEmail() + "\nAddress: " + card.getAddress()
                + "\nPhone: " + card.getPhoneNumber() + "\nWebsite: " + card.getWebsite()
                + "\nImage: " + card.getImageLink() + "\nLogo: " + card.getLogoLink();

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            int qrCodeSize = 400;
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeField.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
