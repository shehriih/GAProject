package com.shehriih.SMSTest;

import java.util.ArrayList;

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
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "database";
	private static final String DATABASE_TABLE = "tblPersonnel";
	private static final int DATABASE_VERSION = 1;

	
	//4 columns to keep personnel data
	//Column 0: Auto increment integer _id
	//Column 1 and 2: Personnel name and number respectively
	//Column 3: An integer field call isActive, which is 1 if the personnel is on the scene of emergency otherwise 0
	private static final String DATABASE_CREATE =
		"create table tblPersonnel (_id integer primary key autoincrement, "
		+ "Name text not null, "
		+ "Number text not null, "
		+ "isActive integer default '0');";

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
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion)
		{
			Log.w(TAG, "Upgrading database from version " + oldVersion
					+ " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS tblPersonnel");
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

	//---insert a title into the database---
	public long insertPersonnel(String Name,String Number)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, Name);
		initialValues.put(KEY_NUMBER, Number);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

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
	public void resetTables()
	{
		DBHelper.onUpgrade(db,1,1);
	}
	
	public void activatePersonnel(String name)
	{
		db.execSQL("UPDATE tblPersonnel SET isActive='1' WHERE Name='"+ name+"'");
	}

	public void deactivatePersonnel(String name)
	{
		db.execSQL("UPDATE tblPersonnel SET isActive='0' WHERE Name='"+ name+"'");
	}
	
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
	
	public int isActive(String name)
	{
		Cursor cursor=db.rawQuery("SELECT isActive FROM tblPersonnel WHERE Name='"+name+"'", null);
		cursor.moveToFirst();
		int temp=cursor.getInt(0);
		return temp;
	}
	
}
