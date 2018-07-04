package com.chehubang.duolejie.model;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2017/12/29.
 */

public class AddressListBean implements Serializable {

    private String city_id;
    private String city_addr;
    private long create_time;
    private String detail_address;
    private String id;
    private String is_default;
    private String province_id;
    private String province_addr;
    private String user_id;
    private String user_name;
    private String user_tel;

    public String getProvince_addr() {
        return province_addr;
    }

    public void setProvince_addr(String province_addr) {
        this.province_addr = province_addr;
    }

    public String getCity_addr() {
        return city_addr;
    }

    public void setCity_addr(String city_addr) {
        this.city_addr = city_addr;
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
