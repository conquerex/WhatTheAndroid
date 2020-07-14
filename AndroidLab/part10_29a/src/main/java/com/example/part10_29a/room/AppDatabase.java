package com.example.part10_29a.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.part10_29a.model.ItemModel;

@Database(entities = {ItemModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArticleDAO articleDAO();
}
