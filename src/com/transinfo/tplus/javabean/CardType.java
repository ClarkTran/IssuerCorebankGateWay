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
public class CardType extends java.lang.Object {

        String CardTypeID = "";
        String BIN = "";
        String AcqCountryCode = "";
        String PEK = "";
        String AcqInstitutionID = "";
        boolean StandIn = false;
        boolean LUHNCheck = false;
        boolean ExpireCheck = false;

    /** Creates a new instance of CardType */
    public CardType() {
    }

    /** Getter for property AcqCountryCode.
     * @return Value of property AcqCountryCode.
     *
     */
    public java.lang.String getAcqCountryCode() {
        return AcqCountryCode;
    }

    /** Setter for property AcqCountryCode.
     * @param AcqCountryCode New value of property AcqCountryCode.
     *
     */
    public void setAcqCountryCode(java.lang.String AcqCountryCode) {
        this.AcqCountryCode = AcqCountryCode;
    }

    /** Getter for property AcqInstitutionID.
     * @return Value of property AcqInstitutionID.
     *
     */
    public java.lang.String getAcqInstitutionID() {
        return AcqInstitutionID;
    }

    /** Setter for property AcqInstitutionID.
     * @param AcqInstitutionID New value of property AcqInstitutionID.
     *
     */
    public void setAcqInstitutionID(java.lang.String AcqInstitutionID) {
        this.AcqInstitutionID = AcqInstitutionID;
    }

    /** Getter for property BIN.
     * @return Value of property BIN.
     *
     */
    public java.lang.String getBIN() {
        return BIN;
    }

    /** Setter for property BIN.
     * @param BIN New value of property BIN.
     *
     */
    public void setBIN(java.lang.String BIN) {
        this.BIN = BIN;
    }

    /** Getter for property CardTypeID.
     * @return Value of property CardTypeID.
     *
     */
    public java.lang.String getCardTypeID() {
        return CardTypeID;
    }

    /** Setter for property CardTypeID.
     * @param CardTypeID New value of property CardTypeID.
     *
     */
    public void setCardTypeID(java.lang.String CardTypeID) {
        this.CardTypeID = CardTypeID;
    }

    /** Getter for property ExpireCheck.
     * @return Value of property ExpireCheck.
     *
     */
    public boolean isExpireCheck() {
        return ExpireCheck;
    }

    /** Setter for property ExpireCheck.
     * @param ExpireCheck New value of property ExpireCheck.
     *
     */
    public void setExpireCheck(boolean ExpireCheck) {
        this.ExpireCheck = ExpireCheck;
    }

    /** Getter for property LUHNCheck.
     * @return Value of property LUHNCheck.
     *
     */
    public boolean isLUHNCheck() {
        return LUHNCheck;
    }

    /** Setter for property LUHNCheck.
     * @param LUHNCheck New value of property LUHNCheck.
     *
     */
    public void setLUHNCheck(boolean LUHNCheck) {
        this.LUHNCheck = LUHNCheck;
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

    /** Getter for property StandIn.
     * @return Value of property StandIn.
     *
     */
    public boolean isStandIn() {
        return StandIn;
    }

    /** Setter for property StandIn.
     * @param StandIn New value of property StandIn.
     *
     */
    public void setStandIn(boolean StandIn) {
        this.StandIn = StandIn;
    }

}
