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

package com.mparang.azlib.text;

import com.mparang.azlib.util.AZData;
import com.mparang.azlib.util.AZList;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by leeyonghun on 14. 11. 13..
 */
public class AZString {
    private final Object value;

    public static final int RANDOM_ALPHABET_ONLY = -101;
    public static final int RANDOM_NUMBER_ONLY = -102;
    public static final int RANDOM_ALPHABET_NUMBER = -103;

    //public static final String FORMAT_STRING_LENGTH_NOT_EQUAL = "";
    /**
     * 기본생성자
     * 작성일 : 2014-10-02 이용훈
     */
    public AZString(Object pString) {
        this.value = pString;
    }

    public static AZString init(Object pString) {
        return new AZString(pString);
    }

    public String string() {
        return string("");
    }

    public String string(String pDefault) {
        String rtnValue = "";
        if (this.value == null) {
            rtnValue = pDefault;
        }
        else {
            if (this.value instanceof Integer) {
                try {
                    rtnValue = "" + this.value;
                } catch (Exception ex) {
                    rtnValue = pDefault;
                }
            }
            else {
                try {
                    rtnValue = this.value.toString();
                } catch (Exception ex) {
                    rtnValue = pDefault;
                }
            }
        }
        return rtnValue;
    }

    public String toString() {
        return string();
    }

    public int toInt() {
        return toInt(0);
    }

    public int toInt(int pDefaultValue) {
        int rtnValue = 0;
        if (this.value == null) {
            rtnValue = pDefaultValue;
        }
        else {
            try {
                rtnValue = Integer.parseInt(this.value.toString());
            }
            catch (Exception ex) {
                rtnValue = pDefaultValue;
            }
        }
        return rtnValue;
    }

    public static int toInt(String pValue, int pDefaultValue) {
        int rtnValue = 0;
        try {
            rtnValue = Integer.parseInt(pValue);
        }
        catch (Exception ex) {
            rtnValue = pDefaultValue;
        }
        return rtnValue;
    }

    public static int toInt(String pValue) {
        return AZString.toInt(pValue, 0);
    }

    public static String getRandom() {
        int randomLength = 6;
        Random random = new Random();
        int rndValue = random.nextInt(12);
        if (rndValue >= 3) {
            rndValue = randomLength;
        }
        return AZString.getRandom(rndValue, AZString.RANDOM_ALPHABET_NUMBER, true);
    }

    public static String getRandom(int pLength) {
        return AZString.getRandom(pLength, AZString.RANDOM_ALPHABET_NUMBER, true);
    }

    public static String getRandom(int pLength, String pSourceString) {
        StringBuffer rtnValue = new StringBuffer();
        Random random = new Random();

        if (pSourceString.length() < 1) {
            pSourceString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        }

        for (int cnti=0; cnti<pLength; cnti++) {
            rtnValue.append(pSourceString.charAt(random.nextInt(pSourceString.length())));
        }

        return rtnValue.toString();
    }

    public static String getRandom(int pLength, int pRandomType, boolean pCaseSensitive) {
        String sourceString = "";
        switch (pRandomType) {
            case AZString.RANDOM_NUMBER_ONLY:
                sourceString = "1234567890";
                break;
            case AZString.RANDOM_ALPHABET_NUMBER:
                sourceString = "1234567890";
            case AZString.RANDOM_ALPHABET_ONLY:
                sourceString += "abcdefghijklmnopqrstuvwxyz";
                if (pCaseSensitive) {
                    sourceString += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                }
                break;
        }
        return AZString.getRandom(pLength, sourceString);
    }

    public static String getRepeat(String pString, int pLength) {
        StringBuilder rtnValue = new StringBuilder();
        for (int cnti=0; cnti<pLength; cnti++) {
            rtnValue.append(pString);
        }
        return rtnValue.toString();
    }

    public String getRepeat(int pLength) {
        StringBuilder rtnValue = new StringBuilder();
        for (int cnti=0; cnti<pLength; cnti++) {
            rtnValue.append(this.value);
        }
        return rtnValue.toString();
    }

    /**
     *
     * @param pLength
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String[] toStringArray(int pLength) {
        String[] rtnValue = new String[(int)Math.ceil(this.string().length() / pLength)];

        String src = this.string();
        int idx = 0;
        try {
            while (true) {
                if (src.length() > pLength) {
                    rtnValue[idx] = src.substring(0, pLength);
                    src = src.substring(pLength);
                    idx++;
                } else {
                    rtnValue[idx] = src;
                    break;
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            rtnValue = null;
        }
        finally {
            src = null;
        }

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @param pLength
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String[] toStringArray(String pSrc, int pLength) {
        return new AZString(pSrc).toStringArray(pLength);
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     *
     */
    public String toXSSSafeEncoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replaceAll("<s", "&lt;&#115;");
        rtnValue = rtnValue.replaceAll("<S", "&lt;&#83;");
        rtnValue = rtnValue.replaceAll("<i", "&lt;&#105;");
        rtnValue = rtnValue.replaceAll("<I", "&lt;&#73;");

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toXSSSafeEncoding(String pSrc) {
        return new AZString(pSrc).toXSSSafeEncoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toSQLSafeEncoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replaceAll("&nbsp;", "&nbsp");
        rtnValue = rtnValue.replaceAll("#", "&#35;");      // # 위치 확인!
        rtnValue = rtnValue.replaceAll(";", "&#59;");
        rtnValue = rtnValue.replaceAll("'", "&#39;");
        rtnValue = rtnValue.replaceAll("--", "&#45;&#45;");
        rtnValue = rtnValue.replaceAll("\\\\", "&#92;");
        rtnValue = rtnValue.replaceAll("[*]", "&#42;");
        rtnValue = rtnValue.replaceAll("&nbsp", "&nbsp;");

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toSQLSafeEncoding(String pSrc) {
        return new AZString(pSrc).toSQLSafeEncoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toSQLSafeDecoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replaceAll("&nbsp;", "&nbsp");
        rtnValue = rtnValue.replaceAll("&#42;", "*");
        rtnValue = rtnValue.replaceAll("&#92;", "\\");
        rtnValue = rtnValue.replaceAll("&#45;&#45;", "--");
        rtnValue = rtnValue.replaceAll("&#39;", "'");
        rtnValue = rtnValue.replaceAll("&#35;", "#");
        rtnValue = rtnValue.replaceAll("&#59;", ";");
        rtnValue = rtnValue.replaceAll("&#35;", "#");
        rtnValue = rtnValue.replaceAll("&nbsp", "&nbsp;");

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toSQLSafeDecoding(String pSrc) {
        return new AZString(pSrc).toSQLSafeDecoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toHTMLSafeEncoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replaceAll("&", "&amp;");
        rtnValue = rtnValue.replaceAll("<", "&lt;");
        rtnValue = rtnValue.replaceAll(">", "&gt;");
        rtnValue = rtnValue.replaceAll("\"", "&quot;");

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toHTMLSafeEncoding(String pSrc) {
        return new AZString(pSrc).toHTMLSafeEncoding();
    }

    /**
     *
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toHTMLSafeDecoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replaceAll("&lt;", "<");
        rtnValue = rtnValue.replaceAll("&gt;", ">");
        rtnValue = rtnValue.replaceAll("&quot;", "\"");
        rtnValue = rtnValue.replaceAll("&amp;", "&");

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static String toHTMLSafeDecoding(String pSrc) {
        return new AZString(pSrc).toHTMLSafeDecoding();
    }

    public String toJSONSafeEncoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replace("\\", "\\\\");
        rtnValue = rtnValue.replace("\"", "\\\"");
        rtnValue = rtnValue.replace("\b", "\\b");
        rtnValue = rtnValue.replace("\f", "\\f");
        rtnValue = rtnValue.replace("\n", "\\n");
        rtnValue = rtnValue.replace("\r", "\\r");
        rtnValue = rtnValue.replace("\t", "\\t");

        return rtnValue;
    }
    public static String toJSONSafeEncoding(String pSrc) {
        return new AZString(pSrc).toJSONSafeEncoding();
    }

    public String toJSONSafeDecoding() {
        String rtnValue;

        rtnValue = this.string();
        rtnValue = rtnValue.replace("\\t", "\t");
        rtnValue = rtnValue.replace("\\r", "\r");
        rtnValue = rtnValue.replace("\\n", "\n");
        rtnValue = rtnValue.replace("\\f", "\f");
        rtnValue = rtnValue.replace("\\b", "\b");
        rtnValue = rtnValue.replace("\\\"", "\"");
        rtnValue = rtnValue.replace("\\\\", "\\");

        return rtnValue;
    }
    public static String toJSONSafeDecoding(String pSrc) {
        return new AZString(pSrc).toJSONSafeDecoding();
    }

    /**
     *
     * @param pChar
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public int getContinuousCharacterCount(char pChar) {
        int rtnValue = 0;

        if (this.string().length() > 0) {
            if (this.string().charAt(0) == pChar) {
                String dmyString;
                int cnti;
                for (cnti=0; cnti<this.string().length(); cnti++) {
                    dmyString = this.string().substring(cnti, cnti + 1);

                    if (!dmyString.equals("" + pChar)) {
                        break;
                    }
                }
                rtnValue = cnti;
            }
        }

        return rtnValue;
    }

    /**
     *
     * @param pSrc
     * @param pChar
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public static int getContinuousCharacterCount(String pSrc, char pChar) {
        return new AZString(pSrc).getContinuousCharacterCount(pChar);
    }

    /**
     *
     * @param pInFormat
     * @param pOutFormat
     * @return
     * 작성일 : 2014-11-23 이용훈
     */
    public String toFormat(String pInFormat, String pOutFormat) {
        String rtnValue = pOutFormat;

        try {
            String divString, compString, dmyString;
            int divIndex, compIndex;

            for (int cnti = 0; cnti < rtnValue.length(); cnti++) {
                compString = pOutFormat.substring(cnti);
                dmyString = compString.substring(0, 1);

                compIndex = getContinuousCharacterCount(compString, dmyString.charAt(0));
                divString = compString.substring(0, compIndex);

                if ((divIndex = pInFormat.indexOf(divString)) > -1) {
                    rtnValue = rtnValue.replaceAll(divString, this.string().substring(divIndex, divString.length() + divIndex));
                }
                else {
                    continue;
                }
                cnti += compIndex - 1;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            rtnValue = null;
        }

        return rtnValue;
    }

    /**
     * JSON 관련된 문자열 처리에 대한 메소드들의 묶음 클래스
     * 작성일 : 2014-11-23 이용훈
     */
    public static class JSON {
        private final String json;

        public JSON(String pJson) { this.json = pJson; }

        public static AZString.JSON init(String pJson) { return new AZString.JSON(pJson); }

        private static String removeInnerDQ(String pString) {
            return removeInner(pString, '"', '"');
        }

        private static String removeInner(String pString, char pStartChr, char pEndChr) {
            StringBuilder builder = new StringBuilder();

            int dqStartIdx = -1, dqEndIdx = -1, idx = 0, prevIdx = 0;
            boolean inDQ = false;
            while (true) {
                idx = inDQ ? pString.indexOf(pEndChr, idx) : pString.indexOf(pStartChr, idx);
                if (idx > -1) {
                    if (idx == 0 || idx > 0 && pString.charAt(idx - 1) != '\\') {
                        if (inDQ) {
                            dqEndIdx = idx;
                            inDQ = false;

                            if (dqEndIdx > -1) {
                                builder.append(AZString.getRepeat(" ", dqEndIdx - dqStartIdx - 1));
                            }
                        }
                        else {
                            dqStartIdx = idx;
                            inDQ = true;
                            builder.append(pString.substring(prevIdx, idx) + pStartChr);
                        }
                    }
                    prevIdx = idx;
                    idx++;
                }
                else {
                    builder.append(pString.substring(dqEndIdx < 0 ? 0 : dqEndIdx));
                    break;
                }
            }
            return builder.toString();
        }

        public AZList getList() {
            return AZString.JSON.getList(this.json);
        }

        public static AZList getList(String pJsonString) {
            AZList rtnValue = new AZList();
            pJsonString = pJsonString.trim();
            if (pJsonString.charAt(0) == '[' && pJsonString.charAt(pJsonString.length() - 1) == ']') {
                pJsonString = pJsonString.substring(1, pJsonString.length() - 1).trim();
                String rmDQStr = removeInnerDQ(pJsonString);
                String rmMStr = removeInner(rmDQStr, '[', ']');
                String rmStr = removeInner(rmMStr, '{', '}');

                int idx = 0, preIdx = 0;
                while (true) {
                    idx = rmStr.indexOf(",", idx);
                    if (idx > -1 || (idx == -1 && preIdx > 0)) {
                        String dataStr = "";
                        if (idx > -1) {
                            dataStr = pJsonString.substring(preIdx, idx);
                        }
                        else if (idx == -1) {
                            dataStr = pJsonString.substring(preIdx + 1);
                        }
                        dataStr = dataStr.trim();
                        if (dataStr.charAt(0) == ',') dataStr = dataStr.substring(1).trim();
                        if (dataStr.charAt(dataStr.length() - 1) == ',') dataStr = dataStr.substring(0, dataStr.length()).trim();

                        if (dataStr.charAt(0) == '{') rtnValue.add(getData(dataStr));

                        if (idx == -1) break;

                        preIdx = idx;
                        idx++;
                    }
                    else {
                        if (preIdx < 1 && idx < 0) {
                            String dataStr = rmStr.trim();
                            if (dataStr.length() > 1) {
                                if (dataStr.charAt(0) == '{') {
                                    dataStr = pJsonString.substring(0);
                                    rtnValue.add(getData(dataStr));
                                }
                            }
                        }
                        break;
                    }
                }
            }
            return rtnValue;
        }


        public AZData getData() { return toAZData(); }
        public AZData toAZData() { return AZString.JSON.toAZData(this.json); }

        public static AZData getData(String pJsonString) {
            return AZString.JSON.toAZData(pJsonString);
        }
        public static AZData toAZData(String pJsonString) {
            AZData rtnValue = new AZData();
            pJsonString = pJsonString.trim();
            if (pJsonString.length() < 3) {
                return rtnValue;
            }
            if (pJsonString.charAt(0) == '{' && pJsonString.charAt(pJsonString.length() - 1) == '}') {
                pJsonString = pJsonString.substring(1, pJsonString.length() - 1).trim();
                String rmDQStr = removeInnerDQ(pJsonString);
                String rmMStr = removeInner(rmDQStr, '[', ']');
                String rmStr = removeInner(rmMStr, '{', '}');

                int idx = 0, preIdx = 0;
                while (true) {
                    idx = rmStr.indexOf(",", idx);
                    if (idx == -1 && preIdx == 0) {
                        String dataStr = "";
                        dataStr = pJsonString;
                        dataStr = dataStr.trim();

                        int key_value_idx = dataStr.indexOf(":");
                        String key = dataStr.substring(0, key_value_idx).trim();
                        String valueString = dataStr.substring(key_value_idx + 1).trim();

                        if (key.charAt(0) == '"' || key.charAt(0) == '\'') key = key.substring(1, key.length() - 1);

                        Object value = null;
                        if (valueString.charAt(0) == '{') {
                            value = (AZData)getData(valueString);
                        }
                        else if (valueString.charAt(0) == '[') {
                            value = (AZList)getList(valueString);
                        }
                        else if (valueString.charAt(0) == '"' || valueString.charAt(0) == '\'') {
                            value = (String)valueString.substring(1, valueString.length() - 1);
                        }
                        else {
                            value = valueString;
                        }

                        rtnValue.add(key, value);

                        break;
                    }
                    else if (idx > -1 || (idx == -1 && preIdx > 0)) {
                        String dataStr = "";
                        if (idx > -1) {
                            dataStr = pJsonString.substring(preIdx, idx);
                        }
                        else if (idx == -1) {
                            dataStr = pJsonString.substring(preIdx + 1);
                        }
                        dataStr = dataStr.trim();
                        if (dataStr.charAt(0) == ',') dataStr = dataStr.substring(1).trim();
                        if (dataStr.charAt(dataStr.length() - 1) == ',') dataStr = dataStr.substring(0, dataStr.length()).trim();

                        int key_value_idx = dataStr.indexOf(":");
                        String key = dataStr.substring(0, key_value_idx).trim();
                        String valueString = dataStr.substring(key_value_idx + 1).trim();

                        if (key.charAt(0) == '"' || key.charAt(0) == '\'') key = key.substring(1, key.length() - 1);

                        Object value = null;
                        if (valueString.charAt(0) == '{') {
                            value = (AZData)getData(valueString);
                        }
                        else if (valueString.charAt(0) == '[') {
                            value = (AZList)getList(valueString);
                        }
                        else if (valueString.charAt(0) == '"' || valueString.charAt(0) == '\'') {
                            value = (String)valueString.substring(1, valueString.length() - 1);
                        }
                        else {
                            value = valueString;
                        }

                        rtnValue.add(key, value);

                        if (idx == -1) break;

                        preIdx = idx;
                        idx++;
                    }
                    else {
                        break;
                    }
                }
            }
            return rtnValue;
        }
    }

    public static class HTML {
        private final String html;

        public static class TAG {
            private final String tag;
            public TAG(String pTagString) {
                this.tag = " " + pTagString.trim() + " ";
            }

            public String getTag() {
                return this.tag;
            }

            public String getProperty(String pProperty) {
                String rtnValue = null;

                int propLength = pProperty.length();

                int idx = this.tag.toLowerCase().indexOf(" " + pProperty.toLowerCase() + "=");
                int closeIdx = idx;

                if (idx > -1) {
                    if (this.tag.charAt(idx + propLength + 2) == '"') {
                        // tag가 "로 감싸져있는경우
                        closeIdx = this.tag.indexOf("\"", idx + propLength + 2 + 1);
                        rtnValue = this.tag.substring(idx + propLength + 3, closeIdx);
                    }
                    else {
                        // tag가 "로 감싸지지 않은 경우
                        closeIdx = this.tag.indexOf(" ", idx + propLength + 2);
                        rtnValue = this.tag.substring(idx + propLength + 2, closeIdx);
                    }
                }

                return rtnValue;
            }

        }

        public HTML(String pHtml) {
            this.html = pHtml;
        }

        public static AZString.HTML init(String pHtml) { return new AZString.HTML(pHtml); }

        public String removeTag() {
            return AZString.HTML.removeTag(this.html);
        }

        /**
         *
         * @param pHtml
         * @return
         */
        public static String removeTag(String pHtml) {
            String rtnValue = "";
            int openTagIdx, closeTagIdx;

            try {
                openTagIdx = pHtml.indexOf("<");
                closeTagIdx = pHtml.indexOf(">");

                if (closeTagIdx < openTagIdx) {
                    pHtml = pHtml.substring(openTagIdx);
                }

                while ((openTagIdx = pHtml.indexOf("<")) >= 0) {
                    closeTagIdx = pHtml.indexOf(">", openTagIdx);

                    if (closeTagIdx >= 0) {
                        pHtml = pHtml.replace(pHtml.substring(openTagIdx, closeTagIdx + 1), "");
                    }
                    else {
                        pHtml = pHtml.substring(0, openTagIdx);
                    }
                }
                rtnValue = pHtml;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return rtnValue;
        }

        /**
         *
         * @param pTag
         * @return
         */
        public AZString.HTML.TAG[] getTags(String pTag) {
            int idx = 0;
            int count = 0;
            int htmlLength = this.html.length();
            pTag = "<" + pTag;

            ArrayList array = new ArrayList();

            while ((idx = this.html.toLowerCase().indexOf(pTag.toLowerCase(), idx)) > -1) {
                if (idx + 1 > htmlLength) {
                    break;
                }
                int closeIdx = this.html.indexOf(">", idx + 1);

                if (idx + 1 + pTag.length() >= closeIdx) {
                    idx += (1 + pTag.length());
                    continue;
                }

                String tagString = this.html.substring(idx + 1 + pTag.length(), closeIdx);
                if (tagString.endsWith("/")) {
                    tagString = tagString.substring(0, tagString.length() - 1);
                }
                tagString = tagString.trim();

                array.add(new AZString.HTML.TAG(tagString));

                count++;
                idx += tagString.length();
            }

            AZString.HTML.TAG[] rtnValue = new AZString.HTML.TAG[array.size()];
            for (int cnti=0; cnti<array.size(); cnti++) {
                rtnValue[cnti] = (AZString.HTML.TAG)array.get(cnti);
            }

            array = null;

            return rtnValue;
        }
    }
}
