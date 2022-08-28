package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

public class Member{

	@SerializedName("apartment_number")
	private int apartmentNumber;

	@SerializedName("update_member_percent")
	private int updateMemberPercent;

	@SerializedName("json_draft")
	private String jsonDraft;

	@SerializedName("city")
	private String city;

	@SerializedName("suite_style")
	private String suiteStyle;

	@SerializedName("house_number")
	private String houseNumber;

	@SerializedName("wife_tz")
	private String wifeTz;

	@SerializedName("wife_phone_spam")
	private int wifePhoneSpam;

	@SerializedName("waived")
	private int waived;

	@SerializedName("female0to16credit")
	private int female0to16credit;

	@SerializedName("status_id")
	private int statusId;

	@SerializedName("hat_style")
	private String hatStyle;

	@SerializedName("branch_id")
	private int branchId;

	@SerializedName("street")
	private String street;

	@SerializedName("number_of_childern")
	private int numberOfChildern;

	@SerializedName("income_per_person")
	private int incomePerPerson;

	@SerializedName("id")
	private int id;

	@SerializedName("male0to13credit")
	private int male0to13credit;

	@SerializedName("email")
	private String email;

	@SerializedName("husband_first")
	private String husbandFirst;

	@SerializedName("husband_work")
	private String husbandWork;

	@SerializedName("husband_tz")
	private String husbandTz;

	@SerializedName("comments")
	private String comments;

	@SerializedName("number_of_married_childern")
	private int numberOfMarriedChildern;

	@SerializedName("last_form_login")
	private String lastFormLogin;

	@SerializedName("created")
	private String created;

	@SerializedName("correct_details")
	private int correctDetails;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("created_by_user_id")
	private int createdByUserId;

	@SerializedName("wife_first")
	private String wifeFirst;

	@SerializedName("husband_phone")
	private String husbandPhone;

	@SerializedName("marital_status")
	private String maritalStatus;

	@SerializedName("phone_spam")
	private int phoneSpam;

	@SerializedName("husband_phone_spam")
	private String husbandPhoneSpam;

	@SerializedName("acknowledge")
	private int acknowledge;

	@SerializedName("phone")
	private String phone;

	@SerializedName("organization_id")
	private int organizationId;

	@SerializedName("wife_phone")
	private String wifePhone;

	@SerializedName("neighborhood")
	private String neighborhood;

	@SerializedName("phone_id")
	private int phoneId;

	public int getApartmentNumber(){
		return apartmentNumber;
	}

	public int getUpdateMemberPercent(){
		return updateMemberPercent;
	}

	public String getJsonDraft(){
		return jsonDraft;
	}

	public String getCity(){
		return city;
	}

	public String getSuiteStyle(){
		return suiteStyle;
	}

	public String getHouseNumber(){
		return houseNumber;
	}

	public String getWifeTz(){
		return wifeTz;
	}

	public int getWifePhoneSpam(){
		return wifePhoneSpam;
	}

	public int getWaived(){
		return waived;
	}

	public int getFemale0to16credit(){
		return female0to16credit;
	}

	public int getStatusId(){
		return statusId;
	}

	public String getHatStyle(){
		return hatStyle;
	}

	public int getBranchId(){
		return branchId;
	}

	public String getStreet(){
		return street;
	}

	public int getNumberOfChildern(){
		return numberOfChildern;
	}

	public int getIncomePerPerson(){
		return incomePerPerson;
	}

	public int getId(){
		return id;
	}

	public int getMale0to13credit(){
		return male0to13credit;
	}

	public String getEmail(){
		return email;
	}

	public String getHusbandFirst(){
		return husbandFirst;
	}

	public String getHusbandWork(){
		return husbandWork;
	}

	public String getHusbandTz(){
		return husbandTz;
	}

	public String getComments(){
		return comments;
	}

	public int getNumberOfMarriedChildern(){
		return numberOfMarriedChildern;
	}

	public String getLastFormLogin(){
		return lastFormLogin;
	}

	public String getCreated(){
		return created;
	}

	public int getCorrectDetails(){
		return correctDetails;
	}

	public String getLastName(){
		return lastName;
	}

	public int getCreatedByUserId(){
		return createdByUserId;
	}

	public String getWifeFirst(){
		return wifeFirst;
	}

	public String getHusbandPhone(){
		return husbandPhone;
	}

	public String getMaritalStatus(){
		return maritalStatus;
	}

	public int getPhoneSpam(){
		return phoneSpam;
	}

	public String getHusbandPhoneSpam(){
		return husbandPhoneSpam;
	}

	public int getAcknowledge(){
		return acknowledge;
	}

	public String getPhone(){
		return phone;
	}

	public int getOrganizationId(){
		return organizationId;
	}

	public String getWifePhone(){
		return wifePhone;
	}

	public String getNeighborhood(){
		return neighborhood;
	}

	public int getPhoneId(){
		return phoneId;
	}
}