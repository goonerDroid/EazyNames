package com.project.sublime.eazynames.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by goonerDroid on 17-04-2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersBean {
    public UsersBean() {
    }

    @JsonProperty("Users")
    private ArrayList<User> userArrayList;

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable,Parcelable {

        public User() {
        }

        public User(Parcel in){
            firstName=in.readString();
            lastName=in.readString();
            userName=in.readString();
        }

        @JsonProperty("firstname")
        private String firstName;
        @JsonProperty("lastname")
        private String lastName;
        @JsonProperty("username")
        private String userName;

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getUserName() {
            return userName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(getFirstName());
            dest.writeString(getLastName());
            dest.writeString(getUserName());
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            public User[] newArray(int size) {
                return new User[size];
            }
        };
    }
}
