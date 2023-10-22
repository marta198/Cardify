package com.application.cardify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TabTraded extends Fragment {

    private String title;
    private CardAdapter cardAdapter;
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

        Button interestingBtn = view.findViewById(R.id.InterestingChip);
        Button maybeBtn = view.findViewById(R.id.MaybeChip);
        Button notInterestingBtn = view.findViewById(R.id.NotInterestingChip);
        List<Button> filterButtons = new ArrayList<>();
        filterButtons.add(interestingBtn);
        filterButtons.add(maybeBtn);
        filterButtons.add(notInterestingBtn);


        for (Button item:filterButtons) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Button> NotItList = new ArrayList<>(filterButtons);
                    NotItList.remove(item);
                    for (Button notIt: NotItList) {
                        notIt.setSelected(false);
                    }
                    item.setSelected(!item.isSelected());
                    cardAdapter.getFilter().filter(item.getText().toString().toLowerCase().trim());
                }
            });
        }





    }

    List<String> choices = Arrays.asList("Interesting", "Maybe", "NotInteresting");

    ;
    private List<Card> createStaticCards() {
        List<Card> staticCards = new ArrayList<>();
        Random rand = new Random();
        for (int i = 1; i <= 13; i++) {;
            int randomIndex = rand.nextInt(choices.size());
            Card card = new Card(
                    "User " + i,
                    "Name " + i,
                    "Company " + i,
                    "Phone " + i,
                    "Email " + i,
                    "Website " + i,
                    "Address " + i,
                    "Image " + i,
                    "Logo " + i,
                    choices.get(randomIndex),
                    true
            );
            staticCards.add(card);
        }

        return staticCards;
    }

    private void displayStaticCards(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardAdapter = new CardAdapter(getContext(), myCurrentCards, false);
        recyclerView.setAdapter(cardAdapter);
    }


}
