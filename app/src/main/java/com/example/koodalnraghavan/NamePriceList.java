package com.example.koodalnraghavan;

public class NamePriceList {

    public String portal;
    public String url;
    public long value;

    public NamePriceList()
    {

    }

    public NamePriceList(String portal, String url, long value) {
        this.portal = portal;
        this.url = url;
        this.value = value;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
