package com.example.myapplication;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Movie implements Serializable {

    @SerializedName("Title")
    public String title;

    @SerializedName("Genre")
    public String genre;

    @SerializedName("Rating")
    public double rating;
}
