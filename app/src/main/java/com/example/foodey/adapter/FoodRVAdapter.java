package com.example.foodey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodey.R;
import com.example.foodey.models.Food;
import com.example.foodey.util.C;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodRVAdapter extends RecyclerView.Adapter<FoodRVAdapter.MyFoodVH> {

    private ArrayList<Food> items;
    private Context mContext;

    public FoodRVAdapter(Context context, ArrayList<Food> foods) {
        items = foods;
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
        return items.get(pos);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addUniquely(@NotNull ArrayList<Food> newFoods) {
        ArrayList<Food> oldFoods = items;

//        for (int i = 0; i < newFoods.size(); i++ ) {
//            for (int j = 0 ; j < oldFoods.size(); j++) {
//                if (oldFoods.get(j).getId() != newFoods.get(i).getId() ) {
//                    items.add(newFoods.get(j));
//                }
//            }
//        }

        items.addAll(newFoods);
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

        private void bind(Food f) {
            tvTitle.setText(f.getName());
            tvPrice.setText(f.getPrice());

            Glide.with(mContext)
                    .load(f.getImage())
                    .into(ivFood);
        }
    }
}
