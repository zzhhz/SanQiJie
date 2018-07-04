package com.chehubang.duolejie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZZH on 2018/2/10.
 *
 * @Date: 2018/2/10
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class Address implements Parcelable {
    private String province;
    private String id;
    private String isDefault;
    private String address;
    private String city;
    private String userName;
    private String area;
    private String mobile;

    private String takeProviceId;
    private String takeCityId;
    private String takeAreaId;
    private String zipcode;
    private int belong;

    //百度搜索详细地址别名
    private String detailName;
    //是否是当前搜索地址（用于百度地图选址）
    private boolean isCurrent;
    //经纬度
    private double longitude;
    private double latitude;
    //门牌号
    private String street;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTakeProviceId() {
        return takeProviceId;
    }

    public void setTakeProviceId(String takeProviceId) {
        this.takeProviceId = takeProviceId;
    }

    public String getTakeCityId() {
        return takeCityId;
    }

    public void setTakeCityId(String takeCityId) {
        this.takeCityId = takeCityId;
    }

    public String getTakeAreaId() {
        return takeAreaId;
    }

    public void setTakeAreaId(String takeAreaId) {
        this.takeAreaId = takeAreaId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getBelong() {
        return belong;
    }

    public void setBelong(int belong) {
        this.belong = belong;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.id);
        dest.writeString(this.isDefault);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.userName);
        dest.writeString(this.area);
        dest.writeString(this.mobile);
        dest.writeString(this.takeProviceId);
        dest.writeString(this.takeCityId);
        dest.writeString(this.takeAreaId);
        dest.writeString(this.zipcode);
        dest.writeInt(this.belong);
        dest.writeString(this.detailName);
        dest.writeByte(this.isCurrent ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.street);
    }

    public Address() {
    }

    protected Address(Parcel in) {
        this.province = in.readString();
        this.id = in.readString();
        this.isDefault = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.userName = in.readString();
        this.area = in.readString();
        this.mobile = in.readString();
        this.takeProviceId = in.readString();
        this.takeCityId = in.readString();
        this.takeAreaId = in.readString();
        this.zipcode = in.readString();
        this.belong = in.readInt();
        this.detailName = in.readString();
        this.isCurrent = in.readByte() != 0;
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.street = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
