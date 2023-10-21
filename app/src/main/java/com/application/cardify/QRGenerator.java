package com.application.cardify;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.util.DisplayMetrics;
import android.graphics.Point;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.nio.charset.StandardCharsets;

public class QRGenerator extends AppCompatActivity {

    private String fullName;
    private String companyName;
    private String email;
    private String address;
    private String phoneNumber;
    private String websiteLink;
    private String imageLink;
    private String logoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);

        // Temporary data that can later be replaced with data from the database
        fullName = "John Doe";
        companyName = "ĀČĒĢĪĶĻŅŠŪŽ Company";
        email = "john.doe@example.com";
        phoneNumber = "123-456-7890";
        address = "1234 Example Street, City, State, 12345";
        websiteLink = "https://www.example.com";
        imageLink = "https://www.example.com/image.png";
        logoLink = "https://www.example.com/logo.png";

        ImageView imageView = findViewById(R.id.qr_img_trade);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            Card card = gson.fromJson(extras.getString("jsonCard"), Card.class);
            fullName = card.getNameSurname();
            companyName = card.getCompanyName();
            email = card.getEmail();
            phoneNumber = card.getPhoneNumber();
            address = card.getAddress();
            websiteLink = card.getWebsite();
            imageLink = card.getImageLink();
            logoLink = card.getLogoLink();
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int qrCodeSize = screenWidth - 100;

        String data = "Name: " + fullName + "\nCompany: " + companyName + "\nEmail: " + email + "\nAddress: " + address
                + "\nPhone: " + phoneNumber + "\nWebsite: " + websiteLink + "\nImage: " + imageLink + "\nLogo: " + logoLink;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1), BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
