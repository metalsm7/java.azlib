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
package com.mparang.azlib.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by leeyonghun on 14. 11. 13..
 */

public class AZHttp {
    public static final String ENCODING_UTF_8 = "utf-8";
    public static final String ENCODING_EUC_KR = "euc-kr";
    public static final String ENCODING_EUC_JP = "euc-jp";

    private String url = "";
    private String query = "";
    private String cookie = "";

    /**
     * 기본 생성자
     * @param pUrl 접속할 원격 주소지
     * 작성일 : 2014-10-01
     */
    public AZHttp(String pUrl) {
        setUrl(pUrl);
    }

    /**
     * @param pUrl 접속할 원격 주소지
     * 작성일 : 2014-10-01 이용훈
     */
    public void setUrl(String pUrl) {
        this.url = pUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public void setQuery(String pQuery) { this.query = pQuery; }

    public void addQuery(String pKey, String pValue) {
        addQuery(pKey, pValue, AZHttp.ENCODING_UTF_8);
    }

    public void addQuery(String pKey, String pValue, String pEncode) {
        try {
            this.query += (this.query.length() < 1 ? "" : "&") + URLEncoder.encode(pKey, pEncode) + "=" + URLEncoder.encode(pValue, pEncode);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getQuery() { return this.query; }

    public void setCookie(String pCookie) { this.cookie = pCookie; }

    public void addCookie(String pKey, String pValue) {
        addCookie(pKey, pValue);
    }

    public void addCookie(String pKey, String pValue, String pEncode) {
        try {
            this.cookie += (this.cookie.length() < 1 ? "" : "&") + URLEncoder.encode(pKey, pEncode) + "=" + URLEncoder.encode(pValue, pEncode);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getCookie() { return this.cookie; }

    /**
     * @return 해당 주소지의 html코드 반환
     * @throws Exception
     * 작성일 : 2014-10-01 이용훈
     */
    public String getHtml() throws Exception {
        return getHtml(this.url, "");
    }

    /**
     * @param pEncoding
     * @return 해당 주소지의 html코드 반환
     * @throws Exception
     * 작성일 : 2014-10-01 이용훈
     */
    public String getHtml(String pEncoding) throws Exception {
        return getHtml(pEncoding, getCookie());
    }

    /**
     * @param pEncoding
     * @param pCookie
     * @return 해당 주소지의 html코드 반환
     * @throws Exception
     * 작성일 : 2014-10-01 이용훈
     */
    public String getHtml(String pEncoding, String pCookie) throws Exception {
        String rtnValue = "";

        URL url = new URL(this.getUrl());
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        if (getQuery().length() > 0) {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(getQuery());
            writer.flush();
            writer.close();
            writer = null;
        }
        if (pCookie.length() > 0) {
            connection.addRequestProperty("Cookie", pCookie);
        }
        connection.connect();
        InputStreamReader inReader = new InputStreamReader(connection.getInputStream(), pEncoding);
        int BUFFER_SIZE = 8192;
        BufferedReader bufReader = new BufferedReader(inReader, BUFFER_SIZE);

        String readLine = "";
        while ((readLine = bufReader.readLine()) != null) {
            rtnValue += !readLine.equals("") ? readLine + "\r\n" : readLine;
        }

        bufReader.close();
        inReader.close();

        bufReader = null;
        inReader = null;

        return rtnValue;
    }
}

