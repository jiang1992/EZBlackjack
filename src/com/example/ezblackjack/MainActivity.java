package com.example.ezblackjack;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private Socket client;
	private PrintWriter pw;
	private String message;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		sendLogin();
		
		SendMessage sendMessageTask = new SendMessage();
		sendMessageTask.execute();

		return true;
	}
	
	private class SendMessage extends AsyncTask<Void, Void, Void> {
		 
		@Override
		protected Void doInBackground(Void... params) {
				InetAddress serverAddr = null;
				try {
					System.out.println("-----------------------TRYING DATA>CS>PURDUE----------------------------------");
					serverAddr = InetAddress.getByName("data.cs.purdue.edu");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					System.out.println("TRYING IT");
					client = new Socket(serverAddr, 1044);
					System.out.println("CONNECTING?");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // connect to the server
				System.out.println("CONNECTING?");
				try {
					pw = new PrintWriter(client.getOutputStream(), true);
					System.out.println("SENT");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		    	Calendar cal = Calendar.getInstance();
		    	cal.getTime();
		    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				message = sdf.format(cal.getTime());
				
				pw.write(message); // write the message to output stream
				System.out.println(message);
				pw.flush();
				pw.close();
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // closing the connection
			return null;
		}
 
	}

	public void sendLogin() {
		  Intent intent = new Intent(this, LoginActivity.class);
		  startActivity(intent);
		}
	public void sendLeaders(View view){
		Intent intent = new Intent(this, LeaderBoards.class);
		startActivity(intent);
	}
	public void sendPlay(View view){
		Intent intent = new Intent(this, PlayGame.class);
		startActivity(intent);
	}
}
