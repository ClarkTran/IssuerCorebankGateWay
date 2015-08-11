package com.transinfo.tplus.javabean;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;

public class CardPointPurseDataBean {
    private DBManager objDBManager = null;
    private boolean recordExist = false;

    private String issuerId = "";
    private String cardNo = "";
    private String balance = "";
    private String totalRedeem = "";
    private String totalEarn = "";
    private String totalTransfer = "";
    private String expiredBalance = "";
    private String lastExpiryDate = "";
    private String lastAwardDate = "";
    private String lastRedeemDate =  "";
    private String lastTransferDate   =  "";
    private String lastUpdatedDate = "";
    private String lastUpdatedBy = "";
    private String transRecordType = "";
    private String cardClassId = "";

    public CardPointPurseDataBean() {
        objDBManager = new DBManager();
    }

    public void execute()throws Exception {
        try{
            StringBuffer query = new StringBuffer();
            query.append("SELECT ISSUER_ID, CARD_NO, BALANCE, TOTAL_REDEEM, TOTAL_EARN, ");
            query.append("TOTAL_TRANSFER, EXPIRED_BALANCE, CARD_CLASS_ID, ");
            query.append("TO_CHAR(LAST_EXPIRY_DATE,'DD/MM/YYYY') AS LAST_EXPIRY_DATE,   ");
            query.append("TO_CHAR(LAST_AWARD_DATE,'DD/MM/YYYY') AS LAST_AWARD_DATE,  ");
            query.append("TO_CHAR(LAST_REDEEM_DATE,'DD/MM/YYYY') AS LAST_REDEEM_DATE,  ");
            query.append("TO_CHAR(LAST_TFR_DATE, 'DD/MM/YYYY') AS LAST_TFR_DATE ");
            query.append("FROM CARD_POINT_PURSE WHERE ISSUER_ID='"+this.getIssuerId()+"' ");
            query.append("AND CARD_NO='"+this.getCardNo()+"' ");
            System.out.println(query.toString());
            objDBManager.executeSQL(query.toString());
            TPlusResultSet rs = objDBManager.getResultSet();

            if(rs.next()) {
                this.setIssuerId(rs.getString("ISSUER_ID"));
                this.setCardNo(rs.getString("CARD_NO"));
                this.setBalance(rs.getString("BALANCE"));
                this.setTotalRedeem(rs.getString("TOTAL_REDEEM"));
                this.setTotalEarn(rs.getString("TOTAL_EARN"));
                this.setTotalTransfer(rs.getString("TOTAL_TRANSFER"));
                this.setExpiredBalance(rs.getString("EXPIRED_BALANCE"));
                this.setLastExpiryDate(rs.getString("LAST_EXPIRY_DATE"));
                this.setLastAwardDate(rs.getString("LAST_AWARD_DATE"));
                this.setLastRedeemDate(rs.getString("LAST_REDEEM_DATE"));
                this.setLastTransferDate(rs.getString("LAST_TFR_DATE"));
                this.setCardClassId(rs.getString("CARD_CLASS_ID"));
                this.setRecordExist(true);
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting card point purse information : "+vep.toString());
            throw vep;
        }
    }

    /**
     * Getter for property recordExist.
     * @return Value of property recordExist.
     */
    public boolean isRecordExist() {
        return recordExist;
    }

    /**
     * Setter for property recordExist.
     * @param recordExist New value of property recordExist.
     */
    public void setRecordExist(boolean recordExist) {
        this.recordExist = recordExist;
    }

    /**
     * Getter for property issuerId.
     * @return Value of property issuerId.
     */
    public java.lang.String getIssuerId() {
        return issuerId;
    }

    /**
     * Setter for property issuerId.
     * @param issuerId New value of property issuerId.
     */
    public void setIssuerId(java.lang.String issuerId) {
        this.issuerId = issuerId;
    }

    /**
     * Getter for property cardNo.
     * @return Value of property cardNo.
     */
    public java.lang.String getCardNo() {
        return cardNo;
    }

    /**
     * Setter for property cardNo.
     * @param cardNo New value of property cardNo.
     */
    public void setCardNo(java.lang.String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * Getter for property balance.
     * @return Value of property balance.
     */
    public java.lang.String getBalance() {
        return balance;
    }

    /**
     * Setter for property balance.
     * @param balance New value of property balance.
     */
    public void setBalance(java.lang.String balance) {
        this.balance = balance;
    }

    /**
     * Getter for property totalRedeem.
     * @return Value of property totalRedeem.
     */
    public java.lang.String getTotalRedeem() {
        return totalRedeem;
    }

    /**
     * Setter for property totalRedeem.
     * @param totalRedeem New value of property totalRedeem.
     */
    public void setTotalRedeem(java.lang.String totalRedeem) {
        this.totalRedeem = totalRedeem;
    }

    /**
     * Getter for property totalEarn.
     * @return Value of property totalEarn.
     */
    public java.lang.String getTotalEarn() {
        return totalEarn;
    }

    /**
     * Setter for property totalEarn.
     * @param totalEarn New value of property totalEarn.
     */
    public void setTotalEarn(java.lang.String totalEarn) {
        this.totalEarn = totalEarn;
    }

    /**
     * Getter for property totalTransfer.
     * @return Value of property totalTransfer.
     */
    public java.lang.String getTotalTransfer() {
        return totalTransfer;
    }

    /**
     * Setter for property totalTransfer.
     * @param totalTransfer New value of property totalTransfer.
     */
    public void setTotalTransfer(java.lang.String totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    /**
     * Getter for property expiredBalance.
     * @return Value of property expiredBalance.
     */
    public java.lang.String getExpiredBalance() {
        return expiredBalance;
    }

    /**
     * Setter for property expiredBalance.
     * @param expiredBalance New value of property expiredBalance.
     */
    public void setExpiredBalance(java.lang.String expiredBalance) {
        this.expiredBalance = expiredBalance;
    }

    /**
     * Getter for property lastExpiryDate.
     * @return Value of property lastExpiryDate.
     */
    public java.lang.String getLastExpiryDate() {
        return lastExpiryDate;
    }

    /**
     * Setter for property lastExpiryDate.
     * @param lastExpiryDate New value of property lastExpiryDate.
     */
    public void setLastExpiryDate(java.lang.String lastExpiryDate) {
        this.lastExpiryDate = lastExpiryDate;
    }

    /**
     * Getter for property lastAwardDate.
     * @return Value of property lastAwardDate.
     */
    public java.lang.String getLastAwardDate() {
        return lastAwardDate;
    }

    /**
     * Setter for property lastAwardDate.
     * @param lastAwardDate New value of property lastAwardDate.
     */
    public void setLastAwardDate(java.lang.String lastAwardDate) {
        this.lastAwardDate = lastAwardDate;
    }

    /**
     * Getter for property lastRedeemDate.
     * @return Value of property lastRedeemDate.
     */
    public java.lang.String getLastRedeemDate() {
        return lastRedeemDate;
    }

    /**
     * Setter for property lastRedeemDate.
     * @param lastRedeemDate New value of property lastRedeemDate.
     */
    public void setLastRedeemDate(java.lang.String lastRedeemDate) {
        this.lastRedeemDate = lastRedeemDate;
    }

    /**
     * Getter for property lastTransferDate.
     * @return Value of property lastTransferDate.
     */
    public java.lang.String getLastTransferDate() {
        return lastTransferDate;
    }

    /**
     * Setter for property lastTransferDate.
     * @param lastTransferDate New value of property lastTransferDate.
     */
    public void setLastTransferDate(java.lang.String lastTransferDate) {
        this.lastTransferDate = lastTransferDate;
    }

    /**
     * Getter for property lastUpdatedDate.
     * @return Value of property lastUpdatedDate.
     */
    public java.lang.String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    /**
     * Setter for property lastUpdatedDate.
     * @param lastUpdatedDate New value of property lastUpdatedDate.
     */
    public void setLastUpdatedDate(java.lang.String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    /**
     * Getter for property lastUpdatedBy.
     * @return Value of property lastUpdatedBy.
     */
    public java.lang.String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * Setter for property lastUpdatedBy.
     * @param lastUpdatedBy New value of property lastUpdatedBy.
     */
    public void setLastUpdatedBy(java.lang.String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * Getter for property transRecordType.
     * @return Value of property transRecordType.
     */
    public java.lang.String getTransRecordType() {
        return transRecordType;
    }

    /**
     * Setter for property transRecordType.
     * @param transRecordType New value of property transRecordType.
     */
    public void setTransRecordType(java.lang.String transRecordType) {
        this.transRecordType = transRecordType;
    }


  public void setCardClassId(String cardClassId)
  {
    this.cardClassId = cardClassId;
  }


  public String getCardClassId()
  {
    return cardClassId;
  }

}
