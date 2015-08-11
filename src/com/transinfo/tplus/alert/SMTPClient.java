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

package com.transinfo.tplus.alert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.transinfo.tplus.debug.DebugWriter;
/**
 * This inner class opens an input stream and read the data from the input stream.
 */

class SMTPInputStream
{
    private InputStream is=null;

    /**
     * Constructor to open the input stream from the socket.
     * @param s Socket which the stream need to opened.
     * @throws IOException
     */

    SMTPInputStream(Socket s) throws IOException
    {

        is = s.getInputStream();
    }

    /**
     * This method reads the data from the stream and returns the string.
     * @return String.
     * @throws IOException
     */

    String ReadFromStream()  throws IOException
    {
        String s=new String("");
        while ((is.available()>0) && (s.length()<255))
        s=s+String.valueOf((char) is.read());
        return s;
    }

    /**
     * This method close the stream.
     * @throws IOException
     */

    void Close() throws IOException
    {
        is.close();
        is=null;
    }

}

/**
 * This class opens an output stream and writes the data to the output stream.
 */

class SMTPOutputStream
{

    private OutputStream os=null;

    /**
     * Constructor to open an output stream from the socket.
     * @param s Socket which the stream need to opened.
     * @throws IOException
     */

    SMTPOutputStream(Socket s) throws IOException
    {
        os=s.getOutputStream();
    }

    /**
     * This method will write the input stream to the string.
     * @param s String which need to be written to the output stream.
     * @throws IOException
     */

    void WriteToStream(String s) throws IOException
    {

        if (s.length()>0)
        {
            for (int i=0;(i<s.length());i++)
            os.write((int) s.charAt(i));
        }
    }

    /**
     * This method close the stream.
     * @throws IOException
     */

    void Close() throws IOException
    {
        os.close();
        os=null;
    }

}



/**
 * This class will send the an email using the SMTP protocol.
 * To use this class to send mails outside the mailserver domain the MTA should give
 * Relay access permission to the server which this program runs.
 */

public class SMTPClient extends Thread
{
    String smtpServer;			 		// The IP address ot URI of the mail server.
    String smtpPort		= "25";	 		// The port at which mail server listens.  Default 25.
    String senderMail;  		 		// The mail id @ whcih mail is send.
    String reciverMail;			 		// The mail id to which mail should ne send.
    String ccMail;				 		// The List of CC'ed mail id.
    String bccMail;				 		// The List of bcc'ed mail id.
    String subjectMail;			 		// The Email Subject.
    String bodyMail;			 		// The Email body
    String[] strArrCCRecipients; 		// Tist of the CC'ed recipients
    boolean showSenderEmailId = true;	// boolean to show the set the sender email id.
    ArrayList toAddressList=null;
    boolean manyToAddress	  = false;  // boolean to show the set the sender email id.


    /**
     * RFC 821 Standarts
     * SMTP Command
     */

    final String[] SMTPCommand =
    {

        "HELO",
        "MAIL FROM:",
        "RCPT TO:",
        "DATA",
        "QUIT",
        "NOOP",
        "TURN",
        "RSET",
        "VRFY",
        "EXPN",
        "SEND FROM:",
        "SOML FROM:",
        "SAML FROM:",
        "\r\n.\r\n",
        "\r\n",
        " "
    };



    //Command codes here
    final int SMTP_CMD_HELO=0;
    final int SMTP_CMD_MAIL_FROM=1;
    final int SMTP_CMD_RCPT_TO=2;
    final int SMTP_CMD_DATA=3;
    final int SMTP_CMD_QUIT=4;
    final int SMTP_CMD_NOOP=5;
    final int SMTP_CMD_TURN=6;
    final int SMTP_CMD_RSET=7;
    final int SMTP_CMD_VRFY=8;
    final int SMTP_CMD_EXPN=9;
    final int SMTP_CMD_SEND_FROM=10;
    final int SMTP_CMD_SOML_FROM=11;
    final int SMTP_CMD_SAML_FROM=12;
    final int SMTP_CMD_CRLF_CRLF=13;
    final int SMTP_CMD_CRLF=14;
    final int SMTP_CMD_SPC=15;




    //	REPLY CODES
    //	500 Syntax error, command unrecognized
    //	[This may include errors such as command line too long]
    final int SMTP_RCODE_UNREC_COMMAND=500;

    //  501 Syntax error in parameters or arguments
    final int SMTP_RCODE_PARAM_ERROR=501;

    //	502 Command not implemented
    final int SMTP_RCODE_IMPLEMENT_COMMAND=502;

    //	503 Bad sequence of commands
    final int SMTP_RCODE_SEQUENCE_COMMAND=503;

    //	504 Command parameter not implemented
    final int SMTP_RCODE_PARAM_COMMAND=504;

    //	211 System status, or system help reply
    final int SMTP_RCODE_SYSTEM_REPLY=211;

    //  214 Help message
    //  [Information on how to use the receiver or the meaning of a
    //  particular non-standard command; this reply is useful only
    //  to the human user]
    final int SMTP_RCODE_HELP=214;

    // 220 <domain> Service ready
    final int SMTP_RCODE_READY=220;

    //  221 <domain> Service closing transmission channel
    final int SMTP_RCODE_CLOSING=221;

    //  421 <domain> Service not available, closing transmission channel
    //  [This may be a reply to any command if
    //  the service knows it must shut down]
    final int SMTP_RCODE_UNAVAILABLE=421;

    //  250 Requested mail action okay, completed
    final int SMTP_RCODE_COMPLETED=250;

    //  251 User not local; will forward to <forward-path>
    final int SMTP_RCODE_FORWARD=251;

    //  450 Requested mail action not taken: mailbox unavailable
    //  [E.g., mailbox busy]
    final int SMTP_RCODE_MAIL_ACTION_NOT_TAKEN=450;

    //  550 Requested action not taken: mailbox unavailable
    //  [E.g., mailbox not found, no access]
    final int SMTP_RCODE_ACTION_NOT_TAKEN=550;

    //  451 Requested action aborted: error in processing
    final int SMTP_RCODE_ACTION_ABORTED=451;

    //  551 User not local; please try <forward-path>
    final int SMTP_RCODE_USER_NOT_LOCAL=551;

    //  452 Requested action not taken: insufficient system storage
    final int SMTP_RCODE_SYSTEM_STORAGE=452;

    //  552 Requested mail action aborted: exceeded storage allocation
    final int SMTP_RCODE_MAIL_STORAGE=552;

    //  553 Requested action not taken: mailbox name not allowed
    //  [E.g., mailbox syntax incorrect]
    final int SMTP_RCODE_NAME_NOT_ALLOWED=553;

    //  354 Start mail input; end with <CRLF>.<CRLF>
    final int SMTP_RCODE_MAIL_START=354;

    //  554 Transaction failed
    final int SMTP_RCODE_TRANS_FAILED=554;


    //timeout 10 seconds
    final int WAIT_TIMEOUT=(10*1000);

    private boolean keepGoing;
    private Vector outgoingMessages;



    //read data from input stream to globalBuffer String
    //return true if ok
    //false if timeout

    String globalBuffer=new String("");

    /**
     * This consturctor will send the mail to a single recipient.
     * @param toUser 				The email address of the user which mail should be send.
     * @param smtpServerText		The IP address of the mail server.
     * @param subjectMailMessage	The subject of the email
     * @param bodyMailMessage		The body of the email.
     * @param strFromAddress		The from address the mail server.
     * This uses the default port 25.
     */

    public SMTPClient(String smtpServerText,String toUser,String subjectMailMessage,String bodyMailMessage,String strFromAddress)
    {

		super("SMTPClient$WriterThread");
		keepGoing = true;
		outgoingMessages = new Vector();

        smtpServer	= new String(smtpServerText);
        smtpPort	= new String(smtpPort);
        senderMail	= new String(strFromAddress);
        reciverMail	= new String(toUser);
        ccMail		= new String("");
        bccMail		= new String("");
        subjectMail	= new String(subjectMailMessage);
        bodyMail	= new String(bodyMailMessage);
    }

    /**
     * This consturctor will send the mail to a single recipient.
     * @param smtpServerText		The IP address of the mail server.
     * @pram  smtpPortText			The Port which SMTP server listens.
     * @param toUser 				The email address of the user which mail should be send.
     * @param subjectMailMessage	The subject of the email
     * @param bodyMailMessage		The body of the email.
     * @param strFromAddress		The from address the mail server.
     */

    public SMTPClient(String smtpServerText,String smtpPortText,String toUser,String subjectMailMessage,String bodyMailMessage,String strFromAddress)
    {
		super("SMTPClient$WriterThread");
		keepGoing = true;
		outgoingMessages = new Vector();

        smtpServer	= new String(smtpServerText);
        smtpPort	= new String(smtpPort);
        senderMail	= new String(strFromAddress);
        reciverMail	= new String(toUser);
        ccMail		= new String("");
        bccMail		= new String("");
        subjectMail	= new String(subjectMailMessage);
        bodyMail	= new String(bodyMailMessage);
        smtpPort	= new String(smtpPortText);

	}

    /**
     * This method sets the list of users to which the mail should be copied.
     * @param strCCUsers The list of users the mail should be copied.
     */
    public void setCopyRecivers(String[] strCCUsers)
    {
        strArrCCRecipients = strCCUsers;
    }

    /**
     * This method sets the mail properties such a way that the mail won't display sender mail - id.
     *
     */
    public void setHideSender()
    {
        showSenderEmailId = false;
    }

    /**
     * This method sets the mail properties such a way that the mail show's display sender mail - id.
     *
     */
    public void setShowSender()
    {
        showSenderEmailId = true;
    }

    /**
     * This method sets the list of users to which the mail should be send.
     * @param toAddresses The list of users the mail should be send.
     * 					  If this metod is set it will reset the initial email id set by the constructer.
     * @throws Exception
     */

    public void setMultipleSenders(ArrayList toAddresses)
    {
		for(int i =0;i<toAddresses.size();i++)
		{
        	reciverMail 	+= (String)toAddresses.get(0);
        	if(i != toAddresses.size())
        	reciverMail += reciverMail+",";
	 	}
    }


    /**
     * This method will open the socket stream and send the mail.
     * @retrun int 0 The it could send the mail with out any error.
     * If it is non zero it couln't send the mail and it will return SMTP error code.
     * returns -1 on any other fatal errors.
     */

    public void sendMail()
    {

		try
		{
		  Properties props = System.getProperties();

		  // -- Attaching to default Session, or we could start a new one --
		  // -- Could use Session.getTransport() and Transport.connect()
		  // , but assume we're using SMTP --

		System.out.println("SMTPSERVER="+smtpServer);

		  props.put("mail.smtp.host", smtpServer);
		  Session session = Session.getDefaultInstance(props, null);

		  // -- Create a new message --
		  Message msg = new MimeMessage(session);

		  // -- Set the FROM and TO fields --
		  System.out.println("SMTPSERVER="+reciverMail);
		  msg.setFrom(new InternetAddress(senderMail));
		  msg.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse(reciverMail, false));

		  // -- We could include CC recipients too --
		   if (ccMail != null && !ccMail.equals(""))
		   msg.setRecipients(Message.RecipientType.CC
		   ,InternetAddress.parse(ccMail, false));

		  // -- Set the subject and body text --
		  msg.setSubject(subjectMail);
		  msg.setText(bodyMail);

		  // -- Set some other header information --
		  msg.setHeader("X-Mailer", "TransInfoEmail");
		  msg.setSentDate(new Date());

		  // -- Send the message --
		  Transport.send(msg);

		  System.out.println("Message sent OK.");
		}
		catch (Exception ex)
		{
		  ex.printStackTrace();
		}


    }

	 public synchronized void run()
    {
        try
        {
			System.out.println("SMTPClient Started ");
            while(keepGoing)
            {
                flushOutputQueue();
                wait();
            }


            if (DebugWriter.boolDebugEnabled) DebugWriter.write("Stopping gracefully.");
        }
        catch(InterruptedException e)
        {
            System.err.println("ConnectionHandlerLocal$WriterThread.run(): Interrupted!");
        }
    }

    void flushOutputQueue()
    {

        for(; outgoingMessages.size() > 0;)
        {
			System.out.println("1");
			ArrayList objMsgArr = (ArrayList)outgoingMessages.elementAt(0);
			System.out.println("2");
			bodyMail	= (String)objMsgArr.get(0);
			System.out.println("3");
			System.out.println((ArrayList)objMsgArr.get(1));
			System.out.println(((ArrayList)objMsgArr.get(1)).toString());
			ArrayList mailTo = (ArrayList)objMsgArr.get(1);
			reciverMail="";
			for(int i=0;i<mailTo.size();i++)
			{
				reciverMail =reciverMail+(String)mailTo.get(i);
				if(i<mailTo.size()-1)
				reciverMail=reciverMail+",";
		 	}
			System.out.println("4"+reciverMail);
			sendMail();
			System.out.println("5");
			outgoingMessages.removeElementAt(0);

        }

    }

    synchronized void queueMessage(ArrayList objArr)
    {
		outgoingMessages.addElement(objArr);
        notify();
    }

    void pleaseStop()
    {
        keepGoing = false;
    }

}

