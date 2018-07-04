package com.chehubang.duolejie.model;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2017/12/13.
 */

public class GoodsTypeBean implements Serializable {


    /**
     * goods_type_header : http://gg.ilanguo.com/FtpUpload/newsIntro/1479878306737.jpg
     * goods_type_name : 零食天地
     * id : 100191
     */

    private String goods_type_header;
    private String goods_type_name;
    private String id;

    public String getGoods_type_header() {
        return goods_type_header;
    }

    public void setGoods_type_header(String goods_type_header) {
        this.goods_type_header = goods_type_header;
    }

    public String getGoods_type_name() {
        return goods_type_name;
    }

    public void setGoods_type_name(String goods_type_name) {
        this.goods_type_name = goods_type_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
