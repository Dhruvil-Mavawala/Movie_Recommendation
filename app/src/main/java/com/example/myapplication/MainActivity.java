package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI references
        Spinner spinnerGenre = findViewById(R.id.spinnerGenre);
        Button btnRecommend = findViewById(R.id.btnRecommend);

        // Spinner data
        String[] genres = {"Action", "Comedy", "Drama", "Romance", "Sci-Fi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                genres
        );
        spinnerGenre.setAdapter(adapter);

        // Load movies from JSON
        loadMovies();

        // Button click → recommendation
        btnRecommend.setOnClickListener(v -> {

            if (movieList == null || movieList.isEmpty()) {
                Toast.makeText(this, "Movie list empty", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedGenre = spinnerGenre.getSelectedItem().toString();

            ArrayList<Movie> recommendedMovies =
                    getRecommendedMovies(selectedGenre);

            Intent intent = new Intent(MainActivity.this, RecommendationActivity.class);
            intent.putExtra("genre", selectedGenre);
            intent.putExtra("movies", recommendedMovies);

            startActivity(intent);
        });

    }

    // 🔹 Load JSON and parse to movie list
    private void loadMovies() {
        try {
            String json = loadJSONFromAsset();

            if (json == null) {
                Toast.makeText(this, "JSON is NULL", Toast.LENGTH_LONG).show();
                return;
            }

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
            movieList = gson.fromJson(json, type);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_LONG).show();
        }
    }

    // 🔹 Recommendation logic (content-based filtering)
    private ArrayList<Movie> getRecommendedMovies(String selectedGenre) {

        ArrayList<Movie> filteredList = new ArrayList<>();

        for (Movie movie : movieList) {
            if (movie.genre != null &&
                    movie.genre.toLowerCase().contains(selectedGenre.toLowerCase())) {
                filteredList.add(movie);
            }
        }

        return filteredList;
    }

    // 🔹 Read JSON from assets
    private String loadJSONFromAsset() {
        try {
            InputStream is = getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
