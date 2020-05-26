package com.example.koodalnraghavan;

public class EventDescriptionLoader {

    String description;
    String name;
    String url;

    public EventDescriptionLoader()
    {

    }

    public EventDescriptionLoader(String description, String name, String url) {
        this.description = description;
        this.name = name;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
