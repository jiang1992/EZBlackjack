package com.example.ezblackjack;

//package com.example.ezblackjack;

import java.util.Scanner;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlayGame extends Activity {
	
	Deck deck;       
	Card newCard;
    BlackjackHand dealerHand;   
    BlackjackHand userHand;
    String cHand = " ";
    String dHand = " ";
    int money;
    int bet = 0;
    
    EditText mBet;
    TextView house;
    TextView player;
    TextView dollers;
    TextView counsol;
    TextView score;
    
    DataHandler handler;
    String getName = "";
    int getMoney = 0;
    GlobalController gc;
    String user = gc.getUser();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_game);
		
		mBet = (EditText)findViewById(R.id.editText1);
	    house = (TextView)findViewById(R.id.houseCards);
	    player = (TextView)findViewById(R.id.playerCards);
	    dollers = (TextView)findViewById(R.id.funds);
	    counsol = (TextView)findViewById(R.id.adviceGet);
	    score = (TextView)findViewById(R.id.textScore);
	    
	    handler = new DataHandler(getBaseContext()); 
		(handler).open();
		Cursor C = handler.returnData();
		if(C.moveToFirst()){
			do{
				getName = C.getString(0);
				getMoney = C.getInt(2);
				if(getName.equals(user)){
					money = getMoney;				
					break;
				}														
			}while(C.moveToNext());
		}			
		handler.close();
	    
		money = getMoney;	
	    dollers.setText( "Hello! " + user + " : $" + money);
		try{
		setupActionBar();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public void placeBet(View v) {
		/*Intent intent = new Intent(this, LeaderBoards.class);
		startActivity(intent);*/
        dollers.setText("$" + money);
        try{
        bet = Integer.parseInt(mBet.getText().toString());
        }catch(Exception e){
        	return;
        }
        
        
        if(money <= 0){
        	AlertDialog.Builder funder = new AlertDialog.Builder(this);
        	funder.setMessage("You do not have anymore funds, do you want to try again?")
        	       .setTitle("No Funds!");
         	funder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
               // 	money = 100;
                	dollers.setText("$" + money);
                   return;
                }
            });
         	funder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   finish();
                }
            });
        	AlertDialog dialog = funder.create();
        	funder.show();
        }
        
        if(bet< 0 || bet > money){
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("Check how much you're betting!")
        	       .setTitle("Not Valid");
         	builder.setPositiveButton("Whoops", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   return;
                }
            });
        	AlertDialog dialog = builder.create();
        	dialog.show();
        }
        playBlackJack();
        //set everything to a certain state
	}
	
	//then the user can see their cards here
	private void playBlackJack(){
        
        deck = new Deck();
        dealerHand = new BlackjackHand();
        userHand = new BlackjackHand();
        cHand = " ";
        dHand = " ";
  
        /*  Shuffle the deck, then deal two cards to each player. */
        
        deck.shuffle();
        dealerHand.addCard( deck.dealCard() );
        dealerHand.addCard( deck.dealCard() );
        userHand.addCard( deck.dealCard() );
        userHand.addCard( deck.dealCard() );
        
        score.setText("" + userHand.getBlackjackValue());
        
        /* Check if one of the two have 21
        */
        
        if (dealerHand.getBlackjackValue() == 21) {
             dHand = dealerHand.getCard(0) + " " + dealerHand.getCard(1);
             cHand = userHand.getCard(0) + " " + userHand.getCard(1);
             house.setText(dHand);
             player.setText(cHand);
             score.setText("" + userHand.getBlackjackValue());
             money = money - bet;
             if (userHand.getBlackjackValue() < 17){
             	counsol.setText("Hint:Hit, live a little");
             }else{
             	counsol.setText("Hint:Stand, it's not worth it");
             }
          	AlertDialog.Builder builder = new AlertDialog.Builder(this);
          	builder.setMessage("The house won...")
          	       .setTitle("Results");
         	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	dollers.setText("$" + money);
                   return;
                }
            });
         	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	handler = new DataHandler(getBaseContext()); 
            		(handler).open();
            		handler.updateData(user, money);
            		handler.close();
                    finish();
                }
            });
          	AlertDialog dialog = builder.create();
          	dialog.show();
             //alert dialog
             //return false;
        }
        
        if (userHand.getBlackjackValue() == 21) {
             dHand = dealerHand.getCard(0) + " " + dealerHand.getCard(1);
             cHand = userHand.getCard(0) + " " + userHand.getCard(1);
             
             house.setText(dHand);
             player.setText(cHand);
             if (userHand.getBlackjackValue() < 17){
             	counsol.setText("Hint:Hit, live a little");
             }else{
             	counsol.setText("Hint:Stand, it's not worth it");
             }
            money = money + bet;
            dollers.setText("$" + money);
         	AlertDialog.Builder builder = new AlertDialog.Builder(this);
         	builder.setMessage("You won!")
         	       .setTitle("Results");
         	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   return;
                }
            });
         	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	handler = new DataHandler(getBaseContext()); 
            		(handler).open();
            		handler.updateData(user, money);
            		handler.close();
                    finish();
                }
            });
         	AlertDialog dialog = builder.create();
         	dialog.show();
             //return true;
        }
        
        if (userHand.getBlackjackValue() < 17){
        	counsol.setText("Hint:Hit, live a little");
        }else{
        	counsol.setText("Hint:Stand, it's not worth it");
        }
        
        //if both players do not have black jack play the game
  
        	String cHand = " ";
        	cHand = userHand.getCard(0) + " " + userHand.getCard(1);
             String dHand = "" + dealerHand.getCard(0);
             player.setText(cHand);
             house.setText(dHand);
     }

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public void sendHit(View view){
		//the user hit
		int temp = 0;
        try{
        temp = Integer.parseInt(mBet.getText().toString());
        }catch(Exception e){
        	return;
        }
        
        newCard = deck.dealCard();
        userHand.addCard(newCard);
        score.setText("" + userHand.getBlackjackValue());
        
        cHand = player.getText().toString() +" " + newCard;
        player.setText(cHand);
        //score is now
        
        if (userHand.getBlackjackValue() < 17){
        	counsol.setText("Hint:Hit, live a little");
        }else{
        	counsol.setText("Hint:Stand, it's not worth it");
        }
        
        if (userHand.getBlackjackValue() > 21) {
        	//alert dialog
         	money = money - bet;
         	dollers.setText("$" + money);
         	AlertDialog.Builder builder = new AlertDialog.Builder(this);
         	builder.setMessage("You busted...")
         	       .setTitle("Results");
         	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    return;
                }
            });
         	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	handler = new DataHandler(getBaseContext()); 
            		(handler).open();
            		handler.updateData(user, money);
            		handler.close();
                    finish();
                }
            });
         	AlertDialog dialog = builder.create();
         	dialog.show();
         	
         	//dollers.setText(money);
            //return false;  
        }
		
	}
	
	public void sendStand(View view){
		//sends the user to the post hit phase
		int temp = 0;
        try{
        temp = Integer.parseInt(mBet.getText().toString());
        }catch(Exception e){
        	return;
        }
		postUser();
	}
	
	public void postUser(){
     // the user folded

	 dHand = house.getText().toString();
	 dHand += " " + dealerHand.getCard(1);
	 house.setText(dHand);
	 
     while (dealerHand.getBlackjackValue() <= 16) {
        newCard = deck.dealCard();
        dealerHand.addCard(newCard);
        dHand += " " + newCard;
        house.setText(dHand);
        if (dealerHand.getBlackjackValue() > 21) {
           System.out.println("");
        	money = money + bet;
        	dollers.setText("$" + money);
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
         	builder.setMessage("The dealer won...")
         	       .setTitle("Results");
         	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   return;
                }
            });
         	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	handler = new DataHandler(getBaseContext()); 
            		(handler).open();
            		handler.updateData(user, money);
            		handler.close();
                    finish();
                }
            });
     // Set other dialog pr
         	AlertDialog dialog = builder.create();
         	dialog.show();
           //return true;
        }
     }
     /* both players folded */
     
     if (dealerHand.getBlackjackValue() == userHand.getBlackjackValue()) {
     	money = money - bet;
     	dollers.setText("$" + money);
     	AlertDialog.Builder builder = new AlertDialog.Builder(this);
     	builder.setMessage("The dealer tied...")
     	       .setTitle("Results");
     	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               return;
            }
        });
     	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	handler = new DataHandler(getBaseContext()); 
        		(handler).open();
        		handler.updateData(user, money);
        		handler.close();
                finish();
            }
        });
     	AlertDialog dialog = builder.create();
     	dialog.show();
        //return false;
     }
     else if (dealerHand.getBlackjackValue() > userHand.getBlackjackValue()) {
     	money = money - bet;
     	dollers.setText("$" + money);
     	AlertDialog.Builder builder = new AlertDialog.Builder(this);
     	builder.setMessage("The dealer won...")
     	       .setTitle("Results");
     	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               return;
            }
        });
     	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	handler = new DataHandler(getBaseContext()); 
        		(handler).open();
        		handler.updateData(user, money);
        		handler.close();
                finish();
            }
        });
     	AlertDialog dialog = builder.create();
     	dialog.show();
        //return false;
     }
     else {
      	money = money + bet;
      	dollers.setText("$" + money);
      	
     	AlertDialog.Builder builder = new AlertDialog.Builder(this);
     	builder.setMessage("You won")
     	       .setTitle("Results");
     	builder.setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               return;
            }
        });
     	builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	handler = new DataHandler(getBaseContext()); 
        		(handler).open();
        		handler.updateData(user, money);
        		handler.close();
                finish();
            }
        });
     	AlertDialog dialog = builder.create();
     	dialog.show();
        //return true;
     }

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_game, menu);
		return true;
	}

}
