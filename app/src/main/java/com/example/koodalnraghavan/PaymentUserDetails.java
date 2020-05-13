package com.example.koodalnraghavan;

public class PaymentUserDetails {

    String amount;
    String name;
    String refno;

    public PaymentUserDetails(String amount, String name, String refno) {
        this.amount = amount;
        this.name = name;
        this.refno = refno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
