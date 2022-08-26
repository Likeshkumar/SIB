package com.ifp.beans;

import java.io.File;
import java.util.Date;
import java.util.List;

import oracle.sql.DATE;

import com.ifp.Action.BaseAction;

public class InstCardRegisterProcessBeans extends BaseAction 
{ 
	String firstname;
	String custname;
	String acctsubtype;
	String Collectionbranch;
	String Embossingname;
	String Accounttype;
	String Accountnumber;
	String P_poxbox;
	String P_district;
	String P_phone1;
	String P_houseno;
	String P_wardnumber;
	String P_phone2;
	public String getP_phone2() {
		return P_phone2;
	}
	public void setP_phone2(String p_phone2) {
		P_phone2 = p_phone2;
	}
	public String getP_wardnumber() {
		return P_wardnumber;
	}
	public void setP_wardnumber(String p_wardnumber) {
		P_wardnumber = p_wardnumber;
	}
	public String getP_houseno() {
		return P_houseno;
	}
	public void setP_houseno(String p_houseno) {
		P_houseno = p_houseno;
	}
	public String getP_phone1() {
		return P_phone1;
	}
	public void setP_phone1(String p_phone1) {
		P_phone1 = p_phone1;
	}
	public String getP_district() {
		return P_district;
	}
	public void setP_district(String p_district) {
		P_district = p_district;
	}
	String Currency1;
	public String getCurrency1() {
		return Currency1;
	}
	public void setCurrency1(String currency1) {
		Currency1 = currency1;
	}
	public String getP_poxbox() {
		return P_poxbox;
	}
	public void setP_poxbox(String p_poxbox) {
		P_poxbox = p_poxbox;
	}
	String P_streetname;
	 
	
	public String getP_streetname() {
		return P_streetname;
	}
	public void setP_streetname(String p_streetname) {
		P_streetname = p_streetname;
	}
	public String getAccounttype() {
		return Accounttype;
	}
	public void setAccounttype(String accounttype) {
		Accounttype = accounttype;
	}
	public String getEmbossingname() {
		return Embossingname;
	}
	public void setEmbossingname(String embossingname) {
		Embossingname = embossingname;
	}
	public String getCollectionbranch() {
		return Collectionbranch;
	}
	public void setCollectionbranch(String Collectionbranch) {
		this.Collectionbranch = Collectionbranch;
	}
	public String getAcctsubtype() {
		return acctsubtype;
	}
	public void setAcctsubtype(String acctsubtype) {
		this.acctsubtype = acctsubtype;
	}
	public String getCustname() {
		return custname;
	}
	public void setCustname(String custname) {
		this.custname = custname;
	}
	String midname;
	String lastname;
	String fahtername;
	String mothername;
	String gender;
	String mstatus;
	public String getInsttypereg() {
		return insttypereg;
	}
	public void setInsttypereg(String insttypereg) {
		this.insttypereg = insttypereg;
	}
	String spname;
	String occupation;
	String nationality;  
	String dob;
	String insttypereg;
    public String formaction;
	public String getFormaction() {
		return formaction;
	}
	public void setFormaction(String formaction) {
		this.formaction = formaction;
	}
	String msterbin;
	public String getMsterbin() {
		return msterbin;
	}
	public void setMsterbin(String msterbin) {
		this.msterbin = msterbin;
	}
	public String getPropriatorybin() {
		return propriatorybin;
	}
	public void setPropriatorybin(String propriatorybin) {
		this.propriatorybin = propriatorybin;
	}
	String propriatorybin;
	
	String phoneno;
	String email; 
	String reqdocuement;
	String documentid; 
	String appdate;
	String appno;
	String paddress1;
	String paddress2;
	String paddress3;
	String paddress4;
	String paddress5;
	String paddress6;
	public String cardeligibility;
	
	
	public String getCardeligibility() {
		return cardeligibility;
	}
	public void setCardeligibility(String cardeligibility) {
		this.cardeligibility = cardeligibility;
	}
	public List acctproduct;
	public List Currency;
	
	public List getAcctproduct() {
		return acctproduct;
	}
	public void setAcctproduct(List acctproduct) {
		this.acctproduct = acctproduct;
	}
	public List getCurrency() {
		return Currency;
	}
	public void setCurrency(List currency) {
		Currency = currency;
	}
	public String getPaddress5() {
		return paddress5;
	}
	public void setPaddress5(String paddress5) {
		this.paddress5 = paddress5;
	}
	public String getPaddress6() {
		return paddress6;
	}
	public void setPaddress6(String paddress6) {
		this.paddress6 = paddress6;
	}
	String resaddress1;
	String resaddress2;
	String resaddress3;
	String resaddress4; 
	public String cardno;
	int custid; 
	String kycuser;
	List branchlist;
	List prodlist;
	String photourl;
	String signatureurl;
	String idproofurl;
	String maritaldesc;
	String genderdesc;
	String documentdesc;
	String countrydesc;
	
	List nationalitylist;
	List documenttypelist;
	List existcustdetails ;
	String makerid;
	String checkerid;
	String makerdate;
	String checkerdate;
	String mkckflag;
	List instorderlist;
	String custtype;
	File customerphoto;
	Boolean existingcust = false;
	 
	
	public Date getCardgendate() {
		return cardgendate;
	}
	public void setCardgendate(Date cardgendate) {
		this.cardgendate = cardgendate;
	}
	public String getSignatureurl() {
		return signatureurl;
	}
	public void setSignatureurl(String signatureurl) {
		this.signatureurl = signatureurl;
	}
	public String getIdproofurl() {
		return idproofurl;
	}
	public void setIdproofurl(String idproofurl) {
		this.idproofurl = idproofurl;
	}
	public String getMaritaldesc() {
		return maritaldesc;
	}
	public void setMaritaldesc(String maritaldesc) {
		this.maritaldesc = maritaldesc;
	}
	public String getGenderdesc() {
		return genderdesc;
	}
	public void setGenderdesc(String genderdesc) {
		this.genderdesc = genderdesc;
	}
	public String getDocumentdesc() {
		return documentdesc;
	}
	public void setDocumentdesc(String documentdesc) {
		this.documentdesc = documentdesc;
	}
	public String getCountrydesc() {
		return countrydesc;
	}
	public void setCountrydesc(String countrydesc) {
		this.countrydesc = countrydesc;
	}
	public Boolean getExistingcust() {
		return existingcust;
	}
	public void setExistingcust(Boolean existingcust) {
		this.existingcust = existingcust;
	}
	public File getCustomerphoto() {
		return customerphoto;
	}
	public void setCustomerphoto(File customerphoto) {
		this.customerphoto = customerphoto;
	}
	public String getCusttype() {
		return custtype;
	}
	public void setCusttype(String custtype) {
		this.custtype = custtype;
	}
	public List getInstorderlist() {
		return instorderlist;
	}
	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}
	public String getMakerid() {
		return makerid;
	}
	public void setMakerid(String makerid) {
		this.makerid = makerid;
	}
	public String getCheckerid() {
		return checkerid;
	}
	public void setCheckerid(String checkerid) {
		this.checkerid = checkerid;
	}
	public String getMakerdate() {
		return makerdate;
	}
	public void setMakerdate(String makerdate) {
		this.makerdate = makerdate;
	}
	public String getCheckerdate() {
		return checkerdate;
	}
	public void setCheckerdate(String checkerdate) {
		this.checkerdate = checkerdate;
	}
	public String getMkckflag() {
		return mkckflag;
	}
	public void setMkckflag(String mkckflag) {
		this.mkckflag = mkckflag;
	}
	public String getPhotourl() {
		return photourl;
	}
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}
	public String getKycuser() {
		return kycuser;
	}
	public void setKycuser(String kycuser) {
		this.kycuser = kycuser;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getMidname() {
		return midname;
	}
	public void setMidname(String midname) {
		this.midname = midname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFahtername() {
		return fahtername;
	}
	public void setFahtername(String fahtername) {
		this.fahtername = fahtername;
	}
	public String getMothername() {
		return mothername;
	}
	public void setMothername(String mothername) {
		this.mothername = mothername;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMstatus() {
		return mstatus;
	}
	public void setMstatus(String mstatus) {
		this.mstatus = mstatus;
	}
	public String getSpname() {
		return spname;
	}
	public void setSpname(String spname) {
		this.spname = spname;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getReqdocuement() {
		return reqdocuement;
	}
	public void setReqdocuement(String reqdocuement) {
		this.reqdocuement = reqdocuement;
	}
	public String getDocumentid() {
		return documentid;
	}
	public void setDocumentid(String documentid) {
		this.documentid = documentid;
	}
	public String getAppdate() {
		return appdate;
	}
	public void setAppdate(String gENERATED_DATE) {
		this.appdate = gENERATED_DATE;
	}
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public String getPaddress1() {
		return paddress1;
	}
	public void setPaddress1(String paddress1) {
		this.paddress1 = paddress1;
	}
	public String getPaddress2() {
		return paddress2;
	}
	public void setPaddress2(String paddress2) {
		this.paddress2 = paddress2;
	}
	public String getPaddress3() {
		return paddress3;
	}
	public void setPaddress3(String paddress3) {
		this.paddress3 = paddress3;
	}
	public String getPaddress4() {
		return paddress4;
	}
	public void setPaddress4(String paddress4) {
		this.paddress4 = paddress4;
	}
	public String getResaddress1() {
		return resaddress1;
	}
	public void setResaddress1(String resaddress1) {
		this.resaddress1 = resaddress1;
	}
	public String getResaddress2() {
		return resaddress2;
	}
	public void setResaddress2(String resaddress2) {
		this.resaddress2 = resaddress2;
	}
	public String getResaddress3() {
		return resaddress3;
	}
	public void setResaddress3(String resaddress3) {
		this.resaddress3 = resaddress3;
	}
	public String getResaddress4() {
		return resaddress4;
	}
	public void setResaddress4(String resaddress4) {
		this.resaddress4 = resaddress4;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public int getCustid() {
		return custid;
	}
	public void setCustid(int custid) {
		this.custid = custid;
	}
	public List getExistcustdetails() {
		return existcustdetails;
	}
	public void setExistcustdetails(List existcustdetails) {
		this.existcustdetails = existcustdetails;
	}
	public List getNationalitylist() {
		return nationalitylist;
	}
	public void setNationalitylist(List nationalitylist) {
		this.nationalitylist = nationalitylist;
	}
	public List getDocumenttypelist() {
		return documenttypelist;
	}
	public void setDocumenttypelist(List documenttypelist) {
		this.documenttypelist = documenttypelist;
	}
	public List getProdlist() {
		return prodlist;
	}
	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}
	public List getBranchlist() {
		return branchlist;
	}
	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}
	
	
	public String chn;
	public int branch_code;
	public String cardcollbranch;
	
	public int getBranch_code() {
		return branch_code;
	}
	public void setBranch_code(int branch_code) {
		this.branch_code = branch_code;
	}
	public String getCardcollbranch() {
		return cardcollbranch;
	}
	public void setCardcollbranch(String cardcollbranch) {
		this.cardcollbranch = cardcollbranch;
	}
	public String order_ref_no;
	public String card_ccy;
	public String hascard;
	public String chn_msk;
	
	
	public String cod_prod;
	 
	public String getCod_prod() {
		return cod_prod;
	}
	public void setCod_prod(String cod_prod) {
		this.cod_prod = cod_prod;
	}
	public String prod_code;
	public int wdl_lmt_amt;
	public int wdl_lmt_cnt;
	public int pur_lmt_amt;
	public int pur_lmt_cnt;
	public String city;
	public String accountno;
	public String usercode;
	public String username;
	public int cardtype;
	public String responcecode;
	
	public String getResponcecode() {
		return responcecode;
	}
	public void setResponcecode(String responcecode) {
		this.responcecode = responcecode;
	}
	public String custidno;
	
   private String branchcode;
	
	public String getBranchcode() {
		return branchcode;
	}
	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}
	
	public String getCustidno() {
		return custidno;
	}
	public void setCustidno(String custidno) {
		this.custidno = custidno;
	}
	public int getCardtype() {
		return cardtype;
	}
	public void setCardtype(int cardtype) {
		this.cardtype = cardtype;
	}
	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccountno() {
		return accountno;
	}
	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getChn() {
		return chn;
	}
	public void setChn(String chn) {
		this.chn = chn;
	}
	
	public String getOrder_ref_no() {
		return order_ref_no;
	}
	public void setOrder_ref_no(String order_ref_no) {
		this.order_ref_no = order_ref_no;
	}
	public String getCard_ccy() {
		return card_ccy;
	}
	public void setCard_ccy(String card_ccy) {
		this.card_ccy = card_ccy;
	}
	public String getHascard() {
		return hascard;
	}
	public void setHascard(String hascard) {
		this.hascard = hascard;
	}
	public String getChn_msk() {
		return chn_msk;
	}
	public void setChn_msk(String chn_msk) {
		this.chn_msk = chn_msk;
	}
	
	public String getProd_code() {
		return prod_code;
	}
	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	public int getWdl_lmt_amt() {
		return wdl_lmt_amt;
	}
	public void setWdl_lmt_amt(int wdl_lmt_amt) {
		this.wdl_lmt_amt = wdl_lmt_amt;
	}
	public int getWdl_lmt_cnt() {
		return wdl_lmt_cnt;
	}
	public void setWdl_lmt_cnt(int wdl_lmt_cnt) {
		this.wdl_lmt_cnt = wdl_lmt_cnt;
	}
	public int getPur_lmt_amt() {
		return pur_lmt_amt;
	}
	public void setPur_lmt_amt(int pur_lmt_amt) {
		this.pur_lmt_amt = pur_lmt_amt;
	}
	public int getPur_lmt_cnt() {
		return pur_lmt_cnt;
	}
	public void setPur_lmt_cnt(int pur_lmt_cnt) {
		this.pur_lmt_cnt = pur_lmt_cnt;
	}
	public String getLmt_based_on() {
		return lmt_based_on;
	}
	public void setLmt_based_on(String lmt_based_on) {
		this.lmt_based_on = lmt_based_on;
	}
	public String getEncrptchn() {
		return encrptchn;
	}
	public void setEncrptchn(String encrptchn) {
		this.encrptchn = encrptchn;
	}
	public String lmt_based_on;
	public String encrptchn;
	public String org_chn;
	public String getOrg_chn() {
		return org_chn;
	}
	public void setOrg_chn(String org_chn) {
		this.org_chn = org_chn;
	}
	public String productdesc;
	public String cardtypedesc;
	public String getProductdesc() {
		return productdesc;
	}
	public void setProductdesc(String productdesc) {
		this.productdesc = productdesc;
	}
	public String getCardtypedesc() {
		return cardtypedesc;
	}
	public void setCardtypedesc(String cardtypedesc) {
		this.cardtypedesc = cardtypedesc;
	}
	public String accttype;
	public String getAccttype() {
		return accttype;
	}
	public void setAccttype(String accttype) {
		this.accttype = accttype;
	}
	public Date expdate;
	public Date cardgendate;
	public String encodingname;
	public Date getExpdate() {
		return expdate;
	}
	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}
	 
	public void setEncodingname(String encodingname) {
		// TODO Auto-generated method stub
		
	}
	 
}