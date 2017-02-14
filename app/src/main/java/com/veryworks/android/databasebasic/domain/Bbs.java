package com.veryworks.android.databasebasic.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "bbs")
public class Bbs {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String content;

    @DatabaseField
    private Date currentDate;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    Bbs() {
        // 이게 없으면 ormlite가 동작하지 않는다
    }

    public Bbs(String title, String content,Date currentDate){
        this.title = title;
        this.content = content;
        this.currentDate = currentDate;
    }
}
