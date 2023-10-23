package com.application.cardify;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class TabTraded extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    DatabaseReference cardsRef = databaseReference.child("cards");
    private String title;
    private CardAdapter cardAdapter;
    View fragmentView;
    private List<Card> dynamicCards;

    private Button importantBtn, maybeBtn, notImportantBtn;
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
        fragmentView = view;
        TextView titleText = view.findViewById(R.id.TradedTitle);
        titleText.setText(this.title);

        createDynamicCards();

        importantBtn = view.findViewById(R.id.InterestingChip);
        maybeBtn = view.findViewById(R.id.MaybeChip);
        notImportantBtn = view.findViewById(R.id.NotInterestingChip);
        List<Button> filterButtons = new ArrayList<>();
        filterButtons.add(importantBtn);
        filterButtons.add(maybeBtn);
        filterButtons.add(notImportantBtn);

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
                    if (item.isSelected()){
                        cardAdapter.getFilter().filter(item.getText().toString().toLowerCase().trim());
                    }
                    else {
                        cardAdapter.getFilter().filter("");
                    }

                }
            });
        }

    }

    List<String> choices = Arrays.asList("Interesting", "Maybe", "NotInteresting");

    ;

    public void createDynamicCards() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dynamicCards = new ArrayList<>();
                Random rand = new Random();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Card card = ds.getValue(Card.class);
                    String cardKey = ds.getKey();
                    card.setKey(cardKey);
                    if (card.getUser().equals(currentUser.getEmail()) && !card.getIsOwner()) {
                        dynamicCards.add(card);
                    }
                }
                filterAndDisplayDynamicCards();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to get data.", Toast.LENGTH_SHORT).show();
            }
        };
        cardsRef.addValueEventListener(valueEventListener);
    }

    public void displayDynamicCards(List<Card> dynamicCards) {
        RecyclerView recyclerView = fragmentView.findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cardAdapter = new CardAdapter(getContext(), dynamicCards, false);
        recyclerView.setAdapter(cardAdapter);
    }

    public void filterAndDisplayDynamicCards() {
        List<Card> filteredCards = new ArrayList<>();

        for (Card card : dynamicCards) {
            if (isCardFiltered(card)) {
                filteredCards.add(card);
            }
        }
        displayDynamicCards(filteredCards);
    }

    public boolean isCardFiltered(Card card) {
        String selectedImportance = getSelectedImportance();
        if (selectedImportance.isEmpty()) {
            return true; // No filter applied, show all cards
        } else {
            return card.getImportance().equalsIgnoreCase(selectedImportance);
        }
    }

    public String getSelectedImportance() {
        if (importantBtn.isSelected()) {
            return "Important";
        } else if (maybeBtn.isSelected()) {
            return "Maybe";
        } else if (notImportantBtn.isSelected()) {
            return "Not Important";
        } else {
            return "";
        }
    }
}
