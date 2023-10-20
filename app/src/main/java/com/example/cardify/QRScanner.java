package com.example.cardify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.nio.charset.StandardCharsets;

public class QRScanner extends AppCompatActivity {

    Button scanButton;
    TextView textView;
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
        setContentView(R.layout.activity_qrscanner);

        scanButton = findViewById(R.id.scanButton);
        textView = findViewById(R.id.qrCodeText);

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

        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String contents = intentResult.getContents();
                if (contents != null) {
                    String[] split = contents.split("\\|");
                    if (split.length == 5) {
                        fullName = split[0];
                        companyName = split[1];
                        email = split[2];
                        address = split[3];
                        phoneNumber = split[4];
                        websiteLink = split[5];
                        imageLink = split[6];
                        logoLink = split[7];
                    }
                    textView.setText(contents);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
