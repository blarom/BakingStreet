package com.bakingstreet.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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

    //TODO: Convert to ArrayList for Parcelable
    @SerializedName("ingredients")
    private Ingredients[] ingredients;
    public void setIngredients(Ingredients[] ingredients) { this.ingredients = ingredients; }
    public Ingredients[] getIngredients() { return ingredients; }

    @SerializedName("steps")
    private Steps[] steps;
    public void setSteps(Steps[] steps) { this.steps = steps; }
    public Steps[] getSteps() { return steps; }

    @SerializedName("servings")
    private int servings;
    public void setServings(int servings) { this.servings = servings; }
    public int getServings() { return servings; }

    @SerializedName("image")
    private String image;
    public void setImage(String image) { this.image = image; }
    public String getImage() { return image; }


    public class Ingredients implements Parcelable {

        Ingredients() {}

        protected Ingredients(Parcel in) {
            quantity = in.readString();
            measure = in.readString();
            ingredient = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(quantity);
            parcel.writeString(measure);
            parcel.writeString(ingredient);
        }

        public final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
            @Override
            public Ingredients createFromParcel(Parcel in) {
                return new Ingredients(in);
            }

            @Override
            public Ingredients[] newArray(int size) {
                return new Ingredients[size];
            }
        };

        @SerializedName("quantity")
        private String quantity;
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

    public class Steps implements Parcelable {

        Steps() {}

        protected Steps(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(shortDescription);
            parcel.writeString(description);
            parcel.writeString(videoURL);
            parcel.writeString(thumbnailURL);
        }

        public final Creator<Steps> CREATOR = new Creator<Steps>() {
            @Override
            public Steps createFromParcel(Parcel in) {
                return new Steps(in);
            }

            @Override
            public Steps[] newArray(int size) {
                return new Steps[size];
            }
        };

        @SerializedName("id")
        private int id;
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
