package com.example.part10_30;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.example.part10_30.datasource.MyDataFactory;
import com.example.part10_30.model.ItemModel;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MyViewModel extends ViewModel {

    LiveData<PagedList<ItemModel>> itemLiveData;
    PagedList.Config pagedListConfig;
    public MyViewModel(){

        pagedListConfig=
                (new PagedList.Config.Builder())
                    .setInitialLoadSizeHint(3)
                    .setPageSize(3)
                    .build();
    }

    public LiveData<PagedList<ItemModel>> getNews() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {

            MyDataFactory dataFactory=new MyDataFactory();
            itemLiveData=(new LivePagedListBuilder(dataFactory, pagedListConfig)).build();
            return itemLiveData;
            
        } else {

            DataSource.Factory<Integer, ItemModel> factory=MyApplication.dao.getAll();
            itemLiveData=(new LivePagedListBuilder(factory, pagedListConfig)).build();
            return itemLiveData;
           
        }
    }

}
