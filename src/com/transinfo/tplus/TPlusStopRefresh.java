/**
 *This class is the main server daemon..This class starts the TPlusServer daemon thread.
 *This server services all the admin requests.
 *

@ Copyright  (c)  2001-2002 transinfo Pte Ltd. Singapore.
@ All Rights Reserved
@ This work contains trade secrets and confidential material of  transinfo Pte Ltd. Singapore.
@ and its use of disclosure in whole or in part without express written permission of
@ transinfo Pte Ltd. Singapore. is prohibited.

@ File Name          : TPlusServerShutdown.java
@ Author             : I.T. Solutions Pte Ltd. Singapore
@ Date of Creation   : 9 July, 2001
@ Description        : This class is used to shutdown the TPlus Server. This class will be invoked from a Batch file.
@
@ Version Number     : 1.0
@					Modification History:
@ Date 		 Version No.		Modified By  	 	  Modification Details.
 */

package com.transinfo.tplus;

//Java specific imports.
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("unused")
public class TPlusStopRefresh {

	public static void main(String args[]) throws IOException {

		String strAction = "";
		System.out.println(args[0]);

		if (args[0].equals("-refresh")) {

			strAction = "REFRESHCACHE";

		} else if (args[0].equals("-stop")) {

			strAction = "SHUTDOWN";

		} else if (args[0].equals("-port")) {

			if (args.length < 2) {
				System.out.println("Invalid port.");
				return;
			}

			strAction = "PORT=" + args[1];

		} else if (args[0].equals("-voice")) {

			strAction = "VSALE&123456&000001050265165&65500011&4565982010129243&0109&123&000000099999&888";
			System.out.println(strAction);

		} else if (args[0].equals("-echo")) {

			strAction = "ECHO";

		} else if (args[0].equals("-signoff")) {

			strAction = "SIGNOFF";
			System.out.println("In SIGNOFF");

		} else if (args[0].equals("-signon")) {

			strAction = "SIGNON";
			System.out.println("IN SIGNON");

		} else if (args[0].equals("-adviceon")) {

			strAction = "ADVICEON";
			System.out.println("In ADVICEON");

		} else if (args[0].equals("-adviceoff")) {

			strAction = "ADVICEOFF";
			System.out.println("In ADVICEOFF");

		} else if (args[0].equals("-keyexchange")) {

			strAction = "KEYEXCHANGE";
			System.out.println("In KEYEXCHANGE for POS");

		} else if (args[0].equals("-cupcomtest")) {

			strAction = "CUPCOMTEST";
			System.out.println("In CUPCOMTEST");

		} else if (args[0].equals("-cupsignon")) {

			strAction = "CUPSIGNON";
			System.out.println("In CUPSIGNON");

		} else if (args[0].equals("-cupsignoff")) {

			strAction = "CUPSIGNOFF";
			System.out.println("In CUPSIGNOFF");

		} else if (args[0].equals("-cupecho")) {

			strAction = "CUPECHO";

		} else if (args[0].equals("-cuptextinfo")) {

			strAction = "CUPTEXTINFO";

		} else if (args[0].equals("-cuppikex")) {

			strAction = "CUPPIKEX";

		} else if (args[0].equals("-jcbecho")) {

			strAction = "JCBECHO";

		} else if (args[0].equals("-jcbsignon")) {

			strAction = "JCBSIGNON";

		} else if (args[0].equals("-jcbsignoff")) {

			strAction = "JCBSIGNOFF";

		}  else if (args[0].equals("-jcbkeyexe")) {

			strAction = "JCBKEYEXE";

		} else {
			System.out.println("Invalid request.");
			return;
		}

		try {

			FileInputStream fisConfig = null;
			// create a file input stream to the ini file.

			// fisConfig = new FileInputStream("TPlusConfig.strConfigFileName");
			// java.util.Properties propConfig = new java.util.Properties();

			// load a properties object with the data in the ini file
			// propConfig.load(fisConfig);
			// String strAdminPort=propConfig.getProperty("AdminPort");
			int intAdminPort = 0;
			intAdminPort = Integer.parseInt("8001");

			// fisConfig.close();

			// Create a socket connection to connect to the admin server.
			System.out.println("POS intAdminPort=" + intAdminPort);
			Socket clientSocket = new Socket("127.0.0.1", intAdminPort);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			InputStream isClient = clientSocket.getInputStream();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			// Fire the shutdown action.
			out.println(strAction);
			String strInput = "";
			byte[] cbuf = new byte[200];
			StringBuffer buff = new StringBuffer();
			int len = 0;

			try {

				if ((len = isClient.read(cbuf)) != -1) {
					String sdata = new String(cbuf, 0, len);

					buff.append(sdata);
				} else {
					buff.setLength(0);
				}

			} catch (IOException e) {

			}
			/*
			 * while((strInput = in.readLine()) != null) {
			 * System.out.println("Data Read="+strInput); }
			 */

			/*
			 * byte[] readBuffer = new byte[1024];
			 * 
			 * int socReadCont = 0;
			 * 
			 * 
			 * 
			 * int size=0; String strRequest = null; while ((size =
			 * isClient.read(readBuffer)) != -1) {
			 * 
			 * System.out.println(size); }
			 */

			// Close the socket connection.
			in.close();
			out.close();
			clientSocket.close();

		} catch (NumberFormatException exp) {
			System.out.println("Configuration parameters not set properly. Unable to service request.");
		} catch (java.net.ConnectException conExp) {
			System.out.println("Error : Trying to post an admin request to a wrong server or the server is not running.");
		} catch (Exception exp) {
			System.out.println("Error while processing admin request. Error: " + exp.getMessage());
		}

	}
}