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

import java.util.ArrayList;
//import java.util.HashMap;

/**
 * Created by leeyonghun on 14. 11. 13..
 */
public class AZList {
    ArrayList<AZData> list = null;
    //private HashMap map_attribute = null;
    public AttributeData Attribute = null;

    private String _name = "", _value = "";

    public AZList() {
        list = new ArrayList<AZData>();
        //map_attribute = new HashMap();
        Attribute = new AttributeData();
    }
    /*
    synchronized public String[] getAttributeNames() {
        String[] rtn_value = new String[map_attribute.size()];

        Object[] arrs = map_attribute.keySet().toArray();
        //Dictionary<string, object>.Enumerator arrs = map_attribute.GetEnumerator();
        for (int cnti = 0; cnti < arrs.length; cnti++) {
            rtn_value[cnti] = arrs[cnti].toString();
        }
        return rtn_value;
    }

    synchronized public Object getAttribute(String pName) {
        return map_attribute.get(pName);
    }

    synchronized public String putAttribute(int pIdx, String pValue) {
        map_attribute.put("" + pIdx, pValue);
        return pValue;
    }

    synchronized public String removeAttribute(int pIdx) {
        return (String)map_attribute.remove("" + pIdx);
    }

    synchronized public int clearAttribute() {
        int rtnValue = map_attribute.size();
        map_attribute.clear();
        return rtnValue;
    }
    */

    public String getName() { return this._name; }

    public void setName(String p_name) { this._name = p_name; }

    public String getValue() { return this._value; }

    public void setValue(String p_value) { this._value = p_value; }

    public boolean add(AZData pData) {
        return list.add(pData);
    }

    public boolean remove(AZData pData) {
        return list.remove(pData);
    }

    public AZData remove(int pIndex) {
        return list.remove(pIndex);
    }

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int cnti=0; cnti<list.size(); cnti++) {
            AZData data = list.get(cnti);
            builder.append((cnti > 0 ? ", " : "") + "{" + data.toString() + "}");
        }
        return builder.toString();
    }

    public String toJsonString() {
        StringBuilder builder = new StringBuilder();
        for (int cnti=0; cnti<list.size(); cnti++) {
            AZData data = list.get(cnti);
            builder.append((cnti > 0 ? ", " : "") + data.toJsonString());
        }
        return "[" + builder.toString() + "]";
    }

    public String toXmlString() {
        StringBuilder builder = new StringBuilder();
        for (int cnti = 0; cnti < size(); cnti++) {
            AZData data = list.get(cnti);
            builder.append(data.toXmlString());
        }
        return builder.toString();
    }

    public AZData get(int pIndex) { return getData(pIndex); }

    public AZData getData(int pIndex) { return list.get(pIndex); }

    private class KeyValue {
        private String key;
        private Object value;
        // 기본 생성자
        public KeyValue() {
            this.key = "";
            this.value = "";
        }

        public KeyValue(String p_key, Object p_value) {
            this.key = p_key;
            this.value = p_value;
        }

        public String getKey() { return this.key; }

        public Object getValue() { return this.value; }
        public void setValue(Object p_value) { this.value = p_value; }
        @Override
        public String toString() { return getKey() + ":" + getValue(); }
    }

    /**
     * 속성값(attribute)에 대한 자료 저장용 클래스
     * 작성일 : 2015-05-22 이용훈
     */
    public class AttributeData {
        private ArrayList<KeyValue> attribute_list;

        public AttributeData() {
            this.attribute_list = new ArrayList<KeyValue>();
        }

        public Object add(String p_key, Object p_value) {
            this.attribute_list.add(new KeyValue(p_key, p_value));
            return p_value;
        }

        public Object insertAt(int p_index, String p_key, Object p_value) {
            Object rtn_value = null;
            if (p_index > -1 && p_index < size()) {
                this.attribute_list.add(p_index, new KeyValue(p_key, p_value));
                rtn_value = p_value;
            }
            return rtn_value;
        }

        public Object remove(String p_key) {
            Object rtn_value = null;
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                if (this.attribute_list.get(cnti).getKey().equals(p_key)) {
                    rtn_value = this.attribute_list.get(cnti).getValue();
                    this.attribute_list.remove(cnti);
                    break;
                }
            }
            return rtn_value;
        }

        public Object remove(int p_index) {
            Object rtn_value = null;
            if (p_index > -1 && p_index < size()) {
                rtn_value = get(p_index);
                this.attribute_list.remove(p_index);
            }
            return rtn_value;
        }

        public Object removeAt(int p_index) {
            return remove(p_index);
        }

        public void clear() {
            this.attribute_list.clear();
        }

        public int indexOf(String p_key) {
            int rtn_value = -1;
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                if (this.attribute_list.get(cnti).getKey().equals(p_key)) {
                    rtn_value = cnti;
                    break;
                }
            }
            return rtn_value;
        }

        public Object get(String p_key) {
            Object rtn_value = null;
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                if (this.attribute_list.get(cnti).getKey().equals(p_key)) {
                    rtn_value = this.attribute_list.get(cnti).getValue();
                    break;
                }
            }
            return rtn_value;
        }

        public Object get(int p_index) {
            Object rtn_value = null;
            if (p_index > -1 && p_index < size()) {
                rtn_value = this.attribute_list.get(p_index).getValue();
            }
            return rtn_value;
        }

        public Object set(String p_key, Object p_value) {
            Object rtn_value = null;
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                if (this.attribute_list.get(cnti).getKey().equals(p_key)) {
                    rtn_value = this.attribute_list.get(cnti).getValue();
                    this.attribute_list.get(cnti).setValue(p_value);
                    break;
                }
            }
            return rtn_value;
        }

        public int size() {
            return this.attribute_list.size();
        }

        public String getKey(int p_index) {
            return this.attribute_list.get(p_index).getKey();
        }

        public String[] getKeys() {
            String[] rtn_value = new String[this.attribute_list.size()];
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                rtn_value[cnti] = this.attribute_list.get(cnti).getKey();
            }
            return rtn_value;
        }

        /**
         *
         * @param p_key
         * @return
         * Created in 2015-07-03, leeyonghun
         */
        public boolean hasKey(String p_key) {
            boolean rtn_value = false;
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                if (this.attribute_list.get(cnti).getKey().equals(p_key)) {
                    rtn_value = true;
                    break;
                }
            }
            return rtn_value;
        }

        @Override
        public String toString() {
            StringBuilder rtn_value = new StringBuilder();
            for (int cnti=0; cnti<this.attribute_list.size(); cnti++) {
                rtn_value.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
                        ":" + "\"" + (get(cnti) == null ? "" : AZString.toJSONSafeEncoding(get(cnti).toString())) + "\"");
            }
            return rtn_value.toString();
        }

        public String toJsonString() {
            return "{" + toString() + "}";
        }
    }
}
