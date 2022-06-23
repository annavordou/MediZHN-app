package com.example.medizhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class MedicinesAdapter extends RecyclerView.Adapter<MedicinesAdapter.ViewHolder>
{
    //Variables storing data to display
    HashMap<Integer, CardsData> cardsData;
    Context context;
    public int position=-1;

    public MedicinesAdapter(HashMap<Integer, CardsData> cardsData, Medicines activity)
    {
        this.cardsData = cardsData;
        this.context = activity;
    }

    public void MedicinesSetAdapterSetter(HashMap<Integer,CardsData> cardsData, CalendarSet activity){
        this.cardsData = cardsData;
        this.context = activity;
    }

    //Class that holds the items to be displayed in Medicines activity (Views in card layout)
    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemTitle, itemDescription;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDescription = itemView.findViewById(R.id.item_secondaryText);
            position = getAdapterPosition();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    System.out.println(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_layout, parent, false);
        MedicinesAdapter.ViewHolder viewHolder = new MedicinesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final CardsData cardsDataList = cardsData.get(position);
        holder.itemTitle.setText(cardsDataList.getTitle());
        holder.itemDescription.setText(cardsDataList.getSecondaryText());
    }

    @Override
    public int getItemCount()
    {
        return cardsData.size();
    }

    public int getPosition() {
        return position;
    }
}
