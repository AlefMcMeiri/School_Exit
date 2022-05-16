package com.example.school_exit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Log;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTools extends SQLiteOpenHelper {

    public static final int VERSION = 14; // version
    public static final String DATABASENAME = "school.db14"; // data base name
    SQLiteDatabase database;

    //------------------------------------------------------------------------------------ TEACHERS_TABLE

    public static final String TEACHERS_TABLE = "Teachers"; // Table name

    // Table columns names
    public static final String TEACHERS_PERSONAL_ID = "teachers_personal_id";
    public static final String TEACHERS_FIRS_NAME = "teachers_first_name";
    public static final String TEACHERS_LAST_NAME = "teachers_last_name";
    public static final String TEACHERS_PASSWORD = "teachers_password";
    public static final String TEACHERS_PHONE_NUMBER = "teachers_phone_namber";
    public static final String TEACHERS_NUMBER_OF_CLASS = "teachers_number_of_class";
    public static final String TEACHERS_PICTURE = "teachers_picture";

    public static final String[] teachersColumns = {TEACHERS_PERSONAL_ID, TEACHERS_FIRS_NAME, TEACHERS_LAST_NAME, TEACHERS_PASSWORD, TEACHERS_PHONE_NUMBER, TEACHERS_NUMBER_OF_CLASS, TEACHERS_PICTURE};

    public static final String CREATE_TEACHER_QUERY = "CREATE TABLE IF NOT EXISTS " + TEACHERS_TABLE + "(" + TEACHERS_PERSONAL_ID + " VARCHAR PRIMARY KEY, " + TEACHERS_FIRS_NAME + " VARCHAR, " + TEACHERS_LAST_NAME
            + " VARCHAR, " + TEACHERS_PASSWORD + " VARCHAR, " + TEACHERS_PHONE_NUMBER + " VARCHAR, " + TEACHERS_NUMBER_OF_CLASS + " VARCHAR, " + TEACHERS_PICTURE + " VARCHAR)";

    //------------------------------------------------------------------------------------ PARENT_TABLE

    public static final String PARENTS_TABLE = "Parents"; // Table name

    // Table columns name
    public static final String PARENTS_PERSONAL_ID = "parents_personal_id";
    public static final String PARENTS_FIRS_NAME = "parents_first_name";
    public static final String PARENTS_LAST_NAME = "parents_last_name";
    public static final String PARENTS_PASSWORD = "parents_password";
    public static final String PARENTS_PHONE_NUMBER = "parents_phone_number";
    public static final String PARENTS_PICTURE = "parents_picture";

    public static final String[] parentsColumns = {PARENTS_PERSONAL_ID, PARENTS_FIRS_NAME, PARENTS_LAST_NAME, PARENTS_PASSWORD, PARENTS_PHONE_NUMBER, PARENTS_PICTURE};

    public static final String CREATE_PARENTS_QUERY = "CREATE TABLE IF NOT EXISTS " + PARENTS_TABLE + "(" + PARENTS_PERSONAL_ID + " VARCHAR PRIMARY KEY, " + PARENTS_FIRS_NAME + " VARCHAR, " + PARENTS_LAST_NAME
            + " VARCHAR, " + PARENTS_PASSWORD + " VARCHAR, " + PARENTS_PHONE_NUMBER + " VARCHAR, " + PARENTS_PICTURE + " VARCHAR)";

    //------------------------------------------------------------------------------------ CHILDREN_TABLE

    public static final String CHILDREN_TABLE = "Children"; // Table name

    // Table columns name
    public static final String CHILDREN_PERSONAL_ID = "children_personal_id";
    public static final String CHILDREN_FIRS_NAME = "children_first_name";
    public static final String CHILDREN_LAST_NAME = "children_last_name";
    public static final String CHILDREN_PARENT_ID = "children_parent_personal_id";
    public static final String CHILDREN_NUMBER_OF_CLASS = "children_number_of_class";

    public static final String[] childrenColumns = {CHILDREN_PERSONAL_ID, CHILDREN_FIRS_NAME, CHILDREN_LAST_NAME, CHILDREN_PARENT_ID, CHILDREN_NUMBER_OF_CLASS};

    public static final String CREATE_CHILDREN_QUERY = "CREATE TABLE IF NOT EXISTS " + CHILDREN_TABLE + "(" + CHILDREN_PERSONAL_ID + " VARCHAR PRIMARY KEY, " + CHILDREN_FIRS_NAME + " VARCHAR, " + CHILDREN_LAST_NAME
            + " VARCHAR, " + CHILDREN_PARENT_ID + " VARCHAR, " + CHILDREN_NUMBER_OF_CLASS + " VARCHAR)";

    //------------------------------------------------------------------------------------ REQUESTS_TABLE

    public static final String REQUESTS_TABLE = "Requests"; // Table name

    // Table columns name
    public static final String REQUESTS_AUTOINCREMENT_TABLE_ID = "requests_table_id";
    public static final String REQUESTS_HOUR_OF_DATE = "requests_hour_of_date";
    public static final String REQUESTS_MINUTE_OF_DATE = "requests_minute_of_date";
    public static final String REQUESTS_DAY_OF_DATE = "requests_day_of_date";
    public static final String REQUESTS_MONTH_OF_DATE = "requests_month_of_date";
    public static final String REQUESTS_YEAR_OF_DATE = "requests_year_of_date";
    public static final String REQUESTS_CHILD_PERSONAL_ID = "requests_child_personal_id";
    public static final String REQUESTS_REASON = "requests_reason";
    public static final String REQUESTS_NOTE = "requests_note";
    public static final String REQUESTS_STATUS = "requests_status";

    public static final String[] requestsColumns = {REQUESTS_AUTOINCREMENT_TABLE_ID,REQUESTS_HOUR_OF_DATE, REQUESTS_MINUTE_OF_DATE, REQUESTS_DAY_OF_DATE, REQUESTS_MONTH_OF_DATE, REQUESTS_YEAR_OF_DATE,
            REQUESTS_CHILD_PERSONAL_ID, REQUESTS_REASON, REQUESTS_NOTE, REQUESTS_STATUS};

    public static final String CREATE_REQUESTS_QUERY = "CREATE TABLE IF NOT EXISTS " + REQUESTS_TABLE + "(" + REQUESTS_AUTOINCREMENT_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REQUESTS_HOUR_OF_DATE + " INTEGER, " + REQUESTS_MINUTE_OF_DATE + " INTEGER, " + REQUESTS_DAY_OF_DATE + " INTEGER, " + REQUESTS_MONTH_OF_DATE
            + " INTEGER, " + REQUESTS_YEAR_OF_DATE + " INTEGER, " + REQUESTS_CHILD_PERSONAL_ID + " VARCHAR, " + REQUESTS_REASON + " VARCHAR, " + REQUESTS_NOTE + " VARCHAR, " + REQUESTS_STATUS + " INTEGER)";

    //------------------------------------------------------------------------------------ REGULAR

    public DatabaseTools(Context context) {
        super(context, DATABASENAME, null, VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) { // onCreate
        db.execSQL(CREATE_TEACHER_QUERY);
        db.execSQL(CREATE_PARENTS_QUERY);
        db.execSQL(CREATE_CHILDREN_QUERY);
        db.execSQL(CREATE_REQUESTS_QUERY);

        Log.i("data1", "Hi Ori, onCreate was been done");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // onUpgrade
        db.execSQL("DROP TABLE IF EXISTS " + TEACHERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PARENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CHILDREN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REQUESTS_TABLE);
        onCreate(db);
    }

    public void open() // data base open
    {
        database=this.getWritableDatabase();
    }

    public synchronized void close() { // data base close
        super.close();

    }

    //------------------------------------------------------------------------------------ INSERTS

    public void insertTeacher(Teacher teacher, String password)
    {
        ContentValues cv = new ContentValues();
        // Insert the relevant details to the table
        cv.put(TEACHERS_PERSONAL_ID, teacher.getPersonalId());
        cv.put(TEACHERS_FIRS_NAME, teacher.getFirstName());
        cv.put(TEACHERS_LAST_NAME, teacher.getLastName());
        cv.put(TEACHERS_PASSWORD, password);
        cv.put(TEACHERS_PHONE_NUMBER, teacher.getPhoneNumber());
        cv.put(TEACHERS_NUMBER_OF_CLASS, teacher.getNumberOfClass());
        cv.put(TEACHERS_PICTURE, teacher.getBitmap());

        database.insert(TEACHERS_TABLE, null, cv); // Insert
        Log.d("data1", "" + teacher.toString() + "inserted to dataBase");
    }

    public void insertParent(Parent parent, String password)
    {
        ContentValues cv = new ContentValues();
        // Insert the relevant details to the table
        cv.put(PARENTS_PERSONAL_ID, parent.getPersonalId());
        cv.put(PARENTS_FIRS_NAME, parent.getFirstName());
        cv.put(PARENTS_LAST_NAME, parent.getLastName());
        cv.put(PARENTS_PASSWORD, password);
        cv.put(PARENTS_PHONE_NUMBER, parent.getPhoneNumber());
        cv.put(PARENTS_PICTURE, parent.getBitmap());

        database.insert(PARENTS_TABLE, null, cv); // Insert
        Log.d("data1", "" + parent.toString() + "inserted to dataBase");

    }

    public void insertChild(Child child)
    {
        ContentValues cv = new ContentValues();
        // Insert the relevant details to the table
        cv.put(CHILDREN_PERSONAL_ID, child.getPersonalId());
        cv.put(CHILDREN_FIRS_NAME, child.getFirstName());
        cv.put(CHILDREN_LAST_NAME, child.getLastName());
        cv.put(CHILDREN_PARENT_ID, child.getParentPersonalId());
        cv.put(CHILDREN_NUMBER_OF_CLASS, child.getNumberClass());

        database.insert(CHILDREN_TABLE, null, cv); // Insert
        Log.d("data1", "" + child.toString() + "inserted to dataBase");

    }

    public void insertRequest(Request request)
    {
        ContentValues cv = new ContentValues();
        // Insert the relevant details to the table
        cv.put(REQUESTS_HOUR_OF_DATE, request.getDate().getHour());
        cv.put(REQUESTS_MINUTE_OF_DATE, request.getDate().getMinute());
        cv.put(REQUESTS_DAY_OF_DATE, request.getDate().getDayOfMonth());
        cv.put(REQUESTS_MONTH_OF_DATE, request.getDate().getMonthValue());
        cv.put(REQUESTS_YEAR_OF_DATE, request.getDate().getYear());
        cv.put(REQUESTS_CHILD_PERSONAL_ID, request.getChildPersonalId());
        cv.put(REQUESTS_REASON, request.getReason());
        cv.put(REQUESTS_NOTE, request.getNote());
        cv.put(REQUESTS_STATUS, request.getStatus());

        long requestInsertId = database.insert(REQUESTS_TABLE, null, cv); // Insert
        request.setId(requestInsertId); // Set the id for what has been given from the table
        Log.d("data1", "" + request.toString() + "inserted to dataBase");
    }

    //------------------------------------------------------------------------------------ GET_PARENT_BY_PERSONAL_ID

    public Parent getParentByPersonalId(String personalId)
    {
        Cursor cursor=database.query(DatabaseTools.PARENTS_TABLE, parentsColumns, DatabaseTools.PARENTS_PERSONAL_ID + "=\"" +personalId + "\"", null, null, null, null);

        if(cursor.getCount()<=0)
            return null;

        cursor.moveToFirst();
        String firstName=cursor.getString(cursor.getColumnIndex(DatabaseTools.PARENTS_FIRS_NAME));
        String lastName=cursor.getString(cursor.getColumnIndex(DatabaseTools.PARENTS_LAST_NAME));
        String phoneNumber=cursor.getString(cursor.getColumnIndex(DatabaseTools.PARENTS_PHONE_NUMBER));
        String bitmap = cursor.getString(cursor.getColumnIndex(PARENTS_PICTURE));

        Parent parent=new Parent(personalId,firstName,lastName,phoneNumber, bitmap);

        ArrayList<Request> requests = getRequestsByParent(parent);
        parent.setRequests(requests);

        ArrayList<Child> children = getChildrenByParentId(personalId);
        parent.setChildren(children);

        return parent;
    }

    //------------------------------------------------------------------------------------ GET_PARENT_BY_CHILD_ID

    public Parent getParentByChildId (String childId)
                                                            // when trying to filter by parent
    {
        String query = "SELECT " + PARENTS_PASSWORD + ", " + PARENTS_PHONE_NUMBER + "," + PARENTS_PICTURE + "," + PARENTS_LAST_NAME + "," + PARENTS_FIRS_NAME + "," + PARENTS_PERSONAL_ID + "\n" +
                "FROM " + CHILDREN_TABLE + " INNER JOIN " + PARENTS_TABLE + "\n" +
                "ON " + CHILDREN_PARENT_ID +  "= \"" +  PARENTS_PERSONAL_ID + "\"" +"\n" +
                "WHERE " + CHILDREN_PERSONAL_ID + "=\"" + childId + "\"";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() <= 0)
            return null;
        cursor.moveToFirst();

        String ID = cursor.getString(cursor.getColumnIndex(PARENTS_PERSONAL_ID));
        String firstName = cursor.getString(cursor.getColumnIndex(PARENTS_FIRS_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(PARENTS_LAST_NAME));
        String phoneNumber = cursor.getString(cursor.getColumnIndex(PARENTS_PHONE_NUMBER));
        String bitmap = cursor.getString(cursor.getColumnIndex(PARENTS_PICTURE));

        return new Parent(ID, firstName, lastName, phoneNumber, bitmap);
    }

    /*
    public String getParentFullNameByChildId(String childId)
    {
        String query = "SELECT " + PARENTS_PASSWORD + ", " + PARENTS_PHONE_NUMBER + "," + PARENTS_LAST_NAME + "," + PARENTS_FIRS_NAME + "," + PARENTS_PERSONAL_ID + "\n" +
                "FROM " + CHILDREN_TABLE + " INNER JOIN " + PARENTS_TABLE + "\n" +
                "ON " + CHILDREN_PARENT_ID +  "= \"" +  PARENTS_PERSONAL_ID + "\"" +"\n" +
                "WHERE " + CHILDREN_PERSONAL_ID + "=\"" + childId + "\"";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() <= 0)
            return null;
        cursor.moveToFirst();

        String firstName = cursor.getString(cursor.getColumnIndex(PARENTS_FIRS_NAME));
        String lastName = cursor.getString(cursor.getColumnIndex(PARENTS_LAST_NAME));

        return firstName + " " + lastName;

    }

     */

    //------------------------------------------------------------------------------------ GET_CHILD_BY_PERSONAL_ID

    public Child getChildByPersonalId (String childId)
    {
        Cursor cursor = database.query(CHILDREN_TABLE, childrenColumns, CHILDREN_PERSONAL_ID + "=\"" + childId + "\"", null, null, null, null);
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToNext();
        Child child = new Child(cursor.getString(cursor.getColumnIndex(CHILDREN_PERSONAL_ID)), cursor.getString(cursor.getColumnIndex(CHILDREN_FIRS_NAME)), cursor.getString(cursor.getColumnIndex(CHILDREN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CHILDREN_PARENT_ID)), cursor.getString(cursor.getColumnIndex(CHILDREN_NUMBER_OF_CLASS)));

        return child;
    }

    //------------------------------------------------------------------------------------ GET_REQUEST_BY_FILTER

    /*
    public ArrayList<Request> getAllRequestsByFilter(String selection, String OrderBy)
    {
        Cursor cursor=database.query(REQUESTS_TABLE, requestsColumns, selection, null, null, null, OrderBy);
        ArrayList<Request>l=convertCurserToList(cursor);
        return  l;
    }

    private ArrayList<Request> convertCurserToList(Cursor cursor) {
        ArrayList<Request>l=new ArrayList<Request>();

        if(cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                long id=cursor.getLong(cursor.getColumnIndex(REQUESTS_AUTOINCREMENT_TABLE_ID));
                int dayOfMonth =cursor.getInt(cursor.getColumnIndex(REQUESTS_DAY_OF_DATE));
                int month =cursor.getInt(cursor.getColumnIndex(REQUESTS_MONTH_OF_DATE));
                int year =cursor.getInt(cursor.getColumnIndex(REQUESTS_YEAR_OF_DATE));
                String childPersonalId=cursor.getString(cursor.getColumnIndex(REQUESTS_CHILD_PERSONAL_ID));
                String reason=cursor.getString(cursor.getColumnIndex(REQUESTS_REASON));
                String note=cursor.getString(cursor.getColumnIndex(REQUESTS_NOTE));
                int status = cursor.getInt(cursor.getColumnIndex(REQUESTS_STATUS));

                Request request = new Request(id, LocalDateTime.of(year, month, dayOfMonth, 0, 0), childPersonalId, reason, note, status);
                l.add(request);
            }
        }
        return l;
    }

     */

    //------------------------------------------------------------------------------------ GET_USER_PASSWORD

    public String[] getPasswordById (String Id)
    {
        Cursor cursor = database.query(PARENTS_TABLE, parentsColumns, PARENTS_PERSONAL_ID + "=\"" + Id + "\"", null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToNext();
            return new String [] {cursor.getString(cursor.getColumnIndex(PARENTS_PASSWORD)), "parent"};
        }
        else
        {
            cursor = database.query(TEACHERS_TABLE, teachersColumns, TEACHERS_PERSONAL_ID + "=\"" + Id + "\"", null, null, null, null);
            if (cursor.getCount() <= 0 )
                return null;
            cursor.moveToNext();
            return new String[] {cursor.getString(cursor.getColumnIndex(TEACHERS_PASSWORD)), "teacher"};
        }
    }

    public Teacher getTeacherById(String teacherId) {

        Cursor cursor = database.query(TEACHERS_TABLE, teachersColumns, TEACHERS_PERSONAL_ID + "=\"" + teacherId + "\"", null, null, null, null);
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToNext();
        Teacher teacher = new Teacher(cursor.getString(cursor.getColumnIndex(TEACHERS_PERSONAL_ID)), cursor.getString(cursor.getColumnIndex(TEACHERS_FIRS_NAME)), cursor.getString(cursor.getColumnIndex(TEACHERS_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(TEACHERS_PHONE_NUMBER)), cursor.getString(cursor.getColumnIndex(TEACHERS_NUMBER_OF_CLASS)), cursor.getString(cursor.getColumnIndex(TEACHERS_PICTURE)));

        ArrayList<Request> requests = getRequestsByTeacher(teacher);
        teacher.setRequests(requests);

        return teacher;
    }

    public ArrayList<Request> getRequestsByTeacher (Teacher teacher)
    {
        ArrayList <Request> l = new ArrayList<Request>();
        String numberClass = teacher.getNumberOfClass();
        Log.d("data1", "teacher number class is: " + numberClass);

        String query = "SELECT " + REQUESTS_CHILD_PERSONAL_ID + ", " + REQUESTS_HOUR_OF_DATE + ", " + REQUESTS_MINUTE_OF_DATE + ", " + REQUESTS_MONTH_OF_DATE + ", " + REQUESTS_DAY_OF_DATE + ", " + REQUESTS_YEAR_OF_DATE + ", " + REQUESTS_NOTE + ", " + REQUESTS_REASON + ", " + REQUESTS_STATUS + ", " + REQUESTS_AUTOINCREMENT_TABLE_ID + "\n" +
                "FROM " + CHILDREN_TABLE + " INNER JOIN " + REQUESTS_TABLE + "\n" +
                "ON " + CHILDREN_PERSONAL_ID + " = " + REQUESTS_CHILD_PERSONAL_ID + "\n" +
                "WHERE " + CHILDREN_NUMBER_OF_CLASS + " = " + "\"" + numberClass +"\"";

        Cursor cursor = database.rawQuery(query, null);

        while(cursor.moveToNext())
        {
            long id=cursor.getLong(cursor.getColumnIndex(REQUESTS_AUTOINCREMENT_TABLE_ID));
            int minute = cursor.getInt(cursor.getColumnIndex(REQUESTS_MINUTE_OF_DATE));
            int hour = cursor.getInt(cursor.getColumnIndex(REQUESTS_HOUR_OF_DATE));
            int dayOfMonth =cursor.getInt(cursor.getColumnIndex(REQUESTS_DAY_OF_DATE));
            int month =cursor.getInt(cursor.getColumnIndex(REQUESTS_MONTH_OF_DATE));
            int year =cursor.getInt(cursor.getColumnIndex(REQUESTS_YEAR_OF_DATE));
            String childPersonalId=cursor.getString(cursor.getColumnIndex(REQUESTS_CHILD_PERSONAL_ID));
            String reason=cursor.getString(cursor.getColumnIndex(REQUESTS_REASON));
            String note=cursor.getString(cursor.getColumnIndex(REQUESTS_NOTE));
            int status = cursor.getInt(cursor.getColumnIndex(REQUESTS_STATUS));

            Request request = new Request(id, LocalDateTime.of(year, month, dayOfMonth, hour, minute), childPersonalId, reason, note, status);
            l.add(request);
        }
        return l;
    }

    public ArrayList<Child> getChildrenByParentId(String parentId) {

        ArrayList<Child> l = new ArrayList<Child>();

        Cursor cursor = database.query(CHILDREN_TABLE, childrenColumns, CHILDREN_PARENT_ID + "=\"" +parentId + "\"", null,null,null,null);
        if (cursor.getCount() <= 0)
            return null;

        while (cursor.moveToNext())
        {
            String id = cursor.getString(cursor.getColumnIndex(CHILDREN_PERSONAL_ID));
            String firstName = cursor.getString(cursor.getColumnIndex(CHILDREN_FIRS_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(CHILDREN_LAST_NAME));
            String numberClass = cursor.getString(cursor.getColumnIndex(CHILDREN_NUMBER_OF_CLASS));

            Child child = new Child (id, firstName, lastName, parentId, numberClass);
            l.add(child);
        }

        return l;
    }

    public ArrayList<Request> getRequestsByParent(Parent parent) {

        ArrayList <Request> l = new ArrayList<Request>();
        String parentId = parent.getPersonalId();

        String query = "SELECT " + REQUESTS_CHILD_PERSONAL_ID + ", " + REQUESTS_HOUR_OF_DATE + ", " + REQUESTS_MINUTE_OF_DATE + ", " + REQUESTS_MONTH_OF_DATE + ", " + REQUESTS_DAY_OF_DATE + ", " + REQUESTS_YEAR_OF_DATE + ", " + REQUESTS_NOTE + ", " + REQUESTS_REASON + ", " + REQUESTS_STATUS + ", " + REQUESTS_AUTOINCREMENT_TABLE_ID + "\n" +
                "FROM " + CHILDREN_TABLE + " INNER JOIN " + REQUESTS_TABLE + "\n" +
                "ON " + CHILDREN_PERSONAL_ID + " = " + REQUESTS_CHILD_PERSONAL_ID + "\n" +
                "WHERE " + CHILDREN_PARENT_ID + " = " + "\"" + parentId +"\"";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.getCount() <= 0)
            return null;

        while(cursor.moveToNext())
        {
            long id=cursor.getLong(cursor.getColumnIndex(REQUESTS_AUTOINCREMENT_TABLE_ID));
            int dayOfMonth =cursor.getInt(cursor.getColumnIndex(REQUESTS_DAY_OF_DATE));
            int month =cursor.getInt(cursor.getColumnIndex(REQUESTS_MONTH_OF_DATE));
            int year =cursor.getInt(cursor.getColumnIndex(REQUESTS_YEAR_OF_DATE));
            String childPersonalId=cursor.getString(cursor.getColumnIndex(REQUESTS_CHILD_PERSONAL_ID));
            String reason=cursor.getString(cursor.getColumnIndex(REQUESTS_REASON));
            String note=cursor.getString(cursor.getColumnIndex(REQUESTS_NOTE));
            int status = cursor.getInt(cursor.getColumnIndex(REQUESTS_STATUS));
            int minute = cursor.getInt(cursor.getColumnIndex(REQUESTS_MINUTE_OF_DATE));
            int hour = cursor.getInt(cursor.getColumnIndex(REQUESTS_HOUR_OF_DATE));

            Request request = new Request(id, LocalDateTime.of(year, month, dayOfMonth, hour, minute), childPersonalId, reason, note, status);
            l.add(request);
        }

        return l;

    }


    //------------------------------------------------------------------------------------


    public Teacher getTeacherByClass (String classNumber) {

        Cursor cursor = database.query(TEACHERS_TABLE, teachersColumns, TEACHERS_NUMBER_OF_CLASS + "=\"" + classNumber + "\"", null, null, null, null);

        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToFirst();

        Teacher teacher = new Teacher(cursor.getString(cursor.getColumnIndex(TEACHERS_PERSONAL_ID)), cursor.getString(cursor.getColumnIndex(TEACHERS_FIRS_NAME)), cursor.getString(cursor.getColumnIndex(TEACHERS_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(TEACHERS_PHONE_NUMBER)), classNumber, cursor.getString(cursor.getColumnIndex(TEACHERS_PICTURE)));

        return teacher;
    }

    public long updateChild(Child child)
    {
        ContentValues values=new ContentValues();
        values.put(CHILDREN_PERSONAL_ID, child.getPersonalId());
        values.put(CHILDREN_FIRS_NAME, child.getFirstName());
        values.put(CHILDREN_LAST_NAME, child.getLastName());
        values.put(CHILDREN_PARENT_ID, child.getParentPersonalId());
        values.put(CHILDREN_NUMBER_OF_CLASS, child.getNumberClass());

        return database.update(CHILDREN_TABLE, values, CHILDREN_PERSONAL_ID +"=\"" + child.getPersonalId() + "\"", null);
    }

    public long deleteChildById(String childId)
    {
        Log.d("data1", "dataBase, deleting child name " + getChildByPersonalId(childId).getFirstName());
        return database.delete(CHILDREN_TABLE, CHILDREN_PERSONAL_ID + "=\"" + childId + "\"", null);
    }

    public ArrayList<Request> deleteRequestsByChildId(String childId) {

        Log.d("data1", "dataBase, deleting requests of child name " + getChildByPersonalId(childId).getFirstName());
        ArrayList<Request> arrayList = new ArrayList<Request>();
        Cursor cursor = database.query(REQUESTS_TABLE, requestsColumns, REQUESTS_CHILD_PERSONAL_ID + "=\"" + childId + "\"", null, null, null, null);
        if (cursor.getCount() <= 0) {
            Log.d("data1", "there is no mach requests (of the child)");
            return null;
        }

        while(cursor.moveToNext())
        {
            deleteRequestById(cursor.getLong(cursor.getColumnIndex(REQUESTS_AUTOINCREMENT_TABLE_ID)));
            long id=cursor.getLong(cursor.getColumnIndex(REQUESTS_AUTOINCREMENT_TABLE_ID));
            int dayOfMonth =cursor.getInt(cursor.getColumnIndex(REQUESTS_DAY_OF_DATE));
            int month =cursor.getInt(cursor.getColumnIndex(REQUESTS_MONTH_OF_DATE));
            int year =cursor.getInt(cursor.getColumnIndex(REQUESTS_YEAR_OF_DATE));
            String childPersonalId=cursor.getString(cursor.getColumnIndex(REQUESTS_CHILD_PERSONAL_ID));
            String reason=cursor.getString(cursor.getColumnIndex(REQUESTS_REASON));
            String note=cursor.getString(cursor.getColumnIndex(REQUESTS_NOTE));
            int status = cursor.getInt(cursor.getColumnIndex(REQUESTS_STATUS));
            int minute = cursor.getInt(cursor.getColumnIndex(REQUESTS_MINUTE_OF_DATE));
            int hour = cursor.getInt(cursor.getColumnIndex(REQUESTS_HOUR_OF_DATE));

            Request request = new Request(id, LocalDateTime.of(year, month, dayOfMonth, hour, minute), childPersonalId, reason, note, status);
            Log.d("data1", "dataBase, request details is: " + request.getReason() + " --- " + request.getDate());
            arrayList.add(request);
        }

        Log.d("data1", "dataBase, array of requests that need to be deleted is " + arrayList.toString());
        return arrayList;

    }

    public long updateRequestStatus(Request request, int status)
    {
        ContentValues values = new ContentValues();
        /* values.put(REQUESTS_AUTOINCREMENT_TABLE_ID, request.getId());
        values.put(REQUESTS_DAY_OF_DATE, request.getDate().getDayOfMonth());
        values.put(REQUESTS_MONTH_OF_DATE, request.getDate().getMonthValue());
        values.put(REQUESTS_YEAR_OF_DATE, request.getDate().getYear());
        values.put(REQUESTS_NOTE, request.getNote());
        values.put(REQUESTS_REASON, request.getReason());
        values.put(REQUESTS_CHILD_PERSONAL_ID, request.getChildPersonalId()); */
        values.put(REQUESTS_STATUS, status);

        int result = database.update(REQUESTS_TABLE, values, REQUESTS_AUTOINCREMENT_TABLE_ID + "=" + request.getId(), null);
        System.out.println(result);
        return result;
    }

    /*
    public User getUserById (String userId)
    {
        Cursor cursor = database.query(PARENTS_TABLE, parentsColumns, PARENTS_PERSONAL_ID + "=\"" + userId + "\"", null, null, null, null);
        if (cursor.getCount() > 0)
        {
            cursor.moveToNext();
            String firstName=cursor.getString(cursor.getColumnIndex(DatabaseTools.PARENTS_FIRS_NAME));
            String lastName=cursor.getString(cursor.getColumnIndex(DatabaseTools.PARENTS_LAST_NAME));
            String phoneNumber=cursor.getString(cursor.getColumnIndex(DatabaseTools.PARENTS_PHONE_NUMBER));
            String bitmap = cursor.getString(cursor.getColumnIndex(PARENTS_PICTURE));
            Parent parent = new Parent(userId,firstName,lastName,phoneNumber, bitmap);

            ArrayList<Request> requests = getRequestsByParent(parent);
            parent.setRequests(requests);

            ArrayList<Child> children = getChildrenByParentId(userId);
            parent.setChildren(children);

            return (User) parent;
        }
        else
        {
            cursor = database.query(TEACHERS_TABLE, teachersColumns, TEACHERS_PERSONAL_ID + "=\"" + userId + "\"", null, null, null, null);
            if (cursor.getCount() <= 0 )
                return null;
            cursor.moveToNext();
            Teacher teacher = new Teacher(cursor.getString(cursor.getColumnIndex(TEACHERS_PERSONAL_ID)), cursor.getString(cursor.getColumnIndex(TEACHERS_FIRS_NAME)), cursor.getString(cursor.getColumnIndex(TEACHERS_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(TEACHERS_PHONE_NUMBER)), cursor.getString(cursor.getColumnIndex(TEACHERS_NUMBER_OF_CLASS)), cursor.getString(cursor.getColumnIndex(TEACHERS_PICTURE)));

            ArrayList<Request> requests = getRequestsByTeacher(teacher);
            teacher.setRequests(requests);

            return (User) teacher;
        }
    }

     */

    public long deleteRequestById(long requestId)
    {
        return database.delete(REQUESTS_TABLE, REQUESTS_AUTOINCREMENT_TABLE_ID + "=" + requestId, null);
    }


    public long updateParent(Parent parent, String password)
    {
        ContentValues values=new ContentValues();
        values.put(PARENTS_FIRS_NAME, parent.getFirstName());
        values.put(PARENTS_LAST_NAME, parent.getLastName());
        values.put(PARENTS_PHONE_NUMBER, parent.getPhoneNumber());
        values.put(PARENTS_PASSWORD, password);
        values.put(PARENTS_PICTURE, parent.getBitmap());

        return database.update(PARENTS_TABLE, values, PARENTS_PERSONAL_ID +"=\"" + parent.getPersonalId() + "\"", null);
    }

    public long updateTeacher(Teacher teacher, String password)
    {
        ContentValues values = new ContentValues();
        values.put(TEACHERS_FIRS_NAME, teacher.getFirstName());
        values.put(TEACHERS_LAST_NAME, teacher.getLastName());
        values.put(TEACHERS_PHONE_NUMBER, teacher.getPhoneNumber());
        values.put(TEACHERS_PICTURE, teacher.getBitmap());
        values.put(TEACHERS_PASSWORD, password);

        return database.update(TEACHERS_TABLE, values, TEACHERS_PERSONAL_ID + "=\"" + teacher.getPersonalId() + "\"", null);
    }

    public boolean isIdAlreadyExist(String id)
    {
        Cursor cursorParents=database.query(DatabaseTools.PARENTS_TABLE, parentsColumns, DatabaseTools.PARENTS_PERSONAL_ID + "=\"" +id + "\"", null, null, null, null);
        Cursor cursorTeachers=database.query(DatabaseTools.TEACHERS_TABLE, teachersColumns, DatabaseTools.TEACHERS_PERSONAL_ID + "=\"" +id + "\"", null, null, null, null);
        Cursor cursorChildren=database.query(DatabaseTools.CHILDREN_TABLE, childrenColumns, DatabaseTools.CHILDREN_PERSONAL_ID + "=\"" +id + "\"", null, null, null, null);

        if (cursorChildren.getCount() > 0 || cursorParents.getCount() > 0 || cursorTeachers.getCount() > 0)
            return true;
        return false;

    }

    public boolean isTeacherExist (String teacherClass)
    {
        Cursor cursor=database.query(DatabaseTools.TEACHERS_TABLE, teachersColumns, DatabaseTools.TEACHERS_NUMBER_OF_CLASS + "=\"" +teacherClass + "\"", null, null, null, null);
        if (cursor.getCount() > 0)
            return true;
        return false;

    }

}
