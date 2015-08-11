package com.transinfo.tplus.messaging.mux;

import java.io.EOFException;
import java.io.IOException;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOFilter;
import org.jpos.iso.ISOFilter.VetoException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;

public class JCBChannel extends ASCIIChannel
{
	String message_length ;
	byte[] header_tmp ;

	public JCBChannel()
	{
	}

	public JCBChannel (String host, int port, ISOPackager p) {
		super(host, port, p);
	}

	//  /**
	//     * @param len the packed Message len
	//     * @exception IOException
	//     */
	//    protected void sendMessageLength(int len) throws IOException {
	//        //byte [] b = getTcpHeader(len).getBytes();
	//        System.out.println("Outgoing Header: "+ISOUtil.hexString(new byte[] {(byte)(len >>8), (byte)len}));
	//        serverOut.write (
	//                new byte[] {(byte)(len >>8), (byte)len}
	//            );
	//    }
	//    /**
	//     * @return the Message len
	//     * @exception IOException, ISOException
	//     */
	//    protected int getMessageLength() throws IOException, ISOException {
	//        return serverIn.read()*256 + serverIn.read();
	//    }
	//
	//   protected int getHeaderLength() {
	//        return 12;
	//    }

	/**
	 * sends an ISOMsg over the TCP/IP session
	 * @param m the Message to be sent
	 * @exception IOException
	 * @exception ISOException
	 * @exception ISOFilter.VetoException;
	 */

	public void send (ISOMsg m)
			throws IOException, ISOException, VetoException
			{
		LogEvent evt = new LogEvent (this, "send");
		evt.addMessage (m);
		try {
			if (!isConnected())
				throw new ISOException ("unconnected ISOChannel");

			m.setDirection(ISOMsg.OUTGOING);
			m = applyOutgoingFilters (m, evt);
			m.setDirection(ISOMsg.OUTGOING); // filter may have drop this info
			m.setPackager (getDynamicPackager(m));

			byte[] b = m.pack();

			synchronized (serverOut) {
				System.out.println("Body Message Length : " + b.length);
				System.out.println("Header Length : " + getHeaderLength(m));
				System.out.println("Dump Message: \n"+ ISOUtil.hexdump(b));

				sendMessageLength(b.length + getHeaderLength(m));
				sendMessageHeader(m, b.length);

				b = m.pack();

				//byte[] tmp1 = ISOUtil.concat(message_length,0,message_length.length,header_tmp,0,header_tmp.length);
				byte[] tmp2 = ISOUtil.concat(header_tmp,0,header_tmp.length,b,0,b.length);

				System.out.println("Full message 1 : \n" + new String(tmp2));
				System.out.println("Full message 2 : \n" + ISOUtil.hexdump(tmp2));
				m.dump(System.out,"");

				sendMessage (tmp2, 0, tmp2.length);

				sendMessageTrailler(m, b.length);

				serverOut.flush ();

			}
			cnt[TX]++;
			setChanged();
			notifyObservers(m);
		} catch (VetoException e) {
			System.out.println(e);
			evt.addMessage (e);
			throw e;
		} catch (ISOException e) {
			System.out.println(e);
			evt.addMessage (e);
			throw e;
		} catch (IOException e) {
			System.out.println(e);
			evt.addMessage (e);
			throw e;
		} catch (Exception e) {
			System.out.println(e);
			evt.addMessage (e);
			throw new ISOException ("unexpected exception", e);
		} finally {
			Logger.log (evt);
		}
			}


	protected void sendMessageLength(int len) throws IOException {
		if (len > 9999)
			throw new IOException ("len exceeded");
		System.out.println("Message Length: " + len);
		message_length = "0000" + Integer.toHexString(len);
		//message_length = "0000" + message_length.substring(message_length.length()-4);
		message_length = message_length.substring(message_length.length()-4)+"0000";
		//message_length = message_length.substring(message_length.length());
		System.out.println("Hexa decimal: " +  message_length);
		//serverOut.write(hexa.getBytes());
	}

	protected void sendMessageHeader(ISOMsg m, int len) throws IOException {
		if (m.getHeader() != null)
		{
			//serverOut.write(m.getHeader());
			System.out.println(m.getHeader().length);
			System.out.println(ISOUtil.hexString(m.getHeader()));

			String msgLength_header =  message_length +  ISOUtil.hexString(m.getHeader()) ;
			//String msgLength_header =  message_length +  "0000" ;
			System.out.println(msgLength_header);
			m.setHeader(ISOUtil.hex2byte(msgLength_header));
			header_tmp = m.getHeader() ;
		}else{
			String msgLength_header =  message_length;
			System.out.println(msgLength_header);
			//m.setHeader(ISOUtil.hex2byte(msgLength_header));
			//header_tmp = m.getHeader() ;
			header_tmp = ISOUtil.hex2byte(msgLength_header);
			//header_tmp = new byte[0];
		}

		//else if (header != null)
		//serverOut.write(header);
	}

	protected int getHeaderLength(ISOMsg m) {
		return m.getHeader() != null ? m.getHeader().length : 0;
	}

	//    protected int getMessageLength() throws IOException, ISOException {
	//        byte[] b = new byte[2];
	//        serverIn.readFully(b,0,2);
	//        serverIn.read();
	//        //return Integer.parseInt (ISOUtil.bcd2str (b, 0, 4, true));
	//        return Integer.parseInt (ISOUtil.hexString(b),16);
	//    }

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



	/**
	 * Waits and receive an ISOMsg over the TCP/IP session
	 * @return the Message received
	 * @exception IOException
	 * @exception ISOException
	 */
	public ISOMsg receive() throws IOException, ISOException {
		System.out.println("receive...");
		byte[] b=null;
		byte[] header=null;
		//byte[] realHeader = null;
		byte[] dataBody = null ;
		LogEvent evt = new LogEvent (this, "receive");
		ISOMsg m = createMsg ();
		//m.setPackager (new JCBPackager());
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
					dataBody = new byte[len-2];
					System.arraycopy(b,2,dataBody,0,dataBody.length);

					System.out.println("ISOUtil.hexString(dataBody) " + ISOUtil.hexString(dataBody));

				}
				else
					throw new ISOException(
							"receive length " +len + " seems strange");
			}
			System.out.println("opopp....");
			m.setPackager (getDynamicPackager(b));
			//m.setPackager (new JCBPackager());
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