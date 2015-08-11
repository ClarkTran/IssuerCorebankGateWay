package com.transinfo.tplus.messaging.mux;

import org.jpos.iso.ISOMsg;

import com.transinfo.tplus.messaging.parser.IParser;

public interface IMux
{

	public ISOMsg process(ISOMsg isomsg);
	public void initialize(IParser objISO);

}