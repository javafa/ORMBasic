# Sqlite ORMLite Basic
Sqlite 데이터베이스를 ORM 툴을 이용해서 다루는 방법을 알아봅니다

## Query Basic
쿼리의 기본이되는 CRUD(Create[insert], Read[select], Update, Delete)
```text
-- 1. 테이블 생성하기
-- create table 테이블명 (컬럼명1 속성, 컬럼명2 속성);
create table bbs (
	bbsno int            -- 숫자는 int, float
	, title varchar(255) -- 숫자값 바이트의 문자열 입력시 사용
	, content text       -- 대용량의 데이터 입력시 사용
    );
-- 2. 데이터 입력하기
-- insert into 테이블명(컬럼명1, 컬럼명2) value(숫자값,'문자값');
INSERT INTO bbs (bbsno, title, content) VALUES(2, '타이틀', '내용입니다');
commit;
-- 3. 데이터 수정
-- update 테이블명 set 변경할컬럼명1 = 값, 컬럼명2 = 값 where 컬럼명 = 값
update bbs set title='이순신' where bbsno = 2;
commit;
-- 4. 데이터 삭제
-- delete from 테이블명 where 컬럼명 = 값;
delete from bbs where bbsno = 1;
commit;
-- 5. 데이터 읽기
-- select 불러올컬럼명1, 컬럼명2 from 테이블명 where 컬럼명 = 값
SELECT * FROM bbs;
-- 6. id 자동증가 테이블 생성하기
-- create table 테이블명 (컬럼명1 속성 autoincrement primary key, 컬럼명2 속성);
create table bbs2 (
	bbsno int primary key auto_increment not null -- 자동증가
	, title varchar(255) -- 숫자값 바이트의 문자열 입력시 사용
	, content text       -- 대용량의 데이터 입력시 사용
    , ndate datetime
    );

-- 자동증가 테이블에는 insert시에 값을 지정하지 않는다
INSERT INTO bbs2 (title, content) VALUES('타이틀', '내용입니다');
commit;
```