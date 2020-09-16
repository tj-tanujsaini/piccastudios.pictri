package com.piccastudios.pictri.Category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.piccastudios.pictri.R;

import java.util.ArrayList;
import java.util.List;

public class SelectCategory extends AppCompatActivity {

    private static final String TAG = "hs_logCheck";
    private RecyclerView rvCategories;
    private LinearLayoutManager linearLayoutManager;
    private RVAdapter rvAdapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_screen);

        list = new ArrayList<>();

        list.add("Multiple Choice");
        list.add("True / False");
        list.add("Select Odd One");
        list.add("Pictographic");

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvCategories = findViewById(R.id.hs_rv_categories);
        rvCategories.setHasFixedSize(true);

        rvAdapter = new RVAdapter(SelectCategory.this, list);
        rvCategories.setAdapter(rvAdapter);

        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
        rvAdapter.notifyDataSetChanged();

    }
}