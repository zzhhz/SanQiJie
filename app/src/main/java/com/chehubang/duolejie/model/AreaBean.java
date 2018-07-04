package com.chehubang.duolejie.model;

import java.util.List;

/**
 * Created by Thinkpad on 2017/12/28.
 */

public class AreaBean {


    private String addr;
    private String id;
    private List<CityBean> city;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    public static class CityBean {
        /**
         * addr : 北京市
         * id : 1
         */

        private String addr;
        private String id;

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
