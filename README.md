# java.azlib

java, .net, node.js 에서 동일한 방식으로 사용가능한 라이브러리입니다.<br />
아직 실사용으로는 부족한 부분이 많은 상태이며, 순차적으로 기능별로 추가해 나갈 예정입니다.

1. Basic data handling class<br />
1) AZData<br />
: AZData는 키:값 의 형태로 구성된 map형식의 클래스입니다.<br />
: 키값은 string값으로 특정 지어지며, 탐색시 입력된 순서대로의 int값으로도 특정지을 수 있습니다.<br />
<br />
2) AZList<br />
: 내부적으로 List&lt;AZData&gt; 를 가집니다.<br />
<br />
3) 기본적으로 Sql결과값 &lt;-&gt; AZData/AZList &lt;-&gt; JSON 상호 변환이 가능합니다.<br />
<br />
2. JSON<br />
1) parsing<br />
JSON형식을 지키는 텍스트에 대해 AZData, AZList형식으로 변경이 가능하며, 역으로도 가능합니다.<br />
ex)<br />
string json_string = "{<br />
&nbsp;&nbsp;&nbsp;&nbsp;  \"key1"\: \"value1\",<br />
&nbsp;&nbsp;&nbsp;&nbsp;  \"key2"\: {\"sub_key1\": \"sub_value1\", \"sub_key2\": \"sub_value2\"},<br />
&nbsp;&nbsp;&nbsp;&nbsp;  \"key3"\: [ {\"list_key1\": \"list_value1\"}, {\"list_key2\": \"list_value2\"}, {\"list_key3\": \"list_value3\"} ]<br />
}";<br />
<br />
AZData json_data = AZString.JSON.Init(json_string).ToAZData();<br />
json_data.get("key1"); // -> "value1"<br />
AZData data_key2 = json_data.get(1); // -> AZData<br />
data_key2.get(0); // -> "sub_value1"<br />
data_key2.get("sub_key2"); // -> "sub_value2"<br />
AZList list_key3 = json_data.get("key3"); // -> AZList<br />
list_key3.get(0).get("list_key1"); // -> "list_value1"<br />
AZData list_key3_data = list_key3.get(1); // -> AZData<br />
list_key3_data.get("list_key2"); // -> "list_value2"<br />
<br />
string json_list_key3 = list_key3.ToJsonString(); <br />
&nbsp;&nbsp;&nbsp;&nbsp;// -> [ {\"list_key1\": \"list_value1\"}, {\"list_key2\": \"list_value2\"}, {\"list_key3\": \"list_value3\"} ]<br />
string json_data_key2 = data_key2.ToJsonString(); <br />
&nbsp;&nbsp;&nbsp;&nbsp;// -> {\"sub_key1\": \"sub_value1\", \"sub_key2\": \"sub_value2\"}<br />
string json_data_copy = json_data.ToJsonString(); <br />
&nbsp;&nbsp;&nbsp;&nbsp;/*<br />
&nbsp;&nbsp;&nbsp;&nbsp;"{<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  \"key1"\: \"value1\",<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  \"key2"\: {\"sub_key1\": \"sub_value1\", \"sub_key2\": \"sub_value2\"},<br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  \"key3"\: [ {\"list_key1\": \"list_value1\"}, {\"list_key2\": \"list_value2\"}, {\"list_key3\": \"list_value3\"} ]<br />
&nbsp;&nbsp;&nbsp;&nbsp;}";<br />
&nbsp;&nbsp;&nbsp;&nbsp; */<br />
<br />
