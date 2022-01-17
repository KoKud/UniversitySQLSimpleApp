package com.example.sqltest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

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

        ConstraintLayout constraintLayout = findViewById(R.id.newQueryLayout);
        EditText editText = findViewById(R.id.queryName);
        Button button = findViewById(R.id.acceptButton);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingNewStatement);

        floatingActionButton.setOnClickListener(view -> {
            if(constraintLayout.getVisibility()==View.VISIBLE){
                constraintLayout.setVisibility(View.GONE);
            }else{
                constraintLayout.setVisibility(View.VISIBLE);
            }
        });
        button.setOnClickListener(view -> {
            try {
                OracleCon.createQuery(editText.getText().toString());
                Log.i("QUERY",editText.getText().toString());
                editText.setText("");
                constraintLayout.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}