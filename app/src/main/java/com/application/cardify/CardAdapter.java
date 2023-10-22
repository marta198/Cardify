package com.application.cardify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> implements Filterable {
    private List<Card> cardList;
    private List<Card> cardListFull;
    private Context context;
    private boolean showEditButton;
    public CardAdapter(Context context, List<Card> cardList, boolean showEditButton) {
        this.context = context;
        this.cardList = cardList;
        this.cardListFull = new ArrayList<>(this.cardList);
        this.showEditButton = showEditButton;
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_card_adapter, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        final Card card = cardList.get(position);
        holder.companyName.setText(card.getCompanyName());

        // Check if the current card can show the edit button
        if (showEditButton) {
            holder.editCard.setVisibility(View.VISIBLE);
        } else {
            holder.editCard.setVisibility(View.GONE);
        }

        holder.textContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCardActivity(card);
            }
        });

        holder.editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditCardActivity(card);
            }
        });

        holder.deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCardFromFirebase(card);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public Filter getFilter() {
        return cardFilter;
    }

    private Filter cardFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Card> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(cardListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Card item : cardListFull) {
                    if (item.getImportance().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cardList.clear();
            cardList.addAll((List<Card>) results.values);
            notifyDataSetChanged();
        }
    };

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        View textContainer;
        View editCard;
        View deleteCard;

        public CardViewHolder(View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            textContainer = itemView.findViewById(R.id.textContainer_card_item);
            editCard = itemView.findViewById(R.id.edit_card);
            deleteCard = itemView.findViewById(R.id.delete_card);
        }
    }

    private void openCardActivity(Card card) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jsonCard = gson.toJson(card);
        Intent intent = new Intent(context, CardActivity.class);
        intent.putExtra("jsonCard", jsonCard);
        context.startActivity(intent);
    }

    private void openEditCardActivity(Card card) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String jsonCard = gson.toJson(card);
        Intent intent = new Intent(context, EditCard.class);

        // Pass the card data as extras
        intent.putExtra("jsonCard", jsonCard);
        intent.putExtra("cardName", card.getNameSurname());
        intent.putExtra("companyName", card.getCompanyName());
        intent.putExtra("phoneNumber", card.getPhoneNumber());
        intent.putExtra("email", card.getEmail());
        intent.putExtra("website", card.getWebsite());
        intent.putExtra("address", card.getAddress());

        context.startActivity(intent);
    }

    private void deleteCardFromFirebase(Card card) {
        // Implement the logic to delete the card from Firebase
        Toast.makeText(context, "Card deleted", Toast.LENGTH_SHORT).show();
        //add a confirmation popup where the user can click yes or no

    }
}
