package nz.co.novozhilov.mikhail.haveaniceprice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mikhail on 03.05.2016.
 */
public class Product implements Parcelable {
    private int id;
    private int shop_id;
    private String url;
    private String title;
    private String img_url;

    public Product(int id, int shop_id, String url, String title, String img_url) {
        this.id = id;
        this.shop_id = shop_id;
        this.url = url;
        this.title = title;
        this.img_url = img_url;
    }

    public Product(Parcel in) {
        this.id = in.readInt();
        this.shop_id = in.readInt();
        this.url = in.readString();
        this.title = in.readString();
        this.img_url = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shop_id;
    }

    public void setShopId(int shop_id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.shop_id);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.img_url);
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

