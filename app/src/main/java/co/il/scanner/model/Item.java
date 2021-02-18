package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class Item{

	@SerializedName("code")
	private Object code;

	public void setComments(Object comments) {
		this.comments = comments;
	}

	public void setImageUrl(Object imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setDescription(Object description) {
		this.description = description;
	}

	public void setWarhouseDescription(Object warhouseDescription) {
		this.warhouseDescription = warhouseDescription;
	}

	public void setBarcode2(String barcode2) {
		this.barcode2 = barcode2;
	}

	public void setBarcode1(String barcode1) {
		this.barcode1 = barcode1;
	}

	public void setForeignId(Object foreignId) {
		this.foreignId = foreignId;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setCategoryId(Object categoryId) {
		this.categoryId = categoryId;
	}

	public void setPrice(Object price) {
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@SerializedName("comments")
	private Object comments;

	@SerializedName("image_url")
	private Object imageUrl;

	@SerializedName("description")
	private Object description;

	@SerializedName("warhouse_description")
	private Object warhouseDescription;

	@SerializedName("barcode2")
	private String barcode2;

	@SerializedName("barcode1")
	private String barcode1;

	@SerializedName("foreign_id")
	private Object foreignId;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("deleted")
	private boolean deleted;

	@SerializedName("category_id")
	private Object categoryId;

	@SerializedName("price")
	private Object price;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedAt")
	private String updatedAt;

	public Object getCode(){
		return code;
	}

	public Object getComments(){
		return comments;
	}

	public Object getImageUrl(){
		return imageUrl;
	}

	public Object getDescription(){
		return description;
	}

	public Object getWarhouseDescription(){
		return warhouseDescription;
	}

	public String getBarcode2(){
		return barcode2;
	}

	public String getBarcode1(){
		return barcode1;
	}

	public Object getForeignId(){
		return foreignId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public boolean isDeleted(){
		return deleted;
	}

	public Object getCategoryId(){
		return categoryId;
	}

	public Object getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}
}