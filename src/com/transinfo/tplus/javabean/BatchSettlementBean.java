package com.transinfo.tplus.javabean;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;


public class BatchSettlementBean {

    public String merchantNo = "";
    public String terminalNo = "";
    public String totalTransactionCount = "0";
    public String totalSaleAmount = "0";
    public String totalPointRedeemed = "0";
    public String totalPointEarned = "0";
    public String totalReloadAmount = "0";
    public String totalRebateAmount = "0";
    public String totalCouponTxnCount = "0";

    /**
     * Getter for property totalTransactionCount.
     * @return Value of property totalTransactionCount.
     */
    public java.lang.String getTotalTransactionCount() {
        return totalTransactionCount;
    }

    /**
     * Setter for property totalTransactionCount.
     * @param totalTransactionCount New value of property totalTransactionCount.
     */
    public void setTotalTransactionCount(java.lang.String totalTransactionCount) {
        this.totalTransactionCount = totalTransactionCount;
    }

    /**
     * Getter for property totalSaleAmount.
     * @return Value of property totalSaleAmount.
     */
    public java.lang.String getTotalSaleAmount() {
        return totalSaleAmount;
    }

    /**
     * Setter for property totalSaleAmount.
     * @param totalSaleAmount New value of property totalSaleAmount.
     */
    public void setTotalSaleAmount(java.lang.String totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    /**
     * Getter for property totalPointRedeemed.
     * @return Value of property totalPointRedeemed.
     */
    public java.lang.String getTotalPointRedeemed() {
        return totalPointRedeemed;
    }

    /**
     * Setter for property totalPointRedeemed.
     * @param totalPointRedeemed New value of property totalPointRedeemed.
     */
    public void setTotalPointRedeemed(java.lang.String totalPointRedeemed) {
        this.totalPointRedeemed = totalPointRedeemed;
    }

    /**
     * Getter for property totalPointEarned.
     * @return Value of property totalPointEarned.
     */
    public java.lang.String getTotalPointEarned() {
        return totalPointEarned;
    }

    /**
     * Setter for property totalPointEarned.
     * @param totalPointEarned New value of property totalPointEarned.
     */
    public void setTotalPointEarned(java.lang.String totalPointEarned) {
        this.totalPointEarned = totalPointEarned;
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

    /**
     * Getter for property totalRebateAmount.
     * @return Value of property totalRebateAmount.
     */
    public java.lang.String getTotalRebateAmount() {
        return totalRebateAmount;
    }

    /**
     * Setter for property totalRebateAmount.
     * @param totalRebateAmount New value of property totalRebateAmount.
     */
    public void setTotalRebateAmount(java.lang.String totalRebateAmount) {
        this.totalRebateAmount = totalRebateAmount;
    }

    /**
     * Getter for property totalCouponTxnCount.
     * @return Value of property totalCouponTxnCount.
     */
    public java.lang.String getTotalCouponTxnCount() {
        return totalCouponTxnCount;
    }

    /**
     * Setter for property totalCouponTxnCount.
     * @param totalCouponTxnCount New value of property totalCouponTxnCount.
     */
    public void setTotalCouponTxnCount(java.lang.String totalCouponTxnCount) {
        this.totalCouponTxnCount = totalCouponTxnCount;
    }

    /**
     * Getter for property merchantNo.
     * @return Value of property merchantNo.
     */
    public java.lang.String getMerchantNo() {
        return merchantNo;
    }

    /**
     * Setter for property merchantNo.
     * @param merchantNo New value of property merchantNo.
     */
    public void setMerchantNo(java.lang.String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * Getter for property terminalNo.
     * @return Value of property terminalNo.
     */
    public java.lang.String getTerminalNo() {
        return terminalNo;
    }

    /**
     * Setter for property terminalNo.
     * @param terminalNo New value of property terminalNo.
     */
    public void setTerminalNo(java.lang.String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public DBManager objDBManager = null;

    public BatchSettlementBean() {
        objDBManager = new DBManager();
    }

    /** Checking Card Details */
    public void execute() throws Exception {
        System.out.println("Validating the card record  & getting the values.");
        try{
            StringBuffer strSql = new StringBuffer();
            strSql.append("SELECT BATCH_NO, MERCHANT_NO, TERMINAL_NO, TOTAL_TXN_COUNT, TOTAL_SALE_AMOUNT, ");
            strSql.append("TOTAL_POINT_EARN, TOTAL_REDEEM_POINTS, TOTAL_CASH_RELOAD, TOTAL_REBATE_EARN, TOTAL_COUPON_TXN ");
            strSql.append("FROM BATCH_SETTLEMENT ");
            strSql.append("WHERE MERCHANT_NO = '"+this.getMerchantNo()+"' ");
            strSql.append("AND TERMINAL_NO = '"+this.getTerminalNo()+"' ");
            strSql.append("AND BATCH_NO = '000000' ");
            System.out.println(strSql.toString());//HIEP
            objDBManager.executeSQL(strSql.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()){
                this.setTotalCouponTxnCount(rs.getString("TOTAL_COUPON_TXN"));
                this.setTotalPointEarned(rs.getString("TOTAL_POINT_EARN"));
                this.setTotalPointRedeemed(rs.getString("TOTAL_REDEEM_POINTS"));
                this.setTotalRebateAmount(rs.getString("TOTAL_REBATE_EARN"));
                this.setTotalReloadAmount(rs.getString("TOTAL_CASH_RELOAD"));
                this.setTotalSaleAmount(rs.getString("TOTAL_SALE_AMOUNT"));
                this.setTotalTransactionCount(rs.getString("TOTAL_TXN_COUNT"));
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting card information : "+vep.toString());
            throw vep;
        }
    }

    public void print(){
        System.out.println(">>>> Values From Database : Start");
        System.out.println("Transaction Count    : "+this.getTotalTransactionCount());
        System.out.println("Sale Amount          : "+this.getTotalSaleAmount());
        System.out.println("Points Earned        : "+this.getTotalPointEarned());
        System.out.println("Points Redeemed      : "+this.getTotalPointRedeemed());
        System.out.println("Reload Amount        : "+this.getTotalReloadAmount());
        System.out.println("Rebate Amount        : "+this.getTotalRebateAmount());
        System.out.println("Coupon Txn Count     : "+this.getTotalCouponTxnCount());
        System.out.println(">>>> Values From Database : End");
    }
}
