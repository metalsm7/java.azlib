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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leeyonghun on 14. 11. 13..
 */
public class AZList {
    ArrayList<AZData> list = null;
    private HashMap map_attribute = null;

    public AZList() {
        list = new ArrayList<AZData>();
        map_attribute = new HashMap();
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

    public AZData get(int pIndex) { return getData(pIndex); }

    public AZData getData(int pIndex) { return list.get(pIndex); }
}
