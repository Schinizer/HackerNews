package com.schinizer.hackernews;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class Item implements Parcelable {

    @SerializedName("by")
    public abstract String by();
    @SerializedName("descendants")
    public abstract Integer descendants();
    @SerializedName("id")
    public abstract Integer id();
    @SerializedName("deleted")
    public abstract Boolean deleted();
    @SerializedName("dead")
    public abstract Boolean dead();
    @SerializedName("parent")
    public abstract Integer parent();
    @SerializedName("kids")
    public abstract List<Integer> kids();
    @SerializedName("parts")
    public abstract List<Integer> parts();
    @SerializedName("score")
    public abstract Integer score();
    @SerializedName("text")
    public abstract String text();
    @SerializedName("time")
    public abstract Date time();
    @SerializedName("title")
    public abstract String title();
    @SerializedName("type")
    public abstract String type();
    @SerializedName("url")
    public abstract String url();

    // The public static method returning a TypeAdapter<Foo> is what
    // tells auto-value-gson to create a TypeAdapter for Foo.
    public static TypeAdapter<Item> typeAdapter(Gson gson) {
        return new AutoValue_Item.GsonTypeAdapter(gson);
    }
}