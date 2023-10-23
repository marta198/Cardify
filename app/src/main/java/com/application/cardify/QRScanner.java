package com.application.cardify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanner extends AppCompatActivity {

    Button scanButton, backButton;

    private String fullName;
    private String companyName;
    private String email;
    private String address;
    private String phoneNumber;
    private String websiteLink;
    private String bgImage;
    private String logoLink;
    private String importance;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference cardsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        cardsRef = databaseReference.child("cards");
        currentUser = firebaseAuth.getCurrentUser();
        scanButton = findViewById(R.id.scanButton);
        backButton = findViewById(R.id.backButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(QRScanner.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan the trading card QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setBeepEnabled(false); // Disable the beep sound
                intentIntegrator.initiateScan();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null && intentResult.getContents() != null) {
            String contents = intentResult.getContents();
            parseQRCodeContents(contents);
        }
    }

    private void parseQRCodeContents(String contents) {
        if (contents != null) {
            String[] split = contents.split("\n");
            if (split.length == 8) {
                fullName = split[0];
                companyName = split[1];
                email = split[2];
                address = split[3];
                phoneNumber = split[4];
                websiteLink = split[5];
                bgImage = split[6];
                logoLink = split[7];

                // Display the data or save it to the database
                showImportanceDialog();
            } else {
                showAlertDialog("Invalid QR Code Data. Please scan a valid trading card QR code.");
            }
        }
    }

    private void showImportanceDialog() {
        final String[] importanceLevels = {"Important", "Maybe", "Not Important"};
        final Spinner importanceSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, importanceLevels);
        importanceSpinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Importance Level");
        builder.setView(importanceSpinner);
        builder.setPositiveButton("Save Card", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                importance = importanceLevels[importanceSpinner.getSelectedItemPosition()];
                saveCardToDatabase();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void saveCardToDatabase() {
        Card newCard = new Card(
                currentUser.getEmail(),
                fullName,
                companyName,
                email,
                address,
                phoneNumber,
                websiteLink,
                bgImage,
                logoLink,
                importance,
                false
        );

        DatabaseReference newCardRef = cardsRef.push();
        String cardKey = newCardRef.getKey();
        newCard.setKey(cardKey);
        newCardRef.setValue(newCard);

        Toast.makeText(this, "Card saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}

