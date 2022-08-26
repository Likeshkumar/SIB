package com.ifd.CurrencyRate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class CurrencyRateBeans {
	
	public String act;
	public String buyingrate;
	public String sellingrate;
	public String currencycode;
	public String currencydesc;
	public List currencylist;
	public List prevratelist;
	
	public String getCurrencydesc() {
		return currencydesc;
	}
	public void setCurrencydesc(String currencydesc) {
		this.currencydesc = currencydesc;
	}
	public List getPrevratelist() {
		return prevratelist;
	}
	public void setPrevratelist(List prevratelist) {
		this.prevratelist = prevratelist;
	}
	public BigDecimal buyrate;
	public BigDecimal getBuyrate() {
		return buyrate;
	}
	public void setBuyrate(BigDecimal buyrate) {
		this.buyrate = buyrate;
	}
	public BigDecimal getSellrate() {
		return sellrate;
	}
	public void setSellrate(BigDecimal sellrate) {
		this.sellrate = sellrate;
	}
	public BigDecimal sellrate;
	
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public List getCurrencylist() {
		return currencylist;
	}
	public void setCurrencylist(List currencylist) {
		this.currencylist = currencylist;
	}
	public String getBuyingrate() {
		return buyingrate;
	}
	public void setBuyingrate(String buyingrate) {
		this.buyingrate = buyingrate;
	}
	public String getSellingrate() {
		return sellingrate;
	}
	public void setSellingrate(String sellingrate) {
		this.sellingrate = sellingrate;
	}
	public String getCurrencycode() {
		return currencycode;
	}
	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}
	
}
