package com.example.cardify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CardFragment extends Fragment {


    String NameSurname;
    String Description;
    String Email;
    String PhoneNumber;
    public CardFragment() {
        // Required empty public constructor
    }

    public CardFragment(Card item) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        this.NameSurname = item.NameSurname;
        this.Description = item.Description;
        this.Email = item.Email;
        this.PhoneNumber = item.PhoneNumber;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nameSurnameField = view.findViewById(R.id.cardNameSurname);
        TextView descriptionField = view.findViewById(R.id.cardDescription);
        TextView emailField = view.findViewById(R.id.cardEmail);
        TextView phoneField = view.findViewById(R.id.cardPhone);
        nameSurnameField.setText(this.NameSurname);
        descriptionField.setText(this.Description);
        emailField.setText(this.Email);
        phoneField.setText(this.PhoneNumber);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);
    }
}