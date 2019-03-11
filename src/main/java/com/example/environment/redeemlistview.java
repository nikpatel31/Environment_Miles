package com.example.environment;

public class redeemlistview {

    private String head;
    private String description;
    private int image;

    public redeemlistview() {
    }

    public redeemlistview(String head, String description, int image) {
        this.head = head;
        this.description = description;
        this.image = image;
    }

    public String getHead() {
        return head;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }
}
