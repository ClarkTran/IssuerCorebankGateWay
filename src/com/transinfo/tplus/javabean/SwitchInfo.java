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

import com.transinfo.tplus.switching.IConnectionHandler;

/**
 *
 * @author  Owner
 */
public class SwitchInfo extends java.lang.Object{


    String SwitchName = "";
    String CardType = "";
    long CardLow = 0;
    long CardHigh = 0;
    String Host = "";
    String DestCurrencyISO = "";
    String IPAddress = "";
    int Port = -1;
    int timeout =-1;
    boolean state = false;
    IConnectionHandler objConnHandler = null;

    /** Creates a new instance of SwitchItem */
    public SwitchInfo() {
    }


    /** Getter for property CardHigh.
     * @return Value of property CardHigh.
     *
     */
    public long getCardHigh() {
        return CardHigh;
    }

    /** Setter for property CardHigh.
     * @param CardHigh New value of property CardHigh.
     *
     */
    public void setCardHigh(long CardHigh) {
        this.CardHigh = CardHigh;
    }

    /** Getter for property CardLow.
     * @return Value of property CardLow.
     *
     */
    public long getCardLow() {
        return CardLow;
    }

    /** Setter for property CardLow.
     * @param CardLow New value of property CardLow.
     *
     */
    public void setCardLow(long CardLow) {
        this.CardLow = CardLow;
    }

    /** Getter for property SwitchName.
     * @return Value of property SwitchName.
     *
     */
    public java.lang.String getSwitchName() {
        return SwitchName;
    }

    /** Setter for property SwitchName.
     * @param SwitchName New value of property SwitchName.
     *
     */
    public void setSwitchName(java.lang.String SwitchName) {
        this.SwitchName = SwitchName;
    }

    /** Getter for property CardType.
     * @return Value of property CardType.
     *
     */
    public java.lang.String getCardType() {
        return CardType;
    }

    /** Setter for property CardType.
     * @param CardType New value of property CardType.
     *
     */
    public void setCardType(java.lang.String CardType) {
        this.CardType = CardType;
    }

    /** Getter for property DestCurrencyISO.
     * @return Value of property DestCurrencyISO.
     *
     */
    public java.lang.String getDestCurrencyISO() {
        return DestCurrencyISO;
    }

    /** Setter for property DestCurrencyISO.
     * @param DestCurrencyISO New value of property DestCurrencyISO.
     *
     */
    public void setDestCurrencyISO(java.lang.String DestCurrencyISO) {
        this.DestCurrencyISO = DestCurrencyISO;
    }

    /** Getter for property Host.
     * @return Value of property Host.
     *
     */
    public java.lang.String getHost() {
        return Host;
    }

    /** Setter for property Host.
     * @param Host New value of property Host.
     *
     */
    public void setHost(java.lang.String Host) {
        this.Host = Host;
    }

    /** Getter for property IPAddress.
     * @return Value of property IPAddress.
     *
     */
    public java.lang.String getIPAddress() {
        return IPAddress;
    }

    /** Setter for property IPAddress.
     * @param IPAddress New value of property IPAddress.
     *
     */
    public void setIPAddress(java.lang.String IPAddress) {
        this.IPAddress = IPAddress;
    }

    /** Getter for property Port.
     * @return Value of property Port.
     *
     */
    public int getPort() {
        return Port;
    }

    /** Setter for property Port.
     * @param Port New value of property Port.
     *
     */
    public void setPort(int Port) {
        this.Port = Port;
    }

    /** Getter for property timeout.
     * @return Value of property timeout.
     *
     */
    public int getTimeout() {
        return timeout;
    }

    /** Setter for property timeout.
     * @param timeout New value of property timeout.
     *
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /** Getter for property timeout.
     * @return Value of property timeout.
     *
     */
    public IConnectionHandler getConnectionHandler() {
        return objConnHandler;
    }

    /** Setter for property timeout.
     * @param timeout New value of property timeout.
     *
     */
    public void setConnectionHandler(IConnectionHandler objConnHandler) {
        this.objConnHandler = objConnHandler;
    }


	/** Setter for property state.
     * @param state New value of property state.
     *
     */

    public void setState(boolean state)
    {
		 this.state = state;
	}

    /** Getter for property state.
	 * @return Value of property state.
	 *
     */

    public boolean isState() {
        return state;
    }



}

