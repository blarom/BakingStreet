package com.bakingstreet.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Recipe implements Parcelable {

    public Recipe() {}

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @SerializedName("id")
    private int id;
    public void setRecipeId(int id) { this.id = id; }
    public int getRecipeId() { return id; }

    @SerializedName("name")
    private String name;
    public void setRecipeName(String name) { this.name = name; }
    public String getRecipeName() { return name; }

    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredients;
    public void setIngredients(ArrayList<Ingredient> ingredients) { this.ingredients = ingredients; }
    public ArrayList<Ingredient> getIngredients() { return ingredients; }

    @SerializedName("steps")
    private ArrayList<Step> steps;
    public void setSteps(ArrayList<Step> steps) { this.steps = steps; }
    public ArrayList<Step> getSteps() { return steps; }

    @SerializedName("servings")
    private int servings;
    public void setServings(int servings) { this.servings = servings; }
    public int getServings() { return servings; }

    @SerializedName("image")
    private String image;
    public void setImage(String image) { this.image = image; }
    public String getImage() { return image; }


    public static class Ingredient implements Parcelable {

        Ingredient() {}

        @SerializedName("quantity")
        private String quantity;

        protected Ingredient(Parcel in) {
            quantity = in.readString();
            measure = in.readString();
            ingredient = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(quantity);
            dest.writeString(measure);
            dest.writeString(ingredient);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
            @Override
            public Ingredient createFromParcel(Parcel in) {
                return new Ingredient(in);
            }

            @Override
            public Ingredient[] newArray(int size) {
                return new Ingredient[size];
            }
        };

        public void setQuantity(String quantity) { this.quantity = quantity; }
        public String getQuantity() { return quantity; }

        @SerializedName("measure")
        private String measure;
        public void setMeasure(String measure) { this.measure = measure; }
        public String getMeasure() { return measure; }

        @SerializedName("ingredient")
        private String ingredient;
        public void setIngredient(String ingredient) { this.ingredient = ingredient; }
        public String getIngredient() { return ingredient; }

    }

    public static class Step implements Parcelable {

        Step() {}

        @SerializedName("id")
        private int id;

        protected Step(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoURL);
            dest.writeString(thumbnailURL);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Step> CREATOR = new Creator<Step>() {
            @Override
            public Step createFromParcel(Parcel in) {
                return new Step(in);
            }

            @Override
            public Step[] newArray(int size) {
                return new Step[size];
            }
        };

        public void setStepId(int id) { this.id = id; }
        public int geStepId() { return id; }

        @SerializedName("shortDescription")
        private String shortDescription;
        public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
        public String getShortDescription() { return shortDescription; }

        @SerializedName("description")
        private String description;
        public void setDescription(String description) { this.description = description; }
        public String getDescription() { return description; }

        @SerializedName("videoURL")
        private String videoURL;
        public void setVideoUrl(String videoURL) { this.videoURL = videoURL; }
        public String getVideoUrl() { return videoURL; }

        @SerializedName("thumbnailURL")
        private String thumbnailURL;
        public void setThumbnailUrl(String thumbnailURL) { this.thumbnailURL = thumbnailURL; }
        public String getThumbnailUrl() { return thumbnailURL; }

    }

}
