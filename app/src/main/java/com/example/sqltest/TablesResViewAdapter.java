package com.example.sqltest;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TablesResViewAdapter extends RecyclerView.Adapter<TablesResViewAdapter.ViewHolder> {
    private final ArrayList<String> items = new ArrayList<>();
    private final boolean isNested;

    public TablesResViewAdapter(boolean isNested) {
        this.isNested=isNested;
    }

    @NonNull
    @Override
    public TablesResViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TablesResViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(position==0) {
            holder.removeButton.setVisibility(View.GONE);
            holder.itemHolder.setBackgroundColor(ContextCompat.getColor(holder.itemHolder.getContext(), R.color.purple_200));
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemHolder.getLayoutParams();
            params.setMargins(0,0,0,0);
            holder.itemHolder.setLayoutParams(params);
        }else{
            holder.removeButton.setVisibility(View.VISIBLE);
            holder.itemHolder.setBackgroundColor(ContextCompat.getColor(holder.itemHolder.getContext(), R.color.teal_200));
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.itemHolder.getLayoutParams();
            params.setMargins(10,5,10,5);
            holder.itemHolder.setLayoutParams(params);
        }
        if(isNested) {
            holder.itemName.setTextSize(15);
        }
        holder.itemName.setText(items.get(position));
        if(!isNested && position!=0) {
            holder.addButton.setVisibility(View.VISIBLE);
            holder.itemHolder.setOnClickListener(view -> {
                if (holder.tableDataRecView.getVisibility() == View.VISIBLE) {
                    holder.tableDataRecView.setVisibility(View.GONE);
                } else {
                    holder.tableDataRecView.setVisibility(View.VISIBLE);

                    TablesResViewAdapter tablesResViewAdapter = new TablesResViewAdapter(true);
                    holder.tableDataRecView.setAdapter(tablesResViewAdapter);
                    holder.tableDataRecView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    try {
                        ArrayList<String> rs = OracleCon.createQuery("SELECT * FROM " + holder.itemName.getText().toString().toLowerCase());
                        if (rs.size() >= 1)
                            tablesResViewAdapter.setItems(rs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.addButton.setOnClickListener(view -> {
                if(holder.addNewItem.getVisibility()==View.VISIBLE){
                    holder.addNewItem.setVisibility(View.GONE);
                }else{
                    holder.addNewItem.setVisibility(View.VISIBLE);
                }
            });
            holder.buttAddNew.setOnClickListener(view -> {
                try {
                    OracleCon.createQuery("INSERT INTO " + holder.itemName.getText().toString().toLowerCase() +
                            "VALUES (" + holder.edtTxtAddNew.getText().toString() + ")");
                    Log.e("TEXT","INSERT INTO " + holder.itemName.getText().toString().toLowerCase()+"VALUES ("+holder.edtTxtAddNew.getText().toString()+")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.addNewItem.setVisibility(View.GONE);
                holder.edtTxtAddNew.setText("");
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final ConstraintLayout itemHolder;
        private final RecyclerView tableDataRecView;
        private final ImageView addButton;
        private final ConstraintLayout addNewItem;
        private final Button buttAddNew;
        private final EditText edtTxtAddNew;
        private final ImageView removeButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemHolder = itemView.findViewById(R.id.itemHolder);
            tableDataRecView = itemView.findViewById(R.id.tableDataRecView);
            addButton = itemView.findViewById(R.id.addButton);
            addNewItem = itemView.findViewById(R.id.addNewItem);
            buttAddNew = itemView.findViewById(R.id.buttAddNew);
            edtTxtAddNew = itemView.findViewById(R.id.edtTxtAddNew);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
    public void setItems(ArrayList<String> list){
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }
}
