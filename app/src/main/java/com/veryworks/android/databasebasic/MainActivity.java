package com.veryworks.android.databasebasic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.veryworks.android.databasebasic.domain.Bbs;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            insert();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert() throws SQLException {
        // 1. 데이터베이스를 연결합니다
        //    싱글턴 구조
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);

        // 2. bbs 테이블을 조작하기 위한 객체를 생성합니다.
        Dao<Bbs, Long> bbsDao = dbHelper.getBbsDao();

        // 3. 입력 값 생성
        String title = "제목";
        String content = "내용입니다";
        Date currDateTime = new Date(System.currentTimeMillis());

        // 4. 위의 입력값으로 Bbs 객체 생성
        Bbs bbs = new Bbs(title, content, currDateTime);

        // ----------- 생성 (Create) ----------------------
        // 5. 생성된 Bbs 객체를 dao를 통해 insert
        bbsDao.create(bbs);
        // 위의 3,4,5 번을 한줄로 표현
        bbsDao.create( new Bbs( "제목2", "내용2", new Date(System.currentTimeMillis()) ) );
        // 내부적으로
        // String query = "insert into bbs(title,content,curDate) values('제목2','내용2',현재시간);"
        // Sqlite.execute(query);
        bbsDao.create( new Bbs( "제목3", "내용3", new Date(System.currentTimeMillis()) ) );

        // ----------- 읽기 (Read) -----------------------
        // 01. 조건 ID
        Bbs bbs2 = bbsDao.queryForId(3L);
        Log.i("Test Bbs one","queryForId :::::::::: content="+bbs2.getContent());

        // 02. 조건 컬럼명 값
        List<Bbs> bbsList2 = bbsDao.queryForEq("id", 3);
        for(Bbs item : bbsList2){
            Log.i("Bbs Item","queryForEq :::::::::: id=" + item.getId() + ", title=" + item.getTitle());
        }

        // 03. 조건 컬럼 raw query
        String query = "SELECT * FROM bbs where title like '%2%'";
        GenericRawResults<Bbs> rawResults = bbsDao.queryRaw(query, bbsDao.getRawRowMapper());

        List<Bbs> bbsList3 = rawResults.getResults();
        for(Bbs item : bbsList3){
            Log.i("Bbs Item","queryRaw ::::::::: id=" + item.getId() + ", title=" + item.getTitle());
        }

        // ------------ 삭제(Delete) ------------
        // 11. 아이디로 삭제
        bbsDao.deleteById(5L);
        // 12. bbs 객체로 삭제
        bbsDao.delete(bbs2);

        // ------------- 수정 (Update) ---------------
        // 21. 수정
        Bbs bbsTemp = bbsDao.queryForId(7L);
        bbsTemp.setTitle("7번 수정됨");
        bbsDao.update(bbsTemp);



        // ------------ 전체 조회 --------------------
        // 99. 전체쿼리
        List<Bbs> bbsList = bbsDao.queryForAll();
        for(Bbs item : bbsList){
            Log.i("Bbs Item","id=" + item.getId() + ", title=" + item.getTitle());
        }


    }
}
