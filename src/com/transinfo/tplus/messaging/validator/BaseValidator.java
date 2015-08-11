package com.transinfo.tplus.messaging.validator;

import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;


public interface BaseValidator
{
	public boolean process(IParser objISO)throws OnlineException;
}
