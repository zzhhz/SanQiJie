package com.chehubang.duolejie.model;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class GoodsTypeChildBean {

    private String good_header;
    private String good_name;
    private String id;
    private String good_price;


    public String getGood_price() {
        return good_price;
    }

    public void setGood_price(String good_price) {
        this.good_price = good_price;
    }



    public String getGoods_type_header() {
        return good_header;
    }

    public void setGoods_type_header(String goods_type_header) {
        this.good_header = goods_type_header;
    }

    public String getGoods_type_name() {
        return good_name;
    }

    public void setGoods_type_name(String goods_type_name) {
        this.good_name = goods_type_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
