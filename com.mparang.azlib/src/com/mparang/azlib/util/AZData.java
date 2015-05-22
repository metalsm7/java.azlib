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
import java.util.HashMap;

/**
 * Created by leeyonghun on 14. 11. 13..
 */
public class AZData {
    //private boolean isSynchronous = false;

    //private ConcurrentHashMap map_sync = null;
    private HashMap map_async = null;
    //private HashMap map_attribute = null;
    public AttributeData Attribute = null;
    //private HashMap map_index = null;
    private ArrayList<KeyLink> indexer = null;

    private String _name = "", _value = "";

    /**
     * 기본 생성자
     * 작성일 : 2014-11-13
     */
    public AZData() {
        //map_sync = new ConcurrentHashMap();
        map_async = new HashMap();
        //map_index = new HashMap();
        //map_attribute = new HashMap();
        Attribute = new AttributeData();
        indexer = new ArrayList<KeyLink>();
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

    synchronized public Object putAttribute(String pName, Object pValue) {
        map_attribute.put(pName, pValue);
        return pValue;
    }

    synchronized public String removeAttribute(String pName) {
        return (String)map_attribute.remove(pName);
    }

    synchronized public int clearAttribute() {
        int rtnValue = map_attribute.size();
        map_attribute.clear();
        return rtnValue;
    }
    */

    /**
     * key값과 쌍을 이루는 value를 추가
     * @param pKey
     * @param pValue
     * @return
     * 작성일 : 2014-11-18
     */
    synchronized public Object add(String pKey, Object pValue) {
        if (map_async.get(pKey) != null) {
            // 동일 키값이 이미 존재하는 경우
            String linkString = AZString.getRandom(20);
            map_async.put(linkString, pValue);
            indexer.add(new KeyLink(pKey, linkString));
        }
        else {
            // 동일 키값이 존재하지 않는 경우 -> put
            map_async.put(pKey, pValue);
            indexer.add(new KeyLink(pKey, pKey));
        }
        return pValue;
    }

    public String getName() { return this._name; }

    public void setName(String p_name) { this._name = p_name; }

    public String getValue() { return this._value; }

    public void setValue(String p_value) { this._value = p_value; }

    public Object get(int pIndex) { return map_async.get(indexer.get(pIndex).getLink()); }

    public Object get(String pKey) { return map_async.get(pKey); }

    public AZList getList(int pIndex) { return (AZList)get(pIndex); }

    public AZList getList(String pKey) { return (AZList)get(pKey); }

    public AZData getData(int pIndex) { return (AZData)get(pIndex); }

    public AZData getData(String pKey) { return (AZData)get(pKey); }

    public String getString(int pIndex) { return AZString.init(get(pIndex)).string(); }

    public String getString(String pKey) { return AZString.init(get(pKey)).string(); }

    public int getInt(int pIndex) { return AZString.toInt(getString(pIndex), 0); }

    public int getInt(int pIndex, int pDefaultValue) { return AZString.toInt(getString(pIndex), pDefaultValue); }

    public int getInt(String pKey) { return AZString.toInt(getString(pKey), 0); }

    public int getInt(String pKey, int pDefaultValue) { return AZString.toInt(getString(pKey), pDefaultValue); }

    public String getKey(int pIndex) { return indexer.get(pIndex).getKey(); }

    public String getLink(int pIndex) { return indexer.get(pIndex).getLink(); }

    synchronized public boolean set(String pKey, Object pValue) {
        boolean rtnValue = false;
        if (map_async.get(pKey) != null) {
            // 동일 키값이 이미 존재하는 경우
            map_async.put(pKey, pValue);
            rtnValue = true;
        }
        return rtnValue;
    }

    synchronized public boolean set(int pIndex, Object pValue) {
        boolean rtnValue = false;
        if (map_async.get(indexer.get(pIndex).getLink()) != null) {
            // 동일 키값이 이미 존재하는 경우
            map_async.put(indexer.get(pIndex).getLink(), pValue);
            rtnValue = true;
        }
        return rtnValue;
    }

    synchronized public boolean remove(String pKey) {
        boolean rtnValue = false;
        map_async.remove(pKey);
        for (int cnti=0; cnti<indexer.size(); cnti++) {
            if (indexer.get(cnti).getKey().equals(pKey)) {
                indexer.remove(cnti);
                rtnValue = true;
                break;
            }
        }
        return rtnValue;
    }

    synchronized public boolean remove(int pIndex) {
        boolean rtnValue = false;
        String keyString = indexer.get(pIndex).getKey();
        rtnValue = map_async.remove(keyString) == null ? false : true;
        for (int cnti=0; cnti<indexer.size(); cnti++) {
            if (indexer.get(cnti).getKey().equals(keyString)) {
                indexer.remove(cnti);
                rtnValue = rtnValue == true ? true : false;
                break;
            }
        }
        return rtnValue;
    }

    public boolean hasKey(String p_key) {
        return map_async.containsKey(p_key);
    }

    synchronized public int indexOf(String pKey) {
        int rtnValue = -1;
        if (map_async.containsKey(pKey)) {
            for (int cnti=0; cnti<indexer.size(); cnti++) {
                if (indexer.get(cnti).getKey().equals(pKey)) {
                    rtnValue = cnti;
                    break;
                }
            }
        }
        return rtnValue;
    }

    public int size() {
        return map_async.size();
    }

    public void clear() {
        map_async.clear();
        indexer.clear();
    }

    public AZData[] toArray() {
        AZData[] rtnValue = new AZData[size()];
        for (int cnti=0; cnti<size(); cnti++) {
            AZData dmyData = new AZData();
            dmyData.add(getKey(cnti), getString(cnti));
            rtnValue[cnti] = dmyData;
            dmyData = null;
        }
        return rtnValue;
    }

    public String[] toStringArray() {
        String[] rtnValue = new String[size()];
        for (int cnti=0; cnti<size(); cnti++) {
            rtnValue[cnti] = getString(cnti);
        }
        return rtnValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int cnti=0; cnti<indexer.size(); cnti++) {
            if (get(cnti) instanceof AZData) {
                builder.append((cnti > 0 ? ", " : "") + "\"" + getKey(cnti) + "\"" +
                        ":" + "{" + ((AZData)get(cnti)).toString() + "}");
            }
            else if (get(cnti) instanceof AZList) {
                builder.append((cnti > 0 ? ", " : "") + "\"" + getKey(cnti) + "\"" +
                        ":" + "[" + ((AZList)get(cnti)).toString() + "]");
            }
            else {
                String valueString = getString(cnti);
                builder.append((cnti > 0 ? ", " : "") + "\"" + getKey(cnti) + "\"" +
                        ":" + "\"" + valueString + "\"");
            }
        }
        return builder.toString();
    }

    public String toJsonString() {
        StringBuilder builder = new StringBuilder();
        for (int cnti=0; cnti<indexer.size(); cnti++) {
            if (get(cnti) instanceof AZData) {
                builder.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
                        ":" + ((AZData)get(cnti)).toJsonString());
            }
            else if (get(cnti) instanceof AZList) {
                builder.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
                        ":" + ((AZList)get(cnti)).toJsonString());
            }
            else {
                String valueString = getString(cnti);
                builder.append((cnti > 0 ? ", " : "") + "\"" + AZString.toJSONSafeEncoding(getKey(cnti)) + "\"" +
                        ":" + "\"" + AZString.toJSONSafeEncoding(valueString) + "\"");
            }
        }
        return "{" + builder.toString() + "}";
    }

    public String toXmlString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<" + getName());
        //String[] attribute_names = getAttributeNames();
        for (int cnti = 0; cnti < Attribute.size(); cnti++) {
            builder.append(" " + Attribute.getKey(cnti) + "=\"" + Attribute.get(cnti) + "\"");
        }
        builder.append(">");

        for (int cnti = 0; cnti < indexer.size(); cnti++) {
            if (get(cnti) instanceof AZData) {
                builder.append(((AZData)get(cnti)).toXmlString());
            }
            else if (get(cnti) instanceof AZList) {
                builder.append(((AZList)get(cnti)).toXmlString());
            }
        }
        builder.append(getValue() + "</" + getName() + ">");
        return builder.toString();
    }

    private class KeyLink {
        private String key, link;
        // 기본생성자
        public KeyLink() {
            this.key = "";
            this.link = "";
        }

        public KeyLink(String pKey, String pLink) {
            this.key = pKey;
            this.link = pLink;
        }

        public String getKey() { return this.key; }
        public String getLink() { return this.link; }
        @Override
        public String toString() { return getKey() + ":" + getLink(); }
    }

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
    }
}
