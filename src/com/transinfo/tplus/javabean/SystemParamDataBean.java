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
public class SystemParamDataBean extends java.lang.Object {

        private int InputTimeout            = 2 * 1000;
		private int SessionTimeout          = 1 * 60 * 1000 ;
    	private int SessionCleanInterval    = 10 * 60 * 1000;
		private int LoopTimeout 			= 5 * 1000;
		private int ServerPort				= 5566;
        private int VoicePort				= 1310;
		private int ThreadPoolSize 			= 5;
		private int ThreadPoolTimeout 		= 5 * 1000;
		private int RiskMaxDays 			= 5;
		private int RiskCurrConvert 		= 840;
		private int DebugLevel 				= 1;
		private String HsmName 				= "HSM_NAME";
		private int HsmPort 				= -1;
		private int HsmConnPoolSize 		= 3;
		private boolean PassThru			= false;
		private boolean ScriptEnable		= false;
        private String MailServerIP 		= "MAIL_SERVER_IP";
        private int MailServerPort 			= 25;
        private String FromMailID  			= "FROM_EMAIL_ID";
        private String ToMailID    			= "TO_EMAIL_ID";
        private String strTransactionID 	= "";
        private String strSessionSeqID		= "";

    /*    public String InputTimeout            = "2000";
	public String SessionTimeout          = "60000" ;
        public String SessionCleanInterval    = "6000000";
	public String LoopTimeout = "5000";

	public String ServerPort= "5566";
        public String VoicePort= "1310";
	public String ThreadPoolSize = "5";
	public String DebugLevel = "1";
	public String HsmName = "HSM_NAME";
	public String HsmPort = "-1";
	public String HsmConnPoolSize = "3";
	public boolean PassThru= false;
	public boolean ScriptEnable= false;    */


    /** Creates a new instance of SystemParamDataBean */
    public SystemParamDataBean() {
    }

    /** Getter for property DebugLevel.
     * @return Value of property DebugLevel.
     *
     */
    public int getDebugLevel() {
        return DebugLevel;
    }

    /** Setter for property DebugLevel.
     * @param DebugLevel New value of property DebugLevel.
     *
     */
    public void setDebugLevel(int DebugLevel) {
        this.DebugLevel = DebugLevel;
    }

    /** Getter for property HsmConnPoolSize.
     * @return Value of property HsmConnPoolSize.
     *
     */
    public int getHsmConnPoolSize() {
        return HsmConnPoolSize;
    }

    /** Setter for property HsmConnPoolSize.
     * @param HsmConnPoolSize New value of property HsmConnPoolSize.
     *
     */
    public void setHsmConnPoolSize(int HsmConnPoolSize) {
        this.HsmConnPoolSize = HsmConnPoolSize;
    }

    /** Getter for property HsmName.
     * @return Value of property HsmName.
     *
     */
    public java.lang.String getHsmName() {
        return HsmName;
    }

    /** Setter for property HsmName.
     * @param HsmName New value of property HsmName.
     *
     */
    public void setHsmName(java.lang.String HsmName) {
        this.HsmName = HsmName;
    }

    /** Getter for property HsmPort.
     * @return Value of property HsmPort.
     *
     */
    public int getHsmPort() {
        return HsmPort;
    }

    /** Setter for property HsmPort.
     * @param HsmPort New value of property HsmPort.
     *
     */
    public void setHsmPort(int HsmPort) {
        this.HsmPort = HsmPort;
    }

    /** Getter for property InputTimeout.
     * @return Value of property InputTimeout.
     *
     */
    public int getInputTimeout() {
        return InputTimeout;
    }

    /** Setter for property InputTimeout.
     * @param InputTimeout New value of property InputTimeout.
     *
     */
    public void setInputTimeout(int InputTimeout) {
        this.InputTimeout = InputTimeout;
    }

    /** Getter for property LoopTimeout.
     * @return Value of property LoopTimeout.
     *
     */
    public int getLoopTimeout() {
        return LoopTimeout;
    }

    /** Setter for property LoopTimeout.
     * @param LoopTimeout New value of property LoopTimeout.
     *
     */
    public void setLoopTimeout(int LoopTimeout) {
        this.LoopTimeout = LoopTimeout;
    }

    /** Getter for property PassThru.
     * @return Value of property PassThru.
     *
     */
    public boolean isPassThru() {
        return PassThru;
    }

    /** Setter for property PassThru.
     * @param PassThru New value of property PassThru.
     *
     */
    public void setPassThru(boolean PassThru) {
        this.PassThru = PassThru;
    }

    /** Getter for property ScriptEnable.
     * @return Value of property ScriptEnable.
     *
     */
    public boolean isScriptEnable() {
        return ScriptEnable;
    }

    /** Setter for property ScriptEnable.
     * @param ScriptEnable New value of property ScriptEnable.
     *
     */
    public void setScriptEnable(boolean ScriptEnable) {
        this.ScriptEnable = ScriptEnable;
    }

    /** Getter for property ServerPort.
     * @return Value of property ServerPort.
     *
     */
    public int getServerPort() {
        return ServerPort;
    }

    /** Setter for property ServerPort.
     * @param ServerPort New value of property ServerPort.
     *
     */
    public void setServerPort(int ServerPort) {
        this.ServerPort = ServerPort;
    }

    /** Getter for property SessionCleanInterval.
     * @return Value of property SessionCleanInterval.
     *
     */
    public int getSessionCleanInterval() {
        return SessionCleanInterval;
    }

    /** Setter for property SessionCleanInterval.
     * @param SessionCleanInterval New value of property SessionCleanInterval.
     *
     */
    public void setSessionCleanInterval(int SessionCleanInterval) {
        this.SessionCleanInterval = SessionCleanInterval;
    }

    /** Getter for property SessionTimeout.
     * @return Value of property SessionTimeout.
     *
     */
    public int getSessionTimeout() {
        return SessionTimeout;
    }

    /** Setter for property SessionTimeout.
     * @param SessionTimeout New value of property SessionTimeout.
     *
     */
    public void setSessionTimeout(int SessionTimeout) {
        this.SessionTimeout = SessionTimeout;
    }

    /** Getter for property ThreadPoolSize.
     * @return Value of property ThreadPoolSize.
     *
     */
    public int getThreadPoolSize() {
        return ThreadPoolSize;
    }

    /** Setter for property ThreadPoolSize.
     * @param ThreadPoolSize New value of property ThreadPoolSize.
     *
     */
    public void setThreadPoolSize(int ThreadPoolSize) {
        this.ThreadPoolSize = ThreadPoolSize;
    }


    /** Getter for property SessionTimeout.
     * @return Value of property SessionTimeout.
     *
     */
    public int getThreadPoolTimeout() {
        return ThreadPoolTimeout;
    }

    /** Setter for property ThreadPoolTimeout.
     * @param ThreadPoolTimeout New value of property ThreadPoolTimeout.
     *
     */
    public void setThreadPoolTimeout(int ThreadPoolTimeout) {
        this.ThreadPoolTimeout = ThreadPoolTimeout;
    }

    /** Getter for property RiskMaxDays.
     * @return Value of property RiskMaxDays.
     *
     */
    public int getRiskMaxDays() {
        return RiskMaxDays;
    }

    /** Setter for property RiskMaxDays.
     * @param ThreadPoolTimeout New value of property RiskMaxDays.
     *
     */
    public void setRiskMaxDays(int RiskMaxDays) {
        this.RiskMaxDays = RiskMaxDays;
    }

    /** Getter for property RiskCurrConvert.
     * @return Value of property RiskCurrConvert.
     *
     */
    public int getRiskCurrConvert() {
        return RiskCurrConvert;
    }

    /** Setter for property RiskMaxDays.
     * @param ThreadPoolTimeout New value of property RiskMaxDays.
     *
     */
    public void setRiskCurrConvert(int RiskCurrConvert) {
        this.RiskCurrConvert = RiskCurrConvert;
    }



    /** Getter for property VoicePort.
     * @return Value of property VoicePort.
     *
     */
    public int getVoicePort() {
        return VoicePort;
    }

    /** Setter for property VoicePort.
     * @param VoicePort New value of property VoicePort.
     *
     */
    public void setVoicePort(int VoicePort) {
        this.VoicePort = VoicePort;
    }

    /** Getter for property MailServerIP.
     * @return Value of property MailServerIP.
     *
     */
    public java.lang.String getMailServerIP() {
        return MailServerIP;
    }

    /** Setter for property MailServerIP.
     * @param MailServerIP New value of property MailServerIP.
     *
     */
    public void setMailServerIP(java.lang.String MailServerIP) {
        this.MailServerIP = MailServerIP;
    }

    /** Getter for property MailServerPort.
     * @return Value of property MailServerPort.
     *
     */
    public int getMailServerPort() {
        return MailServerPort;
    }

    /** Setter for property MailServerPort.
     * @param MailServerPort New value of property MailServerPort.
     *
     */
    public void setMailServerPort(int MailServerPort) {
        this.MailServerPort = MailServerPort;
    }

    /** Getter for property FromMailID.
     * @return Value of property FromMailID.
     *
     */
    public java.lang.String getFromMailID() {
        return FromMailID;
    }

    /** Setter for property FromMailID.
     * @param FromMailID New value of property FromMailID.
     *
     */
    public void setFromMailID(java.lang.String FromMailID) {
        this.FromMailID = FromMailID;
    }

    /** Getter for property ToMailID.
     * @return Value of property ToMailID.
     *
     */
    public java.lang.String getToMailID() {
        return ToMailID;
    }

    /** Setter for property ToMailID.
     * @param ToMailID New value of property ToMailID.
     *
     */
    public void setToMailID(java.lang.String ToMailID) {
        this.ToMailID = ToMailID;
    }


    /*
	 * Gets the Transaction ID for the Request
	 */
	public String getTransactionID()
	{
		return strTransactionID;
	}

	/*
	 * Sets the Transaction ID for the Request
	 */
	public void setTransactionID(String strTransactionID)
	{
		this.strTransactionID = strTransactionID;
	}

	/*
	* Sets the Seq for Session ID for the Request
	*/
	public void setSessionSeqID(String strSeqID)
	{
		this.strSessionSeqID = strSeqID;
	}

	/*
	* Gets the Seq for Session ID for the Request
	*/
	public String getSessionSeqID()
	{
		return strSessionSeqID;
	}
}
