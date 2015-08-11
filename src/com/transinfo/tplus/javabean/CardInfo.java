/*
 * SystemParamDataBean.java
 *
 * Created on March 22, 2005, 1:46 PM
 */

package com.transinfo.tplus.javabean;

/**
 *
 * @author  Owner
 */
public class CardInfo extends java.lang.Object {



	private String cardnumber;
	private String issuerId;
	private String customerId;
	private String accountId;
	private String cardholderType;
	private String expDate;
	private String effectiveDate;
	private String cardstatusId;
	private String CVKI;
	private String PVKI;
	private String CVV;
	private String CVV2;
	private String PVV;
	private String OPVV;
	private String PVVOFFSET;
	private String wrongPINCount;
	private String pinDisabled;
	private String NIP;
	private String visaCode;
	private String billingDate;
	private String status;
	private String lastUpdatedDate;
	private String lastUpdatedBy;
	private String CVKA;
	private String CVKB;
	private String PVKA;
	private String PVKB;
	private String customerTypeId;
	private String cardProductId;
	private double creditPrecent;
	private double cashPrecent;
	private String currencyCode;
	private String savingAcct;
	private String checkingAcct;
	private String pinReset;

	private String oc;
	private String eComEnable;

	private double currConvFee;

	private String pinBlockDate;
	private String cardScheme;

	/** Creates a new instance of NewMain */
	public CardInfo() {
	}



	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getCheckingAcct() {
		return checkingAcct;
	}

	public void setCheckingAcct(String checkingAcct) {
		this.checkingAcct = checkingAcct;
	}

	public String getSavingAcct() {
		return savingAcct;
	}

	public void setSavingAcct(String savingAcct) {
		this.savingAcct = savingAcct;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getIssuerId() {
		return issuerId;
	}

	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCardholderType() {
		return cardholderType;
	}

	public void setCardholderType(String cardholderType) {
		this.cardholderType = cardholderType;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getCardstatusId() {
		return cardstatusId;
	}

	public void setCardstatusId(String cardstatusId) {
		this.cardstatusId = cardstatusId;
	}

	public String getCVKI() {
		return CVKI;
	}

	public void setCVKI(String CVKI) {
		this.CVKI = CVKI;
	}

	public String getPVKI() {
		return PVKI;
	}

	public void setPVKI(String PVKI) {
		this.PVKI = PVKI;
	}

	public String getCVV() {
		return CVV;
	}

	public void setCVV(String CVV) {
		this.CVV = CVV;
	}

	public String getCVV2() {
		return CVV2;
	}

	public void setCVV2(String CVV2) {
		this.CVV2 = CVV2;
	}

	public String getPVV() {
		return PVV;
	}

	public void setPVV(String PVV) {
		this.PVV = PVV;
	}

	public String getOPVV() {
		return OPVV;
	}

	public void setOPVV(String OPVV) {
		this.OPVV = OPVV;
	}

	public String getPVVOFFSET() {
		return PVVOFFSET;
	}

	public void setPVVOFFSET(String PVVOFFSET) {
		this.PVVOFFSET = PVVOFFSET;
	}

	public String getWrongPINCount() {
		return wrongPINCount;
	}

	public void setWrongPINCount(String wrongPINCount) {
		this.wrongPINCount = wrongPINCount;
	}

	public String isPinBlocked() {
		return pinDisabled;
	}

	public void setPinDisabled(String pinDisabled) {
		this.pinDisabled = pinDisabled;
	}

	public String getPinReset() {
		return pinReset;
	}

	public void setPinReset(String pinReset) {
		this.pinReset = pinReset;
	}

	public String getNIP() {
		return NIP;
	}

	public void setNIP(String NIP) {
		this.NIP = NIP;
	}

	public String getVisaCode() {
		return visaCode;
	}

	public void setVisaCode(String visaCode) {
		this.visaCode = visaCode;
	}

	public String getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getCVKA() {
		return CVKA;
	}

	public void setCVKA(String CVKA) {
		this.CVKA = CVKA;
	}

	public String getCVKB() {
		return CVKB;
	}

	public void setCVKB(String CVKB) {
		this.CVKB = CVKB;
	}

	public String getPVKA() {
		return PVKA;
	}

	public void setPVKA(String PVKA) {
		this.PVKA = PVKA;
	}

	public String getPVKB() {
		return PVKB;
	}

	public void setPVKB(String PVKB) {
		this.PVKB = PVKB;
	}

	public String getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(String customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public String getCardProductId() {
		return cardProductId;
	}

	public void setCardProductId(String cardProductId) {
		this.cardProductId = cardProductId;
	}


	public double getCreditPrecent() {
		return creditPrecent;
	}

	public void setCreditPrecent(double creditPrecent) {
		this.creditPrecent = creditPrecent;
	}


	public double getCashPrecent() {
		return cashPrecent;
	}

	public void setCashPrecent(double cashPrecent) {
		this.cashPrecent = cashPrecent;
	}



	public String getOc() {
		return oc;
	}



	public void setOc(String oc) {
		this.oc = oc;
	}



	public String geteComEnable() {
		return eComEnable;
	}



	public void seteComEnable(String eComEnable) {
		this.eComEnable = eComEnable;
	}



	public double getCurrConvFee() {
		return currConvFee;
	}



	public void setCurrConvFee(double currConvFee) {
		this.currConvFee = currConvFee;
	}



	public String getPinBlockDate() {
		return pinBlockDate;
	}



	public void setPinBlockDate(String pinBlockDate) {
		this.pinBlockDate = pinBlockDate;
	}



	public String getCardScheme() {
		return cardScheme;
	}



	public void setCardScheme(String cardScheme) {
		this.cardScheme = cardScheme;
	}



}