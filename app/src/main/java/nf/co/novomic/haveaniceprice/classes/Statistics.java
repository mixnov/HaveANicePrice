package nf.co.novomic.haveaniceprice.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Mikhail on 05.05.2016.
 *
 * Class represents the record of Statistics table of DB
 *
 */
public class Statistics implements Parcelable {
    public static final String LOG_TAG = Statistics.class.getSimpleName();
    private long _id;
    private String shopTitle;
    private String productTitle;
    private long dateTime;
    private long productId;
    private String special;
    private double price;
    private double std_price;
    private double disc_price;
    private double old_price;
    private double save_price;

    public Statistics() {
        this.shopTitle = "";
        this.productTitle = "";
        this.special = "";
    }

    public Statistics(long _id, String shopTitle, String productTitle, long productId, long dateTime,
                      String special, double price, double std_price, double disc_price,
                      double old_price, double save_price) {
        this._id = _id;
        this.shopTitle = shopTitle;
        this.productTitle = productTitle;
        this.productId = productId;
        this.dateTime = dateTime;
        this.special = special;
        this.price = price;
        this.std_price = std_price;
        this.disc_price = disc_price;
        this.old_price = old_price;
        this.save_price = save_price;
    }

    public Statistics(Parcel in) {
        this._id = in.readLong();
        this.shopTitle = in.readString();
        this.productTitle = in.readString();
        this.productId = in.readLong();
        this.dateTime = in.readLong();
        this.special = in.readString();
        this.price = in.readDouble();
        this.std_price = in.readDouble();
        this.disc_price = in.readDouble();
        this.old_price = in.readDouble();
        this.save_price = in.readDouble();
    }

    public long getId() {
        return this._id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getShopTitle() {
        return this.shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getProductTitle() {
        return this.productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
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

    public double getSavePrice() {
        return this.save_price;
    }

    public void setSavePrice(double save_price) {
        this.save_price = save_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeString(this.shopTitle);
        dest.writeString(this.productTitle);
        dest.writeLong(this.productId);
        dest.writeLong(this.dateTime);
        dest.writeString(this.special);
        dest.writeDouble(this.price);
        dest.writeDouble(this.std_price);
        dest.writeDouble(this.disc_price);
        dest.writeDouble(this.old_price);
        dest.writeDouble(this.save_price);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public void print(){
        Log.v(LOG_TAG, "=================== Statistics ========================");
        Log.v(LOG_TAG, "_id         = " + String.valueOf(this._id));
        Log.v(LOG_TAG, "shopTitle   = " + this.shopTitle);
        Log.v(LOG_TAG, "productTitle= " + this.productTitle);
        Log.v(LOG_TAG, "productId   = " + String.valueOf(this.productId));
        Log.v(LOG_TAG, "dateTime    = " + Utility.longToDate(this.dateTime));
        Log.v(LOG_TAG, "special     = " + this.special);
        Log.v(LOG_TAG, "price       = " + String.valueOf(this.price));
        Log.v(LOG_TAG, "std_price   = " + String.valueOf(this.std_price));
        Log.v(LOG_TAG, "disc_price  = " + String.valueOf(this.disc_price));
        Log.v(LOG_TAG, "old_price   = " + String.valueOf(this.old_price));
        Log.v(LOG_TAG, "save_price  = " + String.valueOf(this.save_price));
        Log.v(LOG_TAG, "");
    }
}

