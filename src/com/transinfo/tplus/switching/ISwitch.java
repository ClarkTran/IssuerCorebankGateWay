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

import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.javabean.SwitchInfo;



public interface ISwitch {
	/** Un initialised or unknown */
	public static int UNKNOWN  =  -1;
	public static int STOPPED  =  0;
	public static int INIT =  1;
	public static int SUSPENDED  =  2;
	public static int RUNNING =  5;


	/**
	 * Information about the service, recommended format given below.
	 * <p><code>
	 * &lt;&lt;ServiceName&gt;&gt; v&lt;&lt;Version_No&gt;&gt;\n<br>
	 * &lt;&lt;IP_ADDRESS&gt;&gt; &lt;&lt;PORT_NO&gt;&gt;\n<br>
	 * &lt;&lt;ANY OTHET INFORMATION&gt;&gt;
	 * </code></p>
	 */
	public String info();
	/**
	 * Returns the state of the process
	 * As any constant of {@link Service} interface.
	 */
	public int getServiceState();

	/**
	 *  Send a message to the respective network
	 *  @Param SwitchName, ISOMessage
	 *  @returns
	 */

	public String sendMessage(SwitchInfo objSwitch,byte[] ISOMsg)throws TPlusException ;

}
