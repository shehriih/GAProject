package com.gaproject.mmaer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {

	int id = 0;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "Name";	
	public static final String KEY_NUMBER = "Number";
	public static final String KEY_ISACTIVE = "isActive";
	
	public static final String KEY_MESSAGE= "Message";
	public static final String KEY_MESSAGESTAMP="MessageStamp";
	public static final String KEY_NUMBER_ARRAY="NumberArray";
	
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "database";
	private static final String PERSONNEL_TABLE = "tblPersonnel";
	private static final String MESSAGE_TABLE= "tblMessage";
	private static final String SETTING_TABLE= "tblSetting";
	
	private static final int DATABASE_VERSION = 1;

	
	//4 columns to keep personnel data
	//Column 0: Auto increment integer _id
	//Column 1 and 2: Personnel name and number respectively
	//Column 3: An integer field call isActive, which is 1 if the personnel is on the scene of emergency otherwise 0
	private static final String PERSONNEL_TABLE_CREATE =
		"create table tblPersonnel (_id integer primary key autoincrement, "
		+ "Name text not null, "
		+ "Number text not null, "
		+ "isActive integer default '0');";
	
	private static final String MESSAGE_TABLE_CREATE =
		"create table tblMessage (_id integer primary key autoincrement, "
		+ "Message text not null, "
		+ "MessageStamp text not null,"
		+ "NumberArray text);";
	
	private static final String SETTING_TABLE_CREAT =
		"create table tblSetting (_id integer primary key autoincrement, "
		+ "Key text not null, "
		+ "Value text not null);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(PERSONNEL_TABLE_CREATE);
			db.execSQL(MESSAGE_TABLE_CREATE);
			db.execSQL(SETTING_TABLE_CREAT);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion
					+ " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS tblPersonnel");
			db.execSQL("DROP TABLE IF EXISTS tblMessage");
			db.execSQL("DROP TABLE IF EXISTS tblSetting");
			onCreate(db);
		}
	}

	//---opens the database---
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}

	//---closes the database---
	public void close()
	{
		DBHelper.close();
	}
	
	
	//METHODS RELATED WITH THE PERSONNEL TABLE

	//---insert a personnel into the personnel table---
	public long insertPersonnel(String Name,String Number)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, Name);
		initialValues.put(KEY_NUMBER, Number);
		return db.insert(PERSONNEL_TABLE, null, initialValues);
	}

	//---get names for all personnel in the database---
	public String[] getAllNames()
	{
		ArrayList <String> arraylist=new ArrayList<String>();
		Cursor cursor = db.rawQuery(
				"SELECT Name FROM tblPersonnel", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			arraylist.add(cursor.getString(0));
			cursor.moveToNext();	
		}
		String []array = new String[arraylist.size()];
		arraylist.toArray(array);
		return array;
		
	}
	//---recreates tables/ doesn't populate them----
	public void resetTables()
	{
		DBHelper.onUpgrade(db,1,1);
	}
	
	//---sets isActive attribute of the personnel with given name to 1---
	public void activatePersonnel(String name)
	{
		db.execSQL("UPDATE tblPersonnel SET isActive='1' WHERE Name='"+ name+"'");
	}

	//---sets isActive attribute of the personnel with given name to 0---
	public void deactivatePersonnel(String name)
	{
		db.execSQL("UPDATE tblPersonnel SET isActive='0' WHERE Name='"+ name+"'");
	}
	
	//gets numbers of all personnel with isActive=1
	public String[] getAllActiveNumbers()
	{
		ArrayList <String> arraylist=new ArrayList<String>();
		Cursor cursor = db.rawQuery("SELECT Number FROM tblPersonnel WHERE isActive='1'", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			arraylist.add(cursor.getString(0));
			cursor.moveToNext();	
		}
		String []array = new String[arraylist.size()];
		arraylist.toArray(array);
		return array;
		
	}
	
	//checks if a personnel isActive or not  isActive=1 means active  isActive=0 means not active
	public int isActive(String name)
	{
		Cursor cursor=db.rawQuery("SELECT isActive FROM tblPersonnel WHERE Name='"+name+"'", null);
		cursor.moveToFirst();
		int temp=cursor.getInt(0);
		return temp;
	}
	
	//METHODS RELATED WITH THE MESSAGE TABLE
	
	//---insert a message into the message table---
	public long insertMessage(String Message,String MessageStamp, String NumberArray)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MESSAGE, Message);
		initialValues.put(KEY_MESSAGESTAMP, MessageStamp);
		initialValues.put(KEY_NUMBER_ARRAY, NumberArray);
		return db.insert(MESSAGE_TABLE, null, initialValues);
	}
	
	//---get all messages in the database---
	public String[] getAllMessages()
	{
		ArrayList <String> arraylist=new ArrayList<String>();
		Log.v("--- GA Deb--","In AllMEssages");
		Cursor cursor = db.rawQuery(
				"SELECT Message, MessageStamp,NumberArray FROM tblMessage", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			String acks = cursor.getString(2);
			String[] acksArr = acks.split(";");
			String temp=cursor.getString(0)+"-"+cursor.getString(1)+"-"+acksArr.length;
			arraylist.add(temp);
			cursor.moveToNext();	
		}
		String []array = new String[arraylist.size()];
		arraylist.toArray(array);
		return array;
		
	}
	
	//---get all Missing Acks in the database---
	public List<String> getAllMissingAcks(String messagestamp)
	{
		Cursor cursor = db.rawQuery("SELECT NumberArray FROM tblMessage WHERE MessageStamp='"+ messagestamp+"'",null);
		cursor.moveToFirst();
		String numberString=cursor.getString(0);
		Log.v("---GA NEW---","--- before : "+numberString);
		String[] numArr = numberString.split(";");
		List<String> nameArr = new ArrayList<String>();
		for (int i=0;i<numArr.length;i++)
		{
			cursor = db.rawQuery("SELECT Name FROM tblPersonnel WHERE Number='"+ numArr[i].toString()+"'",null);
			if(cursor.moveToFirst())
				nameArr.add(cursor.getString(0));
		}
			return nameArr;
		
	}
	
	
	
	public void updateNumberArray(String messageStamp, String ackContactNumber)
	{
		Cursor cursor = db.rawQuery("SELECT NumberArray FROM tblMessage WHERE MessageStamp='"+ messageStamp+"'",null);
		cursor.moveToFirst();
		String numberString=cursor.getString(0);
		Log.v("---GA2---","--- before : "+numberString);
		String[] numArr = numberString.split(";");
		
	
	     ArrayList<String> numList = new ArrayList<String>(Arrays.asList(numArr));
	     
	     numList.remove(ackContactNumber);
	     Log.v("---GA2---","Size : "+numList.size()+" ackContactNumber :"+ackContactNumber);
	     
	     String []array = new String[numList.size()];
	     numList.toArray(array);
	     numberString = MessageTab.arrayToString2(array, ";");
	    if(numberString.trim().length()>0)
	    	db.execSQL("UPDATE tblMessage SET NumberArray ='"+numberString+"' WHERE MessageStamp='"+ messageStamp+"'");
	    else
	    	db.execSQL("DELETE FROM tblMessage  WHERE MessageStamp='"+ messageStamp+"'");

	     // debugging
	     Cursor cursor2 = db.rawQuery("SELECT NumberArray FROM tblMessage WHERE MessageStamp='"+ messageStamp+"'",null);
	    if( cursor2.moveToFirst())
			 numberString=cursor2.getString(0);    
	    Log.v("---GA2---","--- From DB : "+numberString);
	 
	     

		
	}
	
	
}
