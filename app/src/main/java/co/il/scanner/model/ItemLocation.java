package co.il.scanner.model;

import com.google.gson.annotations.SerializedName;

public class ItemLocation{

	@SerializedName("item_id")
	private int itemId;

	@SerializedName("catalog_number")
	private int catalogNumber;

	@SerializedName("location")
	private int location;

	@SerializedName("id")
	private int id;

	public int getItemId(){
		return itemId;
	}

	public int getCatalogNumber(){
		return catalogNumber;
	}

	public int getLocation(){
		return location;
	}

	public int getId(){
		return id;
	}
}