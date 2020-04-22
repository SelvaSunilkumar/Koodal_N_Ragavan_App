package com.example.koodalnraghavan;

public class PdfLoader
{
    public String url;
    public String portal;

    public PdfLoader()
    {
    }

    public PdfLoader(String url, String portal) {
        this.url = url;
        this.portal = portal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }
}
