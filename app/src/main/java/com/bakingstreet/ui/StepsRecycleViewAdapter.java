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

public class StepsRecycleViewAdapter extends RecyclerView.Adapter<StepsRecycleViewAdapter.StepsViewHolder> {

    private Context mContext;
    final private StepsListItemClickHandler mOnClickHandler;
    private List<Recipe.Step> mSteps;

    public StepsRecycleViewAdapter(Context context, StepsListItemClickHandler listener, List<Recipe.Step> steps) {
        this.mContext = context;
        this.mOnClickHandler = listener;
        this.mSteps = steps;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.steps_list_item, parent, false);
        view.setFocusable(true);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {

        updateStep(holder, position);
    }
    private void updateStep(StepsViewHolder holder, int position) {
        TextView stepShortDescriptionTV = holder.stepsInRecycleView;
        stepShortDescriptionTV.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        else return mSteps.size();
    }

    public void setContents(List<Recipe.Step> steps) {
        if (steps != null) mSteps = steps;
        if (mSteps != null) this.notifyDataSetChanged();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.short_description) TextView stepsInRecycleView;
        @BindView(R.id.steps_recyclerView_item_layout) ConstraintLayout container;

        StepsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickHandler.onStepsListItemClick(clickedPosition);
        }
    }

    public interface StepsListItemClickHandler {
        void onStepsListItemClick(int clickedItemIndex);
    }
}
