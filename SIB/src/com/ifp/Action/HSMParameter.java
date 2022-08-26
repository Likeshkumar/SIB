package com.ifp.Action;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

public class HSMParameter 
{
	protected String HSMNAME;
	protected String HSMPROTOCOL;
	protected String HSMTYPE;
	protected String HSMADDRESS;
	protected int HSMPORT;
	protected int HEADERLEN;
	protected String HEADERTYPE;
	protected int HSMHEADERLEN;
	protected int HSMTIMEOUT;
	protected int CONNECTIONINTERVAL;
	protected int HSMSTATUS;
	protected String HSM_ID;
	protected String CHNLEN;
	protected String PVK;
	protected String PIN_LENGTH;
	protected String PAN_OFFSET;
	protected String PINOFFSET_LENGTH;
	protected String PIN_PAD_CHAR;
	protected String DECIMILISATION_TABLE;
	protected String MSG_HEADER;
	protected String MAIL_LENGTH;
	protected String MAIL_HEIGHT;
	protected String CVV_REQUIRED;
	protected String CVV_LENGTH;
	protected String CVVK1;
	protected String CVVK2;
	protected String PVK1;
	protected String PVK2;
	protected String GEN_METHOD;
	protected String PVKI;
	protected String PANVALIDATION_LENGTH;
	protected String PVK3;
	protected String PVK4;
	protected String CVK4;
	protected String CVK3;
	protected String PROTOCOL_TYPE;
	protected String STOP_BITS;
	protected String PARITY_VALUE;
	protected String DATA_BITS;
	protected String BAUD_RATE;
	protected String PORT_TYPE;
	protected String EPVK;
	protected String PANPADCHAR;
	protected String DESLENGTH;
	protected String PINMAILER_ID;
	protected String SERVICE_CODE;
	protected String error_hsmparameter;
	protected String PINMAILER_DOC_TYPE;
	
	public HSMParameter(String HSMNAME,String HSMPROTOCOL,String HSMTYPE,String HSMADDRESS,int HSMPORT,int HEADERLEN,String HEADERTYPE,int HSMHEADERLEN,int HSMTIMEOUT,int CONNECTIONINTERVAL,int HSMSTATUS,String HSM_ID,String CHNLEN,String PVK,String PIN_LENGTH,String PAN_OFFSET,String PINOFFSET_LENGTH,String PIN_PAD_CHAR,String DECIMILISATION_TABLE,String MSG_HEADER,String MAIL_LENGTH,String MAIL_HEIGHT,String CVV_REQUIRED,String CVV_LENGTH,String CVVK1,String CVVK2,String PVK1,String PVK2,String GEN_METHOD,String PVKI,String PANVALIDATION_LENGTH,String PVK3,String PVK4,String CVK4,String CVK3,String PROTOCOL_TYPE,String STOP_BITS,String PARITY_VALUE,String DATA_BITS,String BAUD_RATE,String PORT_TYPE,String EPVK,String PANPADCHAR,String DESLENGTH,String PINMAILER_ID,String SERVICE_CODE,String PINMAILER_DOC_TYPE)
	{
				this.HSMNAME = HSMNAME;
				this.HSMPROTOCOL = HSMPROTOCOL;
				this.HSMTYPE = HSMTYPE;
				this.HSMADDRESS = HSMADDRESS;
				this.HSMPORT = HSMPORT;
				this.HEADERLEN = HEADERLEN;
				this.HEADERTYPE = HEADERTYPE;
				this.HSMHEADERLEN = HSMHEADERLEN;
				this.HSMTIMEOUT = HSMTIMEOUT;
				this.CONNECTIONINTERVAL = CONNECTIONINTERVAL;
				this.HSMSTATUS = HSMSTATUS;
				this.HSM_ID = HSM_ID;
				this.CHNLEN = CHNLEN;
				this.PVK = PVK;
				this.PIN_LENGTH = PIN_LENGTH;
				this.PAN_OFFSET = PAN_OFFSET;
				this.PINOFFSET_LENGTH = PINOFFSET_LENGTH;
				this.PIN_PAD_CHAR = PIN_PAD_CHAR;
				this.DECIMILISATION_TABLE = DECIMILISATION_TABLE;
				this.MSG_HEADER = MSG_HEADER;
				this.MAIL_LENGTH = MAIL_LENGTH;
				this.MAIL_HEIGHT = MAIL_HEIGHT;
				this.CVV_REQUIRED = CVV_REQUIRED;
				this.CVV_LENGTH = CVV_LENGTH;
				this.CVVK1 = CVVK1;
				this.CVVK2 = CVVK2;
				this.PVK1 = PVK1;
				this.PVK2 = PVK2;
				this.GEN_METHOD = GEN_METHOD;
				this.PVKI = PVKI;
				this.PANVALIDATION_LENGTH = PANVALIDATION_LENGTH;
				this.PVK3 = PVK3;
				this.PVK4 = PVK4;
				this.CVK4 = CVK4;
				this.CVK3 = CVK3;
				this.PROTOCOL_TYPE = PROTOCOL_TYPE;
				this.STOP_BITS = STOP_BITS;
				this.PARITY_VALUE = PARITY_VALUE;
				this.DATA_BITS = DATA_BITS;
				this.BAUD_RATE = BAUD_RATE;
				this.PORT_TYPE = PORT_TYPE;
				this.EPVK = EPVK;
				this.PANPADCHAR = PANPADCHAR;
				this.DESLENGTH = DESLENGTH;
				this.PINMAILER_ID = PINMAILER_ID;
				this.SERVICE_CODE = SERVICE_CODE;
				this.PINMAILER_DOC_TYPE=PINMAILER_DOC_TYPE;
	}
	public HSMParameter()
	{
		
	}
	public void setError_hsmparameter(String error)
	{
		this.error_hsmparameter=error;
	}
	public void hsmparameter_obj_string()
	{
		String values=  "HSMNAME -"+HSMNAME+", "+
						"HSMPROTOCOL -"+HSMPROTOCOL+", "+
						"HSMTYPE -"+HSMTYPE+", "+
						"HSMADDRESS -"+HSMADDRESS+", "+
						"HSMPORT -"+ HSMPORT+", "+
						"HEADERLEN -"+ HEADERLEN+", "+
						"HEADERTYPE -"+ HEADERTYPE+", "+
						"HSMHEADERLEN -"+ HSMHEADERLEN+", "+
						"HSMTIMEOUT -"+ HSMTIMEOUT+", "+
						"CONNECTIONINTERVAL -"+ CONNECTIONINTERVAL+", "+
						"HSMSTATUS -"+ HSMSTATUS+", "+
						"HSM_ID -"+ HSM_ID+", "+
						"CHNLEN -"+ CHNLEN+", "+
						"PVK -"+ PVK+", "+
						"PIN_LENGTH -"+ PIN_LENGTH+", "+
						"PAN_OFFSET -"+ PAN_OFFSET+", "+
						"PINOFFSET_LENGTH -"+ PINOFFSET_LENGTH+", "+
						"PIN_PAD_CHAR -"+ PIN_PAD_CHAR+", "+
						"DECIMILISATION_TABLE -"+DECIMILISATION_TABLE+", "+
						"MSG_HEADER -"+ MSG_HEADER+", "+
						"MAIL_LENGTH -"+MAIL_LENGTH+", "+
						"MAIL_HEIGHT -"+ MAIL_HEIGHT+", "+
						"CVV_REQUIRED -"+ CVV_REQUIRED+", "+
						"CVV_LENGTH -"+ CVV_LENGTH+", "+
						"CVVK1 -"+ CVVK1+", "+
						"CVVK2 -"+ CVVK2+", "+
						"PVK1 -"+ PVK1+", "+
						"PVK2 -"+ PVK2+", "+
						"GEN_METHOD -"+ GEN_METHOD+", "+
						"PVKI -"+ PVKI+", "+
						"PANVALIDATION_LENGTH -"+ PANVALIDATION_LENGTH+", "+
						"PVK3 -"+ PVK3+", "+
						"PVK4 -"+ PVK4+", "+
						"CVK4 -"+ CVK4+", "+
						"CVK3 -"+CVK3+", "+
						"PROTOCOL_TYPE -"+ PROTOCOL_TYPE+", "+
						"STOP_BITS -"+ STOP_BITS+", "+
						"PARITY_VALUE -"+ PARITY_VALUE+", "+
						"DATA_BITS -"+ DATA_BITS+", "+
						"BAUD_RATE -"+ BAUD_RATE+", "+
						"PORT_TYPE -"+ PORT_TYPE+", "+
						"EPVK -"+ EPVK+", "+
						"PANPADCHAR -"+ PANPADCHAR+", "+
						"DESLENGTH -"+ DESLENGTH+", "+
						"PINMAILER_ID -"+ PINMAILER_ID+", "+
						"SERVICE_CODE -"+ SERVICE_CODE+", "+
						"PINMAILER_DOC_TYPE"+PINMAILER_DOC_TYPE+", "+
						"error_hsmparameter ->>>>>>"+error_hsmparameter+" Object Varible Ends Here  ";
						
		System.out.println(values);
		
		
	}
}
