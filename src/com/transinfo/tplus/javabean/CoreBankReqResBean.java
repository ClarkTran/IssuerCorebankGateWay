package com.transinfo.tplus.javabean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CoreBankReqResBean implements Serializable {
	
	private String sourceId;
	private String atmpos;
	private String acqId;
	private String currCode;
	private String acctNo;
	private String traceNo;
	private double amt;
	private String sale;
	private String dateTime;
	private String appCode;
	private String resCode;
	private String balance;
	private String toAcctNo;
	private int tranxFee;
	
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getAtmpos() {
		return atmpos;
	}
	public void setAtmpos(String atmpos) {
		this.atmpos = atmpos;
	}
	public String getAcqId() {
		return acqId;
	}
	public void setAcqId(String acqId) {
		this.acqId = acqId;
	}
	public String getCurrCode() {
		return currCode;
	}
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public String getTraceNo() {
		return traceNo;
	}
	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public String getSale() {
		return sale;
	}
	public void setSale(String sale) {
		this.sale = sale;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getToAcctNo() {
		return toAcctNo;
	}
	public void setToAcctNo(String toAcctNo) {
		this.toAcctNo = toAcctNo;
	}
	public int getTranxFee() {
		return tranxFee;
	}
	public void setTranxFee(int tranxFee) {
		this.tranxFee = tranxFee;
	}
	
}
