package com.chehubang.duolejie.model;

import java.util.List;

/**
 * Created by ZZH on 2018/3/9.
 *
 * @Date: 2018/3/9
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class HotKey {


    private List<HotSearchListBean> hotSearchList;

    public List<HotSearchListBean> getHotSearchList() {
        return hotSearchList;
    }

    public void setHotSearchList(List<HotSearchListBean> hotSearchList) {
        this.hotSearchList = hotSearchList;
    }

    public static class HotSearchListBean {
        /**
         * search_key : iphonex
         */

        private String search_key;

        public String getSearch_key() {
            return search_key;
        }

        public void setSearch_key(String search_key) {
            this.search_key = search_key;
        }
    }
}
