package com.application.cardify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class OnButtonShowPopupDialogFragment extends DialogFragment {

    private String fullName;
    private String companyName;
    private String email;
    private String address;
    private String phoneNumber;
    private String websiteLink;
    private String bgImage;
    private String logoLink;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private DatabaseReference cardsRef;
    private final String[] importance = {"Maybe"};

    public static OnButtonShowPopupDialogFragment newInstance(String contents) {
        OnButtonShowPopupDialogFragment fragment = new OnButtonShowPopupDialogFragment();
        Bundle args = new Bundle();
        args.putString("contents", contents);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String contents = getArguments().getString("contents");

        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        cardsRef = databaseReference.child("cards");
        currentUser = firebaseAuth.getCurrentUser();

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
                Log.d("testData", fullName);
                Log.d("testData", companyName);
                Log.d("testData", email);
                Log.d("testData", address);
                Log.d("testData", phoneNumber);
                Log.d("testData", websiteLink);
                Log.d("testData", bgImage);
                Log.d("testData", logoLink);
            } else {
                Toast.makeText(getContext(), "Invalid QR Code Data. Please scan a valid trading card QR code.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Card scanned successfully!\nSelect Importance level.");

        String[] choices = {"Important", "Maybe", "Not Important"};
        builder.setSingleChoiceItems(choices, 1, (dialog, which) -> {
            importance[0] = choices[which];
        });

        builder.setPositiveButton("Save", (dialog, which) -> {
            Card newCard = new Card(
                    currentUser.getEmail(),
                    fullName,
                    companyName,
                    phoneNumber,
                    email,
                    websiteLink,
                    address,
                    bgImage,
                    logoLink,
                    importance[0],
                    false
            );
            Log.d("testData", importance[0]);

            Log.d("testData", "Saving card to Firebase...");

            // Save the card to Firebase
            DatabaseReference newCardRef = cardsRef.push();
            String cardKey = newCardRef.getKey();
            newCard.setKey(cardKey);
            newCardRef.setValue(newCard);

            Log.d("testData", "Card saved to Firebase");

            // Dismiss the dialog
            dismiss();
        });

        builder.setNegativeButton("Discard", (dialog, which) -> {
            // Handle the discard action
            dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Dismiss the dialog if the screen orientation has changed
        if (newConfig.orientation != Configuration.ORIENTATION_PORTRAIT) {
            dismiss();
        }
    }
}

