package com.ifp.personalize;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class DamageCardAction extends BaseAction 
{
	private static final long serialVersionUID = 1L;
	
	 
	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	public String comBranchId(){
		HttpSession session = getRequest().getSession();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		return br_id;
	}
	public String comuserType(){
		HttpSession session = getRequest().getSession();
		String usertype = (String)session.getAttribute("USERTYPE"); 
		return usertype;
	}
	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}
	
	
	private String act;
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}
	
	
	private List branchlist;
	
	
	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}


	private List personalproductlist;
	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}
		


	public String personalDamagecardpregenerationhome()
	{
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		
		String temp = act;
		System.out.println(temp);
		session.setAttribute("DMAGECARDPRE_ACT", act);
		String session_act = (String) session.getAttribute("DMAGECARDPRE_ACT");
		System.out.println("session_act " + session_act);
		
		System.out.println("The DATEFILTER_REQ===> "+(String)session.getAttribute("DATEFILTER_REQ"));
		try {
			System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
				System.out.println("Branch list "+br_list);
				if(!(br_list.isEmpty())){
					setBranchlist(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
				}
				else{
					setBranchlist(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					System.out.println("Branch List is empty ");
				}
			}
			pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}
			
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
			
		}

		return "damagecardhome";
	}
	
	private List damagecardslist;
	
	public List getDamagecardslist() {
		return damagecardslist;
	}

	public void setDamagecardslist(List damagecardslist) {
		this.damagecardslist = damagecardslist;
	}

	
	
	
	public String getDamagedcardslist()
	{
		HttpSession session = getRequest().getSession();
		
		//String pingentype = getRequest().getParameter("pingentype");
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");

		
		//System.out.println("branch==> "+branch+"   binno==>"+binno+"  fromdate===> "+fromdate+"  todate===>"+todate);
		String dateflag ="PIN_DATE";
		String inst_id=comInstId();

		
		String cardstatus = "02",mkckstatus="P",caf_rec_status="D";
		List authorizeorderlist = null;
		String session_act = (String) session.getAttribute("DMAGECARDPRE_ACT");
		System.out.println("session_act " + session_act);		
		try {
			
			String condition = commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("Condition Value----->  "+condition);
			
			
			authorizeorderlist = commondesc.maintenanceCardslist( inst_id,cardstatus, mkckstatus,caf_rec_status,condition, jdbctemplate);
			System.out.println("authorizeorderlist===> "+authorizeorderlist);
			if(!(authorizeorderlist.isEmpty())){
				setDamagecardslist(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				
			}else{
				setDamagecardslist(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg"," No Orders To Generate PRE");
				setAct((String) session.getAttribute("DMAGECARDPRE_ACT"));
				return personalDamagecardpregenerationhome();
			}
		} catch (Exception e) {
			System.out.println("Exception--->"+e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Error While Fetching The Orders for Damage Card PRE  ,ERROR:"+e.getMessage());
			
		}
		
		return "damagedcardslist";
		
	}

	
	public String damageCardpregeneration()
	{
		HttpSession session = getRequest().getSession();
		String cardnum[] = getRequest().getParameterValues("personalrefnum");
		String Trk1_StartSentinel = "%";
		String Trk1_Separator = "^";
		String Trk1_EndSentinel = "?";
		String Trk1_Formatecode = "B";
		String Trk1_padchar = "/";
		String Trk2_StartSentinel = ";";
		String Trk2_Separator = "=";
		String Trk2_EndSentinel = "?";
		String Trk2_descrydata = "0000000000";
		String Discry_data = "000000000000000000000";;
		String longtitude_reduncy = "0";
 		String instid = comInstId();
		String userid = comUserId();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		IfpTransObj transact = commondesc.myTranObject("DAMAGE", txManager);
		
		String table = "PERS_CARD_PROCESS";
		int status = 0,processstatus = 0,allcards_pre = 0;
		String errormsg = "";
		
		
		String bin = getRequest().getParameter("binno");
		String prename = bin+"_"+userid+"_"+dateFormat.format(date);
		System.out.println("prename#####====> "+prename);
		
		String makerid = "",checkerid="",makerdate="",checkerdate="",mkckstatus="";
		String actiontype = (String) session.getAttribute("DMAGECARDPRE_ACT");
		System.out.println("Action Type is ==>"+actiontype);
		String updatequry = "";
		if(actiontype.equals("M")){
			makerid = userid;
			makerdate = "(sysdate)";
			mkckstatus = "M";
			updatequry = "UPDATE PERS_CARD_PROCESS SET CARD_STATUS='03',PRE_DATE=(sysdate),PRE_FILE='"+prename+"',MAKER_ID='"+makerid+"',MAKER_DATE= "+makerdate+", MKCK_STATUS = '"+mkckstatus+"' ";
			
		}else if(actiontype.equals("D")){
			makerid = userid;
			checkerid = userid;
			makerdate = "(sysdate)";
			checkerdate = "(sysdate)";
			mkckstatus = "P";
			updatequry = "UPDATE PERS_CARD_PROCESS SET CARD_STATUS='03',PRE_DATE=(sysdate),PRE_FILE='"+prename+"', MAKER_ID='"+makerid+"',MAKER_DATE= "+makerdate+", CHECKER_ID='"+checkerid+"',CHECKER_DATE="+checkerdate+",MKCK_STATUS = '"+mkckstatus+"' ";
		}	
		
		
		try{
			for(int i=0;i<cardnum.length;i++)
			{
				allcards_pre = allcards_pre + 1;
				//String ordertype = "M";
				System.out.println("The CardS Selected is ====> "+cardnum[i]);
				List carddetails = null,cvvdata = null;
				String cafstatus = commondesc.gettingCAFstatus(cardnum[i],"", instid, jdbctemplate);
				/*if(cafstatus.equals("S"))
				{
					ordertype = "P";
				}*/
				System.out.println("cafstatus===> "+cafstatus);
				String cardno="X",orderrefnum="X",branch="X",expiry_1="X",enc_name="X",expiry_2="X",srv_code="X",cvv1="X",cvv2="X";
				carddetails = commondesc.getCarddetails(table, instid, cardnum[i],"Z","Z",null, jdbctemplate);
				if(!(carddetails.isEmpty()))
				{
					Iterator crdItr = carddetails.iterator();
					while(crdItr.hasNext())    
					{
						Map crdmap = (Map)crdItr.next();
						cardno = ((String)crdmap.get("CARD_NO"));
						orderrefnum = ((String)crdmap.get("ORDER_REF_NO"));
						branch = ((String)crdmap.get("BRANCH_CODE"));
						expiry_1 = ((String)crdmap.get("EXP_1"));
						enc_name = ((String)crdmap.get("ENCNAME"));
						expiry_2 = ((String)crdmap.get("EXP_2"));
						srv_code = ((String)crdmap.get("SRVCODE"));
						cvv1 = ((String)crdmap.get("CVV1"));
						cvv2  = ((String)crdmap.get("CVV2"));
					}
					//System.out.println("cardnum====>"+cardnum+" expiry_1===>"+expiry_1+" enc_name===>"+enc_name+" expiry_2===>"+expiry_2+" srv_code===>"+srv_code);
				}
				else{
					System.out.println("No Card Details Found ");
					errormsg = " No Card Details Found ";
					status = -1;
					break;
				}
				/*cvvdata = commondesc.getCVVdata(instid, cardnum[i]);
				if(!(cvvdata.isEmpty()))
				{
					Iterator cvvItr = cvvdata.iterator();
					while(cvvItr.hasNext())
					{
						Map cvvmap = (Map)cvvItr.next();
						cvv1 = ((String)cvvmap.get("CVV1"));
						cvv2  = ((String)cvvmap.get("CVV2"));
					}
					//System.out.println("CVV1====>"+cvv1+" CVV2=====>"+cvv2);
					if(cvv1 == null)
					{
						cvv1 ="";
					}if(cvv2 == null)
					{
						cvv2 = "";
					}
				}
				else{
					System.out.println("No CVV Details Found  ");
					status = -2;
					errormsg = " No CVV Details Found ";
					break;
				}
				*/
				String encode_name = commondesc.formateEncodingname(enc_name);
				//System.out.println("The Encoding Data====>"+encode_name+"Formated");
				
				String printdata = cardnum[i]+expiry_1+encode_name;
				//System.out.println("printdata===>"+printdata+"\n Length===>"+printdata.length());
				String predata = cvv2+Trk1_StartSentinel+Trk1_Formatecode+cardnum[i]+Trk1_Separator+encode_name;
				//System.out.println("predata===>"+predata+"\n Lenght ===>"+predata.length());
				String track1 = Trk1_padchar+Trk1_Separator+expiry_2+srv_code+cvv1+Discry_data+Trk1_EndSentinel+longtitude_reduncy;
				//System.out.println("track1===> "+track1+"\n Lenght ====>"+track1.length());
				String track2 = Trk2_StartSentinel+cardnum[i]+Trk2_Separator+expiry_2+srv_code+Trk2_descrydata+Trk2_EndSentinel+longtitude_reduncy;
				//System.out.println("track2===>"+track2+"\n Lenght====>"+track2.length());
				String prerecord = printdata+predata+track1+track2;
				System.out.println(prerecord);
				//String bin = cardnum[i].substring(0, 6);
				System.out.println("BIN Is ===> "+bin);
				String preinst_qury = "INSERT INTO PERS_PRE_DATA (INST_ID,PRODUCT_CODE,CARD_NO,ORDER_REF_NO,BRANCH_CODE,GENERATED_DATE,PRE_NAME,TRACK_DATA,USER_CODE) VALUES " +
						"('"+instid+"','"+bin+"','"+cardnum[i]+"','"+orderrefnum+"','"+branch+"',(sysdate),'"+prename+"','"+prerecord+"','"+userid+"')";
				
				System.out.println("preinst_qury  ==> "+preinst_qury);
	
				String update_cardstatus = "update PERS_CARD_PROCESS set card_status='03',PRE_DATE=(sysdate),PRE_FILE='"+prename+"',MAKER_ID='"+userid+"',MAKER_DATE=(sysdate) where inst_id='"+instid+"' and card_no='"+cardnum[i]+"'";
				
				//String update_cardstatus =updatequry+" WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardnum[i]+"'";
				System.out.println("update_qury===>"+update_cardstatus);
				
				int insrt_status = commondesc.executeTransaction(preinst_qury, jdbctemplate);
				int update_status = commondesc.executeTransaction(update_cardstatus, jdbctemplate);
				if(insrt_status == 1 && update_status == 1)
				{
					processstatus = processstatus + 1;
				}else{
					status = -3;
					errormsg = " Error While Insert The PRE Data ";
					break;
				}
			}//For Ends
		}
		catch (Exception e) 
		{
			errormsg = e.getMessage();
		}
		if(allcards_pre == processstatus)
		{
			System.out.println(" PRE GENERATED SUCCESSFULLY CAN COMMIT HERE");
			//commondesc.commitTxn(jdbctemplate);
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", " PRE Generated Successfully ");
		}
		else
		{
			System.out.println(" PRE GENERATION FAILED ROLLBACK HERE");
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "PRE Generation Failed "+errormsg);
		}
		
		setAct((String) session.getAttribute("DMAGECARDPRE_ACT"));
		return personalDamagecardpregenerationhome();
	}
	
}
