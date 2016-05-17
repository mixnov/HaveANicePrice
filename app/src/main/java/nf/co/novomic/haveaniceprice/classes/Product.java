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
    private String special;
    private double price;
    private double std_price;
    private double min_price;
    private double max_price;
    private double disc_price;
    private double old_price;

    public Product() {
    }

    public Product(long _id, long shop_id, String url, String title, String img_url, int track,
                   String special, double price, double std_price, double min_price, double max_price,
                   double disc_price, double old_price) {
        this._id = _id;
        this.shop_id = shop_id;
        this.url = url;
        this.title = title;
        this.img_url = img_url;
        this.track = track;
        this.special = special;
        this.price = price;
        this.std_price = std_price;
        this.min_price = min_price;
        this.max_price = max_price;
        this.disc_price = disc_price;
        this.old_price = old_price;

    }

    public Product(Parcel in) {
        this._id = in.readLong();
        this.shop_id = in.readLong();
        this.url = in.readString();
        this.title = in.readString();
        this.img_url = in.readString();
        this.track = in.readInt();
        this.special = in.readString();
        this.price = in.readDouble();
        this.std_price = in.readDouble();
        this.min_price = in.readDouble();
        this.max_price = in.readDouble();
        this.disc_price = in.readDouble();
        this.old_price = in.readDouble();
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

    public String getSpecial() {
        return this.special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getStdPrice() {
        return this.std_price;
    }

    public void setStdPrice(double std_price) {
        this.std_price = std_price;
    }

    public double getMinPrice() {
        return this.min_price;
    }

    public void setMinPrice(double min_price) {
        this.min_price = min_price;
    }

    public double getMaxPrice() {
        return this.max_price;
    }

    public void setMaxPrice(double max_price) {
        this.max_price = max_price;
    }

    public double getOldPrice() {
        return this.old_price;
    }

    public void setOldPrice(double old_price) {
        this.old_price = old_price;
    }

    public double getDiscPrice() {
        return this.disc_price;
    }

    public void setDiscPrice(double disc_price) {
        this.disc_price = disc_price;
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
        dest.writeString(this.special);
        dest.writeDouble(this.price);
        dest.writeDouble(this.std_price);
        dest.writeDouble(this.min_price);
        dest.writeDouble(this.max_price);
        dest.writeDouble(this.disc_price);
        dest.writeDouble(this.old_price);
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
