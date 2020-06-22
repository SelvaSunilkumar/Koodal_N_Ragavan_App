package com.example.koodalnraghavan;

public class DataPriceLister {

    String name;
    String url;
    String price;
    String format;

    public DataPriceLister(String name, String url, String price, String format) {
        this.name = name;
        this.url = url;
        this.price = price;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
