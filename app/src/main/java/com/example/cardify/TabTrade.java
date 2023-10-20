package com.example.cardify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                startActivity(intent);
            }
        });


        myCurrentCards = createStaticCards();
        displayStaticCards(view);
    }


    private List<Card> createStaticCards() {
        List<Card> staticCards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
            Card card = new Card("Name " + i, "Company " + i, "Phone " + i, "Email " + i, "Website " + i, "Address " + i, "Image " + i, "Logo " + i);
            staticCards.add(card);
        }

        return staticCards;
    }

    private void displayStaticCards(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CardTradeAdapter cardTradeAdapter = new CardTradeAdapter(getContext(), myCurrentCards);
        recyclerView.setAdapter(cardTradeAdapter);
    }
}
