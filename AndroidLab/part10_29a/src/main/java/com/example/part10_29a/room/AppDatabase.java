package com.example.part10_29a.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.part10_29a.model.ItemModel;

/**
 * <첫번째 접근>
 * 데이터베이스 이용을 위한 DAO 객체 획득 함수를 제공하는 클래스
 * DAO 획득 함수는 추상함수로 정의하며 데이터베이스를 이용하기 위해 가장 먼저 호출됨
 * (현재 DAO : ArticleDAO)
 */
@Database(entities = {ItemModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // 함수는 매개변수가 없어야 한다. 반환값은 반드시 DAO 타입
    public abstract ArticleDAO articleDAO();
}
