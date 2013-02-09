package com.miteco.document.xml.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("recipient")
public class Recipient {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    private String name;

    @XStreamAlias("address")
    private Address address;

    public boolean hasAddress() {
        return (getAddress() != null);
    }

}
