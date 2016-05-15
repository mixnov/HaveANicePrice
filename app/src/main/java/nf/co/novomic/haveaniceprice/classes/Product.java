package nf.co.novomic.haveaniceprice.classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mikhail on 03.05.2016.
 *
 * Class represents the Product
 */
public class Product implements Parcelable {
    private long _id;
    private long shop_id;
    private String url;
    private String title;
    private String img_url;
    private int track;

    public Product() {
    }

    public Product(long _id, long shop_id, String url, String title, String img_url, int track) {
        this._id = _id;
        this.shop_id = shop_id;
        this.url = url;
        this.title = title;
        this.img_url = img_url;
        this.track = track;
    }

    public Product(Parcel in) {
        this._id = in.readLong();
        this.shop_id = in.readLong();
        this.url = in.readString();
        this.title = in.readString();
        this.img_url = in.readString();
        this.track = in.readInt();
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public long getShopId() {
        return shop_id;
    }

    public void setShopId(long shop_id) {
        this.shop_id = shop_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }

    public int getTrack() {
        return this.track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeLong(this.shop_id);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.img_url);
        dest.writeInt(this.track);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public boolean isEmpty() {
        return this.shop_id == 0 || this.url == null  || this.url.equals("") ||
                this.title == null || this.title.equals("") || this.img_url == null ||
                this.img_url.equals("");
    }
}
