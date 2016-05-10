package nz.co.novozhilov.mikhail.haveaniceprice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mikhail on 28.04.2016.
 *
 * Class represents the Shop
 */
public class Shop implements Parcelable {
    private long _id;
    private String url;
    private String title;
    private String img_url;
    private String special;
    private String std_price;
    private String disc_price;
    private String old_price;
    private String save_price;

    public Shop(long _id, String url, String title, String img_url, String special,
                String std_price, String disc_price, String old_price, String save_price) {
        this._id = _id;
        this.url = url;
        this.title = title;
        this.img_url = img_url;
        this.special = special;
        this.std_price = std_price;
        this.disc_price = disc_price;
        this.old_price = old_price;
        this.save_price = save_price;
    }

    public Shop(long _id, String url, String title) {
        this._id = _id;
        this.url = url;
        this.title = title;
    }

    public Shop(Parcel in) {
        this._id = in.readLong();
        this.url = in.readString();
        this.title = in.readString();
        this.img_url = in.readString();
        this.special = in.readString();
        this.std_price = in.readString();
        this.disc_price = in.readString();
        this.old_price = in.readString();
        this.save_price = in.readString();
    }

    public long getId() {
        return this._id;
    }

    public void getId(long _id) {
        this._id = _id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return this.img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }

    public String getSpecial() {
        return this.special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getStdPrice() {
        return this.std_price;
    }

    public void setStdPrice(String std_price) {
        this.std_price = std_price;
    }

    public String getOldPrice() {
        return this.old_price;
    }

    public void setOldPrice(String old_price) {
        this.old_price = old_price;
    }

    public String getDiscPrice() {
        return this.disc_price;
    }

    public void setDiscPrice(String disc_price) {
        this.disc_price = disc_price;
    }

    public String getSavePrice() {
        return this.save_price;
    }

    public void setSavePrice(String save_price) {
        this.save_price = save_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.img_url);
        dest.writeString(this.special);
        dest.writeString(this.std_price);
        dest.writeString(this.disc_price);
        dest.writeString(this.old_price);
        dest.writeString(this.save_price);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };
}
