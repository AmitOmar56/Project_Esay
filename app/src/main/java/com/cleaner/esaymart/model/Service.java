package com.cleaner.esaymart.model;

/**
 * Created by user on 12/29/2017.
 */

public class Service {
    private String Service_name;
    private int Service_id;
    private String Service_image;

    public Service(String Service_name, int Service_id, String Service_image) {
        this.Service_name = Service_name;
        this.Service_id = Service_id;
        this.Service_image = Service_image;
    }

    public String getService_name() {
        return Service_name;
    }

    public void setService_name(String service_name) {
        Service_name = service_name;
    }

    public int getService_id() {
        return Service_id;
    }

    public void setService_id(int service_id) {
        Service_id = service_id;
    }

    public String getService_image() {
        return Service_image;
    }

    public void setService_image(String service_image) {
        Service_image = service_image;
    }
}

