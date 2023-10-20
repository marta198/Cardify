package com.example.cardify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class CardTradeAdapter extends RecyclerView.Adapter<CardTradeAdapter.CardViewHolder> {
    private List<Card> cardList;
    private Context context;

    public CardTradeAdapter(Context context, List<Card> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_card_trade_adapter, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final Card card = cardList.get(position);
        holder.companyName.setText(card.getCompanyName());
        Log.d("testData", card.getCompanyName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String jsonCard = gson.toJson(card);
                // Get the activity context to start the new activity
                Context context = view.getContext();

                // Create an Intent to start the AddNew activity
                Intent intent = new Intent(context, CardActivity.class);
                intent.putExtra("jsonCard",jsonCard);
                // Start the new activity
                context.startActivity(intent);
            }
        });
        holder.itemView.findViewById(R.id.genQR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                String jsonCard = gson.toJson(card);

                Intent intent = new Intent(context, QRGenerator.class);
                intent.putExtra("jsonCard",jsonCard);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        // Add other TextViews for card data

        public CardViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            // Initialize other TextViews
        }
    }
}
