package com.example.part10_29.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.part10_29.model.ItemModel;

import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

@Dao
public interface ArticleDAO {

    @Query("SELECT * FROM article")
    List<ItemModel> getAll();

    @Insert
    void insertAll(ItemModel... users);

    @Query("DELETE FROM article")
    void deleteAll();
}
