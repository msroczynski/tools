package com.miteco.document.xml.model;

import com.miteco.util.Constants;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.math.BigDecimal;
import java.util.*;

@XStreamAlias("invoice")
public class Invoice {

    private String invoiceNumber;
    private Date issuingDate;
    private Date sellingDate;
    private String paymentType;
    private Date paymentDate;
    private String bankName;
    private String bankAccountNumber;

    public String getInvoiceKind() {
        return invoiceKind;
    }

    public void setInvoiceKind(String invoiceKind) {
        this.invoiceKind = invoiceKind;
    }

    private String invoiceKind;

    public void setAsCopy() {
         setInvoiceKind(Constants.INV_COPY);
    }

    public void setAsOriginal() {
        setInvoiceKind(Constants.INV_ORIGINAL);
    }

    public String getSeparateCopy() {
        return separateCopy;
    }

    public void setSeparateCopy(String separateCopy) {
        this.separateCopy = separateCopy;
    }

    @XStreamAsAttribute
    @XStreamAlias("split")
    private String separateCopy;

    public boolean split() {
        if (getSeparateCopy() == null) return false;
        if (getSeparateCopy().toUpperCase().equals("Y"))
            return true;
        else
            return false;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }

    public Date getSellingDate() {
        return sellingDate;
    }

    public void setSellingDate(Date sellingDate) {
        this.sellingDate = sellingDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    @XStreamAlias("invoiceLines")
    private List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public void addInvoiceLine(InvoiceLine invoiceLine) {
        this.invoiceLines.add(invoiceLine);
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean hasSeller() {
        return (getSeller() != null);
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public boolean hasBuyer() {
        return (getBuyer() != null);
    }

    private Seller seller;

    private Buyer buyer;

    private void sumAmount(Amount fromAmount, Amount toAmount) {
        toAmount.setNet(fromAmount.getNet().add(toAmount.getNet()));
        toAmount.setVat(fromAmount.getVat().add(toAmount.getVat()));
    }

    public Map<Integer, Amount> getSummaryPerVatRateMap() {
        return summaryPerVatRateMap;
    }

    private Map<Integer, Amount> summaryPerVatRateMap = new HashMap<Integer, Amount>();

    public Amount getSummaryTotal() {
        calculateTotal();
        return summaryTotal;
    }

    public BigDecimal getToPayValue() {
        return getSummaryTotal().getGross();
    }

    private Amount summaryTotal;

    private boolean summaryCalculated;

    private void calculateSummary() {
        summaryPerVatRateMap = new HashMap<Integer, Amount>();
        for (InvoiceLine line : getInvoiceLines()) {
            if (!summaryPerVatRateMap.containsKey(line.getVatRate()))
                summaryPerVatRateMap.put(line.getVatRate(), (Amount) line.createAmount().clone());
            else
                sumAmount(line.createAmount(), summaryPerVatRateMap.get(line.getVatRate()));
        }
    }

    public void calculateTotal() {
        if (!summaryCalculated) {
            calculateSummary();
            summaryTotal = null;
            for (Amount vatRateAmount : getSummaryPerVatRateMap().values()) {
                if (summaryTotal == null) summaryTotal = (Amount) vatRateAmount.clone();
                else
                    sumAmount(vatRateAmount, summaryTotal);
            }
            summaryCalculated = true;
        }
    }

}
