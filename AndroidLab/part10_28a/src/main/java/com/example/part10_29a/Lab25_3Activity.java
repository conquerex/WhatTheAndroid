package com.example.part10_29a;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.part10_29a.databinding.ActivityLab253Binding;
import com.example.part10_29a.databinding.ItemLab3Binding;

import java.util.List;

public class Lab25_3Activity extends AppCompatActivity {

    ActivityLab253Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab25_3);
        binding.lab3List.setLayoutManager(new LinearLayoutManager(this));

        MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        viewModel.getNews().observe(this, itemModels -> {
            MyAdapter adapter = new MyAdapter(itemModels);
            binding.lab3List.setAdapter(adapter);
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemLab3Binding binding;

        public ItemViewHolder(ItemLab3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        List<ItemModel> articles;

        public MyAdapter(List<ItemModel> articles) {
            this.articles = articles;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemLab3Binding binding = ItemLab3Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ItemViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            ItemModel item = articles.get(position);
            holder.binding.setItem(item);
        }

        @Override
        public int getItemCount() {
            return articles.size();
        }
    }

    @BindingAdapter("bind:publishedAt")
    public static void publishedAt(TextView view, String date) {
        view.setText(AppUtils.getDate(date) + " at " + AppUtils.getTime(date));
    }

    @BindingAdapter("bind:urlToImage")
    public static void urlToImage(ImageView view, String url) {
        Glide.with(MyApp.getAppContext()).load(url).override(250, 200).into(view);
    }
}