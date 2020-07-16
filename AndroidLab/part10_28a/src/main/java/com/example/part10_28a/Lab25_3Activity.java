package com.example.part10_28a;

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
import com.example.part10_28a.R;
import com.example.part10_28a.databinding.ActivityLab253Binding;
import com.example.part10_28a.databinding.ItemLab3Binding;

import java.util.List;

public class Lab25_3Activity extends AppCompatActivity {

    ActivityLab253Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab25_3);
        binding.lab3List.setLayoutManager(new LinearLayoutManager(this));

        // ViewModelProviders.of(this).get()
        // 액티비티 스코프 내에게서 싱글턴. 여러번 호출해도 한번만 생성
        // 뷰모델 객체가 생성되면 화면회전과 같은 상황에서도 뷰모델 객체는 소멸되지 않음. 데이터 유지
        MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        // 뷰모델 데이터를 콜백함수로 받을 수 있다.
        viewModel.getNews().observe(this, itemModels -> {
            // onChanged 함수 내부 : 비동기 프로그래밍에 유용
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