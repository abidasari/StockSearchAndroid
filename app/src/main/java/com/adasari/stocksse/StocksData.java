package com.adasari.stocksse;

/**
 * Created by adasari on 5/3/16.
 */
public class StocksData {
    private String NAME;
    private String SYMBOL;
    private String LASTPRICE;
    private String CHANGE;
    private String TIMESTAMP;
    private String MARKETCAP;
    private String VOLUME;
    private String CHANGEYTD;
    private String HIGH;
    private String LOW;
    private String OPEN;

    public StocksData(){

    }

    public String getNAME() {
        return NAME;
    }

    public String getSYMBOL() {
        return SYMBOL;
    }

    public String getLASTPRICE() {
        return LASTPRICE;
    }

    public String getCHANGE() {
        return CHANGE;
    }

    public String getTIMESTAMP() {
        return TIMESTAMP;
    }

    public String getMARKETCAP() {
        return MARKETCAP;
    }

    public String getVOLUME() {
        return VOLUME;
    }

    public String getCHANGEYTD() {
        return CHANGEYTD;
    }

    public String getHIGH() {
        return HIGH;
    }

    public String getLOW() {
        return LOW;
    }

    public String getOPEN() {
        return OPEN;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setSYMBOL(String SYMBOL) {
        this.SYMBOL = SYMBOL;
    }

    public void setLASTPRICE(String LASTPRICE) {
        this.LASTPRICE = LASTPRICE;
    }

    public void setCHANGE(String CHANGE) {
        this.CHANGE = CHANGE;
    }

    public void setTIMESTAMP(String TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public void setMARKETCAP(String MARKETCAP) {
        this.MARKETCAP = MARKETCAP;
    }

    public void setVOLUME(String VOLUME) {
        this.VOLUME = VOLUME;
    }

    public void setCHANGEYTD(String CHANGEYTD) {
        this.CHANGEYTD = CHANGEYTD;
    }

    public void setHIGH(String HIGH) {
        this.HIGH = HIGH;
    }

    public void setLOW(String LOW) {
        this.LOW = LOW;
    }

    public void setOPEN(String OPEN) {
        this.OPEN = OPEN;
    }

}
