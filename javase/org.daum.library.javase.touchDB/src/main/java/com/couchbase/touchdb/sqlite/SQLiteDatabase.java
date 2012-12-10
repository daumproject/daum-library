package com.couchbase.touchdb.sqlite;


import com.couchbase.touchdb.ContentValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.jdbc.JdbcConnection;
import org.sqlite.jdbc.JdbcPreparedStatement;

import java.sql.*;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/12/12
 * Time: 10:16
 * To change this template use File | Settings | File Templates.
 */
public class SQLiteDatabase {

    private  Connection  connection;
    public static final int CREATE_IF_NECESSARY = 0x10000000;     // update native code if
    public static final Object CONFLICT_REPLACE = 0x20000000;
    public static final Object CONFLICT_IGNORE =0 ;
    private static  Logger logger = LoggerFactory.getLogger(SQLiteDatabase.class);
    private String dbname;
    /** Used by native code, do not rename */
    /* package */ int mNativeHandle = 0;

    public void execSQL(String sql) throws SQLException{

        logger.debug("execSQL "+sql);
        Statement stmt =connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }


    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public static SQLiteDatabase openDatabase(String db, Object factory, int flags) throws SQLiteException {
        try
        {
            SQLiteDatabase current =  new SQLiteDatabase();
            logger.debug("openDatabase "+db);
            org.sqlite.Driver driver  = new org.sqlite.Driver();
            DriverManager.registerDriver(driver);
            final Connection connection =  DriverManager.getConnection("jdbc:sqlite:file:"+db);
            current.setConnection(connection);
            current.setDbname(db);

            return  current;
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
        return null;
    }

    public int getVersion()
    {
        Statement stmt=null;
        try
        {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("PRAGMA user_version");
            rs.next();
            return   rs.getInt(1);
        }  catch (Exception e){
            e.printStackTrace();
            return 0;
        }  finally {
            assert stmt != null;
            try {
                stmt.close();
            } catch (SQLException e) {

            }

        }
    }

    public void close() {
        logger.debug("close " + getDbname());
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Boolean isOpen() {
        logger.debug("isOpen " + getDbname());
        Boolean rt =true;
        try {
            if(connection.isClosed()){
                return false;
            }else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }

    public void beginTransaction() throws SQLException {
        logger.debug("beginTransaction " + getDbname());
        connection.setAutoCommit(false);
    }

    public void setTransactionSuccessful() {
        logger.debug("setTransactionSuccessful " + getDbname());
    }

    public void endTransaction()  {
        logger.debug("endTransaction " + getDbname());
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Cursor rawQuery(String s,  String[] o) throws  SQLException{
        logger.debug("rawQuery "+s+" "+ getDbname());

        ResultSet resultSet=null;
        PreparedStatement pstmt =connection.prepareStatement(s);
        if(o != null){
            int count=1;
            for(String c :o)
            {
                pstmt.setString(count, c);
                count++;
            }
        }
        resultSet = pstmt.executeQuery();

        return new Cursor(resultSet);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {

        logger.debug("delete "+table+" "+ getDbname());
        String sql = ("DELETE FROM "+table+" WHERE "+whereClause);
        JdbcPreparedStatement stmt = null;
        int updated=0;
        try
        {
            stmt = (JdbcPreparedStatement)connection.prepareStatement(sql.toString());
            int count=1;
            for(String c :whereArgs)
            {
                stmt.setString(count,c);
                count++;
            }

            updated =   stmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            assert stmt != null;
            try {
                stmt.close();
            } catch (SQLException e) {

            }
        }
        return updated;
    }



    // return id
    public long insert(String table, Object nullColumnHack, ContentValues initialValues) throws  SQLException{
        StringBuilder sql = new StringBuilder(152);
        sql.append("INSERT ");
        //sql.append("OR REPLACE ");
        sql.append("INTO ");
        sql.append(table);
        // Measurements show most values lengths < 40
        StringBuilder values = new StringBuilder(40);

        if (initialValues != null && initialValues.size() > 0) {
            sql.append('(');
            Boolean needSeparator = false;
            for(Object key : initialValues.keySet()) {
                if (needSeparator) {
                    sql.append(", ");
                    values.append(", ");
                }
                needSeparator = true;
                sql.append(key);
                values.append('?');
            }

            sql.append(')');
        } else {
            sql.append("(" + nullColumnHack + ") ");
            values.append("NULL");
        }

        sql.append(" VALUES(");
        sql.append(values);
        sql.append(");");

        logger.debug("INSERT "+sql+" "+ getDbname());

        JdbcPreparedStatement stmt = (JdbcPreparedStatement)connection.prepareStatement(sql.toString());
        int count=1;
        for(Object c :initialValues.keySet())
        {
            if(initialValues.get(c) instanceof String)
            {
                stmt.setString(count,(String)initialValues.get(c));
            } else if(initialValues.get(c) instanceof byte[]){
                stmt.setBytes(count,(byte[])initialValues.get(c));

            } else if(initialValues.get(c) instanceof Boolean){
                stmt.setBoolean(count,(Boolean)initialValues.get(c));
            }
            else if(initialValues.get(c) instanceof Long){
                stmt.setLong(count,(Long)initialValues.get(c));
            } else if(initialValues.get(c) instanceof Integer){
                stmt.setInt(count, (Integer) initialValues.get(c));
            }else {
                if(initialValues.get(c) == null){
                    logger.warn("field "+c+" is null");
                }else {
                    logger.error("INSERT TODO ERROR key ="+c+" {"+initialValues.get(c)+"}");
                }

            }
            count++;
        }

        stmt.executeUpdate();
        long rowid=   stmt.getLastInsertRowId();
        stmt.close();

        return  rowid;
    }

    public void execSQL(String s, String[] args) {
        logger.debug("execSQL "+s+" "+ getDbname());
        try {
            rawQuery(s,args);
        } catch (SQLException e) {
            logger.error("execSQL ",e);
        }


    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) throws  SQLException {

        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE ");
        sql.append(table);
        sql.append(" SET ");
        int field=0;
        for(Object key : values.keySet()){
            sql.append(key);
            sql.append("=?");
            if (field < values.keySet().size()-1) {
                sql.append(", ");
            }
            field++;
        }

        logger.warn("update TO CHECK "+sql+" " + getDbname());

        JdbcPreparedStatement stmt = (JdbcPreparedStatement)connection.prepareStatement(sql.toString());
        int count=1;
        for(Object c :values.keySet())
        {
            if(values.get(c) instanceof String)
            {
                stmt.setString(count,(String)values.get(c));
            } else if(values.get(c) instanceof byte[]){
                stmt.setBytes(count,(byte[])values.get(c));

            } else if(values.get(c) instanceof Boolean){
                stmt.setBoolean(count,(Boolean)values.get(c));
            }
            else if(values.get(c) instanceof Long){
                stmt.setLong(count,(Long)values.get(c));
            } else if(values.get(c) instanceof Integer){
                stmt.setInt(count, (Integer) values.get(c));
            }else {
                logger.error("TODO ERROR "+values.get(c));
            }
            count++;
        }


        return         stmt.executeUpdate();
    }

    public long insertWithOnConflict(String replicators, Object o, ContentValues values, Object conflictReplace)    {
        logger.error("TODO insertWithOnConflict "+replicators+" "+ getDbname());
        return 0;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }
}
