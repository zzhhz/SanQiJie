package com.chehubang.duolejie.model;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2017/12/29.
 */

public class AddressListMangerBean implements Serializable {

          /*  "city_id": "115",
            "city_name": "福州市",
            "create_time": 1515036982000,
            "detail_address": "JulPSP",
            "id": 559,
            "is_default": "y",
            "province_id": "13",
            "province_name": "福建省",
            "user_id": "10002",
            "user_name": "催化剂",
            "user_tel": "5745555887722"*/


    private String city_id;
    private String city_name;
    private long create_time;
    private String detail_address;
    private String id;
    private String is_default;
    private String province_id;
    private String province_name;
    private String user_id;
    private String user_name;
    private String user_tel;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }


    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }
}
