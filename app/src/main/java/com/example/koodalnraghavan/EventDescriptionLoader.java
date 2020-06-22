package com.example.koodalnraghavan;

public class EventDescriptionLoader {

    private String month;
    private String month_tml;
    private String day;
    private String thidhi;
    private String star;
    private String event;

    public EventDescriptionLoader()
    {
    }

    public EventDescriptionLoader(String month, String month_tml, String day, String thidhi, String star, String event) {
        this.month = month;
        this.month_tml = month_tml;
        this.day = day;
        this.thidhi = thidhi;
        this.star = star;
        this.event = event;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonth_tml() {
        return month_tml;
    }

    public void setMonth_tml(String month_tml) {
        this.month_tml = month_tml;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getThidhi() {
        return thidhi;
    }

    public void setThidhi(String thidhi) {
        this.thidhi = thidhi;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
