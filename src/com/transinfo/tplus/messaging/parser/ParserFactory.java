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


import com.transinfo.tplus.TPlusCodes;
import com.transinfo.tplus.TPlusException;

public class ParserFactory
{

public static synchronized IParser createPraserObject(String strMsgParser)throws TPlusException
{

	try
	{
		Class classObj = Class.forName(strMsgParser);
		return (IParser)classObj.newInstance();
 	}
 	catch(InstantiationException exp)
 	{

		throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," Parser Class Instantiation Exception"+exp.toString());
 	}
 	catch(ClassNotFoundException exp)
 	{
		throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," Parser Class Not Found Exception"+exp.toString());
	}
	catch(Exception exp)
	{
		throw new TPlusException(TPlusCodes.OBJ_CREATION_FAIL," Error while genarating parser class"+exp.toString());
	}

	/*if(intReqId == 1)
	return new TPlus8583Parser();
	else if(intReqId == 2)
	return new TPlusVoiceParser();
	else if(intReqId == 3)
	return new TPlusEMVParser();
	else
	return null;*/
}



}