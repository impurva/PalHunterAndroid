package com.example.googlemapsapi;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
	 
String FirstName;
String  LastName;
Long lat;
Long longitude;
Long uid;
Long timestamp;
 
 
public User(String fname, String lname, long lat, long longitude, long timestamp, long uid){
FirstName=fname;
LastName=lname;
this.lat=lat;
this.longitude=longitude;
this.timestamp=timestamp;
this.uid=uid;

}
 
 
//parcel part
public User(Parcel in){
String[] data= new String[6];
 
in.readStringArray(data);
this.FirstName= data[0];
this.LastName= data[1];
this.lat= Long.parseLong(data[2]);
this.longitude= Long.parseLong(data[3]);
this.timestamp= Long.parseLong(data[4]);
this.uid= Long.parseLong(data[5]);
}
@Override
public int describeContents() {
// TODO Auto-generated method stub
return 0;
}
 
@Override
public void writeToParcel(Parcel dest, int flags) {
// TODO Auto-generated method stub
 
dest.writeStringArray(new String[]{this.FirstName,this.LastName,String.valueOf(this.lat), String.valueOf(this.longitude), String.valueOf(this.timestamp), String.valueOf(this.uid)});
}
 
public static final Parcelable.Creator<User> CREATOR= new Parcelable.Creator<User>() {
 
@Override
public User createFromParcel(Parcel source) {
// TODO Auto-generated method stub
return new User(source);  //using parcelable constructor
}
 
@Override
public User[] newArray(int size) {
// TODO Auto-generated method stub
return new User[size];
}
};
 
}
