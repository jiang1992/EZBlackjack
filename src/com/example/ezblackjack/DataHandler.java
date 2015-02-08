package com.example.ezblackjack;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DataHandler {
	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String MONEY = "money"; //*
	
	public static final String TABLE_NAME = "mytable";

	public static final String DATA_BASE_NAME = "mydatabase";

	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_CREATE = "create table mytable (name TEXT PRIMARY KEY not  null, email TEXT not null, money INT not null);";

	public static final String TABLE_CLEAR = "DELETE * from mytable;";
	
	
	DataBaseHelper dbhelper;
	Context ctx;
	SQLiteDatabase db;
	public DataHandler( Context ctx){
		
		this.ctx = ctx;
		dbhelper = new DataBaseHelper(ctx);
		
	}
	
	public DataHandler open(){
		db = dbhelper.getWritableDatabase();
		return this;
	}
	
	public void clear(){
		
		try{
			db.execSQL(TABLE_CLEAR);
		}catch(SQLException e){
			e.printStackTrace(); 
		}
		
	}
	private static class DataBaseHelper extends SQLiteOpenHelper{

	
		public DataBaseHelper(Context ctx){
			
			super(ctx, DATA_BASE_NAME, null, DATABASE_VERSION);
			
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//creating the table
			try{
				db.execSQL(TABLE_CREATE);
			}catch(SQLException e){
				e.printStackTrace(); 
			}
		}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS mytable ");
			onCreate(db);
		}
	}

	
	public void close(){
		dbhelper.close();
	}
	
	public long insertData(String name, String email, int moneyAmount){
		ContentValues content = new ContentValues();
		content.put(NAME, name);
		content.put(EMAIL, email);
		content.put(MONEY, moneyAmount);
		
		return db.insertOrThrow(TABLE_NAME, null, content);
	}
	public int updateData(String userName, int newMoney){
		
	    ContentValues content = new ContentValues();
	    content.put(MONEY, newMoney);
	    
	    db.update(TABLE_NAME, content, NAME + "= '" + userName + "'", null);
	    return 0;
	//    content.put(, newValue);
	 //   db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null);
	//	return updateString;
	}
	
	public List<String> getAllUsers(){
		
		List<String> user_list = new ArrayList<String>();
		
		Cursor c = db.rawQuery("SELECT * FROM mytable", null);
		
		System.out.println("Getting foods " + String.valueOf(c.getCount()));
		c.moveToFirst();
		int i = 1;
		while(!c.isAfterLast()){

			String food = c.getString(0);
			String money = Integer.toString(c.getInt(2));
			
			money = padding(money);
			
			String user = money + ":  " + food;
			user_list.add(user);
			c.moveToNext();
			i++;
		}
		
		return user_list;
	} 
	private String padding(String string){
		
		int length = string.length();
		if(length < 12){
			
			for(int i = 0; i < 12-length; i++){
				string = string + " ";
			}
			
		}
		return string;
		
	}
	public Cursor returnData(){	
		return db.query(TABLE_NAME, new String[] {NAME,EMAIL, MONEY},null,null,null,null,null);
	}
}
