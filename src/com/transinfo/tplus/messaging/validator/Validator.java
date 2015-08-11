package com.transinfo.tplus.messaging.validator;

import java.util.ArrayList;

import com.transinfo.tplus.messaging.OnlineException;
import com.transinfo.tplus.messaging.parser.IParser;

@SuppressWarnings("unchecked")
public class Validator implements BaseValidator
{

	ArrayList validatorList = new ArrayList();

	public void addValidator(BaseValidator validator)
	{

		validatorList.add(validator);
	}


	public boolean process(IParser objISO)throws OnlineException
	{
		try
		{
			System.out.println("Validator Process="+validatorList.size());
			for(int i=0;i<validatorList.size();i++)
			{
				BaseValidator validator = (BaseValidator)validatorList.get(i);
				validator.process(objISO);
			}

		}catch(OnlineException oExp)
		{
			throw oExp;
		}
		catch(Exception exp)
		{
			throw new OnlineException("96", "022822", "System Exception  "+exp.getMessage());
		}

		return true;
	}

}

