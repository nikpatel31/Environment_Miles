package com.example.environment;

public class blogrank {

    public String points;
    public String image;
    public String name;
    public int rank;

    public blogrank() {
    }

    public blogrank(String points, String image, String name) {
        this.points = points;
        this.image = image;
        this.name = name;

    }


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
}