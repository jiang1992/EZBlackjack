package com.example.ezblackjack;



import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.example.ezblackjack.R;
import com.example.ezblackjack.R.layout;
import com.example.ezblackjack.R.menu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.TargetApi;
import android.os.Build;

public class LeaderBoards extends ListActivity {
	private static List<String> user_list = new ArrayList<String>();
	private static ArrayAdapter<String> list_adapter;
	private ListView list; 
	DataHandler handler;
	private EditText et;
	private int textlength;
	private List<String> array_sorted = new LinkedList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leader_boards);
		
		
		

		handler = new DataHandler(getBaseContext()); 
		(handler).open();
		user_list = handler.getAllUsers();
		
		list = (ListView) findViewById(android.R.id.list);
		Collections.sort(user_list);
		list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user_list);
		list.setAdapter(list_adapter);
		
		
		
		
		et = (EditText) findViewById(R.id.editText1);
		et.setHint("Search by User or Money");
		et.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				array_sorted.clear();
				textlength = et.getText().length();
				if( textlength == 0){
					list.setAdapter(new ArrayAdapter<String>(LeaderBoards.this,android.R.layout.simple_list_item_1, user_list));
				}
				else{
				for (int i = 0; i < user_list.size(); i++){
					int spacePos = user_list.get(i).indexOf(" ");
					//int subStringLen = food_list.get(i).length() - spacePos - 1;
					String segments[] =  user_list.get(i).split("-|\\ ");
					
					for(int j = 0; j < segments.length; j++){
						if (textlength <=  user_list.get(i).length() && textlength <= segments[j].length()){
							if(et.getText().toString().equalsIgnoreCase((String)segments[j].subSequence(0,textlength))){
								array_sorted.add( user_list.get(i)); break;
					        }
						}
				    }
				}
				list.setAdapter(new ArrayAdapter<String>(LeaderBoards.this,android.R.layout.simple_list_item_1, array_sorted));
			}
			}	
		});	
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leader_boards, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
