package com.example.cardify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        Button goToQRGeneratorButton = view.findViewById(R.id.goto_qrgenerator);


        goToQRScannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRScanner.class);
                startActivity(intent);
            }
        });

        goToQRGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QRGenerator.class);
                startActivity(intent);
            }
        });
    }
}
