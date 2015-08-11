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
public class LicenseInfo extends java.lang.Object{

    String SerialNum = "";
    String LicenceKey = "";

    /** Creates a new instance of LicenseInfo */
    public LicenseInfo() {
    }

    /** Getter for property SerialNum.
     * @return Value of property SerialNum.
     *
     */
    public java.lang.String getSerialNum() {
        return SerialNum;
    }

    /** Setter for property SerialNum.
     * @param SerialNum New value of property SerialNum.
     *
     */
    public void setSerialNum(java.lang.String SerialNum) {
        this.SerialNum = SerialNum;
    }

    /** Getter for property LicenceKey.
     * @return Value of property LicenceKey.
     *
     */
    public java.lang.String getLicenceKey() {
        return LicenceKey;
    }

    /** Setter for property LicenceKey.
     * @param LicenceKey New value of property LicenceKey.
     *
     */
    public void setLicenceKey(java.lang.String LicenceKey) {
        this.LicenceKey = LicenceKey;
    }

}
