package com.chehubang.duolejie.model;

/**
 * Created by Thinkpad on 2018/1/8.
 */

public class CouponListBean {


    /**
     * id : 48
     * memory_table : fight_record_1
     * need_lettory_num : 2
     * need_people : 10
     * period : 10003
     * progress : 40
     */

    private String id;
    private String memory_table;
    private String need_lettory_num;
    private String need_people;
    private String period;
    private int progress;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemory_table() {
        return memory_table;
    }

    public void setMemory_table(String memory_table) {
        this.memory_table = memory_table;
    }

    public String getNeed_lettory_num() {
        return need_lettory_num;
    }

    public void setNeed_lettory_num(String need_lettory_num) {
        this.need_lettory_num = need_lettory_num;
    }

    public String getNeed_people() {
        return need_people;
    }

    public void setNeed_people(String need_people) {
        this.need_people = need_people;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
