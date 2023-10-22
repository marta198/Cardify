package com.application.cardify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanner extends AppCompatActivity {

    Button scanButton;
    TextView textView;
    private String user; // New variable for the user's email
    private String fullName;
    private String companyName;
    private String email;
    private String address;
    private String phoneNumber;
    private String websiteLink;
    private String bgImage; // Variable for background image URL
    private String logoLink;
    private boolean straightTrought = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        scanButton = findViewById(R.id.scanButton);
        textView = findViewById(R.id.qrCodeText);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            Gson gson = builder.create();
            if (extras.getBoolean("openScanner")) {
                straightTrought = true;
                IntentIntegrator intentIntegrator = new IntentIntegrator(QRScanner.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan the trading card QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setBeepEnabled(false); // Disable the beep sound, can be enabled for debugging
                intentIntegrator.initiateScan();
            }
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(QRScanner.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan the trading card QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setBeepEnabled(false); // Disable the beep sound, can be enabled for debugging
                intentIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (straightTrought) {
            // Handle the result directly without user interaction
            if (intentResult != null && intentResult.getContents() != null) {
                String contents = intentResult.getContents();
                setResult(resultCode, getIntent().putExtra("returnData", contents));
                finish();
                parseQRCodeContents(contents);
            }
        } else {
            // Handle the result with user interaction
            if (intentResult != null && intentResult.getContents() != null) {
                String contents = intentResult.getContents();
                parseQRCodeContents(contents);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void parseQRCodeContents(String contents) {
        if (contents != null) {
            String[] split = contents.split("\\|");
            if (split.length == 9) {
                user = split[0]; // New variable for the user's email
                fullName = split[1];
                companyName = split[2];
                email = split[3];
                address = split[4];
                phoneNumber = split[5];
                websiteLink = split[6];
                bgImage = split[7]; // New variable for background image URL
                logoLink = split[8];

                // Update the UI or save the data to Firebase as needed
                textView.setText(contents);
            }
        }
    }
}
