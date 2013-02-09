package com.miteco.document.xml.model;


import java.math.BigDecimal;

public class Amount implements Cloneable{

    public Amount(BigDecimal net, BigDecimal vat) {
        setNet(net);
        setVat(vat);
    }
    
    private BigDecimal net;
    private BigDecimal vat;

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getGross() {
        return getNet().add(getVat());
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch( CloneNotSupportedException e )
        {
            return null;
        }
    }


}
