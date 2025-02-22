package com.learning.rest.webservices.restfulwebservices.versioning;

public class Name {
	
	private String fristName;
	private String lastName;
	public Name(String fristName, String lastName) {
		super();
		this.fristName = fristName;
		this.lastName = lastName;
	}
	public String getFristName() {
		return fristName;
	}
	public void setFristName(String fristName) {
		this.fristName = fristName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String toString() {
		return "Name [fristName=" + fristName + ", lastName=" + lastName + "]";
	}
	
	

}
