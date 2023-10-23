package com.application.cardify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
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
import java.util.List;

public class TabCards extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    DatabaseReference cardsRef = databaseReference.child("cards");
    private String title;
    private View fragmentView;

    public TabCards(String title) {
        this.title = title;
    }

    public TabCards() {
        this.title = "My Cards";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_cards, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentView = view;
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

        createDynamicCards();
    }

    public void createDynamicCards() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Card> dynamicCards = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    Card card = ds.getValue(Card.class);
                    String cardKey = ds.getKey();
                    card.setKey(cardKey);
                    if (card.getUser().equals(currentUser.getEmail()) && card.getIsOwner()) {
                        dynamicCards.add(card);
                    }
                }
                displayDynamicCards(dynamicCards);
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
        CardAdapter cardAdapter = new CardAdapter(getContext(), dynamicCards, true);
        recyclerView.setAdapter(cardAdapter);
    }
}
