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

package com.transinfo.tplus.messaging;

import com.transinfo.tplus.TPlusException;
import com.transinfo.tplus.messaging.parser.IParser;

/**
 * This interface declare to execute request message
 *
 */

public interface MessageListener
{

 /**
  *  Flag indicate that the response need to send to switch manager
  *  futher processing
  */

  int REQUEST_ISSUER = 0;

 /**
  *  Flag is indicate that the response need to send back to client
  */

  int RESPONSE_CLIENT = 1;


/**
 *  The sub classes which implements this class must override this method
 *  to implemets actual processing the request.
 *  @param TPlusISOParser ,String
 *  @return TPlusISOParser; return value 0 - > send to Issuer 1 -> response back to client
 */
	public IParser execute(IParser objISO,String Header)throws TPlusException;

}
