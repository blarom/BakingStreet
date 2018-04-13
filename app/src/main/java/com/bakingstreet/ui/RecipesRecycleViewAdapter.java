package com.bakingstreet.ui;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingstreet.*;
import com.bakingstreet.data.Recipe;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesRecycleViewAdapter extends RecyclerView.Adapter<RecipesRecycleViewAdapter.RecipeViewHolder> {

    private Context mContext;
    final private RecipesListItemClickHandler mOnClickHandler;
    private Recipe[] mRecipes;

    public RecipesRecycleViewAdapter(Context context, RecipesListItemClickHandler listener, Recipe[] recipes) {
        this.mContext = context;
        this.mOnClickHandler = listener;
        this.mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipes_list_item, parent, false);
        view.setFocusable(true);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        updateItemDescription(holder, position);
        updateItemImage(holder, position);
    }
    private void updateItemDescription(RecipeViewHolder holder, int position) {
        TextView recipeDescriptionTV = holder.recipeDescriptionInRecycleView;
        recipeDescriptionTV.setText(mRecipes[position].getRecipeName());
    }
    private void updateItemImage(final RecipeViewHolder holder, int position) {

        Uri imageUri = Uri.fromFile(new File(mRecipes[position].getImage()));

        ImageView imageView = holder.recipeImageInRecycleView;
        holder.container.requestLayout();
        Picasso.with(mContext)
                .load(imageUri)
                .error(R.drawable.ic_missing_image)
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        else return mRecipes.length;
    }

    public void setContents(Recipe[] recipe) {
        if (recipe != null) mRecipes = recipe;
        if (mRecipes != null) this.notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_description) TextView recipeDescriptionInRecycleView;
        @BindView(R.id.recipe_image) ImageView recipeImageInRecycleView;
        @BindView(R.id.recipes_recyclerView_item_layout) ConstraintLayout container;

        RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickHandler.onRecipesListItemClick(clickedPosition);
        }
    }

    public interface RecipesListItemClickHandler {
        void onRecipesListItemClick(int clickedItemIndex);
    }
}
