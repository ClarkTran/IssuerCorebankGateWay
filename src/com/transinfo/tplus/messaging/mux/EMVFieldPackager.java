package com.transinfo.tplus.messaging.mux;

import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOField;

public class EMVFieldPackager extends ISOBinaryField
{
 public ISOField tagName = new ISOField();
 public ISOField tagValue = new ISOField();

  public EMVFieldPackager()
  {
    super();
  }
  public EMVFieldPackager(int i)
  {
    super(i);
  }
}