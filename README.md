# java.azlib

##### *java, .net, node.js 에서 특정 동작에 대해 동일한 방식으로 사용하기 위한 라이브러리입니다.*

#### 기본 자료형 클래스

* AZData<br />
1) key(string):value(object)의 형태를 가집니다. <br />
2) 동일한 키값을 여러개 가질 수 있습니다.<br />
3) key값 또는 index(입력한 순서)값으로 탐색이 가능합니다.<br />
4) 동일한 key값이 여러개인 경우, 최초로 입력된 key값에 대응하는 value값을 반환합니다.

* AZList<br />
내부적으로 List<AZData)를 가진다.

#### SQL 처리 클래스

* AZSql<br />
현재는 단순히 쿼리문에 대한 결과값 반환 처리만 가능합니다.<br />
*(현재 지원 sql서버: mysql[mysql-conector-java.jar 사용], sqlite[sqlite-jdbc.jar 사용], sqlite_android[sqldroid.jar 사용])*

1) 초기화
```java
String setup_string = "{sql_type:mysql, server:127.0.0.1, port:3306, id:user, pw:password, catalog:database}";
AZSql sql = new AZSql(setup_string);
```
2) 쿼리 실행
```java
int affected_count = 0;
affected_count = sql.executeUpdate("INSERT ...");
affected_count = sql.executeUpdate("UPDATE ...");
affected_count = sql.executeUpdate("DELETE ...");
```
3) 쿼리 결과값 받아오기

*단일 결과값*
```java
String result_string = sql.get("SELECT id FROM user_info WHERE idx=1;");
int result_int = AZString.Init(sql.Get("SELECT idx FROM user_info WHERE id='test';")).ToInt(-1);
```
*단행 결과값*
```java
AZData data = sql.getData("SELECT idx, id, name, email FROM user_info WHERE idx=1;");
```
*다행 결과값*
```java
AZList list = sql.getList("SELECT idx, id, name, email FROM user_info WHERE idx in (1, 2, 3, 4, 5);");
```

#### JSON의 처리

* Parsing

```java
String json_string = "
  \"key1"\: \"value1\",
  \"key2"\: {\"sub_key1\": \"sub_value1\", \"sub_key2\": \"sub_value2\"},
  \"key3"\: [ {\"list_key1\": \"list_value1\"}, {\"list_key2\": \"list_value2\"}, {\"list_key3\": \"list_value3\"} ]
";
AZData json_data = AZString.JSON.Init(json_string).toAZData();
```

* JSON으로 변환
```java
AZData json_data = new AZData();
AZData data_sub = new AZData();
AZList list_sub = new AZList();

data_sub.add("sub_key1", "sub_value1");
data_sub.add("sub_key2", "sub_value2");

list_sub.add(new AZData("list_key1", "list_value1"));
list_sub.add(new AZData("list_key2", "list_value2"));
list_sub.add(new AZData("list_key3", "list_value3"));

json_data.add("key1", "value1");
json_data.add("key2", data_sub);
json_data.add("key3", list_sub);

String json_string = json_data.toJsonString();
```
