package com.dz.address.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * @Description address bean
 * Created by deng on 2018/8/29.
 */
public class AddressBean implements Parcelable {

    public int id;
    public String name;
    public String phone;
    public String address;
    public boolean isDefault;
    public String tags;

    public AddressBean() {

    }

    public AddressBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phone = in.readString();
        address = in.readString();
        isDefault = in.readInt() == 1;
        tags = in.readString();
    }

    public AddressBean(JSONObject obj) {
        id = obj.optInt("id");
        name = obj.optString("name");
        phone = obj.optString("phone");
        address = obj.optString("address");
        isDefault = obj.optBoolean("isDefault");
        tags = obj.optString("tags");
    }

    public static final Parcelable.Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeInt(isDefault?1:0);
        dest.writeString(tags);
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "id:" + id + "," +
                "name:" + name + "," +
                "phone:" + phone + "," +
                "address:" + address + "," +
                "isDefault:" + isDefault + "," +
                "tags:" + tags +
                "}";
    }
}
