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
public class CurrencyInfo extends java.lang.Object {

     String CurrCode = "";
     double Rate = -1;
    /** Creates a new instance of Currency */
    public CurrencyInfo() {
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

    /** Getter for property Rate.
     * @return Value of property Rate.
     *
     */
    public double getRate() {
        return Rate;
    }

    /** Setter for property Rate.
     * @param Rate New value of property Rate.
     *
     */
    public void setRate(double Rate) {
        this.Rate = Rate;
    }

}
