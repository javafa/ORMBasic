package com.veryworks.android.databasebasic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.veryworks.android.databasebasic.domain.Bbs;
import com.veryworks.android.databasebasic.domain.Memo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnCreate,btnRead,btnUpdate,btnDelete;
    EditText editNo, editMemo;
    TextView textList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget(); // 위젯 등록
        setListener(); // 리스너 등록

        try {
            read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnCreate:
                    create();
                    break;
                case R.id.btnRead:
                    read();
                    break;
                case R.id.btnUpdate:
                    update();
                    break;
                case R.id.btnDelete:
                    delete();
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void create() throws SQLException{
        // 1. DB 연결
        DBHelper dbHelper = new DBHelper(this);
        // 2. Table 연결
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        // 3. 입력값 화면에서 가져와서 변수에 담고
        String memo = editMemo.getText().toString();
        // 4. 변수에 담긴 입력값으로 domain 클래스 생성자에 대입한 후 DB 에 입력
        memoDao.create(new Memo(memo));
        // 5. DB 연결 해제
        dbHelper.close();
        // 6. create 후에 화면에서 글자를 지워준다
        editMemo.setText("");

        read();
    }

    private void read() throws SQLException{
        DBHelper dbHelper = new DBHelper(this);
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        // 데이터를 전체 읽어와 화면에 뿌려준다
        List<Memo> list = memoDao.queryForAll();
        String temp = "";
        // 데이터를 한줄씩 읽어서 임시 변수인 temp에 저장한다.
        for(Memo memo : list){
            temp = temp + "no:" + memo.getId() + ", " + memo.getContent() + "\n";
        }
        // 화면에 temp 변수의 내용을 뿌려준다
        textList.setText(temp);
        dbHelper.close();
    }

    private void update() throws SQLException{
        int no = Integer.parseInt(editNo.getText().toString());
        String temp = editMemo.getText().toString();

        DBHelper dbHelper = new DBHelper(this);
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        // 1. 변경할 레코드를 가져온다
        Memo memo = memoDao.queryForId(no);
        // 2. 변경한 값을 입력한다
        memo.setContent(temp);
        // 3. Table 에 반영한다
        memoDao.update(memo);

        dbHelper.close();

        read();
    }

    private void delete() throws SQLException{
        int no = Integer.parseInt(editNo.getText().toString());

        DBHelper dbHelper = new DBHelper(this);
        Dao<Memo, Integer> memoDao = dbHelper.getDao(Memo.class);
        memoDao.deleteById(no);

        dbHelper.close();

        read();
    }


    private void setWidget(){
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        editNo = (EditText) findViewById(R.id.editNo);
        editMemo = (EditText) findViewById(R.id.editMemo);

        textList = (TextView) findViewById(R.id.list);
    }

    private void setListener(){
        btnCreate.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

























    private void examples() throws SQLException {
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
        List<Bbs> bbsList2 = bbsDao.queryForEq("title", "제목3");
        for(Bbs item : bbsList2){
            Log.i("Bbs Item","queryForEq :::::::::: id=" + item.getId() + ", title=" + item.getTitle());
        }

        // 03. 조건 컬럼 raw query - %문자열%
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
