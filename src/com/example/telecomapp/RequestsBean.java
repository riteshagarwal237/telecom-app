package com.example.telecomapp;

public class RequestsBean {
	private String name = "";
	private String plan = "";
	private String accnt_no = "";
	private String type = "";
	private String billDate = "";
	private String billAmount = "";

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getAccnt_no() {
		return accnt_no;
	}

	public void setAccnt_no(String accnt_no) {
		this.accnt_no = accnt_no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

}
