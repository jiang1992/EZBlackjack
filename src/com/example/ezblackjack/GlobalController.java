package com.example.ezblackjack;

public class GlobalController {
	
	static String CURRENT_USER = "";
	public GlobalController( String current_user){
		
		CURRENT_USER = current_user;
		
	}
	
	public static String getUser(){
		return CURRENT_USER;
	}
	
	
}
