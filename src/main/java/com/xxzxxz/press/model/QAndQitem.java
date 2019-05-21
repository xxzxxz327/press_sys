package com.xxzxxz.press.model;

import java.util.List;

public class QAndQitem {

	private Question question;
	private List<Questionitem> Qitemlist;
	
	public Question getQuestion() {
		return question;
	}


	public void setQuestion(Question question) {
		this.question = question;
	}


	public List<Questionitem> getQitemlist() {
		return Qitemlist;
	}


	public void setQitemlist(List<Questionitem> qitemlist) {
		Qitemlist = qitemlist;
	}



	
	
	private void setByQAndQitem(Question question,List<Questionitem> Qitemlist)
	{
		this.question=question;
		this.Qitemlist=Qitemlist;
		
	}
	
	
}
