package com.chehubang.duolejie.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2017/12/19.
 */

public class MoneyTypeBean implements Serializable {


    /**
     * id : 26
     * type : oilcard_10000
     * value : 10000
     */

    private String id;
    private String type;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 充值类型
     *
     * @return
     */
    public String getChargeType() {
        if (!TextUtils.isEmpty(type) && type.startsWith("flow")) {
            return "2";
        } else {
            return "1";
        }
    }
}
