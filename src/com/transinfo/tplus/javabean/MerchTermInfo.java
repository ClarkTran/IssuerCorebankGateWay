/**
* Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
* This work contains trade secrets and confidential material of
* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
* or in part without express written permission of
* Trans-Info Pte Ltd. Singapore. is prohibited.
* Date of Creation   : Feb 25, 2008
* Version Number     : 1.0
*                   Modification History:
* Date          Version No.         Modified By           Modification Details.
*/
package com.transinfo.tplus.javabean;

/**
 *
 * @author  Owner
 */
public class MerchTermInfo extends java.lang.Object {

        // Terminal
        String MerchantRefno = "";
        String TerminalRefno = "";
        String TerminalID = "";
        String MerchantID = "";
        String AcqID = "";
        String AcqInstID = "";


        String MCC = "";
        String TCC = "";
        String PEK = "";
        String KEK = "";
        String CurrCode = "";
        boolean ATM =false;
        // Merchant
        String MerchantName = "";
        String merCity = "";
        String merCntryCode="";
        String countryCode = "";
        String POSConditionCode = "";
        String POSAddtionalInfo = "";
        String Recurring = "";
        String BinName = "";

    /** Creates a new instance of MerchTermInfo */
    public MerchTermInfo() {
    }

    /** Getter for property ATM.
     * @return Value of property ATM.
     *
     */
    public boolean isATM() {
        return ATM;
    }

    /** Setter for property ATM.
     * @param ATM New value of property ATM.
     *
     */
    public void setATM(boolean ATM) {
        this.ATM = ATM;
    }

    /** Getter for property City.
     * @return Value of property City.
     *
     */
    public java.lang.String getCity() {
        return merCity;
    }

    /** Setter for property City.
     * @param City New value of property City.
     *
     */
    public void setCity(java.lang.String merCity) {
        this.merCity = merCity;
    }


    /** Getter for property BINName.
     * @return Value of property BINName.
     *
     */
    public java.lang.String getBinName() {
        return BinName;
    }

    /** Setter for property BinName.
     * @param City New value of property BinName.
     *
     */
    public void setBinName(java.lang.String BinName) {
        this.BinName = BinName;
    }

    /** Getter for property Country.
     * @return Value of property Country.
     *
     */
    public java.lang.String getCountry() {
        return merCntryCode;
    }

    /** Setter for property Country.
     * @param City New value of property Country.
     *
     */
    public void setCountry(java.lang.String merCntryCode) {
        this.merCntryCode = merCntryCode;
    }

	/** Getter for property Country.
	 * @return Value of property Country.
	 *
	 */
	public java.lang.String getAcqCountryCode() {
	        return countryCode;
	  }

	/** Setter for property City.
	 * @param City New value of property City.
	 *
	 */
    public void setAcqCountryCode(java.lang.String countryCode) {
	        this.countryCode = countryCode;
    }

	/** Getter for property AcqId.
	 * @return Value of property AcqId.
	 *
	 */
	public java.lang.String getAcqId() {
	        return AcqID;
	  }

	/** Setter for property AcqId.
	 * @param AcqId New value of property AcqId.
	 *
	 */
    public void setAcqId(java.lang.String AcqID) {
	        this.AcqID = AcqID;
    }



	/** Getter for property AcqId.
	 * @return Value of property AcqId.
	 *
	 */
	public java.lang.String getAcqInstId() {
	        return AcqInstID;
	  }

	/** Setter for property AcqId.
	 * @param AcqId New value of property AcqId.
	 *
	 */
    public void setAcqInstId(java.lang.String AcqInstID) {
	        this.AcqInstID = AcqInstID;
    }

    /** Getter for property CurrCode.
     * @return Value of property CurrCode.
     *
     */
    public java.lang.String getCurrCode() {
        return CurrCode;
    }

    /** Setter for property CurrCode.
     * @param CurrCode New value of property CurrCode.
     *
     */
    public void setCurrCode(java.lang.String CurrCode) {
        this.CurrCode = CurrCode;
    }

    /** Getter for property KEK.
     * @return Value of property KEK.
     *
     */
    public java.lang.String getKEK() {
        return KEK;
    }

    /** Setter for property KEK.
     * @param KEK New value of property KEK.
     *
     */
    public void setKEK(java.lang.String KEK) {
        this.KEK = KEK;
    }

    /** Getter for property MCC.
     * @return Value of property MCC.
     *
     */
    public java.lang.String getMCC() {
        return MCC;
    }

    /** Setter for property MCC.
     * @param MCC New value of property MCC.
     *
     */
    public void setMCC(java.lang.String MCC) {
        this.MCC = MCC;
    }


	/** Getter for property MerchantRefno.
     * @return Value of property MerchantRefno.
     *
     */
    public java.lang.String getMerchantRefno() {
        return MerchantRefno;
    }

    /** Setter for property MerchantID.
     * @param MerchantID New value of property MerchantID.
     *
     */
    public void setMerchantRefno(java.lang.String MerchantRefno) {
        this.MerchantRefno = MerchantRefno;
    }

	/** Getter for property MerchantRefno.
     * @return Value of property MerchantRefno.
     *
     */
    public java.lang.String getTerminalRefno() {
        return TerminalRefno;
    }

    /** Setter for property MerchantID.
     * @param MerchantID New value of property MerchantID.
     *
     */
    public void setTerminalRefno(java.lang.String TerminalRefno) {
        this.TerminalRefno = TerminalRefno;
    }


    /** Getter for property MerchantID.
     * @return Value of property MerchantID.
     *
     */
    public java.lang.String getMerchantID() {
        return MerchantID;
    }

    /** Setter for property MerchantID.
     * @param MerchantID New value of property MerchantID.
     *
     */
    public void setMerchantID(java.lang.String MerchantID) {
        this.MerchantID = MerchantID;
    }

    /** Getter for property MerchantName.
     * @return Value of property MerchantName.
     *
     */
    public java.lang.String getMerchantName() {
        return MerchantName;
    }

    /** Setter for property MerchantName.
     * @param MerchantName New value of property MerchantName.
     *
     */
    public void setMerchantName(java.lang.String MerchantName) {
        this.MerchantName = MerchantName;
    }

    /** Getter for property PEK.
     * @return Value of property PEK.
     *
     */
    public java.lang.String getPEK() {
        return PEK;
    }

    /** Setter for property PEK.
     * @param PEK New value of property PEK.
     *
     */
    public void setPEK(java.lang.String PEK) {
        this.PEK = PEK;
    }

    /** Getter for property POSAddtionalInfo.
     * @return Value of property POSAddtionalInfo.
     *
     */
    public java.lang.String getPOSAddtionalInfo() {
        return POSAddtionalInfo;
    }

    /** Setter for property POSAddtionalInfo.
     * @param POSAddtionalInfo New value of property POSAddtionalInfo.
     *
     */
    public void setPOSAddtionalInfo(java.lang.String POSAddtionalInfo) {
        this.POSAddtionalInfo = POSAddtionalInfo;
    }

    /** Getter for property POSConditionCode.
     * @return Value of property POSConditionCode.
     *
     */
    public java.lang.String getPOSConditionCode() {
        return POSConditionCode;
    }

    /** Setter for property POSConditionCode.
     * @param POSConditionCode New value of property POSConditionCode.
     *
     */
    public void setPOSConditionCode(java.lang.String POSConditionCode) {
        this.POSConditionCode = POSConditionCode;
    }

    /** Getter for property Recurring.
     * @return Value of property Recurring.
     *
     */
    public java.lang.String getRecurring() {
        return Recurring;
    }

    /** Setter for property Recurring.
     * @param Recurring New value of property Recurring.
     *
     */
    public void setRecurring(java.lang.String Recurring) {
        this.Recurring = Recurring;
    }

    /** Getter for property TCC.
     * @return Value of property TCC.
     *
     */
    public java.lang.String getTCC() {
        return TCC;
    }

    /** Setter for property TCC.
     * @param TCC New value of property TCC.
     *
     */
    public void setTCC(java.lang.String TCC) {
        this.TCC = TCC;
    }

    /** Getter for property TerminalID.
     * @return Value of property TerminalID.
     *
     */
    public java.lang.String getTerminalID() {
        return TerminalID;
    }

    /** Setter for property TerminalID.
     * @param TerminalID New value of property TerminalID.
     *
     */
    public void setTerminalID(java.lang.String TerminalID) {
        this.TerminalID = TerminalID;
    }

}
