package com.example.part10_29a.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * <세번째 접근>
 * 데이터 구조를 표현하기 위한 클래스
 * DBMS에 이용되기 위한 데이터를 위한 클래스
 * 개발자 입장에서 VO 클래스
 */
@Entity(tableName = "article")
public class ItemModel {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String author;
    public String title;
    public String description;
    public String urlToImage;
    public String publishedAt;
}
