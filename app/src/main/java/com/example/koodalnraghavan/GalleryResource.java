package com.example.koodalnraghavan;

public class GalleryResource {

    private String imageUrl;

    public GalleryResource(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
