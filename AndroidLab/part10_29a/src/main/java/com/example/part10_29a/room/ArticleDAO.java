package com.example.part10_29a.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.part10_29a.model.ItemModel;

import java.util.List;

@Dao
public interface ArticleDAO {
    @Query("select * from article")
    List<ItemModel> getAll();

    @Insert
    void insertAll(ItemModel... users);

    @Query("delete from article")
    void deleteAll();
}
