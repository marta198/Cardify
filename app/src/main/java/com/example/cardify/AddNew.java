package com.example.cardify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.cardify.Card;

import java.util.HashSet;
import java.util.Set;

public class AddNew extends AppCompatActivity {

    EditText nameSurnameField;
    EditText descriptionField;
    EditText phoneField;
    EditText emailField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        nameSurnameField = findViewById(R.id.nameSurname);
        descriptionField = findViewById(R.id.description);
        phoneField = findViewById(R.id.phoneNr);
        emailField = findViewById(R.id.email);


        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(view -> saveCard());
    }

    private void saveCard(){
        Card c = new Card(nameSurnameField.getText().toString(),descriptionField.getText().toString(),phoneField.getText().toString(),emailField.getText().toString());
        Card.addMyCard(c,this);
    }
}