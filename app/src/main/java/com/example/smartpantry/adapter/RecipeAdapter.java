package com.example.smartpantry.adapter;

import android.view.*;
import android.view.ViewGroup;
<<<<<<< HEAD
=======
import android.widget.ViewGroup;
>>>>>>> b517ab72a129e948466a5776369863065dde702c
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.Recipe;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD

=======
>>>>>>> b517ab72a129e948466a5776369863065dde702c
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();
    private final onRecipeClickListener listener;

<<<<<<< HEAD
    public interface onRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(onRecipeClickListener listener) {
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes) {
=======
    public interface onRecipeClickListener{
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(onRecipeClickListener listener){
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes){
>>>>>>> b517ab72a129e948466a5776369863065dde702c
        this.recipes = recipes != null ? recipes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
<<<<<<< HEAD
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
=======
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
>>>>>>> b517ab72a129e948466a5776369863065dde702c
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
<<<<<<< HEAD
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
=======
    public void  onBindViewHolder(@NonNull RecyclerViewHolder holder, int position){
>>>>>>> b517ab72a129e948466a5776369863065dde702c
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, listener);
    }

    @Override
<<<<<<< HEAD
    public int getItemCount() {
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvCount;

        public RecipeViewHolder(@NonNull View itemView) {
=======
    public int getItemCount(){
        return recipes.size();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvName;
        private final TextView tvCount;

        public RecipeViewHolder(@NonNull View itemView){
>>>>>>> b517ab72a129e948466a5776369863065dde702c
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvCount = itemView.findViewById(R.id.tvIngredientCount);
        }

<<<<<<< HEAD
        public void bind(Recipe recipe, onRecipeClickListener listener) {
=======
        public void bind(Recipe recipe, onRecipeClickListener listener){
>>>>>>> b517ab72a129e948466a5776369863065dde702c
            tvName.setText(recipe.getName());
            int count = recipe.getIngredients() != null ? recipe.getIngredients().size() : 0;
            tvCount.setText(count + "ingredients required (100% matched)");

<<<<<<< HEAD
            itemView.setOnClickListener(v -> listener.onRecipeClick(recipe));
=======
            itemView.setOnClickListener(v->listener.onRecipeClick(recipe));
>>>>>>> b517ab72a129e948466a5776369863065dde702c
        }
    }
}






















