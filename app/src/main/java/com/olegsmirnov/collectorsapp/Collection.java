package com.olegsmirnov.collectorsapp;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Collection extends RealmObject {

    private double price;
    private String description;
    private String path;
    private RealmList<CollectionItem> items;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RealmList<CollectionItem> getItems() {
        return items;
    }

    public void setItems(RealmList<CollectionItem> items) {
        this.items = items;
    }


}
