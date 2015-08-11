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
package com.transinfo.tplus.switching;

import java.io.IOException;

import com.transinfo.tplus.TPlusException;


public interface IConnectionHandler
{

	public static boolean ACTIVE  =  true;
	public static boolean NOTACTIVE  =  false;

    public abstract void queueMessage(String s);

    public abstract String sendAndRead(byte[] s)throws IOException,TPlusException;

    public abstract void shutdown(boolean flag);

    public abstract void closeSocket();

    public abstract void setListener(IConnectionListener iconnectionlistener);

    public abstract boolean isConnected();

    public abstract String getSwitchName();
}
