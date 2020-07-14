package com.example.part10_29a;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.part10_29a.model.ItemModel;
import com.example.part10_29a.model.PageListModel;
import com.example.part10_29a.retrofit.RetrofitFactory;
import com.example.part10_29a.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyViewModel extends ViewModel {

    private static final String QUERY = "korean";
    private static final String API_KEY = "904c175c529149e28a13f6b227708f8f";
    RetrofitService networkService = RetrofitFactory.create();

    public MutableLiveData<List<ItemModel>> getNews() {
        MutableLiveData<List<ItemModel>> liveData = new MutableLiveData<>();
        networkService.getList(QUERY, API_KEY, 1, 10)
                .enqueue(new Callback<PageListModel>() {
                    @Override
                    public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                        if (response.isSuccessful()) {
                            liveData.postValue(response.body().articles);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageListModel> call, Throwable t) {
                        //
                    }
                });
        return liveData;
    }
}
