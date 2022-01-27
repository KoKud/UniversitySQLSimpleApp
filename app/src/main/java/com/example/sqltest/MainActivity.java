package com.example.sqltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
        TablesResViewAdapter.removeCL = findViewById(R.id.rUSureWindow);
        TablesResViewAdapter.removeBtn = findViewById(R.id.btnAcceptRemoval);

        RecyclerView tables = findViewById(R.id.recycleView);
        TablesResViewAdapter tablesResViewAdapter = new TablesResViewAdapter(false);
        tables.setAdapter(tablesResViewAdapter);
        tables.setLayoutManager(new LinearLayoutManager(this));

        try {
            ArrayList<String> rs = OracleCon.createQuery("SELECT table_name FROM user_tables");
            tablesResViewAdapter.setItems(rs);
        }catch (Exception e){
            e.printStackTrace();
        }

        ConstraintLayout searchBar = findViewById(R.id.newQueryLayout);
        EditText searchQuery = findViewById(R.id.queryName);
        Button searchBtn = findViewById(R.id.acceptButton);
        Button cancelRemoval = findViewById(R.id.btnCancelRemoval);
        FloatingActionButton searchFlABtn = findViewById(R.id.floatingNewStatement);

        ConstraintLayout rUSureWindow = findViewById(R.id.rUSureWindow);
        ConstraintLayout detailedView = findViewById(R.id.resultsContainer);
        TextView queryTitle = findViewById(R.id.txtQueryTitle);
        TextView txtServerAnswer = findViewById(R.id.txtServerAnswer);
        ImageView btnClose = findViewById(R.id.btnCloseResults);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailedView.setVisibility(View.GONE);
            }
        });
        searchFlABtn.setOnClickListener(view -> {
            if(searchBar.getVisibility()==View.VISIBLE){
                searchBar.setVisibility(View.GONE);
            }else{
                searchBar.setVisibility(View.VISIBLE);
            }
        });
        searchBtn.setOnClickListener(view -> {
            try {
                ArrayList<String> answer = OracleCon.createQuery(searchQuery.getText().toString());
                Log.i("QUERY",searchQuery.getText().toString());

                StringBuilder stringBuilder = new StringBuilder();
                for (String i : answer){
                    stringBuilder.append(i);
                    stringBuilder.append('\n');
                }
                detailedView.setVisibility(View.VISIBLE);
                queryTitle.setText(searchQuery.getText().toString());
                txtServerAnswer.setText(stringBuilder.toString());
                txtServerAnswer.setMovementMethod(new ScrollingMovementMethod());

                searchQuery.setText("");
                searchBar.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        cancelRemoval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rUSureWindow.setVisibility(View.GONE);
            }
        });
    }
}