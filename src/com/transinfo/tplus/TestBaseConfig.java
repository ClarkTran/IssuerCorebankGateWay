package com.transinfo.tplus;

import java.util.Vector;

public class TestBaseConfig {


	public static String Version             = "";
    public static boolean boolDebug           = false;
    public static String  DebugFile           = "";
    public static String  DateFormatDebug     = "yyyy-MM-dd HH:mm:ss";

    public static String  SMTPServer			= "" ;
    public static String  SMTPPort    ;
    public static String  SMTPFrom            = "";
    public static String  SMTPTo 	;
    public static String  MailSubject         = "";
    public static String  MailBody            = "";
    public static String  MailSign            = "";

    public static String  DBPoolName          = "";
    public static String  JDBCDriver          = "";
    public static String  JDBCURL             = "";
    public static String  DBUserID            = "";
    public static String  DBPassword          = "";
    public static String DBInitSize		        = "5";
    public static String DBMaxActive         	= "5";
    public static String DBMaxWait           	= "2";
    public static String  DateFormat   = "dd/MM/yy HH:mm:ss";



	private static Vector messageHandler;
	private static String ATMName;
	private static String Address;

	public TestBaseConfig(){

		messageHandler = new Vector();
	}

    	public static void setName(String name){
			ATMName = name;
		}

		public static String getName(){

			return ATMName;
		}

		public static void setAddress(String address){

			Address = address;
		}

		public static String getAddress(){

			return Address;
		}

		/*public static void addMessageHandler(MessageHandler msgHandler){

			messageHandler.add(msgHandler);
		}*/

		public static Vector getMessageHandler()
		{

			return messageHandler;
		}



	public static  boolean isBoolDebug() {
		return boolDebug;
	}
	public static  void setBoolDebug(boolean boolDebug) {
		boolDebug = boolDebug;
	}
	public static  String getDateFormat() {
		return DateFormat;
	}
	public static  void setDateFormat(String dateFormat) {
		DateFormat = dateFormat;
	}
	public static  String getDateFormatDebug() {
		return DateFormatDebug;
	}
	public static  void setDateFormatDebug(String dateFormatDebug) {
		DateFormatDebug = dateFormatDebug;
	}
	public static  String getDBInitSize() {
		return DBInitSize;
	}
	public static  void setDBInitSize(String initSize) {
		DBInitSize = initSize;
	}
	public static  String getDBMaxActive() {
		return DBMaxActive;
	}
	public static  void setDBMaxActive(String maxActive) {
		DBMaxActive = maxActive;
	}
	public static  String getDBMaxWait() {
		return DBMaxWait;
	}
	public static  void setDBMaxWait(String maxWait) {
		DBMaxWait = maxWait;
	}
	public static  String getDBPassword() {
		return DBPassword;
	}
	public static  void setDBPassword(String password) {
		DBPassword = password;
	}
	public static String getDBPoolName() {
		return DBPoolName;
	}
	public static void setDBPoolName(String poolName) {
		DBPoolName = poolName;
	}
	public static String getDBUserID() {
		return DBUserID;
	}
	public static void setDBUserID(String userID) {
		DBUserID = userID;
	}
	public static String getDebugFile() {
		return DebugFile;
	}
	public static void setDebugFile(String debugFile) {
		DebugFile = debugFile;
	}
	public static String getJDBCDriver() {
		return JDBCDriver;
	}
	public static void setJDBCDriver(String driver) {
		JDBCDriver = driver;
	}
	public static String getJDBCURL() {
		return JDBCURL;
	}
	public static void setJDBCURL(String jdbcurl) {
		JDBCURL = jdbcurl;
	}
	public static String getMailBody() {
		return MailBody;
	}
	public static void setMailBody(String mailBody) {
		MailBody = mailBody;
	}
	public static String getMailSign() {
		return MailSign;
	}
	public static void setMailSign(String mailSign) {
		MailSign = mailSign;
	}
	public static String getMailSubject() {
		return MailSubject;
	}
	public static void setMailSubject(String mailSubject) {
		MailSubject = mailSubject;
	}
	public static String getSMTPFrom() {

		return SMTPFrom;
	}
	public static void setSMTPFrom(String from) {
		SMTPFrom = from;
	}
	public static String getSMTPPort() {
		return SMTPPort;
	}
	public static void setSMTPPort(String port) {
		SMTPPort = port;
	}
	public static String getSMTPServer() {
		return SMTPServer;
	}
	public static void setSMTPServer(String server) {
		System.out.println("SMTPServer"+server);
		SMTPServer = server;
	}
	public static String getSMTPTo() {
		return SMTPTo;
	}
	public static void setSMTPTo(String to) {
		SMTPTo = to;
	}
	public static String getVersion() {
		return Version;
	}
	public static void setVersion(String version) {
		Version = version;
	}




	public String toString(){

		StringBuffer buffer = new StringBuffer("Company: " + this.ATMName);
		buffer.append("\nlocated at: " + this.Address);

		buffer.append("\nhas " + this.messageHandler.size() + " employees.");

	  buffer.append("\nEmployees are: ");
	  for(int i=0; i < this.messageHandler.size(); i++){
    	buffer.append("\n" + this.messageHandler.elementAt(i));
    }

    return buffer.toString();

	}

}
