package com.couchbase.touchdb.sqlite;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 05/12/12
 * Time: 10:26
 * To change this template use File | Settings | File Templates.
 */
public class Cursor {

    private ResultSet data;
    private  int count=0;
    public ResultSet getData() {
        return data;
    }

    public void setData(ResultSet data) {
        this.data = data;
        try
        {
            while (data.next()){
                count++;
            }
            data.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public Cursor(ResultSet r ){
        this.data = r;
    }

    public Boolean moveToFirst() throws SQLException {
        return data.first();
    }

    public String getString(int i) throws SQLException {
        return data.getString(i+1);
    }

    public void close()   {
        try {
            data.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int getInt(int i) throws SQLException {
        return data.getInt(i+1);
    }

    public long getLong(int i) throws SQLException {
        return data.getLong(i+1);
    }

    public byte[] getBlob(int i) throws SQLException {
        Blob blob = data.getBlob(i+1);
        int blobLength = (int) blob.length();
        byte[] blobAsBytes = blob.getBytes(1, blobLength);
        return blobAsBytes;
    }

    public Boolean isAfterLast() throws SQLException {
        return data.isAfterLast();
    }

    public void moveToNext() throws SQLException {
        data.next();
    }

    public int getCount()  {
        return count;
    }
}
