package com.example.medhere.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.medhere.R;
import com.example.medhere.models.MedicineReminder;
import com.example.medhere.models.PastRecordItem;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class PastRecordsAdapter extends RecyclerView.Adapter<PastRecordsAdapter.HolderCallHistory>{

    private Context context;
    private ArrayList<PastRecordItem> pastRecordItems;
    private TextDrawable mDrawableBuilder;
    private DatabaseReference CallRef;

    public PastRecordsAdapter(Context context, ArrayList<PastRecordItem> pastRecordItems) {
        this.context = context;
        this.pastRecordItems = pastRecordItems;
    }

    @NonNull
    @Override
    public PastRecordsAdapter.HolderCallHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.past_records_item_view, parent, false);

        return new PastRecordsAdapter.HolderCallHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastRecordsAdapter.HolderCallHistory holder, int position) {

    }

    @Override
    public int getItemCount() {
        return pastRecordItems.size();
    }


    class HolderCallHistory extends RecyclerView.ViewHolder{

        TextView doctorName, dateOfVisit;

        public HolderCallHistory(@NonNull View itemView) {
            super(itemView);

            doctorName = itemView.findViewById(R.id.doctor_name);
            dateOfVisit = itemView.findViewById(R.id.date_of_visit);

        }
    }

}

