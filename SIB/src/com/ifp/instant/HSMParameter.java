package com.ifp.instant;

import java.io.IOException;

import com.ifp.util.CommonDesc;


public class HSMParameter 
{
	public String HSMNAME;
	public String HSMPROTOCOL;
	public String HSMTYPE;
	public String HSMADDRESS;
	public int HSMPORT;
	public int HEADERLEN;
	public String HEADERTYPE;
	public int HSMHEADERLEN;
	public int HSMTIMEOUT;
	public int CONNECTIONINTERVAL;
	public int HSMSTATUS;
	public String HSM_ID;
	public String CHNLEN;
	public String PVK;
	public String PPK;
	public String PIN_LENGTH;
	public String PAN_OFFSET;
	public String PINOFFSET_LENGTH;
	public String PIN_PAD_CHAR;
	public String DECIMILISATION_TABLE;
	public String MSG_HEADER;
	public String MAIL_LENGTH;
	public String MAIL_HEIGHT;
	public String CVV_REQUIRED;
	public String CVV_LENGTH;
	public String CVVK1;
	public String CVVK2;
	public String PVK1;
	public String PVK2;
	public String GEN_METHOD;
	public String PVKI;
	public String PANVALIDATION_LENGTH;
	public String PVK3;
	public String PVK4;
	public String CVK4;
	public String CVK3;
	public String PROTOCOL_TYPE;
	public String STOP_BITS;
	public String PARITY_VALUE;
	public String DATA_BITS;
	public String BAUD_RATE;
	public String PORT_TYPE;
	public String EPVK;
	public String PANPADCHAR;
	public String DESLENGTH;
	public String PINMAILER_ID;
	public String SERVICE_CODE;
	public String error_hsmparameter;
	public String PINMAILER_DOC_TYPE="A";
	
	public CommonDesc comInsntance(){
		CommonDesc commondesc = new CommonDesc();
		return commondesc;
	}
	
	public HSMParameter(String HSMNAME,String HSMPROTOCOL,String HSMTYPE,String HSMADDRESS,int HSMPORT,int HEADERLEN,String HEADERTYPE,int HSMHEADERLEN,int HSMTIMEOUT,int CONNECTIONINTERVAL,int HSMSTATUS,String HSM_ID,String CHNLEN,String PVK,String PPK,String PIN_LENGTH,String PAN_OFFSET,String PINOFFSET_LENGTH,String PIN_PAD_CHAR,String DECIMILISATION_TABLE,String MSG_HEADER,String MAIL_LENGTH,String MAIL_HEIGHT,String CVV_REQUIRED,String CVV_LENGTH,String CVVK1,String CVVK2,String PVK1,String PVK2,String GEN_METHOD,String PVKI,String PANVALIDATION_LENGTH,String PVK3,String PVK4,String CVK4,String CVK3,String PROTOCOL_TYPE,String STOP_BITS,String PARITY_VALUE,String DATA_BITS,String BAUD_RATE,String PORT_TYPE,String EPVK,String PANPADCHAR,String DESLENGTH,String PINMAILER_ID,String SERVICE_CODE,String PINMAILER_DOC_TYPE)
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
				this.PPK = PPK;
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
	public void hsmparameter_obj_string() throws IOException
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
						"PPK -"+ PPK+", "+
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
						
		comInsntance().printLog(values);
		
		
	}
}
