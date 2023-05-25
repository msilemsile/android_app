package me.msile.app.androidapp.common.contact.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 联系人model
 */
public class ContactModel implements Parcelable {
    private String name;
    private String phone;
    private String pinYinName;

    public ContactModel() {
    }

    public String getPinYinName() {
        if (TextUtils.isEmpty(pinYinName)) {
            return "";
        }
        return pinYinName;
    }

    public void setPinYinName(String pinYinName) {
        this.pinYinName = pinYinName;
    }

    public String getName() {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.phone = source.readString();
    }

    protected ContactModel(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
    }

    public static final Creator<ContactModel> CREATOR = new Creator<ContactModel>() {
        @Override
        public ContactModel createFromParcel(Parcel source) {
            return new ContactModel(source);
        }

        @Override
        public ContactModel[] newArray(int size) {
            return new ContactModel[size];
        }
    };
}
