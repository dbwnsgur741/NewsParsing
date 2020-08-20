package com.weather.fixyoo;

public class ItemObject {
    private String title;
    private String img_url;
    private String detail_link;
    private String desc;
    private String link_url;

    public ItemObject(String title,String desc,String img_url,String link_url){
        this.title = title;
        this.desc = desc;
        this.img_url = img_url;
        this.link_url = link_url;
    }

    public String getLink_url(){
        return link_url;
    }

    public String getTitle() {
        return title;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public String getDesc() {
        return desc;
    }
}
