package com.chehubang.duolejie.model;

import java.util.List;

/**
 * Created by ZZH on 2018/2/28.
 *
 * @Date: 2018/2/28
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class BrandType {

    private List<BrandTypeListBean> brandTypeList;

    public List<BrandTypeListBean> getBrandTypeList() {
        return brandTypeList;
    }

    public void setBrandTypeList(List<BrandTypeListBean> brandTypeList) {
        this.brandTypeList = brandTypeList;
    }

    public static class BrandTypeListBean {
        /**
         * brand_type : 休闲娱乐
         * is_open : y
         * id : 1
         */

        private String brand_type;
        private String is_open;
        private int id;

        public String getBrand_type() {
            return brand_type;
        }

        public void setBrand_type(String brand_type) {
            this.brand_type = brand_type;
        }

        public String getIs_open() {
            return is_open;
        }

        public void setIs_open(String is_open) {
            this.is_open = is_open;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
