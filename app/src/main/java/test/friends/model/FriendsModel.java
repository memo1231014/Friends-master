package test.friends.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FriendsModel implements Parcelable {

    private String id, firstName, lastName, email, call;
    private double latitude, longitude;
    private boolean selected = false;

    public FriendsModel() {
    }

    private FriendsModel(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        id = in.readString();
        id = in.readString();
        email = in.readString();
        call = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(call);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(id);
    }

    public static final Parcelable.Creator<FriendsModel> CREATOR = new Parcelable.Creator<FriendsModel>() {
        public FriendsModel createFromParcel(Parcel in) {
            return new FriendsModel(in);
        }

        public FriendsModel[] newArray(int size) {
            return new FriendsModel[size];

        }
    };

    public String getEmail() {
        return email;
    }

    public String getCall() {
        return call;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
