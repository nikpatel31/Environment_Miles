package com.example.environment;

import com.google.firebase.Timestamp;

import java.util.Date;

public class blogpost {

    public String user_id , image_url1 , description , image_thumb1,address;
    public Date timestamp;

    public blogpost() {

    }


    public blogpost(String user_id, String image_url, String description, String image_thumb, Date timestamp , String address) {
        this.user_id = user_id;
        this.image_url1 = image_url;
        this.description = description;
        this.image_thumb1 = image_thumb;
        this.timestamp = timestamp;
        this.address=address;

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url1() {
        return image_url1;
    }

    public void setImage_url1(String image_url1) {
        this.image_url1 = image_url1;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_thumb1() {
        return image_thumb1;
    }

    public void setImage_thumb1(String image_thumb1) {
        this.image_thumb1 = image_thumb1;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
