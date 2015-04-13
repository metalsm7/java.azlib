/**
 * Created by leeyonghun on 15. 4. 2..
 */

import com.mparang.azlib.text.*;
import com.mparang.azlib.util.*;

import java.sql.Connection;

public class Console {
    public Console() {

    }

    public static void main(String[] args) {


        String json = "{k1:\"v1\", \"k2\":\"v2\", \"k3 \"a-1\":v3, k4:{sk1:sv1, sk2:\"sv\\\"2\"}}";
        AZData data = AZString.JSON.init(json).getData();

        System.out.println("azdata : " + data.toJsonString());

        data = new AZData();
        data.add("k1", "v1");
        data.add("k2", "\"value \\2");

        System.out.println("azdata : " + data.toString());
        System.out.println("azdata : " + data.toJsonString());

        try {
            AZSql sql = new AZSql("{sql_type:mysql, server:xxx, port:3306, id:root, pw:xxx, catalog:xxx}");
            int result_int = sql.execute("SELECT user, score FROM island_ranking;");

            System.out.println("sql : " + result_int);

            data = sql.getData("SELECT user, score FROM island_ranking;");
            System.out.println("azdata : " + data.toString());

            AZList list = sql.getList("SELECT user, score FROM island_ranking;");
            System.out.println("azlist : " + list.toString());
        }
        catch (Exception ex) {
            System.out.println("ex : " + ex);
        }
    }
}
