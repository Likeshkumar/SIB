package com.ifd.personalize;

import java.util.List;

public class DebitCustRegBean {
	public String applicationid;
	public String customerid;
	public String firstname;
	public String middlename;
	public String lastname;
	public String encname;
	public String embname;
	public String nationality;
	public String dob;
	public String gender;
	public String mstatus;
	public String residence;
	public String vehicle;
	public String vehicleno;
	public String idproof;
	public String idproofno;
	public String dateofissue;
	public String districofissue;
	public String typeofcard;
	public String spousename;
	public String mothername;
	public String fathername;
	public String grandfathername;
	public String occupation;
	public String workfor;
	public String designation;
	public String natofbusiness;
	public String employedsince;
	public String stmtdelivery;
	public String smsalert;
	
	public String companyname;
	
	public String referenceid;
	public String referncename;
	public String referencephone;

	
	public String prpobox;
	public String prhouseno;
	public String prstreetname;
	public String prwardname;
	public String prcity;
	public String prdistrict;
	public String prphone1;
	public String prphone2;
	public String prfax;
	public String primaryemail;
	public String prmobileno;
	public String copobox;
	public String cohouseno;
	public String costreetname;
	public String cowardname;
	public String cocity;
	public String codistrict;
	public String cophone1;
	public String cophone2;
	public String cofax;
	public String secemail;
	public String comobileno;
	public String emppobox;
	public String emphouseno;
	public String empstreetname;
	public String empwardname;
	public String empcity;
	public String empdistrict;
	public String empphone1;
	public String empphone2;
	public String empfax;
	public String empemail;
	public String empmobileno;
	
	public String documenttype;
	public String documentnumber;
	public String annualsal;
	public String annualbonus;
	public String annualbusinessincome;
	public String rentalincome;
	public String agriculture;
	public String otherincome;
	public String totannualincome;
	
	public String existcreditcard;
	public String excrcardbank;
	public String excrcardbranch;
	public String excrcardacctype;
	 
	
	public String validfrom;
	public String validto;
	public String acctwithprimarybank;
	public String primarybankacctno;
	public String primarybankbranch;
	public String primaryacctcur;
	public String paytype;
	public String payableamt;
	public String payableday;
	
	public String supfirstname;
	public String supmidname;
	public String suplastname;
	public String supdob;
	public String supnationality;
	public String supnameoncard;
	public String supgender;
	public String suprelationship; 
	public String relationcode;
	public String relationdesc;
	public String status;
	
	public Boolean valid ; 
	public String gototab="1";
	public List listofyears ;
	
	public Boolean hasreference = false;
	public List reflist;
	public Boolean hasdocument = false;
	public List documentlist;
	public List configdocumentlist; 
	public List curmasterlist;
	public List creditmasterlimit;
	public List creditpaytype;
	public List accttypelist;
	public List supplimentrelationlist; 
	public List applicationlist; 
	public String addnew = "true"; 
	public List supplimentlist;
	public Boolean hassupliment;
	
	public String doact ;
	
	public Boolean hasmasterproducst;
	public Boolean hasproduct;
	public Boolean hassubpruct;
	public List productlist;
	public List subproductlist;
	public String productcode;
	public String subproductcode;
	
	public String reglevel;
	
	
	
	public String getReglevel() {
		return reglevel;
	}
	public void setReglevel(String reglevel) {
		this.reglevel = reglevel;
	}
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getSubproductcode() {
		return subproductcode;
	}
	public void setSubproductcode(String subproductcode) {
		this.subproductcode = subproductcode;
	}
	public Boolean getHasmasterproducst() {
		return hasmasterproducst;
	}
	public void setHasmasterproducst(Boolean hasmasterproducst) {
		this.hasmasterproducst = hasmasterproducst;
	}
	public Boolean getHasproduct() {
		return hasproduct;
	}
	public void setHasproduct(Boolean hasproduct) {
		this.hasproduct = hasproduct;
	}
	public Boolean getHassubpruct() {
		return hassubpruct;
	}
	public void setHassubpruct(Boolean hassubpruct) {
		this.hassubpruct = hassubpruct;
	}
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}
	public List getSubproductlist() {
		return subproductlist;
	}
	public void setSubproductlist(List subproductlist) {
		this.subproductlist = subproductlist;
	}
	public String getDoact() {
		return doact;
	}
	public void setDoact(String doact) {
		this.doact = doact;
	}
	public Boolean getHassupliment() {
		return hassupliment;
	}
	public void setHassupliment(Boolean hassupliment) {
		this.hassupliment = hassupliment;
	}
	public List getSupplimentlist() {
		return supplimentlist;
	}
	public void setSupplimentlist(List supplimentlist) {
		this.supplimentlist = supplimentlist;
	}
	public String getAddnew() {
		return addnew;
	}
	public void setAddnew(String addnew) {
		this.addnew = addnew;
	}
	public List getApplicationlist() {
		return applicationlist;
	}
	public void setApplicationlist(List applicationlist) {
		this.applicationlist = applicationlist;
	}
	public String getApplicationid() {
		return applicationid;
	}
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}
	public List getSupplimentrelationlist() {
		return supplimentrelationlist;
	}
	public void setSupplimentrelationlist(List supplimentrelationlist) {
		this.supplimentrelationlist = supplimentrelationlist;
	}
	public List getAccttypelist() {
		return accttypelist;
	}
	public void setAccttypelist(List accttypelist) {
		this.accttypelist = accttypelist;
	}
	public List getCreditpaytype() {
		return creditpaytype;
	}
	public void setCreditpaytype(List creditpaytype) {
		this.creditpaytype = creditpaytype;
	}
	 
	public List getCreditmasterlimit() {
		return creditmasterlimit;
	}
	public void setCreditmasterlimit(List creditmasterlimit) {
		this.creditmasterlimit = creditmasterlimit;
	}
	public List getCurmasterlist() {
		return curmasterlist;
	}
	public void setCurmasterlist(List curmasterlist) {
		this.curmasterlist = curmasterlist;
	}
	public List getConfigdocumentlist() {
		return configdocumentlist;
	}
	public void setConfigdocumentlist(List configdocumentlist) {
		this.configdocumentlist = configdocumentlist;
	}
	public Boolean getHasdocument() {
		return hasdocument;
	}
	public void setHasdocument(Boolean hasdocument) {
		this.hasdocument = hasdocument;
	}
	public List getDocumentlist() {
		return documentlist;
	}
	public void setDocumentlist(List documentlist) {
		this.documentlist = documentlist;
	}
	public List getReflist() {
		return reflist;
	}
	public void setReflist(List reflist) {
		this.reflist = reflist;
	}
	public Boolean getHasreference() {
		return hasreference;
	}
	public void setHasreference(Boolean hasreference) {
		this.hasreference = hasreference;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public List getListofyears() {
		return listofyears;
	}
	public void setListofyears(List listofyears) {
		this.listofyears = listofyears;
	}
	public String getGototab() {
		return gototab;
	}
	public void setGototab(String gototab) {
		this.gototab = gototab;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEncname() {
		return encname;
	}
	public void setEncname(String encname) {
		this.encname = encname;
	}
	public String getEmbname() {
		return embname;
	}
	public void setEmbname(String embname) {
		this.embname = embname;
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
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getIdproof() {
		return idproof;
	}
	public void setIdproof(String idproof) {
		this.idproof = idproof;
	}
	public String getIdproofno() {
		return idproofno;
	}
	public void setIdproofno(String idproofno) {
		this.idproofno = idproofno;
	}
	public String getDateofissue() {
		return dateofissue;
	}
	public void setDateofissue(String dateofissue) {
		this.dateofissue = dateofissue;
	}
	public String getDistricofissue() {
		return districofissue;
	}
	public void setDistricofissue(String districofissue) {
		this.districofissue = districofissue;
	}
	public String getTypeofcard() {
		return typeofcard;
	}
	public void setTypeofcard(String typeofcard) {
		this.typeofcard = typeofcard;
	}
	public String getSpousename() {
		return spousename;
	}
	public void setSpousename(String spousename) {
		this.spousename = spousename;
	}
	public String getMothername() {
		return mothername;
	}
	public void setMothername(String mothername) {
		this.mothername = mothername;
	}
	public String getFathername() {
		return fathername;
	}
	public void setFathername(String fathername) {
		this.fathername = fathername;
	}
	public String getGrandfathername() {
		return grandfathername;
	}
	public void setGrandfathername(String grandfathername) {
		this.grandfathername = grandfathername;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getWorkfor() {
		return workfor;
	}
	public void setWorkfor(String workfor) {
		this.workfor = workfor;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getNatofbusiness() {
		return natofbusiness;
	}
	public void setNatofbusiness(String natofbusiness) {
		this.natofbusiness = natofbusiness;
	}
	public String getEmployedsince() {
		return employedsince;
	}
	public void setEmployedsince(String employedsince) {
		this.employedsince = employedsince;
	}
	public String getStmtdelivery() {
		return stmtdelivery;
	}
	public void setStmtdelivery(String stmtdelivery) {
		this.stmtdelivery = stmtdelivery;
	}
	public String getSmsalert() {
		return smsalert;
	}
	public void setSmsalert(String smsalert) {
		this.smsalert = smsalert;
	}
	public String getReferenceid() {
		return referenceid;
	}
	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}
	 
	public String getReferncename() {
		return referncename;
	}
	public void setReferncename(String referncename) {
		this.referncename = referncename;
	}
	public String getReferencephone() {
		return referencephone;
	}
	public void setReferencephone(String referencephone) {
		this.referencephone = referencephone;
	}
	public String getPrpobox() {
		return prpobox;
	}
	public void setPrpobox(String prpobox) {
		this.prpobox = prpobox;
	}
	public String getPrhouseno() {
		return prhouseno;
	}
	public void setPrhouseno(String prhouseno) {
		this.prhouseno = prhouseno;
	}
	public String getPrstreetname() {
		return prstreetname;
	}
	public void setPrstreetname(String prstreetname) {
		this.prstreetname = prstreetname;
	}
	public String getPrwardname() {
		return prwardname;
	}
	public void setPrwardname(String prwardname) {
		this.prwardname = prwardname;
	}
	public String getPrcity() {
		return prcity;
	}
	public void setPrcity(String prcity) {
		this.prcity = prcity;
	}
	public String getPrdistrict() {
		return prdistrict;
	}
	public void setPrdistrict(String prdistrict) {
		this.prdistrict = prdistrict;
	}
	public String getPrphone1() {
		return prphone1;
	}
	public void setPrphone1(String prphone1) {
		this.prphone1 = prphone1;
	}
	public String getPrphone2() {
		return prphone2;
	}
	public void setPrphone2(String prphone2) {
		this.prphone2 = prphone2;
	}
	public String getPrfax() {
		return prfax;
	}
	public void setPrfax(String prfax) {
		this.prfax = prfax;
	}
	public String getPrimaryemail() {
		return primaryemail;
	}
	public void setPrimaryemail(String primaryemail) {
		this.primaryemail = primaryemail;
	}
	public String getPrmobileno() {
		return prmobileno;
	}
	public void setPrmobileno(String prmobileno) {
		this.prmobileno = prmobileno;
	}
	public String getCopobox() {
		return copobox;
	}
	public void setCopobox(String copobox) {
		this.copobox = copobox;
	}
	public String getCohouseno() {
		return cohouseno;
	}
	public void setCohouseno(String cohouseno) {
		this.cohouseno = cohouseno;
	}
	public String getCostreetname() {
		return costreetname;
	}
	public void setCostreetname(String costreetname) {
		this.costreetname = costreetname;
	}
	public String getCowardname() {
		return cowardname;
	}
	public void setCowardname(String cowardname) {
		this.cowardname = cowardname;
	}
	public String getCocity() {
		return cocity;
	}
	public void setCocity(String cocity) {
		this.cocity = cocity;
	}
	public String getCodistrict() {
		return codistrict;
	}
	public void setCodistrict(String codistrict) {
		this.codistrict = codistrict;
	}
	public String getCophone1() {
		return cophone1;
	}
	public void setCophone1(String cophone1) {
		this.cophone1 = cophone1;
	}
	public String getCophone2() {
		return cophone2;
	}
	public void setCophone2(String cophone2) {
		this.cophone2 = cophone2;
	}
	public String getCofax() {
		return cofax;
	}
	public void setCofax(String cofax) {
		this.cofax = cofax;
	}
	public String getSecemail() {
		return secemail;
	}
	public void setSecemail(String secemail) {
		this.secemail = secemail;
	}
	public String getComobileno() {
		return comobileno;
	}
	public void setComobileno(String comobileno) {
		this.comobileno = comobileno;
	}
	public String getEmppobox() {
		return emppobox;
	}
	public void setEmppobox(String emppobox) {
		this.emppobox = emppobox;
	}
	public String getEmphouseno() {
		return emphouseno;
	}
	public void setEmphouseno(String emphouseno) {
		this.emphouseno = emphouseno;
	}
	public String getEmpstreetname() {
		return empstreetname;
	}
	public void setEmpstreetname(String empstreetname) {
		this.empstreetname = empstreetname;
	}
	public String getEmpwardname() {
		return empwardname;
	}
	public void setEmpwardname(String empwardname) {
		this.empwardname = empwardname;
	}
	public String getEmpcity() {
		return empcity;
	}
	public void setEmpcity(String empcity) {
		this.empcity = empcity;
	}
	public String getEmpdistrict() {
		return empdistrict;
	}
	public void setEmpdistrict(String empdistrict) {
		this.empdistrict = empdistrict;
	}
	public String getEmpphone1() {
		return empphone1;
	}
	public void setEmpphone1(String empphone1) {
		this.empphone1 = empphone1;
	}
	public String getEmpphone2() {
		return empphone2;
	}
	public void setEmpphone2(String empphone2) {
		this.empphone2 = empphone2;
	}
	public String getEmpfax() {
		return empfax;
	}
	public void setEmpfax(String empfax) {
		this.empfax = empfax;
	}
	public String getEmpemail() {
		return empemail;
	}
	public void setEmpemail(String empemail) {
		this.empemail = empemail;
	}
	public String getEmpmobileno() {
		return empmobileno;
	}
	public void setEmpmobileno(String empmobileno) {
		this.empmobileno = empmobileno;
	}
	public String getDocumenttype() {
		return documenttype;
	}
	public void setDocumenttype(String documenttype) {
		this.documenttype = documenttype;
	}
	public String getDocumentnumber() {
		return documentnumber;
	}
	public void setDocumentnumber(String documentnumber) {
		this.documentnumber = documentnumber;
	}
	public String getAnnualsal() {
		return annualsal;
	}
	public void setAnnualsal(String annualsal) {
		this.annualsal = annualsal;
	}
	public String getAnnualbonus() {
		return annualbonus;
	}
	public void setAnnualbonus(String annualbonus) {
		this.annualbonus = annualbonus;
	}
	public String getAnnualbusinessincome() {
		return annualbusinessincome;
	}
	public void setAnnualbusinessincome(String annualbusinessincome) {
		this.annualbusinessincome = annualbusinessincome;
	}
	public String getRentalincome() {
		return rentalincome;
	}
	public void setRentalincome(String rentalincome) {
		this.rentalincome = rentalincome;
	}
	public String getAgriculture() {
		return agriculture;
	}
	public void setAgriculture(String agriculture) {
		this.agriculture = agriculture;
	}
	public String getOtherincome() {
		return otherincome;
	}
	public void setOtherincome(String otherincome) {
		this.otherincome = otherincome;
	}
	public String getTotannualincome() {
		return totannualincome;
	}
	public void setTotannualincome(String totannualincome) {
		this.totannualincome = totannualincome;
	}
	public String getExistcreditcard() {
		return existcreditcard;
	}
	public void setExistcreditcard(String existcreditcard) {
		this.existcreditcard = existcreditcard;
	}
	 
	 
	public String getAcctwithprimarybank() {
		return acctwithprimarybank;
	}
	public void setAcctwithprimarybank(String acctwithprimarybank) {
		this.acctwithprimarybank = acctwithprimarybank;
	}
	public String getPrimarybankacctno() {
		return primarybankacctno;
	}
	public void setPrimarybankacctno(String primarybankacctno) {
		this.primarybankacctno = primarybankacctno;
	}
	public String getPrimarybankbranch() {
		return primarybankbranch;
	}
	public void setPrimarybankbranch(String primarybankbranch) {
		this.primarybankbranch = primarybankbranch;
	}
	public String getValidfrom() {
		return validfrom;
	}
	public void setValidfrom(String validfrom) {
		this.validfrom = validfrom;
	}
	public String getValidto() {
		return validto;
	}
	public void setValidto(String validto) {
		this.validto = validto;
	}  
	public String getExcrcardbank() {
		return excrcardbank;
	}
	public void setExcrcardbank(String excrcardbank) {
		this.excrcardbank = excrcardbank;
	}
	public String getExcrcardbranch() {
		return excrcardbranch;
	}
	public void setExcrcardbranch(String excrcardbranch) {
		this.excrcardbranch = excrcardbranch;
	}
	public String getExcrcardacctype() {
		return excrcardacctype;
	}
	public void setExcrcardacctype(String excrcardacctype) {
		this.excrcardacctype = excrcardacctype;
	}
	public String getPrimaryacctcur() {
		return primaryacctcur;
	}
	public void setPrimaryacctcur(String primaryacctcur) {
		this.primaryacctcur = primaryacctcur;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getPayableamt() {
		return payableamt;
	}
	public void setPayableamt(String payableamt) {
		this.payableamt = payableamt;
	}
	public String getPayableday() {
		return payableday;
	}
	public void setPayableday(String payableday) {
		this.payableday = payableday;
	}
	public String getSupfirstname() {
		return supfirstname;
	}
	public void setSupfirstname(String supfirstname) {
		this.supfirstname = supfirstname;
	}
	public String getSupmidname() {
		return supmidname;
	}
	public void setSupmidname(String supmidname) {
		this.supmidname = supmidname;
	}
	public String getSuplastname() {
		return suplastname;
	}
	public void setSuplastname(String suplastname) {
		this.suplastname = suplastname;
	}
	 
	public String getSupdob() {
		return supdob;
	}
	public void setSupdob(String supdob) {
		this.supdob = supdob;
	}
	public String getSupnationality() {
		return supnationality;
	}
	public void setSupnationality(String supnationality) {
		this.supnationality = supnationality;
	}
	public String getSupnameoncard() {
		return supnameoncard;
	}
	public void setSupnameoncard(String supnameoncard) {
		this.supnameoncard = supnameoncard;
	}
	public String getSupgender() {
		return supgender;
	}
	public void setSupgender(String supgender) {
		this.supgender = supgender;
	}
	public String getSuprelationship() {
		return suprelationship;
	}
	public void setSuprelationship(String suprelationship) {
		this.suprelationship = suprelationship;
	}
	public String getRelationcode() {
		return relationcode;
	}
	public void setRelationcode(String relationcode) {
		this.relationcode = relationcode;
	}
	public String getRelationdesc() {
		return relationdesc;
	}
	public void setRelationdesc(String relationdesc) {
		this.relationdesc = relationdesc;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
	
}
