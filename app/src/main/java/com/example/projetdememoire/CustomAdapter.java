package com.example.projetdememoire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
     private ArrayList PackageName;
    private ArrayList AppName;
    private ArrayList TimeSpent;
CustomAdapter(Context context, ArrayList PackageName, ArrayList AppName, ArrayList TimeSpent){
    this.context=context;
    this.PackageName = PackageName;
    this.AppName = AppName;
    this.TimeSpent=TimeSpent;
}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
      View view =  inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     holder.packageNameX.setText((String.valueOf(PackageName.get(position))));
        holder.AppNameX.setText((String.valueOf(AppName.get(position))));
        holder.timeSpentX.setText((String.valueOf(TimeSpent.get(position))));

    }

    @Override
    public int getItemCount() {
       return PackageName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView packageNameX , AppNameX,timeSpentX;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            packageNameX = itemView.findViewById(R.id.packageName);
            AppNameX = itemView.findViewById(R.id.AppName);
            timeSpentX = itemView.findViewById(R.id.timeSpent);
        }
    }
}
