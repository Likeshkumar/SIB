package com.ifp.personalize;

public class Personalizeorderdetails 
{
	public String inst_id;
	public String order_ref_no;
	public String card_type_id;
	public String sub_prod_id;
	public String product_code;
	public String card_quantity;
	public String embossing_name;
	public String encode_data;
	public String branch_code;
	public String bin;
	public String cin;
	public String appno;
	public String appdate;
	public String mcardno;
	
	public Personalizeorderdetails(String inst_id,String order_ref_no,String card_type_id,String sub_prod_id,String product_code,String card_quantity,String embossing_name,String encode_data,String branch_code,String bin,String cin,String appno,String appdate,String mcardno)
	{
		this.inst_id = inst_id;
		this.order_ref_no = order_ref_no;
		this.card_type_id = card_type_id;
		this.sub_prod_id = sub_prod_id;
		this.product_code = product_code;
		this.card_quantity = card_quantity;
		this.embossing_name = embossing_name;
		this.encode_data = encode_data;
		this.branch_code = branch_code;
		this.bin = bin;
		this.cin = cin;
		this.appno = appno;
		this.appdate = appdate;
		this.mcardno = mcardno;
	}
	
	public String chnlen_glcode;
	public String prodcard_expiry;
	public String brcode_servicecode;
	public String baselen_feecode;
	public String cardscount_cardccy;
	public String chnbaseno_limitid;
	public String apptypelen;
	public String apptypevalue;
	public String servicecode;
	public Personalizeorderdetails(String chnlen,String prodcard,String brcode,String baselen,String cardscount,String chn_base_no, String apptypelen2, String apptype_value,String servicecode)
	{
		this.chnlen_glcode = chnlen;
		this.prodcard_expiry = prodcard;
		this.brcode_servicecode = brcode;
		this.baselen_feecode = baselen;
		this.cardscount_cardccy = cardscount;
		this.chnbaseno_limitid = chn_base_no;
		this.apptypelen=apptypelen2;
		this.apptypevalue = apptype_value;
		this.servicecode = servicecode;  
	}
	
}
