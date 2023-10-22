package com.application.cardify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.cardify.Card;
import java.util.ArrayList;
import java.util.List;

public class TabCards extends Fragment {

    private String title;
    private List<Card> myCurrentCards;

    public TabCards(String title) {
        this.title = title;
    }

    public TabCards() {
        this.title = "My Cards";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView titleText = view.findViewById(R.id.MyCardsTitle);
        titleText.setText(this.title);
        AppCompatImageButton addNewBtn = view.findViewById(R.id.cardAddNew);

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNew.class);
                startActivity(intent);
            }
        });

        // Initialize the list of static cards
        myCurrentCards = createStaticCards();
        displayStaticCards(view);
    }


    private List<Card> createStaticCards() {
        List<Card> staticCards = new ArrayList<>();
        for (int i = 1; i <= 13; i++) {
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
                    "Important Notes " + i,
                    true
            );
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
