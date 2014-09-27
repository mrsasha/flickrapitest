package com.alterego.flickr.app.test.data;

import com.google.gson.annotations.Expose;

public class Photo {

    @Expose
    private String id;
    @Expose
    private String owner;
    @Expose
    private String secret;
    @Expose
    private String server;
    @Expose
    private Integer farm;
    @Expose
    private String title;
    @Expose
    private Integer ispublic;
    @Expose
    private Integer isfriend;
    @Expose
    private Integer isfamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

    public String getUrlFromPhoto (String size) {
        //{ "id": "15347057646", "owner": "94043004@N06", "secret": "dd18a1f3da", "server": "3900", "farm": 4, "title": "", "ispublic": 1, "isfriend": 0, "isfamily": 0 }
        //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{o-secret}_o.(jpg|gif|png)
//        farm-id: 1
//        server-id: 2
//        photo-id: 1418878
//        secret: 1e92283336
//        size: m
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg", getFarm(), getServer(), getId(), getSecret(), size);
    }

}
