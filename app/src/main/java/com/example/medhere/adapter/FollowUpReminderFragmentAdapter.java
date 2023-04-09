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
import com.example.medhere.models.FollowUpReminder;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FollowUpReminderFragmentAdapter extends RecyclerView.Adapter<FollowUpReminderFragmentAdapter.HolderCallHistory>{

    private Context context;
    private ArrayList<FollowUpReminder> followUpReminders;
    private TextDrawable mDrawableBuilder;
    private DatabaseReference CallRef;

    public FollowUpReminderFragmentAdapter(Context context, ArrayList<FollowUpReminder> followUpReminders) {
        this.context = context;
        this.followUpReminders = followUpReminders;
    }

    @NonNull
    @Override
    public FollowUpReminderFragmentAdapter.HolderCallHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follow_ups_item_view_main, parent, false);

        return new FollowUpReminderFragmentAdapter.HolderCallHistory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowUpReminderFragmentAdapter.HolderCallHistory holder, int position) {

    FollowUpReminder reminder = followUpReminders.get(position);
    holder.doctorName.setText(reminder.getDoctorName());
    holder.dateOfFollowUp.setText(reminder.getDateOfVisit());
    holder.timeOfFollowUp.setText(reminder.getTimeOfVisit());

    }

    @Override
    public int getItemCount() {
        return followUpReminders.size();
    }


    class HolderCallHistory extends RecyclerView.ViewHolder{

        ImageView locationPic;
        TextView doctorName,dateOfFollowUp, timeOfFollowUp;
        Button edit, delete;

        public HolderCallHistory(@NonNull View itemView) {
            super(itemView);

            locationPic = itemView.findViewById(R.id.location);
            doctorName = itemView.findViewById(R.id.doctor_name);
            dateOfFollowUp = itemView.findViewById(R.id.date);
            timeOfFollowUp = itemView.findViewById(R.id.time);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);

        }
    }

}

