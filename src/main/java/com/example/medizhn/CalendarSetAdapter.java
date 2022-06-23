package com.example.medizhn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

/**
 * Η παρούσα κλάση είναι υπεύθυνη για την δυναμική διαρρύθμιση των καρτών.
 */
public class CalendarSetAdapter extends RecyclerView.Adapter<CalendarSetAdapter.ViewHolder>
{
    //Μεταβήτές που αποθηκεύουν τα δεδομενα που πρέπει να εμφανιστούν
    HashMap<Integer,CardsData> cardsData;
    Context context;
    public int position=-1;

    /**
     * Κατασκευαστής κλάσης
     * @param cardsData πίνακας κατακερματισμού με τα δεδομένα
     * @param activity το activity στο οποίο παρουσιάζονται τα δεδομένα
     * */
    public CalendarSetAdapter(HashMap<Integer,CardsData> cardsData, CalendarSet activity)
    {
        this.cardsData = cardsData;
        this.context = activity;
    }

    /**
     * Setter κλάσης
     * @param cardsData πίνακας κατακερματισμού με τα δεδομένα
     * @param activity το activity στο οποίο παρουσιάζονται τα δεδομένα
     * */
    public void CalendarSetAdapterSetter(HashMap<Integer,CardsData> cardsData, CalendarSet activity){
        this.cardsData = cardsData;
        this.context = activity;
    }


    /**
     * Κλάση που κρατά τα δεδομένα που πρόκειται να εμφανιστούν στο CalendarSet
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        //Στοιχεία που εμφανίζουν το κείμενο της κάρτας
        TextView itemTitle, itemDates;

        /**
         * Κατασκευαστής κλάσης
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDates = itemView.findViewById(R.id.item_secondaryText);
            position = getAdapterPosition();
            /**
             * Ακροατής γεγονότος κλικ σε συγκεκριμένη κάρτα.
             * Ενημερώνει την θέση της κάρτας.
             */
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
        //@Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.card_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

       // @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final CardsData cardsDataList = cardsData.get(position);
            holder.itemTitle.setText(cardsDataList.getTitle());
            holder.itemDates.setText(cardsDataList.getSecondaryText());
        }

    /**
     * Getter του πλήθους καρτών μέσω υπολογισμού μεγέθους του πίνακα κατακερματισμου
     * @return int πλήθος καρτών μέσω υπολογισμού μεγέθους του πίνακα κατακερματισμου
     * */
        //@Override
        public int getItemCount() {
            return cardsData.size();
        }

    /**
     * Getter της θέσης συγκεκριμένης κάρτας
     * @return int position θέση κάρτας
     */
    public int getPosition() {
        return position;
    }
}


