package com.example.medizhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class PillRemainderAdapter extends RecyclerView.Adapter<PillRemainderAdapter.ViewHolder>
{
    //Variables storing data to display
    HashMap<Integer, CardsData> cardsData;
    Context context;
    public int position=-1;

    public PillRemainderAdapter(HashMap<Integer, CardsData> cardsData, PillRemainder activity)
    {
        this.cardsData = cardsData;
        this.context = activity;
    }

    public void PillRemainderAdapterSetter(HashMap<Integer, CardsData> cardsData, PillRemainder activity)
    {
        this.cardsData = cardsData;
        this.context = activity;
    }

    //Class that holds the items to be displayed in PillRemainder activity (Views in card layout)
    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemTitle, itemAmount;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemAmount = itemView.findViewById(R.id.item_secondaryText);
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
    public PillRemainderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_layout, parent, false);
        PillRemainderAdapter.ViewHolder viewHolder = new PillRemainderAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PillRemainderAdapter.ViewHolder holder, int position)
    {
        final CardsData cardsDataList = cardsData.get(position);
        holder.itemTitle.setText(cardsDataList.getTitle());
        holder.itemAmount.setText(cardsDataList.getSecondaryText());
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
