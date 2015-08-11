package com.transinfo.tplus.javabean;

import java.util.ArrayList;

public class Bit63DataBean {
    public Bit63DataBean() {
    }
    // Bit63 Data
    /** Transaction Amount */
    public String strTransactionAmt = "";
    /** PointAward */
    public String strPointAward = "";
    /** Point_Balance_Sign */
    public String strPoint_Balance_Sign = "";
    /** PointBalance */
    public String strPointBalance = "";
    /** ReminderMessage */
    public String strReminderMessage = "";
    /** SpecialDateMessage */
    public String strSpecialDateMessage = "";
    /** PointExpiryDate */
    public String strPointExpiryDate = "";
    /** RedeemPoints */
    public String strRedeemPoints = "";
    /** PointsRedeem */
    public String strPointsRedeem = "";
    /** PointsEarned */
    public String strPointsEarned = "";
    /** PointVoidedSign */
    public String strPointVoidedSign =  "";
    /** PointsVoided */
    public String strPointsVoided = "";
    /** CustSeq */
    public String strCustSeq = "";
    /** Cust_nric */
    public String strCust_nric = "";
    /** LoyaltyClub */
    public String strLoyaltyClub = "";
    /** CouponPurse */
    public String strCouponPurse = "";
    /** TotalSaleCount */
    public String strTotalSaleCount = "";
    /** TotalOriginalSaleAmount */
    public String strTotalOriginalSaleAmount = "";
    /** Total_Cash$_Redeemed */
    public String strTotal_Cash$_Redeemed = "";
    /** Total_Cash$_Earned */
    public String strTotal_Cash$_Earned = "";
    /** Total_Gift_Coupon_Redemption_Count */
    public String strTotal_Gift_Coupon_Redemption_Count = "";
    /** Total_Cash_Coupon_Redemption_Count */
    public String strTotal_Cash_Coupon_Redemption_Count = "";
    /** Total_Cash_Coupon_Value_Redeemed */
    public String strTotal_Cash_Coupon_Value_Redeemed = "";

    public ArrayList couponData = null;
    public CouponDataBean redemptionCouponData = null;
    public BatchSettlementBean settlementData = null;

    public String oldAmountBalance = "";
    public String oldAmountBalanceSign = "";
    public String amountSpend = "";
    public String amountReload = "";
    public String amountBalance = "";
    public String amountBalanceSign = "";
    public String amountVoid = "";
    public String amountVoidSign = "";

    public String totalSpendCount = "";
    public String totalSpendAmount = "";
    public String totalReloadCount = "";
    public String totalReloadAmount = "";


    public void setTransactionAmt(String transactionAmt) {
        this.strTransactionAmt = transactionAmt;
    }

    public String getTransactionAmt() {
        return this.strTransactionAmt;
    }

    public void setPointAward(String pointAward) {
        this.strPointAward = pointAward;
    }

    public String getPointAward() {
        return this.strPointAward;
    }

    public void setPointBalanceSign(String pointBalanceSign) {
        this.strPoint_Balance_Sign = pointBalanceSign;
    }

    public String getPointBalanceSign() {
        return this.strPoint_Balance_Sign;
    }

    public void setPointBalance(String pointBalance) {
        this.strPointBalance = pointBalance;
    }

    public String getPointBalance() {
        return this.strPointBalance;
    }


    public void setReminderMessage(String reminderMessage) {
        this.strReminderMessage = reminderMessage;
    }
    public String getReminderMessage() {
        return this.strReminderMessage;
    }

    public void setSpecialDateMessage(String specialDateMessage) {
        this.strSpecialDateMessage = specialDateMessage;
    }
    public String getSpecialDateMessage() {
        return this.strSpecialDateMessage;
    }

    public void setPointExpiryDate(String pointExpiryDate) {
        this.strPointExpiryDate = pointExpiryDate;
    }
    public String getPointExpiryDate() {
        return this.strPointExpiryDate;
    }

    public void setRedeemPoints(String redeemPoints) {
        this.strRedeemPoints = redeemPoints;
    }
    public String getRedeemPoints() {
        return this.strRedeemPoints;
    }

    public void setPointsRedeem(String pointsRedeem) {
        this.strPointsRedeem = pointsRedeem;
    }
    public String getPointsRedeem() {
        return this.strPointsRedeem;
    }

    public void setPointsEarned(String pointsEarned) {
        this.strPointsEarned = pointsEarned;
    }
    public String getPointsEarned() {
        return this.strPointsEarned;
    }

    public void setPointVoidedSign(String pointVoidedSign) {
        this.strPointVoidedSign = pointVoidedSign;
    }
    public String getPointVoidedSign() {
        return this.strPointVoidedSign;
    }

    public void setPointsVoided(String pointsVoided) {
        this.strPointsVoided = pointsVoided;
    }
    public String getPointsVoided() {
        return this.strPointsVoided;
    }

    public void setCustSeq(String custSeq) {
        this.strCustSeq = custSeq;
    }
    public String getCustSeq() {
        return this.strCustSeq;
    }

    public void setCust_Nric(String cust_Nric) {
        this.strCust_nric = cust_Nric;
    }
    public String getCust_Nric() {
        return this.strCust_nric;
    }

    public void setLoyaltyClub(String loyaltyClub) {
        this.strLoyaltyClub = loyaltyClub;
    }
    public String getLoyaltyClub() {
        return this.strLoyaltyClub;
    }

    public void setTotalSaleCount(String totalSaleCount) {
        this.strTotalSaleCount = totalSaleCount;
    }
    public String getTotalSaleCount() {
        return this.strTotalSaleCount;
    }

    public void setTotalOriginalSaleAmt(String totalOriginalSaleAmt) {
        this.strTotalOriginalSaleAmount = totalOriginalSaleAmt;
    }
    public String getTotalOriginalSaleAmt() {
        return this.strTotalOriginalSaleAmount;
    }


    public void setTotal_Cash$_Redeemed(String total_Cash$_Redeemed) {
        this.strTotal_Cash$_Redeemed = total_Cash$_Redeemed;
    }
    public String getTotal_Cash$_Redeemed() {
        return this.strTotal_Cash$_Redeemed;
    }

    public void setTotal_Cash$_Earned(String total_Cash$_Earned) {
        this.strTotal_Cash$_Earned = total_Cash$_Earned;
    }
    public String getTotal_Cash$_Earned() {
        return this.strTotal_Cash$_Earned;
    }

    public void setTotalGiftCouponRedCount(String totalGiftCouponRedCount) {
        this.strTotal_Gift_Coupon_Redemption_Count = totalGiftCouponRedCount;
    }
    public String getTotalGiftCouponRedCount() {
        return this.strTotal_Gift_Coupon_Redemption_Count;
    }

    public void setTotalCashCouponRedCount(String totalCashCouponRedCount) {
        this.strTotal_Cash_Coupon_Redemption_Count = totalCashCouponRedCount;
    }
    public String getTotalCashCouponRedCount() {
        return this.strTotal_Cash_Coupon_Redemption_Count;
    }


    public void setTotalCashCouponValueRedeemed(String totalCashCouponValueRedeemed) {
        this.strTotal_Cash_Coupon_Value_Redeemed = totalCashCouponValueRedeemed;
    }
    public String getTotalCashCouponValueRedeemed() {
        return this.strTotal_Cash_Coupon_Value_Redeemed;
    }


    /**
     * Getter for property couponData.
     * @return Value of property couponData.
     */
    public java.util.ArrayList getCouponData() {
        return couponData;
    }

    /**
     * Setter for property couponData.
     * @param couponData New value of property couponData.
     */
    public void setCouponData(java.util.ArrayList couponData) {
        this.couponData = couponData;
    }

    /**
     * Getter for property redemptionCouponData.
     * @return Value of property redemptionCouponData.
     */
    public CouponDataBean getRedemptionCouponData() {
        return redemptionCouponData;
    }

    /**
     * Setter for property redemptionCouponData.
     * @param redemptionCouponData New value of property redemptionCouponData.
     */
    public void setRedemptionCouponData(CouponDataBean redemptionCouponData) {
        this.redemptionCouponData = redemptionCouponData;
    }

    /**
     * Getter for property settlementData.
     * @return Value of property settlementData.
     */
    public BatchSettlementBean getSettlementData() {
        return settlementData;
    }

    /**
     * Setter for property settlementData.
     * @param settlementData New value of property settlementData.
     */
    public void setSettlementData(BatchSettlementBean settlementData) {
        this.settlementData = settlementData;
    }

    /**
     * Getter for property amountSpend.
     * @return Value of property amountSpend.
     */
    public java.lang.String getAmountSpend() {
        return amountSpend;
    }

    /**
     * Setter for property amountSpend.
     * @param amountSpend New value of property amountSpend.
     */
    public void setAmountSpend(java.lang.String amountSpend) {
        this.amountSpend = amountSpend;
    }

    /**
     * Getter for property amountReload.
     * @return Value of property amountReload.
     */
    public java.lang.String getAmountReload() {
        return amountReload;
    }

    /**
     * Setter for property amountReload.
     * @param amountReload New value of property amountReload.
     */
    public void setAmountReload(java.lang.String amountReload) {
        this.amountReload = amountReload;
    }

    /**
     * Getter for property amountBalance.
     * @return Value of property amountBalance.
     */
    public java.lang.String getAmountBalance() {
        return amountBalance;
    }

    /**
     * Setter for property amountBalance.
     * @param amountBalance New value of property amountBalance.
     */
    public void setAmountBalance(java.lang.String amountBalance) {
        this.amountBalance = amountBalance;
    }

    /**
     * Getter for property amountBalanceSign.
     * @return Value of property amountBalanceSign.
     */
    public java.lang.String getAmountBalanceSign() {
        return amountBalanceSign;
    }

    /**
     * Setter for property amountBalanceSign.
     * @param amountBalanceSign New value of property amountBalanceSign.
     */
    public void setAmountBalanceSign(java.lang.String amountBalanceSign) {
        this.amountBalanceSign = amountBalanceSign;
    }

    /**
     * Getter for property amountVoid.
     * @return Value of property amountVoid.
     */
    public java.lang.String getAmountVoid() {
        return amountVoid;
    }

    /**
     * Setter for property amountVoid.
     * @param amountVoid New value of property amountVoid.
     */
    public void setAmountVoid(java.lang.String amountVoid) {
        this.amountVoid = amountVoid;
    }

    /**
     * Getter for property amountVoidSign.
     * @return Value of property amountVoidSign.
     */
    public java.lang.String getAmountVoidSign() {
        return amountVoidSign;
    }

    /**
     * Setter for property amountVoidSign.
     * @param amountVoidSign New value of property amountVoidSign.
     */
    public void setAmountVoidSign(java.lang.String amountVoidSign) {
        this.amountVoidSign = amountVoidSign;
    }

    /**
     * Getter for property oldAmountBalance.
     * @return Value of property oldAmountBalance.
     */
    public java.lang.String getOldAmountBalance() {
        return oldAmountBalance;
    }

    /**
     * Setter for property oldAmountBalance.
     * @param oldAmountBalance New value of property oldAmountBalance.
     */
    public void setOldAmountBalance(java.lang.String oldAmountBalance) {
        this.oldAmountBalance = oldAmountBalance;
    }

    /**
     * Getter for property oldAmountBalanceSign.
     * @return Value of property oldAmountBalanceSign.
     */
    public java.lang.String getOldAmountBalanceSign() {
        return oldAmountBalanceSign;
    }

    /**
     * Setter for property oldAmountBalanceSign.
     * @param oldAmountBalanceSign New value of property oldAmountBalanceSign.
     */
    public void setOldAmountBalanceSign(java.lang.String oldAmountBalanceSign) {
        this.oldAmountBalanceSign = oldAmountBalanceSign;
    }

    /**
     * Getter for property totalSpendCount.
     * @return Value of property totalSpendCount.
     */
    public java.lang.String getTotalSpendCount() {
        return totalSpendCount;
    }

    /**
     * Setter for property totalSpendCount.
     * @param totalSpendCount New value of property totalSpendCount.
     */
    public void setTotalSpendCount(java.lang.String totalSpendCount) {
        this.totalSpendCount = totalSpendCount;
    }

    /**
     * Getter for property totalSpendAmount.
     * @return Value of property totalSpendAmount.
     */
    public java.lang.String getTotalSpendAmount() {
        return totalSpendAmount;
    }

    /**
     * Setter for property totalSpendAmount.
     * @param totalSpendAmount New value of property totalSpendAmount.
     */
    public void setTotalSpendAmount(java.lang.String totalSpendAmount) {
        this.totalSpendAmount = totalSpendAmount;
    }

    /**
     * Getter for property totalReloadCount.
     * @return Value of property totalReloadCount.
     */
    public java.lang.String getTotalReloadCount() {
        return totalReloadCount;
    }

    /**
     * Setter for property totalReloadCount.
     * @param totalReloadCount New value of property totalReloadCount.
     */
    public void setTotalReloadCount(java.lang.String totalReloadCount) {
        this.totalReloadCount = totalReloadCount;
    }

    /**
     * Getter for property totalReloadAmount.
     * @return Value of property totalReloadAmount.
     */
    public java.lang.String getTotalReloadAmount() {
        return totalReloadAmount;
    }

    /**
     * Setter for property totalReloadAmount.
     * @param totalReloadAmount New value of property totalReloadAmount.
     */
    public void setTotalReloadAmount(java.lang.String totalReloadAmount) {
        this.totalReloadAmount = totalReloadAmount;
    }

}
