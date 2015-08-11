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

package com.transinfo.tplus.messaging.parser;

import com.transinfo.tplus.TPlusException;

public abstract class IParser extends TPlusISOMsg
{

	public abstract void parse(String strISOString)throws TPlusException;
	//public abstract void parse(String strISOString,String bits)throws TPlusException;
	//public abstract IParser parse(IParser objISO,String bits)throws TPlusException;
	public abstract byte[] repack()throws TPlusException;
	//public abstract byte[] repack(IParser msgObj,String bits)throws TPlusException;

	//public abstract ISOMsg visaParse(String strISOString)throws TPlusException;

}