package com.example.whatmovietoday;

public class ListItem {
    private String head;
    private String desc;
    private String imageUrl;
    private String ID;

    public ListItem(String head, String desc,String imageUrl, String ID) {
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.ID=ID;
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

    public String getID(){return ID;}

    @Override
    public String toString() {
        return "ListItem{" +
                "head='" + head + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
