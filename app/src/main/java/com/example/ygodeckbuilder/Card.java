package com.example.ygodeckbuilder;

import android.graphics.Bitmap;

public class Card {
	private String id;
	private Bitmap image;
	private Bitmap imageSmall;
	private String imageUrl      = "";
	private String imageUrlSmall = "";
	private String name          = "";
	private String type          = "";
	private String race          = "";

	// getters

	public String getId() {
		return id;
	}

	public Bitmap getImage() {
		return image;
	}

	public Bitmap getImageSmall() {
		return imageSmall;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getRace() {
		return race;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getImageUrlSmall() {
		return imageUrlSmall;
	}

	// setters

	public void setId(String id) {
		this.id = id;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void setImageSmall(Bitmap imageSmall) {
		this.imageSmall = imageSmall;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setImageUrlSmall(String imageUrlSmall) {
		this.imageUrlSmall = imageUrlSmall;
	}
}
