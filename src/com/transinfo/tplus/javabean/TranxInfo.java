/*
 * TranxInfo.java
 *
 * Created on March 21, 2005, 11:18 AM
 */

package com.transinfo.tplus.javabean;

/**
 *
 * @author  Owner
 */
public class TranxInfo extends java.lang.Object {

    double Amount = 0.0;
    String ApprovalCode = "";
    String ResponseCode = "";
    String TraceNo = "";
    String TransmissionDateTime = "";
    String RefNo = "";
    String TranxCode ="";
    String OrgTraceNo="";
    String F55Res="";
    String strCardNumber = "";
	private String strTranxTime=null;
    private String strTranxDate=null;
    private String strMTI=null;
    private String strCUPCutoff="";
    double tranxCardHolderAmt = 0.0;
    double tranxCurrConvAmt = 0.0;

    private String tranxLogId="";
    private String isAuthComVoid="";
    
    private String tranxCardHolderCurr="";
    private String posEntryMode ="";

    private String preAuthStatus="";
    
    private String tranx_dateTime;
    /** Creates a new instance of TranxInfo */
    public TranxInfo() {
    }

    /** Getter for property Amount.
     * @return Value of property Amount.
     *
     */
    public double getAmount() {
        return Amount;
    }

    /** Setter for property Amount.
     * @param Amount New value of property Amount.
     *
     */
    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    /** Getter for property ApprovalCode.
     * @return Value of property ApprovalCode.
     *
     */
    public java.lang.String getApprovalCode() {
        return ApprovalCode;
    }

    /** Setter for property ApprovalCode.
     * @param ApprovalCode New value of property ApprovalCode.
     *
     */
    public void setApprovalCode(java.lang.String ApprovalCode) {
        this.ApprovalCode = ApprovalCode;
    }


/** Getter for property ApprovalCode.
     * @return Value of property ApprovalCode.
     *
     */
    public java.lang.String getCardNumber() {
        return strCardNumber;
    }

    /** Setter for property ApprovalCode.
     * @param ApprovalCode New value of property ApprovalCode.
     *
     */
    public void setCardNumber(java.lang.String strCardNumber) {
        this.strCardNumber = strCardNumber;
    }


    /** Getter for property RefNo.
     * @return Value of property RefNo.
     *
     */
    public java.lang.String getRefNo() {
        return RefNo;
    }

    /** Setter for property RefNo.
     * @param RefNo New value of property RefNo.
     *
     */
    public void setRefNo(java.lang.String RefNo) {
        this.RefNo = RefNo;
    }

    /** Getter for property ResponseCode.
     * @return Value of property ResponseCode.
     *
     */
    public java.lang.String getResponseCode() {
        return ResponseCode;
    }

    /** Setter for property ResponseCode.
     * @param ResponseCode New value of property ResponseCode.
     *
     */
    public void setResponseCode(java.lang.String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    /** Getter for property TraceNo.
     * @return Value of property TraceNo.
     *
     */
    public java.lang.String getTraceNo() {
        return TraceNo;
    }

    /** Setter for property TraceNo.
     * @param TraceNo New value of property TraceNo.
     *
     */
    public void setTraceNo(java.lang.String TraceNo) {
        this.TraceNo = TraceNo;
    }

	/** Getter for property OrgTraceNo.
	 * @return Value of property OrgTraceNo.
	 *
	 */
	public java.lang.String getOrgTraceNo() {
		return OrgTraceNo;
	}

	/** Setter for property OrgTraceNo.
	 * @param TraceNo New value of property OrgTraceNo.
	 *
	 */
	public void setOrgTraceNo(java.lang.String OrgTraceNo) {
		this.OrgTraceNo = OrgTraceNo;
    }


    /** Getter for property TransmissionDateTime.
     * @return Value of property TransmissionDateTime.
     *
     */
    public java.lang.String getTransmissionDateTime() {
        return TransmissionDateTime;
    }

    /** Setter for property TransmissionDateTime.
     * @param TransmissionDateTime New value of property TransmissionDateTime.
     *
     */
    public void setTransmissionDateTime(java.lang.String TransmissionDateTime) {
        this.TransmissionDateTime = TransmissionDateTime;
    }


    /** Getter for property strTranxCode.
     * @return Value of property strTranxCode.
     *
     */
    public java.lang.String getTranxCode() {
        return TranxCode;
    }

    /** Setter for property strTranxCode.
     * @param strTranxCode New value of property strTranxCode.
     *
     */
    public void setTranxCode(java.lang.String TranxCode) {
        this.TranxCode = TranxCode;
    }

    /** Getter for property F55Res.
     * @return Value of property F55Res.
     *
     */
    public java.lang.String getF55Res() {
        return F55Res;
    }

    /** Setter for property F55Res.
     * @param F55Res New value of property F55Res.
     *
     */
    public void setF55Res(java.lang.String F55Res) {
        this.F55Res = F55Res;
    }

	public String getTranxTime()
	{
		return strTranxTime;
	}

	public void setTranxTime(String strTranxTime)
	{
		this.strTranxTime = strTranxTime;
	}


	public String getTranxDate()
	{
		return strTranxDate;
	}

	public void setTranxDate(String strTranxDate)
	{
		this.strTranxDate = strTranxDate;
	}

	public String getCupCutoff()
	{
		return strCUPCutoff;
	}

	public void setCupCutoff(String strCUPCutoff)
	{
		this.strCUPCutoff = strCUPCutoff;
	}

    /** Getter for property strTranxCode.
     * @return Value of property strTranxCode.
     *
     */
    public java.lang.String getMTI() {
        return strMTI;
    }

    /** Setter for property strTranxCode.
     * @param strTranxCode New value of property strTranxCode.
     *
     */
    public void setMTI(java.lang.String strMTI) {
        this.strMTI = strMTI;
    }

	public double getTranxCardHolderAmt() {
		return tranxCardHolderAmt;
	}

	public void setTranxCardHolderAmt(double tranxCardHolderAmt) {
		this.tranxCardHolderAmt = tranxCardHolderAmt;
	}

	public String getTranxLogId() {
		return tranxLogId;
	}

	public void setTranxLogId(String tranxLogId) {
		this.tranxLogId = tranxLogId;
	}

	public String getIsAuthComVoid() {
		return isAuthComVoid;
	}

	public void setIsAuthComVoid(String isAuthComVoid) {
		this.isAuthComVoid = isAuthComVoid;
	}

	public double getTranxCurrConvAmt() {
		return tranxCurrConvAmt;
	}

	public void setTranxCurrConvAmt(double tranxCurrConvAmt) {
		this.tranxCurrConvAmt = tranxCurrConvAmt;
	}

	public String getTranxCardHolderCurr() {
		return tranxCardHolderCurr;
	}

	public void setTranxCardHolderCurr(String tranxCardHolderCurr) {
		this.tranxCardHolderCurr = tranxCardHolderCurr;
	}

	public String getPosEntryMode() {
		return posEntryMode;
	}

	public void setPosEntryMode(String posEntryMode) {
		this.posEntryMode = posEntryMode;
	}

	public String getPreAuthStatus() {
		return preAuthStatus;
	}

	public void setPreAuthStatus(String preAuthStatus) {
		this.preAuthStatus = preAuthStatus;
	}

	public String getTranx_dateTime() {
		return tranx_dateTime;
	}

	public void setTranx_dateTime(String tranx_dateTime) {
		this.tranx_dateTime = tranx_dateTime;
	}



}
