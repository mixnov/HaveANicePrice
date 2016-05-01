package nz.co.novozhilov.mikhail.haveaniceprice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mikhail on 28.04.2016.
 */
public class Shop implements Parcelable {
    private int id;
    private String title;
    private String url;
    private String div_title;
    private String title_in;
    private Double std_price;
    private Double disc_price;
    private Double old_price;

    public Shop(int id, String title, String url, String div_title, String title_in,
                Double std_price, Double disc_price, Double old_price) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.div_title = div_title;
        this.title_in = title_in;
        this.std_price = std_price;
        this.disc_price = disc_price;
        this.old_price = old_price;
    }

    public Shop(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.url = in.readString();
        this.div_title = in.readString();
        this.title_in = in.readString();
        this.std_price = in.readDouble();
        this.disc_price = in.readDouble();
        this.old_price = in.readDouble();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDivTitle() {
        return div_title;
    }

    public String getTitleIn() {
        return title_in;
    }

    public Double getStdPrice() {
        return std_price;
    }

    public Double getDiscPrice() {
        return disc_price;
    }

    public Double getOldPrice() {
        return old_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.div_title);
        dest.writeString(this.title_in);
        dest.writeDouble(this.std_price);
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
}
