package com.example.androidarduino;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.util.List;




public class PlaniAdapter extends RecyclerView.Adapter<PlaniAdapter.PlaniViewHolder> implements View.OnClickListener {

    public PlaniAdapter(List<Medicine> planificationList, Context context) {
        this.planificationList = planificationList;
        this.context = context;
    }

    List<Medicine> planificationList;
    Context context;

    @NonNull
    @NotNull
    @Override
    public PlaniViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calendar,parent,false);
        PlaniViewHolder holder = new PlaniViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PlaniAdapter.PlaniViewHolder holder, int position) {

        Medicine plani = planificationList.get(position);
        holder.medicineName.setText(plani.getMedicine_name());




    }

    @Override
    public int getItemCount() {
        return planificationList.size();
    }

    @Override
    public void onClick(View v) {

    }


    public class PlaniViewHolder extends  RecyclerView.ViewHolder{
        TextView medicineName;


        public PlaniViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            medicineName = itemView.findViewById(R.id.pillName_row);

        }
    }
}
