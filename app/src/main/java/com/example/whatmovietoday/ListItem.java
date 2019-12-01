package com.example.whatmovietoday;

public class ListItem {
    private String head;
    private String desc;
    private String imageUrl;

    public ListItem(String head, String desc,String imageUrl) {
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "head='" + head + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
