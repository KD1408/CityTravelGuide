package com.example.ctg_final;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	final static String DB_NAME = "City_Travel_Guide.db";
	final static int DB_VER = 1;

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String CreateTableStation = "CREATE TABLE stations("
				+"station_name TEXT NOT NULL)";
		
		String CreateTableStop = "CREATE TABLE stops("
				+"route_no TEXT NOT NULL,"
				+"stops_name TEXT NOT NULL,"
				+"stop_no INTEGER NOT NULL)";
		
		db.execSQL(CreateTableStop);
		db.execSQL(CreateTableStation);
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void insertStation(String station)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues insValue = new ContentValues();
		insValue.put("station_name", station);
		db.insert("stations", null, insValue);
	}
	
	//inserting stops through json syncronisation
	public void insertStops(String rt_no, String stp_name, String stp_no)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues insValue = new ContentValues();
		insValue.put("route_no", rt_no);
		insValue.put("stops_name", stp_name);
		insValue.put("stop_no", stp_no);
		db.insert("stops", null, insValue);
	}
	
	//For deleting old values
	public void truncateTables()
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DELETE from stops");
		db.execSQL("DELETE from stations");
	}
	
	public String[] getStation()
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query("stations", new String[] {"station_name"}, null, null, null, null, null);
		if(cursor.getCount() > 0)
		{
			String[] str = new String[cursor.getCount()+1];
			str[0] = "";
			int i=1;
			
			while (cursor.moveToNext()) 
			{
				str[i] = cursor.getString(cursor.getColumnIndex("station_name"));
				i++;
			}
			return str;
		}
		else
		{
			return new String[] {};
		}
	}
	
	//for offline result getting the route number
	public ArrayList<String> resultRoutesNo(String src, String dest)
	{
		ArrayList<String> commonRoutes = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		
		String srcRoutes = "SELECT route_no from stops where stops_name = '"+src+"' ";
		Cursor curSrcRoutes = db.rawQuery(srcRoutes, null);
		
		String destRoutes = "SELECT route_no from stops where stops_name = '"+dest+"' ";
		Cursor curDestRoutes = db.rawQuery(destRoutes, null);
		
		if (curSrcRoutes.getCount() > 0 && curDestRoutes.getCount() > 0)
		{
			curSrcRoutes.moveToFirst();
		
			do
			{
				curDestRoutes.moveToFirst();
				String srcRoute = curSrcRoutes.getString(0);
				do
				{
					String destRoute = curDestRoutes.getString(0);
					if (srcRoute.equals(destRoute))
					{
						commonRoutes.add(srcRoute);
					}
					
				}while (curDestRoutes.moveToNext());
			}while (curSrcRoutes.moveToNext());
		}
		return commonRoutes;
	}
	
	public Cursor stopLists(String route, String src, String dest)
	{
		Cursor result = null;
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT stops_name FROM stops "
				+ " WHERE route_no = '"+route+"'"
				+ " AND stop_no BETWEEN (SELECT stop_no from stops where route_No = '"+route+"' AND stops_name = '"+src+"')"
				+ " AND (SELECT stop_no from stops where route_no = '"+route+"' AND stops_name = '"+dest+"') ORDER BY stop_no";
		
		result = db.rawQuery(query, null);
		return result;
	}
	

}
