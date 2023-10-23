package com.application.cardify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> implements Filterable {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cardify-402213-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    DatabaseReference cardsRef = databaseReference.child("cards");
    List<Card> cardList;
    private List<Card> filteredList = new ArrayList<>();
    Context context;
    boolean showEditButton;
    public CardAdapter(Context context, List<Card> cardList, boolean showEditButton) {
        this.context = context;
        this.cardList = cardList;
        this.filteredList = new ArrayList<>(cardList);
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
        final Card card = filteredList.get(position);
        holder.nameSurname.setText(card.getNameSurname());
        holder.companyName.setText(card.getCompanyName());
        holder.email.setText(card.getEmail());

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
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return cardFilter;
    }

    private Filter cardFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null || constraint.length() == 0) {
                filteredList.clear();
                filteredList.addAll(cardList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                filteredList.clear();
                for (Card item : cardList) {
                    if (item.getImportance().toString().toLowerCase().trim().equals(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        };
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Card> templist = new ArrayList<>();
            templist.addAll((List<Card>) results.values);

            filteredList.clear();
            filteredList.addAll(templist);
            notifyDataSetChanged();
        }
    };

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView nameSurname, companyName, email;
        View textContainer;
        View editCard;
        View deleteCard;

        public CardViewHolder(View itemView) {
            super(itemView);
            nameSurname = itemView.findViewById(R.id.nameSurname);
            companyName = itemView.findViewById(R.id.companyName);
            email = itemView.findViewById(R.id.email);
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
        Intent intent = new Intent(context, EditCard.class);

        // Pass the card data as extras
        intent.putExtra("nameSurname", card.getNameSurname());
        intent.putExtra("companyName", card.getCompanyName());
        intent.putExtra("phoneNumber", card.getPhoneNumber());
        intent.putExtra("email", card.getEmail());
        intent.putExtra("website", card.getWebsite());
        intent.putExtra("address", card.getAddress());
        intent.putExtra("image", card.getImage());
        intent.putExtra("bgImage", card.getBgImage());
        intent.putExtra("cardKey", card.getKey()); // Pass the card key

        context.startActivity(intent);
    }

    private void deleteCardFromFirebase(Card card) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Card");
        builder.setMessage("Are you sure you want to delete this card?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cardsRef.child(card.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(context, "Card deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete the card", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
