package test.friends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import test.friends.model.FriendsModel;

public class SqlLiteHelper {
	SQLiteDatabase db;

	public SqlLiteHelper(Context context) {
		db = context.openOrCreateDatabase("cacheDB", Context.MODE_PRIVATE,null);
		db.execSQL("create table if not exists friends(id varchar(255) PRIMARY KEY NOT NULL,first_name varchar(255), last_name varchar(255), email varchar(255) , call varchar(255) ,lat double , lng double)");
	}

	public long insert(String tableName, ContentValues cv) {
		return db.insert(tableName, null, cv);
	}

	public Cursor select(String quary) {
		return db.rawQuery(quary, null);
	}

	public void closeDbConnection(){
		try {
			db.close();
		}catch (Exception e){
			e.getStackTrace();
		}

	}

	public void addFriend(FriendsModel friendsModel){
		try {
			ContentValues cv = new ContentValues();
			//i added the comming id to avoid repeat data
			cv.put("id",friendsModel.getId());
			cv.put("first_name", friendsModel.getFirstName());
			cv.put("last_name", friendsModel.getLastName());
			cv.put("email", friendsModel.getEmail());
			cv.put("call", friendsModel.getCall());
			cv.put("lat", friendsModel.getLatitude());
			cv.put("lng", friendsModel.getLongitude());
			insert("friends", cv);
		}
		catch (Exception e){
			e.getStackTrace();
		}
	}

	public ArrayList<FriendsModel> getFriends(){
		ArrayList<FriendsModel> friends = new ArrayList<>();
		Cursor c = select("select * from friends");
		while (c.moveToNext()) {
			FriendsModel friendsModel = new FriendsModel();
			friendsModel.setId(c.getString(c.getColumnIndex("id")));
			friendsModel.setFirstName(c.getString(c.getColumnIndex("first_name")));
			friendsModel.setLastName(c.getString(c.getColumnIndex("last_name")));
			friendsModel.setEmail(c.getString(c.getColumnIndex("email")));
			friendsModel.setCall(c.getString(c.getColumnIndex("call")));
			friendsModel.setLatitude(c.getDouble(c.getColumnIndex("lat")));
			friendsModel.setLongitude(c.getDouble(c.getColumnIndex("lng")));
			friends.add(friendsModel);
		}

		return friends;
	}
}
