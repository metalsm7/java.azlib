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

import java.sql.*;

import com.mparang.azlib.text.AZString;

/**
 * Created by leeyonghun on 14. 11. 27..
 */
public class AZSql {
    public static final String SQL_TYPE_MYSQL = "mysql";                        // mysql-connector-java-5.1.34-bin
    public static final String SQL_TYPE_SQLITE = "sqlite";                      // sqlite-jdbc-3.8.7
    public static final String SQL_TYPE_SQLITE_ANDROID = "sqlite_android";      // sqldroid-1.0.3
    public static final String SQL_TYPE_MSSQL_2000 = "mssql_2000";              // sqljdbc4(3.0)
    public static final String SQL_TYPE_MSSQL = "mssql";                        // sqljdbc41(4.0/4.1)
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

    private DBConnectionInfo db_info = null;

    private boolean connected = false;

    private Connection connection = null;
    private Statement statement = null;
    //private PreparedStatement preparedStatement = null;

    private static AZSql this_object = null;

    /**
     * Created : 2015-06-03 leeyonghun
     */
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

    /**
     * Created : 2015-06-03 leeyonghun
     */
    public AZSql(String p_json) throws Exception {
        Set(p_json);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    public AZSql(DBConnectionInfo p_db_connection_info) {
        this.db_info = p_db_connection_info;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    public AZSql Set(String p_json) throws Exception {
        this.db_info = new DBConnectionInfo(p_json);
        return this;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    public static AZSql init(String p_json) throws Exception {
        return new AZSql(p_json);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    public static AZSql init(DBConnectionInfo p_db_connection_info) throws Exception {
        return new AZSql(p_db_connection_info);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
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

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public int executeUpdate(String pQuery) throws Exception {
        int rtnValue = 0;
        open();

        if (isConnected()) {
            try {
                statement = connection.createStatement();
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

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public Object get(String pQuery) throws Exception {
        Object rtnValue = null;

        try {
            open();

            if (isConnected()) {
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(pQuery);
                ResultSetMetaData rsmd = rs.getMetaData();

                if (rs.first() && rsmd.getColumnCount() > 0) {
                    rtnValue = rs.getObject(0);
                }
            }
            else {
                throw new Exception("Exception occured in get : Can not open connection!");
            }
        }
        catch (Exception ex) {
            rtnValue = null;
            throw new Exception("Exception occured in get : ", ex);
        }
        finally {
            close();
        }
        return rtnValue;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public Object getObject(String pQuery) throws Exception {
        return get(pQuery);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public int getInt(String pQuery, int pDefaultValue) throws Exception {
        return AZString.init(getObject(pQuery)).toInt(pDefaultValue);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public int getInt(String pQuery) throws Exception {
        return getInt(pQuery, 0);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public float getFloat(String pQuery, float pDefaultValue) throws Exception {
        return AZString.init(getObject(pQuery)).toFloat(pDefaultValue);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public float getFloat(String pQuery) throws Exception {
        return getFloat(pQuery, 0f);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public String getString(String pQuery, String pDefaultValue) throws Exception {
        return AZString.init(getObject(pQuery)).string(pDefaultValue);
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public String getString(String pQuery) throws Exception {
        return getString(pQuery, "");
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public AZData getData(String pQuery) throws Exception {
        AZData rtnValue = null;

        try {
            open();

            if (isConnected()) {
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(pQuery);
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    rtnValue = new AZData();
                    for (int cnti=1; cnti<=rsmd.getColumnCount(); cnti++) {
                        rtnValue.add(rsmd.getColumnLabel(cnti), rs.getObject(cnti));

                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_LABEL, rsmd.getColumnLabel(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_NAME, rsmd.getColumnName(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_TYPE, AZString.init(rsmd.getColumnType(cnti)).string()); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_TYPE_NAME, rsmd.getColumnTypeName(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_SCHEMA_NAME, rsmd.getSchemaName(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_DISPLAY_SIZE, rsmd.getColumnDisplaySize(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_PRECISION, rsmd.getPrecision(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_IS_AUTO_INCREMENT, rsmd.isAutoIncrement(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_IS_CASE_SENSITIVE, rsmd.isCaseSensitive(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_IS_NULLABLE, rsmd.isNullable(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_IS_READONLY, rsmd.isReadOnly(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_IS_WRITABLE, rsmd.isWritable(cnti)); } catch (Exception ex) { }
                        try { rtnValue.Attribute.add(ATTRIBUTE_COLUMN_IS_SIGNED, rsmd.isSigned(cnti)); } catch (Exception ex) { }
                    }
                    break;
                }
            }
            else {
                throw new Exception("Exception occured in getData : Can not open connection!");
            }
        }
        catch (Exception ex) {
            rtnValue = null;
            throw new Exception("Exception occured in getData : ", ex);
        }
        finally {
            close();
        }

        return rtnValue;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized public AZList getList(String pQuery) throws Exception {
        AZList rtnValue = null;

        try {
            open();

            if (isConnected()) {
                rtnValue = new AZList();

                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(pQuery);
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    AZData data = new AZData();
                    for (int cnti=1; cnti<=rsmd.getColumnCount(); cnti++) {
                        data.add(rsmd.getColumnName(cnti), rs.getObject(cnti));

                        try { data.Attribute.add(ATTRIBUTE_COLUMN_LABEL, rsmd.getColumnLabel(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_NAME, rsmd.getColumnName(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_TYPE, AZString.init(rsmd.getColumnType(cnti)).string()); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_TYPE_NAME, rsmd.getColumnTypeName(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_SCHEMA_NAME, rsmd.getSchemaName(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_DISPLAY_SIZE, rsmd.getColumnDisplaySize(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_PRECISION, rsmd.getPrecision(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_IS_AUTO_INCREMENT, rsmd.isAutoIncrement(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_IS_CASE_SENSITIVE, rsmd.isCaseSensitive(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_IS_NULLABLE, rsmd.isNullable(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_IS_READONLY, rsmd.isReadOnly(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_IS_WRITABLE, rsmd.isWritable(cnti)); } catch (Exception ex) { }
                        try { data.Attribute.add(ATTRIBUTE_COLUMN_IS_SIGNED, rsmd.isSigned(cnti)); } catch (Exception ex) { }
                    }
                    rtnValue.add(data);
                }

            }
            else {
                throw new Exception("Exception occured in getList : Can not open connection!");
            }
        }
        catch (Exception ex) {
            rtnValue = null;
            //throw new Exception("Exception occured in getList : ", ex);
            throw ex;
        }
        finally {
            close();
        }

        return rtnValue;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized private boolean open() throws Exception {
        boolean rtnValue = false;

        if (this.db_info.getSqlType().equals(SQL_TYPE_MSSQL_2000) || this.db_info.getSqlType().equals(SQL_TYPE_MSSQL)) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(this.db_info.getConnectionString() != null ? this.db_info.getConnectionString() : "jdbc:sqlserver://" + this.db_info.getServer() + ":" + this.db_info.getPort() + ";DatabaseName=" + this.db_info.getCatalog(), this.db_info.getID(), this.db_info.getPW());
            rtnValue = true;
        }
        else if (this.db_info.getSqlType().equals(SQL_TYPE_MYSQL)) {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(this.db_info.getConnectionString() != null ? this.db_info.getConnectionString() : "jdbc:mysql://" + this.db_info.getServer() + ":" + this.db_info.getPort() + "/" + this.db_info.getCatalog(), this.db_info.getID(), this.db_info.getPW());
            rtnValue = true;
        }
        else if (this.db_info.getSqlType().equals(SQL_TYPE_SQLITE)) {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(this.db_info.getConnectionString() != null ? this.db_info.getConnectionString() : "jdbc:sqlite://" + this.db_info.getServer());
            rtnValue = true;
        }
        else if (this.db_info.getSqlType().equals(SQL_TYPE_SQLITE_ANDROID)) {
            Class.forName("org.sqldroid.SqldroidDriver");
            connection = DriverManager.getConnection(this.db_info.getConnectionString() != null ? this.db_info.getConnectionString() : "jdbc:sqldroid://" + this.db_info.getServer());
        }
        /** for 1.7
        switch (this.db_info.getSqlType()) {
            case SQL_TYPE_MSSQL_2000:
            case SQL_TYPE_MSSQL:
                break;
            case SQL_TYPE_MYSQL:
                break;
            case SQL_TYPE_SQLITE:
                break;
            case SQL_TYPE_SQLITE_ANDROID:
                break;
        }
         */
        connected = rtnValue;
        return rtnValue;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    synchronized private boolean close() throws Exception {
        boolean rtnValue = false;

        if (!connection.isClosed()) {
            connection.close();
            statement.close();
        }

        connected = !rtnValue;

        return rtnValue;
    }

    /**
     * Created : 2015-06-03 leeyonghun
     */
    public boolean isConnected() { return this.connected; }


    /**
     * DB 연결 정보 저장용 객체
     * Created : 2015-06-04, leeyonghun
     */
    public static class DBConnectionInfo {
        private String sql_type, connection_string, server, catalog, id, pw;
        private int port;

        /**
         * 기본 생성자
         * @param p_json
         * @throws Exception
         * Created : 2015-06-04, leeyonghun
         */
        public DBConnectionInfo(String p_json) throws Exception {
            p_json = p_json.trim();
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
                if (!sql_type.equals(SQL_TYPE_SQLITE) && !sql_type.equals(SQL_TYPE_SQLITE_ANDROID) && (data_id.length() < 1 || data_pw.length() < 1)) {
                    throw new Exception ("parameters not exist.");
                }
                else {
                    id = data_id;
                    pw = data_pw;
                }
                connection_string = data_connection_string;

                if (sql_type.equals(SQL_TYPE_SQLITE) && !connection_string.startsWith("jdbc:sqlite:")) {
                    connection_string = "jdbc:sqlite:" + connection_string;
                }
            }
            else {
                if (sql_type.equals(SQL_TYPE_SQLITE) && data_server.length() < 1) {
                    throw new Exception ("parameters not exist.");
                }
                else if (!sql_type.equals(SQL_TYPE_SQLITE) &&
                        (data_server.length() < 1 || data_port < 0 || data_id.length() < 1 ||
                                data_pw.length() < 1 || data_catalog.length() < 1)) {
                    throw new Exception ("parameters not exist.");
                    //return;
                }

                server = data_server;
                port = data_port;
                id = data_id;
                pw = data_pw;
                catalog = data_catalog;

                if (this.sql_type.equals(SQL_TYPE_MSSQL_2000) || this.sql_type.equals(SQL_TYPE_MSSQL)) {
                    connection_string = "jdbc:sqlserver://" + server + ":" + port + ":DatabaseName=" + catalog;
                }
                else if (this.sql_type.equals(SQL_TYPE_MYSQL)) {
                    connection_string = "jdbc:mysql://" + server + ":" + port + "/" + catalog;
                }
                else if (this.sql_type.equals(SQL_TYPE_SQLITE)) {
                    connection_string = "jdbc:sqlite:" + server;
                }
                else if (this.sql_type.equals(SQL_TYPE_SQLITE_ANDROID)) {
                    connection_string = "jdbc:sqldroid:" + server;
                }
                else if (this.sql_type.equals(SQL_TYPE_MARIADB)) {
                }
                else if (this.sql_type.equals(SQL_TYPE_ORACLE)) {
                }
                /** for 1.7
                switch (this.sql_type) {
                    case SQL_TYPE_MYSQL:
                        break;
                    case SQL_TYPE_SQLITE:
                        break;
                    case SQL_TYPE_SQLITE_ANDROID:
                        break;
                    case SQL_TYPE_MSSQL_2000:
                    case SQL_TYPE_MSSQL:
                        break;
                    case SQL_TYPE_MARIADB:
                        break;
                    case SQL_TYPE_ORACLE:
                        break;
                }
                 */
            }
        }

        // properties below...
        public String getSqlType() { return this.sql_type; }
        public void setSqlType(String p_value) { this.sql_type = p_value; }

        public String getConnectionString() { return this.connection_string; }
        public void setConnectionString(String p_value) { this.connection_string = p_value; }

        public String getServer() { return this.server; }
        public void setServer(String p_value) { this.server = p_value; }

        public int getPort() { return this.port; }
        public void setPort(int p_value) { this.port = p_value; }

        public String getCatalog() { return this.catalog; }
        public void setCatalog(String p_value) { this.catalog = p_value; }

        public String getID() { return this.id; }
        public void setID(String p_value) { this.id = p_value; }

        public String getPW() { return this.pw; }
        public void setPW(String p_value) { this.pw = p_value; }
    }

    public static class Basic {
        public enum WHERETYPE {
            GREATER_THAN, GREATER_THAN_OR_EQUAL,
            LESS_THAN, LESS_THAN_OR_EQUAL,
            EQUAL, NOT_EQUAL,
            BETWEEN,
            IN
        }
        public enum VALUETYPE {
            VALUE, QUERY
        }
        public enum CREATE_QUERY_TYPE {
            INSERT, UPDATE, DELETE
        }
        public class ATTRIBUTE {
            public static final String VALUE = "value";
            public static final String WHERE = "where";
        }

        private String table_name;
        private DBConnectionInfo db_info;
        private AZList sql_where, sql_set;
        private AZData data_schema;
        private String query;
        private boolean has_schema_data;

        public Basic(String p_table_name, DBConnectionInfo p_db_connection_info) throws Exception {
            if (p_table_name.trim().length() < 1) {
                throw new Exception("Target table name not specified.");
            }
            this.table_name = AZString.toSQLSafeEncoding(p_table_name);
            this.db_info = p_db_connection_info;

            sql_where = new AZList();
            sql_set = new AZList();
            data_schema = null;

            has_schema_data = false;

            query = "";

            // 지정된 테이블에 대한 스키마 설정
            setSchemaData();
        }

        /**
         *
         * @param p_table_name
         * @param p_db_connection_info
         * @return
         * @throws Exception
         * Created : 2015-06-03 leeyonghun
         */
        public static AZSql.Basic init(String p_table_name, DBConnectionInfo p_db_connection_info) throws Exception {
            if (p_table_name.trim().length() < 1) {
                throw new Exception("Target table name not specified.");
            }
            return new AZSql.Basic(p_table_name, p_db_connection_info);
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        private void setSchemaData() throws Exception {
            if (this.table_name.trim().length() < 1) {
                throw new Exception("Target table name not specified.");
            }

            if (this.db_info.getSqlType().equals(SQL_TYPE_MSSQL_2000)
                    || this.db_info.getSqlType().equals(SQL_TYPE_MSSQL)
                    || this.db_info.getSqlType().equals(SQL_TYPE_MYSQL)) {
                try {
                    String mainSql = "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, CHARACTER_MAXIMUM_LENGTH FROM INFORMATION_SCHEMA.COLUMNS with (nolock) WHERE TABLE_NAME='" + this.table_name + "';";
                    AZList list = AZSql.init(this.db_info).getList(mainSql);

                    this.data_schema = new AZData();

                    for (int cnti=0; cnti<list.size(); cnti++) {
                        AZData data = list.get(cnti);

                        AZData.AttributeData info = new AZData.AttributeData();
                        for (int cntk=0; cntk<data.size(); cntk++) {
                            info.add(data.getKey(cntk), data.getString(cntk));
                        }
                        this.data_schema.add(data.getString(0), info);
                    }

                    if (list.size() > 0) {
                        this.has_schema_data = true;
                    }
                }
                catch (Exception ex) {
                    this.data_schema = null;
                }
            }
            /** for 1.7
            switch (this.db_info.getSqlType()) {
                case AZSql.SQL_TYPE_MSSQL_2000:
                case AZSql.SQL_TYPE_MSSQL:
                case AZSql.SQL_TYPE_MYSQL:
                    break;
            }
             */
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        public AZSql.Basic set(String p_column, Object p_value)throws Exception {
            return set(p_column, p_value, VALUETYPE.VALUE);
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        public AZSql.Basic set(String p_column, Object p_value, VALUETYPE p_valuetype) throws Exception {
            if (p_column.trim().length() < 1) {
                throw new Exception("Target column name is not specified.");
            }
            if (hasSchemaData()) {
                if (!this.data_schema.hasKey(p_column)) {
                    throw new Exception("Target column name is not exist.");
                }
            }
            AZData data = new AZData();
            data.Attribute.add(ATTRIBUTE.VALUE, p_valuetype);
            data.add(p_column, p_value);

            this.sql_set.add(data);
            data = null;

            return this;
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        public AZSql.Basic where(String p_column, Object p_value) throws Exception {
            return where(p_column, p_value, WHERETYPE.EQUAL, VALUETYPE.VALUE);
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        public AZSql.Basic where(String p_column, Object p_value, WHERETYPE p_wheretype) throws Exception {
            return where(p_column, p_value, p_wheretype, VALUETYPE.VALUE);
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        public AZSql.Basic where(String p_column, Object p_value, WHERETYPE p_wheretype, VALUETYPE p_valuetype) throws Exception {
            if (p_column.trim().length() < 1) {
                throw new Exception("Target column name is not specified.");
            }
            if (hasSchemaData()) {
                if (!this.data_schema.hasKey(p_column)) {
                    throw new Exception("Target column name is not exist.");
                }
            }
            AZData data = new AZData();
            data.Attribute.add(ATTRIBUTE.WHERE, p_wheretype);
            data.Attribute.add(ATTRIBUTE.VALUE, p_valuetype);
            data.add(p_column, p_value);

            this.sql_where.add(data);
            data = null;

            return this;
        }

        /**
         * Created : 2015-06-03, leeyonghun
         */
        public AZSql.Basic where(String p_column, Object[] p_values) throws Exception {
            return where(p_column, p_values, WHERETYPE.EQUAL, VALUETYPE.VALUE);
        }

        /**
         * Created : 2015-06-03, leeyonghun
         */
        public AZSql.Basic where(String p_column, Object[] p_values, WHERETYPE p_wheretype) throws Exception {
            return where(p_column, p_values, p_wheretype, VALUETYPE.VALUE);
        }

        /**
         * Created : 2015-06-03, leeyonghun
         */
        public AZSql.Basic where(String p_column, Object[] p_values, WHERETYPE p_wheretype, VALUETYPE p_valuetype) throws Exception {
            if (p_column.trim().length() < 1) {
                throw new Exception("Target column name is not specified.");
            }
            if (hasSchemaData()) {
                if (!this.data_schema.hasKey(p_column)) {
                    throw new Exception("Target column name is not exist.");
                }
            }
            AZData data = new AZData();
            data.Attribute.add(ATTRIBUTE.WHERE, p_wheretype);
            data.Attribute.add(ATTRIBUTE.VALUE, p_valuetype);
            for (int cnti=0; cnti<p_values.length; cnti++) {
                data.add(p_column, p_values[cnti]);
            }

            this.sql_where.add(data);
            data = null;

            return this;
        }

        /**
         * 특정된 쿼리 타입에 맞게 현재의 자료를 바탕으로 쿼리 문자열 생성
         * Created : 2015-06-04, leeyonghun
         */
        private String createQuery(CREATE_QUERY_TYPE p_type) {
            StringBuilder rtn_value = new StringBuilder();
            switch (p_type) {
                case INSERT:
                    rtn_value.append("INSERT INTO " + table_name + " ( " + "\r\n");
                    for (int cnti=0; cnti<this.sql_set.size(); cnti++) {
                        AZData data = this.sql_set.get(cnti);
                        rtn_value.append("  " + (cnti > 0 ? ", " : "") + data.getKey(0));
                    }
                    rtn_value.append("\r\n" + ") " + "\r\n");
                    rtn_value.append("VALUES ( " + "\r\n");
                    for (int cnti=0; cnti<this.sql_set.size(); cnti++) {
                        AZData data = this.sql_set.get(cnti);
                        if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.QUERY)) {
                            rtn_value.append("  " + (cnti > 0 ? ", " : "") + data.getString(0) + "\r\n");
                        }
                        else if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.VALUE)) {
                            rtn_value.append("  " + (cnti > 0 ? ", " : "") + "'" + data.getString(0) + "'" + "\r\n");
                        }
                    }
                    rtn_value.append(")");
                    break;
                case UPDATE:
                    rtn_value.append("UPDATE " + table_name + " " + "\r\n");
                    rtn_value.append("SET " + "\r\n");
                    for (int cnti=0; cnti<this.sql_set.size(); cnti++) {
                        AZData data = this.sql_set.get(cnti);
                        if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.QUERY)) {
                            rtn_value.append("  " + (cnti > 0 ? ", " : "") + data.getKey(0) + " = " + data.getString(0) + "\r\n");
                        }
                        else if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.VALUE)) {
                            rtn_value.append("  " + (cnti > 0 ? ", " : "") + data.getKey(0) + " = " + "'" + data.getString(0) + "'" + "\r\n");
                        }
                    }
                    for (int cnti=0; cnti<this.sql_where.size(); cnti++) {
                        if (cnti < 1) {
                            rtn_value.append("WHERE " + "\r\n");
                        }
                        AZData data = this.sql_where.get(cnti);
                        if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.QUERY)) {
                            rtn_value.append("  " + (cnti > 1 ? " AND " : "") + data.getKey(0));

                            if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.EQUAL)) {
                                rtn_value.append(" = ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN)) {
                                rtn_value.append(" > ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN_OR_EQUAL)) {
                                rtn_value.append(" >= ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN)) {
                                rtn_value.append(" < ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN_OR_EQUAL)) {
                                rtn_value.append(" <= ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.NOT_EQUAL)) {
                                rtn_value.append(" <> ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.BETWEEN)) {
                                rtn_value.append(" BETWEEN ");
                                rtn_value.append(data.getString(0));
                                if (data.size() > 1) {
                                    rtn_value.append(" AND " + data.getString(1));
                                }
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.IN)) {
                                rtn_value.append(" IN ( ");
                                for (int cntk = 0; cntk < data.size(); cntk++) {
                                    rtn_value.append(", " + data.getString(cntk));
                                }
                                rtn_value.append(" ) ");
                            }
                            rtn_value.append("\r\n");
                        }
                        else if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.VALUE)) {
                            rtn_value.append("  " + (cnti > 1 ? " AND " : "") + data.getKey(0));

                            if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.EQUAL)) {
                                rtn_value.append(" = ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN)) {
                                rtn_value.append(" > ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN_OR_EQUAL)) {
                                rtn_value.append(" >= ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN)) {
                                rtn_value.append(" < ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN_OR_EQUAL)) {
                                rtn_value.append(" <= ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.NOT_EQUAL)) {
                                rtn_value.append(" <> ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.BETWEEN)) {
                                rtn_value.append(" BETWEEN ");
                                rtn_value.append("'" + data.getString(0) + "'");
                                if (data.size() > 1) {
                                    rtn_value.append(" AND " + "'" + data.getString(0) + "'");
                                }
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.IN)) {
                                rtn_value.append(" IN ( ");
                                for (int cntk = 0; cntk < data.size(); cntk++) {
                                    rtn_value.append(", " + "'" + data.getString(0) + "'");
                                }
                                rtn_value.append(" ) ");
                            }
                            rtn_value.append("\r\n");
                        }
                    }
                    break;
                case DELETE:
                    rtn_value.append("DELETE FROM " + table_name + " " + "\r\n");
                    for (int cnti = 0; cnti < this.sql_where.size(); cnti++) {
                        if (cnti < 1) {
                            rtn_value.append("WHERE " + "\r\n");
                        }
                        AZData data = this.sql_where.get(cnti);
                        if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.QUERY)) {
                            rtn_value.append("  " + (cnti > 1 ? " AND " : "") + data.getKey(0));

                            if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.EQUAL)) {
                                rtn_value.append(" = ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN)) {
                                rtn_value.append(" > ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN_OR_EQUAL)) {
                                rtn_value.append(" >= ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN)) {
                                rtn_value.append(" < ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN_OR_EQUAL)) {
                                rtn_value.append(" <= ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.NOT_EQUAL)) {
                                rtn_value.append(" <> ");
                                rtn_value.append(data.getString(0));
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.BETWEEN)) {
                                rtn_value.append(" BETWEEN ");
                                rtn_value.append(data.getString(0));
                                if (data.size() > 1) {
                                    rtn_value.append(" AND " + data.getString(1));
                                }
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.IN)) {
                                rtn_value.append(" IN ( ");
                                for (int cntk = 0; cntk < data.size(); cntk++) {
                                    rtn_value.append(", " + data.getString(cntk));
                                }
                                rtn_value.append(" ) ");
                            }
                            rtn_value.append("\r\n");
                        }
                        else if (data.Attribute.get(ATTRIBUTE.VALUE).equals(VALUETYPE.VALUE)) {
                            rtn_value.append("  " + (cnti > 1 ? " AND " : "") + data.getKey(0));

                            if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.EQUAL)) {
                                rtn_value.append(" = ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN)) {
                                rtn_value.append(" > ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.GREATER_THAN_OR_EQUAL)) {
                                rtn_value.append(" >= ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN)) {
                                rtn_value.append(" < ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.LESS_THAN_OR_EQUAL)) {
                                rtn_value.append(" <= ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.NOT_EQUAL)) {
                                rtn_value.append(" <> ");
                                rtn_value.append("'" + data.getString(0) + "'");
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.BETWEEN)) {
                                rtn_value.append(" BETWEEN ");
                                rtn_value.append("'" + data.getString(0) + "'");
                                if (data.size() > 1) {
                                    rtn_value.append(" AND " + "'" + data.getString(0) + "'");
                                }
                            }
                            else if (data.Attribute.get(ATTRIBUTE.WHERE).equals(WHERETYPE.IN)) {
                                rtn_value.append(" IN ( ");
                                for (int cntk = 0; cntk < data.size(); cntk++) {
                                    rtn_value.append(", " + "'" + data.getString(0) + "'");
                                }
                                rtn_value.append(" ) ");
                            }
                            rtn_value.append("\r\n");
                        }
                    }
                    break;
            }
            return rtn_value.toString();
        }

        /**
         * 주어진 자료를 바탕으로 delete 쿼리 실행
         * Created : 2015-06-04, leeyonghun
         */
        public int doDelete() throws Exception {
            return doDelete(true);
        }

        /**
         * 주어진 자료를 바탕으로 delete 쿼리 실행
         * Created : 2015-06-04, leeyonghun
         */
        public int doDelete(boolean p_need_where) throws Exception {
            int rtn_value = -1;
            if (p_need_where && this.sql_where.size() < 1) {
                throw new Exception("Where datas required.");
            }
            rtn_value = AZSql.init(this.db_info).executeUpdate(getQuery(CREATE_QUERY_TYPE.DELETE));
            return rtn_value;
        }

        /**
         * 주어진 자료를 바탕으로 update 쿼리 실행
         * Created : 2015-06-04, leeyonghun
         */
        public int doUpdate() throws Exception {
            return doUpdate(true);
        }

        /**
         * 주어진 자료를 바탕으로 update 쿼리 실행
         * Created : 2015-06-04, leeyonghun
         */
        public int doUpdate(boolean p_need_where) throws Exception {
            int rtn_value = -1;
            if (this.sql_set.size() < 1) {
                throw new Exception("Set datas required.");
            }
            if (p_need_where && this.sql_where.size() < 1) {
                throw new Exception("Where datas required.");
            }
            rtn_value = AZSql.init(this.db_info).executeUpdate(getQuery(CREATE_QUERY_TYPE.UPDATE));
            return rtn_value;
        }

        /**
         * 주어진 자료를 바탕으로 insert 쿼리 실행
         * Created : 2015-06-04, leeyonghun
         */
        public int doInsert() throws Exception {
            int rtn_value = -1;
            if (this.sql_set.size() < 1) {
                throw new Exception("Set datas required.");
            }
            rtn_value = AZSql.init(this.db_info).executeUpdate(getQuery(CREATE_QUERY_TYPE.INSERT));
            return rtn_value;
        }

        /**
         * 특정된 쿼리 실행 종류에 맞는 쿼리 문자열 생성 후 반환
         * Created : 2015-06-03 leeyonghun
         */
        public String getQuery(CREATE_QUERY_TYPE p_create_query_type) {
            return createQuery(p_create_query_type);
        }

        /**
         * 스키마 데이터를 가지고 있는지 확인 용
         * Created : 2015-06-03 leeyonghun
         */
        public boolean hasSchemaData() {
            return this.has_schema_data;
        }

        /**
         * Created : 2015-06-03 leeyonghun
         */
        public AZData getSchemaData() {
            return this.data_schema;
        }
    }
}