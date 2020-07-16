package com.example.part10_28a;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyViewModel extends ViewModel {

    private static final String QUERY = "korean";
    private static final String API_KEY = "904c175c529149e28a13f6b227708f8f";
    RetrofitService networkService = RetrofitFactory.create();

    public MutableLiveData<List<ItemModel>> getNews() {
        // MutableLiveData란 변경할 수 있는 LiveData 형입니다.
        // 일반적인 LiveData형은 변경할 수 없고 오로지 데이터의 변경값만을 소비하는데 반해
        // MutableLiveData는 데이터를 UI Thread와 Background Thread에서 선택적으로 바꿀 수 있습니다.
        MutableLiveData<List<ItemModel>> liveData = new MutableLiveData<>();
        networkService.getList(QUERY, API_KEY, 1, 10)
                .enqueue(new Callback<PageListModel>() {
                    @Override
                    public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                        if (response.isSuccessful()) {
                            // postValue : 메인, 백그라운드 스레드 모두 사용 가능. 옵서버 없이는 데이터를 직접 받지 못함
                            // setValue : 메인 스레드에서만. 옵서버 없이도 데이터 받을 수 있음
                            liveData.postValue(response.body().articles);
                        }
                    }

                    @Override
                    public void onFailure(Call<PageListModel> call, Throwable t) {
                        //
                    }
                });
        // 뷰모델에서 라이브데이터를 액티비티에 전달하면
        // 라이브데이터에 데이터가 담길 때마다 자동으로 등록한 옵서버가 실행
        return liveData;
    }
}
