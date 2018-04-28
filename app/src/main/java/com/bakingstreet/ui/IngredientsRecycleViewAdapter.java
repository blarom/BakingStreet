package com.bakingstreet.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingstreet.R;
import com.bakingstreet.data.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecycleViewAdapter extends RecyclerView.Adapter<IngredientsRecycleViewAdapter.IngredientViewHolder> {

    private Context mContext;
    private List<Recipe.Ingredient> mIngredients;

    public IngredientsRecycleViewAdapter(Context context, List<Recipe.Ingredient> ingredients) {
        this.mContext = context;
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_ingredients, parent, false);
        view.setFocusable(true);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        updateIngredient(holder, position);
    }
    private void updateIngredient(IngredientViewHolder holder, int position) {
        holder.ingredientInRecycleView.setText(mIngredients.get(position).getIngredient());
        holder.quantityInRecycleView.setText(mIngredients.get(position).getQuantity());
        holder.measureInRecycleView.setText(mIngredients.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        else return mIngredients.size();
    }

    public void setContents(List<Recipe.Ingredient> ingredients) {
        if (ingredients != null) mIngredients = ingredients;
        if (mIngredients != null) this.notifyDataSetChanged();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient) TextView ingredientInRecycleView;
        @BindView(R.id.quantity) TextView quantityInRecycleView;
        @BindView(R.id.measure) TextView measureInRecycleView;
        @BindView(R.id.ingredients_recyclerView_item_layout) ConstraintLayout container;

        IngredientViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
