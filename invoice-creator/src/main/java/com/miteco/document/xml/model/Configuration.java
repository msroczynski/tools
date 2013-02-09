package com.miteco.document.xml.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("configuration")
public class Configuration {

    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    @XStreamAlias("invoices")
    private List<Invoice> invoices = new ArrayList<Invoice>();


    public AcceptanceProtocol getAcceptanceProtocol() {
        return acceptanceProtocol;
    }

    public void setAcceptanceProtocol(AcceptanceProtocol acceptanceProtocol) {
        this.acceptanceProtocol = acceptanceProtocol;
    }

    @XStreamAlias("acceptance-protocol")
    private AcceptanceProtocol acceptanceProtocol;

}
