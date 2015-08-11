package com.transinfo.tplus.messaging.mux;

import java.io.EOFException;
import java.io.IOException;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.RawChannel;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;

public class TestChannel extends RawChannel {
	
	public TestChannel (String host, int port, ISOPackager p, byte[] h) {
		super(host, port, p, h);
	}
	
	protected int getMessageLength() throws IOException, ISOException {
		int l = 0;
		byte[] b = new byte[2];
		while (l == 0) {
			try {
				serverIn.readFully(b,0,2);
				if ((l=Integer.parseInt (ISOUtil.hexString(b),16)) == 0) {
					serverOut.write(b);
					serverOut.flush();
				}
			}catch(EOFException ef){
				l = -1 ;
				// super.getSocket().close(); //for VCB only
				//super.connect();
				super.reconnect();
			}catch(java.net.SocketException se){
				super.reconnect();
			}catch (NumberFormatException e) {
				throw new ISOException ("Invalid message length "+ ISOUtil.hexString(b));
			}
		}
		return l;
	}

	@Override
	public ISOMsg receive() throws IOException, ISOException {
		System.out.println("receive...");
		byte[] b=null;
		byte[] header=null;
		//byte[] realHeader = null;
		byte[] dataBody = null ;
		LogEvent evt = new LogEvent (this, "receive");
		ISOMsg m = createMsg ();
		m.setPackager (new JCBPackager());
		m.setSource (this);
		try {
			if (!isConnected())
				throw new ISOException ("unconnected ISOChannel");

			synchronized (serverIn) {
				int len  = getMessageLength();
				len = len+2;
				System.out.println("Message length : " + len);
				int hLen = getHeaderLength();
				System.out.println("Header length : " + hLen);

				if (len == -1) {
					if (hLen > 0) {
						header = readHeader(hLen);
					}
					b = streamReceive();
				}
				else if (len > 10 && len <= 4096) {
					if (hLen > 0) {
						// ignore message header (TPDU)
						// Note header length is not necessarily equal to hLen (see VAPChannel)
						header = readHeader(hLen);
						len -= header.length;
					}
					b = new byte[len];
					serverIn.readFully(b, 0, len);

					getMessageTrailler();

					System.out.println("ISOUtil.hexString(b) " + ISOUtil.hexString(b));

					System.out.println("Message data : \n" + ISOUtil.hexdump(b));

					//realHeader = new byte[5];
					//System.arraycopy(b,0,realHeader,0,realHeader.length);
					//dataBody = new byte[len-5];
					//System.arraycopy(b,5,dataBody,0,dataBody.length);
					dataBody = new byte[len];
					System.arraycopy(b,0,dataBody,0,dataBody.length);

					System.out.println("ISOUtil.hexString(dataBody) " + ISOUtil.hexString(dataBody));

				}
				else
					throw new ISOException(
							"receive length " +len + " seems strange");
			}
			System.out.println("opopp....");
			//m.setPackager (getDynamicPackager(b));
			m.setPackager (new JCBPackager());
			//m.setHeader (realHeader);
			if (b.length > 0 && !shouldIgnore (header))  // Ignore NULL messages
				m.unpack (dataBody);
			m.setDirection(ISOMsg.INCOMING);
			//m = applyIncomingFilters (m, realHeader, dataBody, evt);
			m.setDirection(ISOMsg.INCOMING);
			evt.addMessage (m);

			m.dump(System.out,"");

			cnt[RX]++;
			setChanged();
			notifyObservers(m);
		} catch (ISOException e) {
			evt.addMessage (e);
			if (header != null) {
				evt.addMessage ("--- header ---");
				evt.addMessage (ISOUtil.hexdump (header));
			}
			if (b != null) {
				evt.addMessage ("--- data ---");
				evt.addMessage (ISOUtil.hexdump (b));
			}
			throw e;
		}

		catch (Exception e) {
			System.out.println("Error------------ : " + e.toString());
			e.printStackTrace();
		}


		finally {
			Logger.log (evt);
		}
		return m;
	}

}
