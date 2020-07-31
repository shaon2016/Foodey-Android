package com.example.foodey.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodey.R;
import com.example.foodey.ui.food_details.FoodDetailsActivity;
import com.example.foodey.models.Food;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodRVAdapter extends RecyclerView.Adapter<FoodRVAdapter.MyFoodVH> implements Filterable {
    public ArrayList<Food> items;
    private Context mContext;

    private ArrayList<Food> itemListFiltered;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    itemListFiltered = items;
                } else {
                    ArrayList<Food> filteredList = new ArrayList<>();

                    for (int i = 0; i < items.size(); i++) {
                        Food row = items.get(i);
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }

                    itemListFiltered = filteredList;
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (ArrayList<Food>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public FoodRVAdapter(Context context, ArrayList<Food> foods) {
        items = foods;
        itemListFiltered = foods;
        mContext = context;
    }

    @NonNull
    @Override
    public MyFoodVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_foods_row, parent, false);

        return new MyFoodVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFoodVH holder, int position) {
        Food f = getItem(holder.getAdapterPosition());
        holder.bind(f);
    }

    private Food getItem(int pos) {
        return itemListFiltered.get(pos);
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    public void addUniquely(@NotNull ArrayList<Food> newFoods) {
        ArrayList<Food> oldFoods = items;

        for (int i = 0; i < newFoods.size(); i++) {
            boolean isExists = false;
            for (int j = 0; j < oldFoods.size(); j++) {
                if (oldFoods.get(j).getId() == newFoods.get(i).getId()) {
                    // Exists in old food list
                    isExists = true;
                    break;
                }
            }
            // Adding new food to view
            if (!isExists) {
                items.add(newFoods.get(i));
            }
        }

        notifyDataSetChanged();
    }


    class MyFoodVH extends RecyclerView.ViewHolder {
        TextView tvTitle;
        private TextView tvPrice;
        private ImageView ivFood;

        MyFoodVH(@NonNull View v) {
            super(v);

            tvTitle = v.findViewById(R.id.tvTitle);
            tvPrice = v.findViewById(R.id.tvprice);
            ivFood = v.findViewById(R.id.ivFood);
        }

        private void bind(final Food f) {
            tvTitle.setText(f.getName());
            tvPrice.setText(f.getPrice());

            Glide.with(mContext)
                    .load(f.getImage())
                    .into(ivFood);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, FoodDetailsActivity.class);
                    i.putExtra("getCartItem", f);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
