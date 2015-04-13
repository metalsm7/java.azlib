/**
 * Copyright (C) <2014~>  <Lee Yonghun, metalsm7@gmail.com, visit http://azlib.mparang.com/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mparang.azlib.util;

import com.mparang.azlib.text.AZString;

import java.sql.*;

/**
 * Created by leeyonghun on 14. 11. 27..
 */
public class AZSql {
    public static final String SQL_TYPE_MYSQL = "mysql";                        // mysql-connector-java-5.1.34-bin
    public static final String SQL_TYPE_SQLITE = "sqlite";                      // sqlite-jdbc-3.8.7
    public static final String SQL_TYPE_SQLITE_ANDROID = "sqlite_android";      // sqldroid-1.0.3
    public static final String SQL_TYPE_MSSQL = "mssql";
    public static final String SQL_TYPE_MARIADB = "mariadb";
    public static final String SQL_TYPE_ORACLE = "oracle";

    public static final String ATTRIBUTE_COLUMN_LABEL = "attribute_column_label";
    public static final String ATTRIBUTE_COLUMN_NAME = "attribute_column_name";
    public static final String ATTRIBUTE_COLUMN_TYPE = "attribute_column_type";
    public static final String ATTRIBUTE_COLUMN_TYPE_NAME = "attribute_column_type_name";
    public static final String ATTRIBUTE_COLUMN_SCHEMA_NAME = "attribute_column_schema_name";
    public static final String ATTRIBUTE_COLUMN_DISPLAY_SIZE = "attribute_column_display_size";
    public static final String ATTRIBUTE_COLUMN_SCALE = "attribute_column_scale";
    public static final String ATTRIBUTE_COLUMN_PRECISION = "attribute_column_precision";
    public static final String ATTRIBUTE_COLUMN_IS_AUTO_INCREMENT = "attribute_column_auto_increment";
    public static final String ATTRIBUTE_COLUMN_IS_CASE_SENSITIVE = "attribute_column_case_sensitive";
    public static final String ATTRIBUTE_COLUMN_IS_NULLABLE = "attribute_column_is_nullable";
    public static final String ATTRIBUTE_COLUMN_IS_READONLY = "attribute_column_is_readonly";
    public static final String ATTRIBUTE_COLUMN_IS_WRITABLE = "attribute_column_is_writable";
    public static final String ATTRIBUTE_COLUMN_IS_SIGNED = "attribute_column_is_signed";

    private String sql_type;
    private String connection_string;
    private String server;
    private int port;
    private String catalog;
    private String id, pw;

    private boolean connected = false;

    private Connection connection = null;
    private Statement statement = null;
    //private PreparedStatement preparedStatement = null;

    private static AZSql this_object = null;

    public static AZSql getInstance() {
        if (this_object == null) {
            this_object = new AZSql ();
        }
        return this_object;
    }

    /**
     * 기본생성자
     * 작성일 : 2014-11-27 이용훈
     */
    public AZSql() {

    }

    public AZSql(String p_json) throws Exception {
        Set(p_json);
    }

    public AZSql Set(String p_json) throws Exception {
        if (!p_json.startsWith("{") || !p_json.endsWith("}")) {
            throw new Exception ("parameter must be json text.");
            //return;
        }
        AZData data = AZString.JSON.toAZData(p_json);

        String data_sql_type = data.getString("sql_type");

        String data_server = data.getString("server");
        int data_port = data.getInt("port");
        String data_id = data.getString("id");
        String data_pw = data.getString("pw");
        String data_catalog = data.getString("catalog");

        String data_connection_string = data.getString("connection_string");

        if (data_sql_type.length() < 1) {
            throw new Exception ("sql_type not exist.");
            //return;
        }

        sql_type = data_sql_type;

        if (data_connection_string.length() > 0) {
            connection_string = data_connection_string;
        }
        else {
            if (data_server.length() < 1 || data_port < 0 || data_id.length() < 1 ||
                    data_pw.length() < 1 || data_catalog.length() < 1) {
                throw new Exception ("parameters not exist.");
                //return;
            }

            server = data_server;
            port = data_port;
            id = data_id;
            pw = data_pw;
            catalog = data_catalog;
        }

        return this;
    }

    public AZSql(String SQL_TYPE, String pServer, int pPort, String pCatalog, String pID, String pPW) {
        sql_type = SQL_TYPE;
        server = pServer;
        port = pPort;
        catalog = pCatalog;
        id = pID;
        pw = pPW;
    }

    public AZSql(String SQL_TYPE, String pConnectionString, String pID, String pPW) {
        sql_type = SQL_TYPE;
        connection_string = pConnectionString;
        id = pID;
        pw = pPW;
    }

    public AZSql(String SQL_TYPE, String pConnectionString) {
        sql_type = SQL_TYPE;
        connection_string = pConnectionString;
    }

    public static AZSql init(String SQL_TYPE, String pServer, int pPort, String pCatalog, String pID, String pPW) {
        return new AZSql(SQL_TYPE, pServer, pPort, pCatalog, pID, pPW);
    }

    public static AZSql init(String SQL_TYPE, String pConnectionString, String pID, String pPW) {
        return new AZSql(SQL_TYPE, pConnectionString, pID, pPW);
    }

    public static AZSql init(String SQL_TYPE, String pConnectionString) {
        return new AZSql(SQL_TYPE, pConnectionString);
    }

    public static AZSql init(String p_json) throws Exception {
        return new AZSql(p_json);
    }

    synchronized private boolean open() throws Exception {
        boolean rtnValue = false;

        if (sql_type.equals(SQL_TYPE_MYSQL)) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (Exception ex) {
                throw new Exception("Require JDBC Driver (com.mysql.jdbc.Driver) : ", ex);
            }
            if (connection_string != null) {
                connection = DriverManager.getConnection(connection_string, id, pw);
            }
            else {
                connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + catalog, id, pw);
            }
            rtnValue = true;
        }
        else if (sql_type.equals(SQL_TYPE_SQLITE)) {
            try {
                Class.forName("org.sqlite.JDBC");
            }
            catch (Exception ex) {
                throw new Exception("Exception occured in open : Require JDBC Driver (org.sqlite.JDBC) : ", ex);
            }
            if (connection_string != null) {
                if (connection_string.toLowerCase().startsWith("jdbc:sqlite:")) {
                    connection = DriverManager.getConnection(connection_string);
                }
                else {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + connection_string);
                }
            }
            else {
                throw new Exception("Exception occured in open : wrong connection string : ");
            }
            rtnValue = true;
        }
        else if (sql_type.equals(SQL_TYPE_SQLITE_ANDROID)) {
            try {
                Class.forName("org.sqldroid.SQLDroidDriver");
            }
            catch (Exception ex) {
                throw new Exception("Exception occured in open : Require JDBC Driver (org.sqldroid.SQLDroidDriver) : ", ex);
            }
            if (connection_string != null) {
                if (connection_string.toLowerCase().startsWith("jdbc:sqldroid:")) {
                    connection = DriverManager.getConnection(connection_string);
                }
                else {
                    connection = DriverManager.getConnection("jdbc:sqldroid:" + connection_string);
                }
            }
            else {
                throw new Exception("Exception occured in open : wrong connection string : ");
            }
            rtnValue = true;
        }

        connected = rtnValue;

        return rtnValue;
    }

    synchronized private boolean close() throws Exception {
        boolean rtnValue = false;

        if (!connection.isClosed()) {
            connection.close();
            statement.close();
        }

        connected = !rtnValue;

        return rtnValue;
    }

    synchronized public int execute(String pQuery) throws Exception {
        int rtnValue = -1;
        open();

        if (isConnected()) {
            try {

                statement = connection.createStatement();
                boolean rtn = statement.execute(pQuery);
                rtnValue = rtn ? 1 : -1;
            }
            catch (Exception ex) {
                throw new Exception("Exception occured in execute : ", ex);
            }
            finally {
                close();
            }

        }
        else {
            throw new Exception("Exception occured in execute : Can not open connection!");
        }
        return rtnValue;
    }

    synchronized public static int execute(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).execute(pQuery);
    }

    synchronized public static int execute(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).execute(pQuery);
    }

    synchronized public static int execute(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).execute(pQuery);
    }

    synchronized public int executeUpdate(String pQuery) throws Exception {
        int rtnValue = 0;
        open();

        if (isConnected()) {
            try {

                statement = connection.createStatement();
                //ResultSet rs = statement.executeQuery(pQuery);
                rtnValue = statement.executeUpdate(pQuery);
            }
            catch (Exception ex) {
                rtnValue = 0;
                throw new Exception("Exception occured in executeUpdate : ", ex);
            }
            finally {
                close();
            }

        }
        else {
            throw new Exception("Exception occured in execute : Can not open connection!");
        }
        return rtnValue;
    }

    synchronized public static int executeUpdate(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).executeUpdate(pQuery);
    }

    synchronized public static int executeUpdate(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).executeUpdate(pQuery);
    }

    synchronized public static int executeUpdate(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).executeUpdate(pQuery);
    }

    synchronized public int getInt(String pQuery) throws Exception {
        return AZString.init(getObject(pQuery)).toInt();
    }

    synchronized public static int getInt(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).getInt(pQuery);
    }

    synchronized public static int getInt(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).getInt(pQuery);
    }

    synchronized public static int getInt(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).getInt(pQuery);
    }

    synchronized public String getString(String pQuery) throws Exception {
        return AZString.init(getObject(pQuery)).string();
    }

    synchronized public static String getString(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).getString(pQuery);
    }

    synchronized public static String getString(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).getString(pQuery);
    }

    synchronized public static String getString(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).getString(pQuery);
    }

    synchronized public Object get(String pQuery) throws Exception {
        return getObject(pQuery);
    }

    synchronized public Object getObject(String pQuery) throws Exception {
        Object rtnValue = "";

        AZData data = getData(pQuery);
        if (data != null) {
            if (data.size() > 0) {
                rtnValue = data.get(0);
            }
        }
        data = null;

        return rtnValue;
    }

    synchronized public static Object getObject(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).getObject(pQuery);
    }

    synchronized public static Object getObject(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).getObject(pQuery);
    }

    synchronized public static Object getObject(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).getObject(pQuery);
    }

    synchronized public AZData getData(String pQuery) throws Exception {
        AZData rtnValue = null;

        open();

        if (isConnected()) {
            try {

                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(pQuery);
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    rtnValue = new AZData();
                    for (int cnti=1; cnti<=rsmd.getColumnCount(); cnti++) {
                        rtnValue.add(rsmd.getColumnLabel(cnti), rs.getObject(cnti));

                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_LABEL, rsmd.getColumnLabel(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_NAME, rsmd.getColumnName(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_TYPE, AZString.init(rsmd.getColumnType(cnti)).string()); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_TYPE_NAME, rsmd.getColumnTypeName(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_SCHEMA_NAME, rsmd.getSchemaName(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_DISPLAY_SIZE, rsmd.getColumnDisplaySize(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_PRECISION, rsmd.getPrecision(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_IS_AUTO_INCREMENT, rsmd.isAutoIncrement(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_IS_CASE_SENSITIVE, rsmd.isCaseSensitive(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_IS_NULLABLE, rsmd.isNullable(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_IS_READONLY, rsmd.isReadOnly(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_IS_WRITABLE, rsmd.isWritable(cnti)); } catch (Exception ex) { }
                        try { rtnValue.putAttribute(ATTRIBUTE_COLUMN_IS_SIGNED, rsmd.isSigned(cnti)); } catch (Exception ex) { }
                    }
                    break;
                }
            }
            catch (Exception ex) {
                rtnValue = null;
                throw new Exception("Exception occured in getData : ", ex);
            }
            finally {
                close();
            }

        }
        else {
            throw new Exception("Exception occured in getData : Can not open connection!");
        }

        return rtnValue;
    }

    synchronized public static AZData getData(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).getData(pQuery);
    }

    synchronized public static AZData getData(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).getData(pQuery);
    }

    synchronized public static AZData getData(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).getData(pQuery);
    }

    synchronized public AZList getList(String pQuery) throws Exception {
        AZList rtnValue = null;

        open();

        if (isConnected()) {
            try {
                rtnValue = new AZList();

                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(pQuery);
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    AZData data = new AZData();
                    for (int cnti=1; cnti<=rsmd.getColumnCount(); cnti++) {
                        data.add(rsmd.getColumnName(cnti), rs.getObject(cnti));

                        try { data.putAttribute(ATTRIBUTE_COLUMN_LABEL, rsmd.getColumnLabel(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_NAME, rsmd.getColumnName(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_TYPE, AZString.init(rsmd.getColumnType(cnti)).string()); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_TYPE_NAME, rsmd.getColumnTypeName(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_SCHEMA_NAME, rsmd.getSchemaName(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_DISPLAY_SIZE, rsmd.getColumnDisplaySize(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_PRECISION, rsmd.getPrecision(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_IS_AUTO_INCREMENT, rsmd.isAutoIncrement(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_IS_CASE_SENSITIVE, rsmd.isCaseSensitive(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_IS_NULLABLE, rsmd.isNullable(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_IS_READONLY, rsmd.isReadOnly(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_IS_WRITABLE, rsmd.isWritable(cnti)); } catch (Exception ex) { }
                        try { data.putAttribute(ATTRIBUTE_COLUMN_IS_SIGNED, rsmd.isSigned(cnti)); } catch (Exception ex) { }
                    }
                    rtnValue.add(data);
                }
            }
            catch (Exception ex) {
                rtnValue = null;
                throw new Exception("Exception occured in getList : ", ex);
            }
            finally {
                close();
            }

        }
        else {
            throw new Exception("Exception occured in getList : Can not open connection!");
        }

        return rtnValue;
    }

    synchronized public static AZList getList(String pQuery, String SQL_TYPE, String pServerIP, int pPort, String pCatalog, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pServerIP, pPort, pCatalog, pID, pPW).getList(pQuery);
    }

    synchronized public static AZList getList(String pQuery, String SQL_TYPE, String pConnectionString, String pID, String pPW) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString, pID, pPW).getList(pQuery);
    }

    synchronized public static AZList getList(String pQuery, String SQL_TYPE, String pConnectionString) throws Exception {
        return AZSql.init(SQL_TYPE, pConnectionString).getList(pQuery);
    }

    public boolean isConnected() { return this.connected; }
}
