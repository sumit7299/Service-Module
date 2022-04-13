package com.mit.servicemodule.ServiceList;

public class ServiceModel {

    private String id;
    private String name;
    private String description;
    private String categoryid;
    private String categoryname;
    private String imageid;
    private String hours;
    private String minutes;
    private String price;
    private String image;

    public ServiceModel(String id, String name, String description, String categoryid, String categoryname, String imageid, String hours, String minutes, String price, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.imageid = imageid;
        this.hours = hours;
        this.minutes = minutes;
        this.price = price;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getImageid() {
        return imageid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

