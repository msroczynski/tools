package com.miteco.document.xml.model;


import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Date;

@XStreamAlias("acceptance-protocol")
public class AcceptanceProtocol {

    public Orderer getOrderer() {
        return orderer;
    }

    public void setOrderer(Orderer orderer) {
        this.orderer = orderer;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public boolean hasOrderer() {
        return (getOrderer() != null);
    }

    public boolean hasRecipient() {
        return (getRecipient() != null);
    }

    @XStreamAlias("orderer")
    private Orderer orderer;

    @XStreamAlias("recipient")
    private Recipient recipient;

    public Date getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }

    public double getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(double daysNumber) {
        this.daysNumber = daysNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    private Date issuingDate;
    private double daysNumber;
    private String orderNumber;
    private Date orderDate;


}
