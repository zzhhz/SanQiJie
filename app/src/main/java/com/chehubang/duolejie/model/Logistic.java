package com.chehubang.duolejie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2018/1/31.
 *
 * @date: 2018/1/31
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: 物流公司
 */
public class Logistic implements Parcelable {
    /**
     * id
     */
    private int id;
    /**
     * 名称
     */
    private String logistics;

    /**
     *
     */
    private String logistics_code;


    public String getLogistics_code() {
        return logistics_code;
    }

    public void setLogistics_code(String logistics_code) {
        this.logistics_code = logistics_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogistics() {
        return logistics;
    }

    public void setLogistics(String logistics) {
        this.logistics = logistics;
    }


    public Logistic() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.logistics);
        dest.writeString(this.logistics_code);
    }

    protected Logistic(Parcel in) {
        this.id = in.readInt();
        this.logistics = in.readString();
        this.logistics_code = in.readString();
    }

    public static final Creator<Logistic> CREATOR = new Creator<Logistic>() {
        @Override
        public Logistic createFromParcel(Parcel source) {
            return new Logistic(source);
        }

        @Override
        public Logistic[] newArray(int size) {
            return new Logistic[size];
        }
    };

    @Override
    public String toString() {
        return logistics;
    }
}
