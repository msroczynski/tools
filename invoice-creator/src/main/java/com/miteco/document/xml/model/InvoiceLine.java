package com.miteco.document.xml.model;

import com.miteco.document.xml.model.Amount;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.math.BigDecimal;
import java.math.RoundingMode;

@XStreamAlias("invoiceLine")
public class InvoiceLine {

    private int lineNumber;

    private static final int decimalPlaces = 2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private int unitsNumber;
    private BigDecimal netUnitPrice;
    private int vatRate;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getUnitsNumber() {
        return unitsNumber;
    }

    public void setUnitsNumber(int unitsNumber) {
        this.unitsNumber = unitsNumber;
    }

    public BigDecimal getNetUnitPrice() {
        return netUnitPrice;
    }

    public void setNetUnitPrice(BigDecimal netUnitPrice) {
        this.netUnitPrice = netUnitPrice;
    }

    public int getVatRate() {
        return vatRate;
    }

    public void setVatRate(int vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getNetValue() {
        BigDecimal result = getNetUnitPrice().multiply(BigDecimal.valueOf(getUnitsNumber()));
        // TODO configure it global
        result = result.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    public BigDecimal getVatValue() {
        BigDecimal result = BigDecimal.valueOf(getVatRate()).divide(BigDecimal.valueOf(100)).multiply(getNetValue());
        // TODO configure it global
        result = result.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        return result;
    }

    public BigDecimal getGrossValue() {
        return getNetValue().add(getVatValue());
    }

    public Amount createAmount() {
        return new Amount(getNetValue(), getVatValue());
    }

}
