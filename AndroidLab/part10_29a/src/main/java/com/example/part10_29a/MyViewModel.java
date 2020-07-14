package com.example.part10_29a;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.part10_29a.model.ItemModel;
import com.example.part10_29a.model.PageListModel;
import com.example.part10_29a.retrofit.RetrofitFactory;
import com.example.part10_29a.retrofit.RetrofitService;
import com.example.part10_29a.room.AppDatabase;
import com.example.part10_29a.room.ArticleDAO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyViewModel extends ViewModel {

    private static final String QUERY = "pubg";
    private static final String API_KEY = "904c175c529149e28a13f6b227708f8f";
    RetrofitService networkService = RetrofitFactory.create();

    AppDatabase db = Room.databaseBuilder(MyApp.getAppContext(), AppDatabase.class, "database-name").build();
    ArticleDAO dao = db.articleDAO();

    public MutableLiveData<List<ItemModel>> getNews() {
        // network 상태 파악
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return getNewsFromNetwork();
        } else {
            MutableLiveData<List<ItemModel>> liveData = new MutableLiveData<>();
            new GetAllThread(liveData).start();
            return liveData;
        }
    }

    public MutableLiveData<List<ItemModel>> getNewsFromNetwork() {
        MutableLiveData<List<ItemModel>> liveData = new MutableLiveData<>();
        networkService.getList(QUERY, API_KEY, 1, 10)
                .enqueue(new Callback<PageListModel>() {
                    @Override
                    public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                        if (response.isSuccessful()) {
                            liveData.postValue(response.body().articles);
                            new InsertDataThread(response.body().articles).start();
                        }
                    }

                    @Override
                    public void onFailure(Call<PageListModel> call, Throwable t) {
                        //
                    }
                });
        return liveData;
    }

    class GetAllThread extends Thread {
        MutableLiveData<List<ItemModel>> liveData;
        public GetAllThread(MutableLiveData<List<ItemModel>> liveData) {
            this.liveData = liveData;
        }

        @Override
        public void run() {
            List<ItemModel> daoData = dao.getAll();
            liveData.postValue(daoData);
        }
    }

    class InsertDataThread extends Thread {
        MutableLiveData<List<ItemModel>> liveData;
        List<ItemModel> articles;
        public InsertDataThread(List<ItemModel> articles) {
            this.articles = articles;
        }

        @Override
        public void run() {
            dao.deleteAll();
            dao.insertAll(articles.toArray(new ItemModel[articles.size()]));
        }
    }
}
