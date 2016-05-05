package nz.co.novozhilov.mikhail.haveaniceprice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mikhail on 05.05.2016.
 */
public class Statistics implements Parcelable {
    private int id;
    private String shopTitle;
    private String productTitle;
    private long dateTime;
    private boolean special;
    private double std_price;
    private double disc_price;
    private double old_price;
    private double save_price;

    public Statistics(int id, String shopTitle, String productTitle, long dateTime, boolean special,
                double std_price, double disc_price, double old_price, double save_price) {
        this.id = id;
        this.shopTitle = shopTitle;
        this.productTitle = productTitle;
        this.dateTime = dateTime;
        this.special = special;
        this.std_price = std_price;
        this.disc_price = disc_price;
        this.old_price = old_price;
        this.save_price = save_price;
    }

    public Statistics(Parcel in) {
        this.id = in.readInt();
        this.shopTitle = in.readString();
        this.productTitle = in.readString();
        this.dateTime = in.readLong();
        this.special = (in.readByte() == 1) ? true : false;
        this.std_price = in.readDouble();
        this.disc_price = in.readDouble();
        this.old_price = in.readDouble();
        this.save_price = in.readDouble();
    }

    public int getId() {
        return this.id;
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

    public long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getSpecial() {
        return this.special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
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
        dest.writeInt(this.id);
        dest.writeString(this.shopTitle);
        dest.writeString(this.productTitle);
        dest.writeLong(this.dateTime);
        dest.writeByte((byte) ((this.special) ? 1 : 0));
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
}
