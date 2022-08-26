package com.ifp.personalize;

import java.util.List;

public class PersonalCardOrderBean 
{
	List branch_list;
	List personalproductlist;
	List nationlist;
	List questionlist;
	

	public List getNationlist() {
		return nationlist;
	}

	public void setNationlist(List nationlist) {
		this.nationlist = nationlist;
	}

	public List getQuestionlist() {
		return questionlist;
	}

	public void setQuestionlist(List questionlist) {
		this.questionlist = questionlist;
	}


	
	
	public List getBranch_list() {
		return branch_list;
	}

	public void setBranch_list(List branch_list) {
		this.branch_list = branch_list;
	}

	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}

	public void clearNationlist()
	{
		this.nationlist.clear();
	}
	public void clearQuestionlist()
	{
		this.questionlist.clear();
	}
}
