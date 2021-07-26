package com.example.androidarduino;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MedicineDayAdapter extends RecyclerView.Adapter<MedicineDayAdapter.MedicineViewHolder> implements View.OnClickListener{

    List<Medicine> medicineList;
    Context context;
    int selected;

    public MedicineDayAdapter(List<Medicine> medicineList, Context context, int selected) {
        this.medicineList = medicineList;
        this.context = context;
        this.selected = selected;
    }

    @NonNull
    @NotNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_calendar,parent,false);
        MedicineViewHolder holder = new MedicineViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MedicineDayAdapter.MedicineViewHolder holder, int position) {

        Medicine medicine = medicineList.get(position);
        holder.medicineName.setText(medicine.getMedicine_name());
        if(this.selected == position){
            holder.constraintLayout.setBackgroundColor(Color.rgb(30,201,198));
        }
        else {
            holder.constraintLayout.setBackgroundColor(Color.WHITE);
        }

        if(medicine.getTook().isEmpty()){
            holder.medicineTaken.setText("?");
        }else{
            holder.medicineTaken.setText(medicine.getTook());
        }
    }



    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView medicineName;
        ConstraintLayout constraintLayout;
        TextView medicineTaken;

        public MedicineViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            medicineTaken = itemView.findViewById(R.id.pillTaken_row);
            medicineName = itemView.findViewById(R.id.pillName_row);
            constraintLayout = itemView.findViewById(R.id.const_medDay_row);
        }
    }
}
