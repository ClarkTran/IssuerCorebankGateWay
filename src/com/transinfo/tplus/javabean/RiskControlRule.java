/*
 * RiskControlRule.java
 *
 * Created on March 21, 2005, 11:29 AM
 */

package com.transinfo.tplus.javabean;

/**
 *
 * @author  Owner
 */
public class RiskControlRule extends java.lang.Object {

        String Type = "";
        int LastRecentDays = -1;
        String Scope = "";
        String MCC = "";
        String MerchantRefno = "";
        int Count = -1;
        double Amount = -1;
        String CurrCode = "";
        String Response = "";



    /** Creates a new instance of RiskControlRule */
    public RiskControlRule() {
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

    /** Getter for property Count.
     * @return Value of property Count.
     *
     */
    public int getCount() {
        return Count;
    }

    /** Setter for property Count.
     * @param Count New value of property Count.
     *
     */
    public void setCount(int Count) {
        this.Count = Count;
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

    /** Getter for property LastRecentDays.
     * @return Value of property LastRecentDays.
     *
     */
    public int getLastRecentDays() {
        return LastRecentDays;
    }

    /** Setter for property LastRecentDays.
     * @param LastRecentDays New value of property LastRecentDays.
     *
     */
    public void setLastRecentDays(int LastRecentDays) {
        this.LastRecentDays = LastRecentDays;
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

    /** Setter for property MerchantRefno.
     * @param MerchantRefno New value of property MerchantRefno.
     *
     */
    public void setMerchantRefno(java.lang.String MerchantRefno) {
        this.MerchantRefno = MerchantRefno;
    }

    /** Getter for property Response.
     * @return Value of property Response.
     *
     */
    public java.lang.String getResponse() {
        return Response;
    }

    /** Setter for property Response.
     * @param Response New value of property Response.
     *
     */
    public void setResponse(java.lang.String Response) {
        this.Response = Response;
    }

    /** Getter for property Scope.
     * @return Value of property Scope.
     *
     */
    public java.lang.String getScope() {
        return Scope;
    }

    /** Setter for property Scope.
     * @param Scope New value of property Scope.
     *
     */
    public void setScope(java.lang.String Scope) {
        this.Scope = Scope;
    }

    /** Getter for property Type.
     * @return Value of property Type.
     *
     */
    public java.lang.String getType() {
        return Type;
    }

    /** Setter for property Type.
     * @param Type New value of property Type.
     *
     */
    public void setType(java.lang.String Type) {
        this.Type = Type;
    }

}
