package com.example.cardify;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private List<Card> myCurrentCards;

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

        String fullName = "" ;
        String companyName= "" ;
        String email = "";
        String address = "";
        String phoneNumber = "";
        String websiteLink = "";
        String imageLink = "";
        String logoLink= "" ;
        // inflate the layout of the popup window
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.popup_window, null);
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
        }



        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        TextView phoneText =(TextView) popupWindow.getContentView().findViewById(R.id.phone);
        TextView emailText =(TextView) popupWindow.getContentView().findViewById(R.id.email);
        TextView companynameText =(TextView) popupWindow.getContentView().findViewById(R.id.companyName);
        phoneText.setText(phoneNumber);
        emailText.setText(email);
        companynameText.setText(companyName);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

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
