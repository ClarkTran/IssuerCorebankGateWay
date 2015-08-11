package com.transinfo.tplus.javabean;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class AccountChangeLogBean implements Serializable {
	
	private String sno;
	private Long cardNumber;
	private Double adjAmt=0.0;
	private String amtSign;
	private Date effectiveDate;
	private String reason;
	private String amtSrc;
	private String changeType;
	private String updatedBy;
	private Date updatedDate;
	private Double preLimitUsed=0.0;
	private Double limitUsed=0.0;
	private Double prePurchasedUsed=0.0;
	private Double preCashUsed=0.0;
	private Double purchaseUsed=0.0;
	private Double cashUsed=0.0;
	private String accountId;
	
	public String getSno() {
		return sno;
	}
	public void setSno(String sno) {
		this.sno = sno;
	}
	public Long getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Double getAdjAmt() {
		return adjAmt;
	}
	public void setAdjAmt(Double adjAmt) {
		this.adjAmt = adjAmt;
	}
	public String getAmtSign() {
		return amtSign;
	}
	public void setAmtSign(String amtSign) {
		this.amtSign = amtSign;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAmtSrc() {
		return amtSrc;
	}
	public void setAmtSrc(String amtSrc) {
		this.amtSrc = amtSrc;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Double getPreLimitUsed() {
		return preLimitUsed;
	}
	public void setPreLimitUsed(Double preLimitUsed) {
		this.preLimitUsed = preLimitUsed;
	}
	public Double getLimitUsed() {
		return limitUsed;
	}
	public void setLimitUsed(Double limitUsed) {
		this.limitUsed = limitUsed;
	}
	public Double getPrePurchasedUsed() {
		return prePurchasedUsed;
	}
	public void setPrePurchasedUsed(Double prePurchasedUsed) {
		this.prePurchasedUsed = prePurchasedUsed;
	}
	public Double getPreCashUsed() {
		return preCashUsed;
	}
	public void setPreCashUsed(Double preCashUsed) {
		this.preCashUsed = preCashUsed;
	}
	public Double getPurchaseUsed() {
		return purchaseUsed;
	}
	public void setPurchaseUsed(Double purchaseUsed) {
		this.purchaseUsed = purchaseUsed;
	}
	public Double getCashUsed() {
		return cashUsed;
	}
	public void setCashUsed(Double cashUsed) {
		this.cashUsed = cashUsed;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getInsert() throws Exception {
		
		String res = "";
		
		try{
			
			StringBuffer strSql = new StringBuffer();
			strSql.append("INSERT INTO ACCOUNT_CHG_LOG ( ");
			strSql.append("SNO,CARDNUMBER,ADJ_AMT,AMOUNT_SIGN,EFFECTIVE_DATE,REASON,AMT_SRC, ");
			strSql.append("CHG_TYPE,UPDATED_BY,UPDATED_DATE,PREV_LIMIT_USED,LIMIT_USED, ");
			strSql.append("PREV_PURCHASE_USED,PREV_CASH_USED,PURCHASE_USED,CASH_USED,ACCOUNT_ID ");
	        strSql.append(") VALUES ( ");
	        strSql.append("'" + this.sno + "', ");
	        strSql.append("" + this.cardNumber + ", ");
	        strSql.append("" + this.adjAmt + ", ");
	        strSql.append("'" + this.amtSign + "', ");
	        strSql.append("SYSDATE, ");
	        strSql.append("'" + this.reason + "', ");
	        strSql.append("'" + this.amtSrc + "', ");
	        strSql.append("'" + this.changeType + "', ");
	        strSql.append("'ISS ENGINE', ");
	        strSql.append("SYSDATE, ");
	        strSql.append("" + this.preLimitUsed + ", ");
	        strSql.append("" + this.limitUsed + ", ");
	        strSql.append("" + this.prePurchasedUsed + ", ");
	        strSql.append("" + this.preCashUsed + ", ");
	        strSql.append("" + this.purchaseUsed + ", ");
	        strSql.append("" + this.cashUsed + ", ");
	        strSql.append("'" + this.accountId + "' ");
	        strSql.append(") ");
			
			res = strSql.toString();
			
		}catch (Exception e) {
			throw e;
		}
		
		return res;
	}
	
}
