package com.application.cardify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>  implements Filterable {
    private List<Card> cardList;
    private  List<Card> cardListFull;
    private Context context;

    public CardAdapter(Context context, List<Card> cardList) {
        this.context = context;
        this.cardList = cardList;
        this.cardListFull = new ArrayList<>(this.cardList);
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

            if (constraint ==null || constraint.length() == 0){
                filteredList.addAll(cardListFull);
            }
            else {
               String filterPattern = constraint.toString().toLowerCase().trim();

                for (Card item:cardListFull) {
                    if (item.getImportance().toLowerCase().contains(filterPattern)){
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
            cardList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

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
