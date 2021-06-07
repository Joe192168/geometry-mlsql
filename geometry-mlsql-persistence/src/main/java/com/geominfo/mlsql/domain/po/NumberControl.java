package com.geominfo.mlsql.domain.po;


import java.math.BigDecimal;

/**
 * @program: geometry-bi
 * @description: 序列号体类
 * @author: 肖乔辉
 * @create: 2018-11-02 09:53
 * @version: 3.0.0
 */
public class NumberControl {

    private static final long serialVersionUID = 1L;

    private BigDecimal id;
    private String itemcode;
    private String itemname;
    private int numlength;
    private String numrule;
    private int autofill;
    private String prefixion;
    private int maximum;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getNumlength() {
        return numlength;
    }

    public void setNumlength(int numlength) {
        this.numlength = numlength;
    }

    public String getNumrule() {
        return numrule;
    }

    public void setNumrule(String numrule) {
        this.numrule = numrule;
    }

    public int getAutofill() {
        return autofill;
    }

    public void setAutofill(int autofill) {
        this.autofill = autofill;
    }

    public String getPrefixion() {
        return prefixion;
    }

    public void setPrefixion(String prefixion) {
        this.prefixion = prefixion;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public String toString() {
        return "NumberControl{" +
                "id=" + id +
                ", itemcode=" + itemcode +
                ", itemname=" + itemname +
                ", numlength=" + numlength +
                ", numrule=" + numrule +
                ", autofill=" + autofill +
                ", prefixion=" + prefixion +
                ", maximum=" + maximum +
                "}";
    }
}

