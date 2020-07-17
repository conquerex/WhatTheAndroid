package com.example.part10_29a.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.part10_29a.model.ItemModel;

import java.util.List;

/**
 * <두번째 접근>
 * 실제 DBMS를 위해 호출되는 함수를 선언하는 인터페이스 or 추상클래스
 * 인터페이스나 추상클래스르르 구현해 DBMS를 수행하는 코드는 자동으로 만들어집니다.
 * (현재 Entity : ItemModel)
 */
@Dao
public interface ArticleDAO {
    @Query("select * from article")
    List<ItemModel> getAll();

    @Insert
    void insertAll(ItemModel... users);

    @Query("delete from article")
    void deleteAll();
}
