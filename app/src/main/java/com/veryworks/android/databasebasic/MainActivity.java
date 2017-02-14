package com.veryworks.android.databasebasic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
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

        // 5. 생성된 Bbs 객체를 dao를 통해 insert
        bbsDao.create(bbs);

        // 위의 3,4,5 번을 한줄로 표현
        bbsDao.create( new Bbs( "제목2", "내용2", new Date(System.currentTimeMillis()) ) );
        bbsDao.create( new Bbs( "제목3", "내용3", new Date(System.currentTimeMillis()) ) );

        List<Bbs> bbsList = bbsDao.queryForAll();
        for(Bbs item : bbsList){
            Log.i("Bbs Item","id=" + item.getId() + ", title=" + item.getTitle());
        }
    }
}
