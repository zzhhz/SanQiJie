package com.chehubang.duolejie.model;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2017/12/19.
 */

public class OilCardMoneyTypeBean implements Serializable {


    /**
     * id : 26
     * type : oilcard_10000
     * value : 10000
     */

    private String id;
    private String type;
    private String value;
    private String lettory;

    public String getLettory() {
        return lettory;
    }

    public void setLettory(String lettory) {
        this.lettory = lettory;
    }

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
}
