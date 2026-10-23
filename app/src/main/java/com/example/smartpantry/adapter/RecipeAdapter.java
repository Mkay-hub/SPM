package com.example.smartpantry.adapter;

import android.view.*;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private final onRecipeClickListener listener;

    public interface onRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(onRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes != null ? recipes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, listener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCount;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvCount = itemView.findViewById(R.id.tvIngredientCount);
        }

        public void bind(Recipe recipe, onRecipeClickListener listener) {
            tvName.setText(recipe.getName());
            int count = recipe.getIngredients() != null ? recipe.getIngredients().size() : 0;
            tvCount.setText(count + "ingredients required (100% matched)");

            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
        }
    }
}






















