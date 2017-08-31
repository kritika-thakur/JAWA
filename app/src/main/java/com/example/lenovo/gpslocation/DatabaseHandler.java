package com.example.lenovo.gpslocation;
/**
 * Created by Lenovo on 5/19/2017.
 */
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context, Object name,
                           Object factory, int version) {
        // TODO Auto-generated constructor stub
        super(context,  DATABASE_NAME, null, DATABASE_VERSION);
    }
    String password;
    // Database Version
    private static final int DATABASE_VERSION = 14;
    // Database Name
    private static final String DATABASE_NAME = "Mydatabase.db";
    // Contacts table name
    private static final String TABLE_REGISTER= "register";
    private static final String TABLE_LOCATION= "location";
    private static final String TABLE_SELECTED_DATA= "selected_data";
    private static final String TABLE_NEW_CATEGORY= "store_category";
    private static final String TABLE_ADD_MARKER_DATA= "add_marker_data";
    private static final String TABLE_ADD_CATEGORY= "add_category";

    public static final String KEY_ID = "id";
    public static final String KEY_FIRST_NAME = " first_name";
    public static final String KEY_lAST_NAME = "last_name";
    public static final String KEY_EMAIL_ID="email_id";
    public static final String KEY_MOB_NO = "mobile_number";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_PLACE_NAME = "place_name";
    public static final String KEY_PLACE_ADD = "place_add";
    public static final String KEY_PLACE_RATING = "place_rating";
    public static final String KEY_PLACE_ICON = "place_icon";
    public static final String KEY_FAVOURITES = "favourites";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_NEW_CATEGORY = "new_category";
    public static final String KEY_CATEGORY_IMAGE = "category_image";
    public static final String KEY_NOTIFY_TIME = "notify_time";
    public static final String KEY_SELECTED_RADIUS= "radius";
    public static final String KEY_SELECTED_RAD= "rad";
    public static final String KEY_TIMEW= "timing";
    public static final String KEY_MARKER_COLOR= "marker_color";
    public static final String KEY_DISTANCE= "distance";
    public static final String KEY_INSERT_CATEGORY = "insert_category";
    public static final String KEY_MARKER_PLACE = "marker_place";
    public static final String KEY_MARKER_LAT = "marker_lat";
    public static final String KEY_MARKER_LONG = "marker_long";
    public static final String KEY_MARKER_CAT = "marker_cat";
    public static final String KEY_FAV_ITEMS = "fav_item";
    public static final String KEY_CAT_TYPE = "cat_type";
    public static final String KEY_CAT_CHECKED = "cat_checked";
    //Table For Registration
    public static final String CREATE_TABLE="CREATE TABLE " + TABLE_REGISTER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FIRST_NAME + " TEXT,"+KEY_lAST_NAME + " TEXT,"+KEY_EMAIL_ID+ " TEXT,"
            + KEY_MOB_NO + " TEXT," + KEY_PASSWORD + " TEXT " + ")";
    //Table for storing location(comparing location)
    public static final String LOCATION_TABLE="CREATE TABLE " + TABLE_LOCATION + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LATITUDE + " REAL,"+KEY_LONGITUDE + " REAL,"+KEY_NOTIFY_TIME + " REAL,"+KEY_SELECTED_RADIUS + " REAL,"+KEY_TIMEW + " TEXT,"+KEY_DISTANCE + " TEXT,"+KEY_SELECTED_RAD + " REAL "+ ")";

    public static final String CREATE_SELECTED_TABLE="CREATE TABLE " + TABLE_SELECTED_DATA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CATEGORY + " TEXT," + KEY_PLACE_NAME + " TEXT,"+KEY_PLACE_ADD + " TEXT,"
            + KEY_PLACE_ICON + " BLOB NOT NULL," + KEY_PLACE_RATING + " REAL," + KEY_FAVOURITES + " INTEGER," + KEY_LATITUDE + " TEXT unique," + KEY_MARKER_COLOR + " TEXT ," + KEY_LONGITUDE + " TEXT " + ")";

    public static final String CREATE_CATEGORY_TABLE="CREATE TABLE " + TABLE_NEW_CATEGORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NEW_CATEGORY + " TEXT ,"+ KEY_CAT_TYPE + " TEXT ," + KEY_MARKER_COLOR + " TEXT ,"  + KEY_CATEGORY_IMAGE + " INTEGER " + ")";

    public static final String CREATE_ADD_CATEGORY="CREATE TABLE " + TABLE_ADD_CATEGORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_INSERT_CATEGORY + " TEXT NOT NULL,"
            + KEY_CATEGORY_IMAGE  + " TEXT," + KEY_CAT_CHECKED + " TEXT," + KEY_CAT_TYPE + " TEXT " +  ")";

    public static final String CREATE_TABLE_MARKER="CREATE TABLE " + TABLE_ADD_MARKER_DATA + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MARKER_PLACE + " TEXT," + KEY_MARKER_LAT + " TEXT,"
            + KEY_MARKER_LONG + " TEXT," + KEY_MARKER_CAT + " TEXT," + KEY_FAV_ITEMS + " TEXT " +  ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE);
        db.execSQL(LOCATION_TABLE);
        db.execSQL(CREATE_SELECTED_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_ADD_CATEGORY);
        db.execSQL(CREATE_TABLE_MARKER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEW_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADD_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADD_MARKER_DATA);
        // Create tables again
        onCreate(db);
    }
    void addregister(Registerdata registerdata)
    // code to add the new register
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME,registerdata.getfirstName()); // register first Name
        values.put(KEY_lAST_NAME, registerdata. getlastName() ); // register last name
        values.put(KEY_EMAIL_ID, registerdata.getEmailId());//register email id
        values.put(KEY_MOB_NO, registerdata.getMobNo());//register mobile no
        values.put(KEY_PASSWORD, registerdata.getPassword());
        // Inserting Row
        db.insert(TABLE_REGISTER, null, values);
        db.close(); // Closing database connection
    }
    void insertitems (ListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CATEGORY, item.getCategory());
        contentValues.put(KEY_PLACE_NAME, item.getPlace_name());
        contentValues.put(KEY_PLACE_ADD, item.getPlace_add());
        contentValues.put(KEY_PLACE_RATING, item.getRating());
        contentValues.put(KEY_PLACE_ICON, item.getPlace_icon());
        contentValues.put(KEY_FAVOURITES, item.getFavourites());
        contentValues.put(KEY_LATITUDE, item.getLatitude());
        contentValues.put(KEY_LONGITUDE, item.getLongitude());
        contentValues.put(KEY_MARKER_COLOR, item.getMarkerColor());
        long result= db.insert(TABLE_SELECTED_DATA, null, contentValues);
        db.close();
    }
    void insertAllcategoryList(category_model cat_item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CAT_CHECKED, cat_item.getCat_checked());
        contentValues.put(KEY_CAT_TYPE, cat_item.getCat_type());
        contentValues.put(KEY_INSERT_CATEGORY, cat_item.getCategory_name());
        long result= db.insert(TABLE_ADD_CATEGORY, null, contentValues);
        db.close();
    }
    public void addToCategory(String categoryItems,String cat_type, int color) {
        SQLiteDatabase db = this.getWritableDatabase();
        //INSERT INTO COMPANY (NAME)VALUES ('Paul');
        db.execSQL("INSERT INTO "+ TABLE_NEW_CATEGORY+"("+KEY_NEW_CATEGORY+","+KEY_MARKER_COLOR+","+KEY_CAT_TYPE+")"+" VALUES ('"+categoryItems+"','"+color+"','"+cat_type+"');");
        db.close();
    }
    public void addIngCategories(String name,String type, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();
        //INSERT INTO COMPANY (NAME)VALUES ('Paul');
        db.execSQL("INSERT INTO "+ TABLE_ADD_CATEGORY+"("+KEY_INSERT_CATEGORY+","+KEY_CAT_TYPE+","+KEY_CAT_CHECKED+")"+" VALUES ('"+name+"','"+type+"','"+checked+"');");
        db.close();
    }
    public void updateCategory(String category, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE selected_data SET category = '"+category+"' WHERE id = "+id);
        db.close();
    }
    public void updateCHeckedCat(boolean checkedOrNot, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE add_category SET cat_checked = '"+checkedOrNot+"' WHERE insert_category = '"+category+"'");
        db.close();
    }

    public void addtomarkerdata(String mar_place,double mar_lat,double mar_lon,String mar_cat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO "+ TABLE_ADD_MARKER_DATA+"("+KEY_MARKER_PLACE+","+KEY_MARKER_LAT+","+KEY_MARKER_LONG+","+KEY_MARKER_CAT+")"+" VALUES ('"+mar_place+"','"+mar_lat+"','"+mar_lon+"','"+mar_cat+"');");
    }
    public void fav_item(int fav_store){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("INSERT INTO "+ TABLE_ADD_MARKER_DATA+"("+KEY_FAV_ITEMS+")"+" VALUES ('"+fav_store+"');");
    }
    public void updatefav(int fav_status,int data_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE selected_data SET favourites = "+fav_status+" WHERE id = "+data_id);
        db.close();
    }
    public void delete_need(int Id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_NEW_CATEGORY +" WHERE "+KEY_ID+"='"+Id+"'");
    }
    public void delete_needfromSelectedTable(String cat)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_SELECTED_DATA +" WHERE "+KEY_CATEGORY+"='"+cat+"'"+" AND favourites = 0");
    }
    //db.update(DatabaseHandler.TABLE_SELECTED_DATA, values,null, null);
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void delete_SelectItemTable(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_SELECTED_DATA);
    }
    //Delete method for UNCHECK items
    public void delete_items(String cate){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+TABLE_NEW_CATEGORY +" WHERE "+KEY_NEW_CATEGORY+"='"+cate+"'");
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getTableContacts() {
        return TABLE_REGISTER;
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

}