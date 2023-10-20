package com.example.cardify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TabTraded extends Fragment {

    private String title;
    private List<Card> myCurrentCards;
    public TabTraded(String title) {
        this.title = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_traded, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleText = view.findViewById(R.id.TradedTitle);
        titleText.setText(this.title);
        // Initialize the list of static cards
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
        CardAdapter cardAdapter = new CardAdapter(getContext(), myCurrentCards);
        recyclerView.setAdapter(cardAdapter);
    }


}