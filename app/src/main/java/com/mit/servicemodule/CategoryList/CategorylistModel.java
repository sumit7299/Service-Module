package com.mit.servicemodule.CategoryList;

public class CategorylistModel {

    private String id;
    private String servicename;
    private String servicehours;
    private String serviceminutes;
    private String categoryservice;
    private String serviceprice;

    public CategorylistModel(String id, String servicename, String servicehours, String serviceminutes, String categoryservice, String serviceprice) {
        this.id = id;
        this.servicename = servicename;
        this.servicehours = servicehours;
        this.serviceminutes = serviceminutes;
        this.categoryservice = categoryservice;
        this.serviceprice = serviceprice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getServicehours() {
        return servicehours;
    }

    public void setServicehours(String servicehours) {
        this.servicehours = servicehours;
    }

    public String getServiceminutes() {
        return serviceminutes;
    }

    public void setServiceminutes(String serviceminutes) {
        this.serviceminutes = serviceminutes;
    }

    public String getCategoryservice() {
        return categoryservice;
    }

    public void setCategoryservice(String categoryservice) {
        this.categoryservice = categoryservice;
    }

    public String getServiceprice() {
        return serviceprice;
    }

    public void setServiceprice(String serviceprice) {
        this.serviceprice = serviceprice;
    }
}
