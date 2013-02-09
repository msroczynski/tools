package com.miteco.document.xml.model;

import com.miteco.document.xml.model.Address;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("buyer")
public class Buyer {

    private String name;
    private String nip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @XStreamAlias("address")
    private Address address;

    public boolean hasAddress() {
        return (getAddress() != null);
    }
}
