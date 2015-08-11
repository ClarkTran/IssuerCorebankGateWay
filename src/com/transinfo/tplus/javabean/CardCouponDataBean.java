package com.transinfo.tplus.javabean;

import java.util.ArrayList;

public class CardCouponDataBean {
    public CardCouponDataBean() {
    }

    // Coupon data

    /** CustSeq */
    public String strCustSeq = "";
    /** CustNRIC */
    public String strCustNRIC;
    /** LoyaltyClub */
    public String strClubId = "";
    /** CouponPurse */
    public String strPurseId = "";
    /** CouponStatus */
    public ArrayList strCouponStatus = new ArrayList();
    /** CouponId */
    public ArrayList strCouponId = new ArrayList();
    /** CouponSerialNo */
    public ArrayList strSerialNo = new ArrayList();
    /** CouponUnit */
    public ArrayList strCouponUnit = new ArrayList();
    /** CouponValue */
    public ArrayList strCouponValue = new ArrayList();
    /** CouponText */
    public ArrayList strCouponText = new ArrayList();
    /** CouponType */
    public ArrayList strCouponType = new ArrayList();
    /** CouponExpiryDate */
    public ArrayList strCouponExpiryDate = new ArrayList();
    /** CouponMinSaleAmt */
    public ArrayList strCouponMinSaleAmt = new ArrayList();
    /** CouponIndicator */
    public ArrayList strCouponIndicator = new ArrayList();
    /** CouponRedeemAmt */
    // public ArrayList strCouponRedeemAmt = new ArrayList();



    public CardCouponDataBean(String couponId, String couponUnit, String couponType, String couponText, String couponValue, String couponExpiry, String serialNo){

        this.strCouponId.add(couponId);
        this.strCouponUnit.add(couponUnit);
        this.strCouponType.add(couponType);
        this.strCouponText.add(couponText);
        this.strCouponValue.add(couponValue);
        this.strCouponExpiryDate.add(couponExpiry);
        this.strSerialNo.add(serialNo);
    }
    /** Getters and Setters */

    public void setCustSeq(String custSeq) {
        this.strCustSeq = custSeq;
    }

    public String getCustSeq() {
        return this.strCustSeq;
    }

    public void setCustNRIC(String custNRIC) {
        this.strCustNRIC = custNRIC;
    }

    public String getCustNRIC() {
        return this.strCustNRIC;
    }

    public void setLoyaltyClub(String  clubId) {
        this.strClubId = clubId;
    }

    public String getLoyaltyClub() {
        return this.strClubId;
    }

    public void setCouponPurse(String purseId) {
        this.strPurseId = purseId;
    }

    public String getPurseId() {
        return this.strPurseId;
    }

    public void setCouponId(String couponId) {
        this.strCouponId.add(couponId);
    }

    public ArrayList getCouponId() {
        return this.strCouponId;
    }

    public void setCouponSerialNo(String couponSerialNo) {
        this.strSerialNo.add(couponSerialNo);
    }

    public ArrayList getCouponSerialNo() {
        return this.strSerialNo;
    }

    public void setCouponUnit(String couponUnit) {
        this.strCouponUnit.add(couponUnit);
    }

    public ArrayList getCouponUnit() {
        return this.strCouponUnit;
    }

    public void setCouponValue(String couponValue) {
        this.strCouponValue.add(couponValue);
    }

    public ArrayList getCouponValue() {
        return this.strCouponValue;
    }


    public void setCouponType(String couponType) {
        this.strCouponType.add(couponType);
    }

    public ArrayList getCouponType() {
        return this.strCouponType;
    }

    public void setCouponText(String couponText) {
        this.strCouponText.add(couponText);
    }

    public ArrayList getCouponText() {
        return this.strCouponText;
    }

    public void setCouponStatus(String couponStatus) {
        this.strCouponStatus.add(couponStatus);
    }

    public ArrayList getCouponStatus() {
        return this.strCouponStatus;
    }


    public void setCouponExpiryDate(String couponExpiryDate) {
        this.strCouponExpiryDate.add(couponExpiryDate);
    }

    public ArrayList getCouponExpiryDate() {
        return this.strCouponExpiryDate;
    }

    public void setCouponMinSaleAmt(String couponMinSaleAmt) {
        this.strCouponMinSaleAmt.add(couponMinSaleAmt);
    }

    public ArrayList getCouponMinSaleAmt() {
        return this.strCouponMinSaleAmt;
    }


    public void setCouponIndicator(String couponIndicator) {
        this.strCouponIndicator.add(couponIndicator);
    }

    public ArrayList getCouponIndicator() {
        return this.strCouponIndicator;
    }


}
