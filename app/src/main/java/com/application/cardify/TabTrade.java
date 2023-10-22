package com.application.cardify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class TabTrade extends Fragment {

    private String title;

    public TabTrade(String title) {
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_trade, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleText = view.findViewById(R.id.TradeTitle);
        titleText.setText(this.title);

        Button goToQRScannerButton = view.findViewById(R.id.goto_qrscanner);


        goToQRScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRScanner.class);
                intent.putExtra("openScanner",true);
                startActivityForResult(intent,1);
            }
        });

    }

    public void onButtonShowPopupWindowClick(View view,String contents) {

        String fullName;
        String companyName;
        String email;
        String address;
        String phoneNumber;
        String websiteLink;
        String imageLink;
        String logoLink;
        final String[] importance = {"Maybe"};
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
            } else {
                logoLink = "";
                imageLink = "";
                address = "";
                websiteLink = "";
                email = "";
                phoneNumber = "";
                companyName = "";
                fullName = "";
            }
        } else {
            logoLink = "";
            imageLink = "";
            address = "";
            websiteLink = "";
            email = "";
            phoneNumber = "";
            companyName = "";
            fullName = "";
        }


        String[] choices = {"Interesting", "Maybe", "NotInteresting"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setTitle("Card scanned successfully!\nSelect Importance level.")
                .setPositiveButton("Save", (dialog, which) -> {
                    Card addedCard = new Card(
                            "User ",
                            fullName,
                            companyName,
                            phoneNumber,
                            email,
                            websiteLink,
                            address,
                            imageLink,
                            logoLink,
                            importance[0],
                            false
                    );;
                    Log.d("testData", importance[0]);
                })
                .setNegativeButton("Discard", (dialog, which) -> {

                })
                .setSingleChoiceItems(choices, 1, (dialog, which) -> {
                    importance[0] = choices[which];
                });

        AlertDialog dialog = builder.create();
        dialog.show();







    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String contents = data.getStringExtra("returnData");
        if (contents != null) {
            String[] split = contents.split("\\|");
            onButtonShowPopupWindowClick(getView(),contents);

        }
    }
}
