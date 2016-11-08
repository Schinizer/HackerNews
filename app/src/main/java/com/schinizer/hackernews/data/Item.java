package com.schinizer.hackernews.data;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Item implements Parcelable {

    @SerializedName("by")
    @Nullable
    public abstract String by();
    @SerializedName("descendants")
    @Nullable
    public abstract Integer descendants();
    @SerializedName("id")
    public abstract Integer id();
    @SerializedName("deleted")
    @Nullable
    public abstract Boolean deleted();
    @SerializedName("dead")
    @Nullable
    public abstract Boolean dead();
    @SerializedName("parent")
    @Nullable
    public abstract Integer parent();
    @SerializedName("kids")
    @Nullable
    public abstract List<Integer> kids();
    @SerializedName("parts")
    @Nullable
    public abstract List<Integer> parts();
    @SerializedName("score")
    @Nullable
    public abstract Integer score();
    @SerializedName("text")
    @Nullable
    public abstract String text();
    @SerializedName("time")
    @Nullable
    public abstract Long time();
    @SerializedName("title")
    @Nullable
    public abstract String title();
    @SerializedName("type")
    @Nullable
    public abstract String type();
    @SerializedName("url")
    @Nullable
    public abstract String url();

    // The public static method returning a TypeAdapter<Foo> is what
    // tells auto-value-gson to create a TypeAdapter for Foo.
    public static TypeAdapter<Item> typeAdapter(Gson gson) {
        return new AutoValue_Item.GsonTypeAdapter(gson);
    }

    public static Item createEmpty(Integer id){
        return new AutoValue_Item("", 0, id, null, null, null, new ArrayList<Integer>(), new ArrayList<Integer>(), 0, "", null, "", "", "");
    }

    public static Builder builder()
    {
        return new AutoValue_Item.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder
    {
        public abstract Builder setBy(String by);
        public abstract Builder setDescendants(Integer descendants);
        public abstract Builder setId(Integer id);
        public abstract Builder setDeleted(Boolean deleted);
        public abstract Builder setDead(Boolean dead);
        public abstract Builder setParent(Integer parent);
        public abstract Builder setKids(List<Integer> kids);
        public abstract Builder setParts(List<Integer> parts);
        public abstract Builder setScore(Integer score);
        public abstract Builder setText(String text);
        public abstract Builder setTime(Long time);
        public abstract Builder setTitle(String title);
        public abstract Builder setType(String type);
        public abstract Builder setUrl(String url);

        public abstract Item build();
    }
}