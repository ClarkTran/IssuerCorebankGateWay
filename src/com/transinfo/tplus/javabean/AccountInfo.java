package com.transinfo.tplus.javabean;

public class AccountInfo {
	
	private String accountId;
	private double limitUsed;
	private double cashused;
	private double purchaseUsed;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public double getLimitUsed() {
		return limitUsed;
	}
	public void setLimitUsed(double limitUsed) {
		this.limitUsed = limitUsed;
	}
	public double getCashused() {
		return cashused;
	}
	public void setCashused(double cashused) {
		this.cashused = cashused;
	}
	public double getPurchaseUsed() {
		return purchaseUsed;
	}
	public void setPurchaseUsed(double purchaseUsed) {
		this.purchaseUsed = purchaseUsed;
	}

}
