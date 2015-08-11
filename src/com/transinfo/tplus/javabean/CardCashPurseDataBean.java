package com.transinfo.tplus.javabean;

import com.transinfo.tplus.util.DBManager;
import com.transinfo.tplus.util.TPlusResultSet;


public class CardCashPurseDataBean {
    private DBManager objDBManager = null;
    private boolean recordExist = false;

    private String issuerId = "";
    private String cardNo = "";
    private String balance = "";
    private String totalSpend = "";
    private String totalReload = "";
    private String totalTransfer = "";
    private String totalAuth = "";
    private String expiredBalance = "";
    private String lastExpiryDate = "";
    private String lastSpendDate = "";
    private String lastTransferDate = "";
    private String lastReloadDate = "";
    private String cardValue = "";
    private String lastUpdatedDate = "";
    private String lastUpdatedBy = "";
    private String leftSaleAmount = "";
    private String totalPreTopupAmount = "";
    private String totalGetPreTopupAmount = "";
    private String totalLeftPreTopupAmount = "";

    private String previousCashBalance = "";
    private String accountId = "";

    public CardCashPurseDataBean() {
        objDBManager = new DBManager();
    }

    public void execute()throws Exception {
        try{
            StringBuffer query = new StringBuffer();
            query.append("select ca.cardnumber, cu.total_previous_bal, cu.account_id, ");
            query.append("cu.previous_cash_balance, (cu.credit_limit-cu.limit_used) as balance ");
            query.append("from cards ca, customer_account cu ");
            query.append("where ca.account_id = cu.account_id ");
           // query.append("and ca.customer_id = cu.customer_id ");
            query.append("and ca.cardnumber='"+this.getCardNo()+"' ");
            System.out.println(query.toString());//HIEP
            objDBManager.executeSQL(query.toString());
            TPlusResultSet rs = objDBManager.getResultSet();
            if(rs.next()) {
                this.setCardNo(rs.getString("cardnumber"));
                this.setBalance(rs.getString("balance"));
                this.setPreviousCashBalance(rs.getString("previous_cash_balance"));
                this.setAccountId(rs.getString("account_id"));
                this.setRecordExist(true);
            }
        }catch(Exception vep) {
            System.out.println("Exception while getting card cash purse information : "+vep.toString());
            throw vep;
        }
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
     * Getter for property totalSpend.
     * @return Value of property totalSpend.
     */
    public java.lang.String getTotalSpend() {
        return totalSpend;
    }

    /**
     * Setter for property totalSpend.
     * @param totalSpend New value of property totalSpend.
     */
    public void setTotalSpend(java.lang.String totalSpend) {
        this.totalSpend = totalSpend;
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
     * Getter for property totalAuth.
     * @return Value of property totalTransfer.
     */
    public java.lang.String getTotalAuth() {
        return totalAuth;
    }

    /**
     * Setter for property totalTransfer.
     * @param totalTransfer New value of property totalTransfer.
     */
    public void setTotalAuth(java.lang.String totalAuth) {
        this.totalAuth = totalAuth;
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
     * Getter for property lastSpendDate.
     * @return Value of property lastSpendDate.
     */
    public java.lang.String getLastSpendDate() {
        return lastSpendDate;
    }

    /**
     * Setter for property lastSpendDate.
     * @param lastSpendDate New value of property lastSpendDate.
     */
    public void setLastSpendDate(java.lang.String lastSpendDate) {
        this.lastSpendDate = lastSpendDate;
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
     * Getter for property lastReloadDate.
     * @return Value of property lastReloadDate.
     */
    public java.lang.String getLastReloadDate() {
        return lastReloadDate;
    }

    /**
     * Setter for property lastReloadDate.
     * @param lastReloadDate New value of property lastReloadDate.
     */
    public void setLastReloadDate(java.lang.String lastReloadDate) {
        this.lastReloadDate = lastReloadDate;
    }

    /**
     * Getter for property cardValue.
     * @return Value of property cardValue.
     */
    public java.lang.String getCardValue() {
        return cardValue;
    }

    /**
     * Setter for property cardValue.
     * @param cardValue New value of property cardValue.
     */
    public void setCardValue(java.lang.String cardValue) {
        this.cardValue = cardValue;
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
     * Getter for property totalReload.
     * @return Value of property totalReload.
     */
    public java.lang.String getTotalReload() {
        return totalReload;
    }

    /**
     * Setter for property totalReload.
     * @param totalReload New value of property totalReload.
     */
    public void setTotalReload(java.lang.String totalReload) {
        this.totalReload = totalReload;
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


  public void setLeftSaleAmount(String leftSaleAmount)
  {
    this.leftSaleAmount = leftSaleAmount;
  }


  public String getLeftSaleAmount()
  {
    return leftSaleAmount;
  }


  public void setTotalPreTopupAmount(String totalPreTopupAmount)
  {
    this.totalPreTopupAmount = totalPreTopupAmount;
  }


  public String getTotalPreTopupAmount()
  {
    return totalPreTopupAmount;
  }


  public void setTotalGetPreTopupAmount(String totalGetPreTopupAmount)
  {
    this.totalGetPreTopupAmount = totalGetPreTopupAmount;
  }


  public String getTotalGetPreTopupAmount()
  {
    return totalGetPreTopupAmount;
  }


  public void setTotalLeftPreTopupAmount(String totalLeftPreTopupAmount)
  {
    this.totalLeftPreTopupAmount = totalLeftPreTopupAmount;
  }


  public String getTotalLeftPreTopupAmount()
  {
    return totalLeftPreTopupAmount;
  }

public String getPreviousCashBalance() {
	return previousCashBalance;
}

public void setPreviousCashBalance(String previousCashBalance) {
	this.previousCashBalance = previousCashBalance;
}

public String getAccountId() {
	return accountId;
}

public void setAccountId(String accountId) {
	this.accountId = accountId;
}

}
