package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RecommendationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommendation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvTitle = findViewById(R.id.tvTitle);
        ListView listViewMovies = findViewById(R.id.listViewMovies);

        String genre = getIntent().getStringExtra("genre");

        ArrayList<Movie> movies =
                (ArrayList<Movie>) getIntent().getSerializableExtra("movies");

        tvTitle.setText(genre + " Movies");

        ArrayList<String> movieNames = new ArrayList<>();

        if (movies != null) {
            for (Movie movie : movies) {
                movieNames.add(
                        movie.title + "  ⭐ " + movie.rating
                );
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                movieNames
        );

        listViewMovies.setAdapter(adapter);
    }
}