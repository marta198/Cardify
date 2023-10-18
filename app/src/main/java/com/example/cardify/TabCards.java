package com.example.cardify;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import com.example.cardify.Card;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;


public class TabCards extends Fragment {

    private String title;
    Set<Card> myCards;
    Set<Card> myCurrentCards;
    public TabCards(String title) {
        this.title = title;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_tab_cards, container, false);
            return view;

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
        myCurrentCards = new HashSet<>();
        myCards = Card.getMyCards(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();
        myCards = Card.getMyCards(getActivity());
        for (Card item:myCards) {
            boolean isAdded = false;
            for (Card currentCard:myCurrentCards) {
                if(currentCard.toString().equals(item.toString())) isAdded=true;
            }
            if (!isAdded){
                myCurrentCards.add(item);
                Log.d("testData", Integer.toString(myCurrentCards.size()));
                loadFragment(new CardFragment(item),getContext());
            }

        }
    }

    private void loadFragment(Fragment fragment, Context mContext) {
        ScrollView fragmentsLayout = (ScrollView)getView().findViewById(R.id.scrollView);
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.linearLayout, fragment)
                .commit();
    }
}