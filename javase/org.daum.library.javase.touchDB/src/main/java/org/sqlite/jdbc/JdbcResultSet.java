package org.sqlite.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;
import org.sqlite.Database;
import org.sqlite.swig.SQLite3;
import static org.sqlite.swig.SQLite3Constants.SQLITE_BLOB;
import static org.sqlite.swig.SQLite3Constants.SQLITE_DONE;
import static org.sqlite.swig.SQLite3Constants.SQLITE_FLOAT;
import static org.sqlite.swig.SQLite3Constants.SQLITE_INTEGER;
import static org.sqlite.swig.SQLite3Constants.SQLITE_NULL;
import static org.sqlite.swig.SQLite3Constants.SQLITE_ROW;
import static org.sqlite.swig.SQLite3Constants.SQLITE3_TEXT;

/**
 *
 * @author calico
 */
public class JdbcResultSet implements ResultSet {

    private final Statement owner;
    private final org.sqlite.Statement stmt;
    private boolean isClosed = false;
    private boolean isBeforeFirst = true;
    private boolean isFirst = false;
    private boolean isAfterLast = false;
    private int currentRow = 0;
    private int lastRow = -1;
    private String[] columnLabels;
    private int[] columnTypes;
    private int lastColumnReadIndex;
    private JdbcBlob blob;
    
    /**
     * default constructor.
     * @param owner
     * @param stmt
     */
    public JdbcResultSet(Statement owner, org.sqlite.Statement stmt) {
        this.owner = owner;
        this.stmt = stmt;
    }

    // START implements
    /**
     * invoke org.sqlite.Statement#step() method.
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#step()
     */
    public boolean next() throws SQLException {
        validateResultSetOpen();

        // close the Blob that has opened.
        if (blob != null) {
            blob.close();
            blob = null;
        }
        
        if (isAfterLast) {
            // no more records
            return false;
        }
        
        final int ret = stmt.step();
        if (ret == SQLITE_ROW) {
            // has more records
            if (isBeforeFirst) {
                // first record
                isBeforeFirst = false;
                isFirst = true;
            } else {
                isFirst = false;
            }
            ++currentRow;
            return true;
            
        } else if (ret == SQLITE_DONE) {
            // no more records
            isAfterLast = true;
            isFirst = false;
            if (lastRow == -1) {
                lastRow = currentRow;
            }
        }
        
        return false;
    }

    /**
     * invoke org.sqlite.Statement#close() or org.sqlite.Statement#reset() method.
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#close()
     * @see org.sqlite.Statement#reset()
     */
    public void close() throws SQLException {
        if (!isClosed) {
            isClosed = true;
            if (!stmt.isClosed()) {
                if (stmt.isManaged()) {
                    // StatementがJdbcPreparedStatementによって生成されている場合はcloseしない
                    // ※ closeしてしまうとJdbcPreparedStatementも一緒にcloseされてしまう為
                    stmt.reset();
                } else {
                    // TODO ResultSet#close()の仕様を満たしていない！
                    // 「ResultSet がクローズしても、getMetaData メソッドを呼び出して作成された任意の ResultSetMetaData インスタンスはアクセス可能なままです。」
                    stmt.close();
                }
            }
        }
    }

    /**
     * invoke isNull(<em>last column read index</em>) method.
     * @return true if the Statement#getColumnType(<em>last column read index</em>) method returns SQLITE_NULL.
     * @throws java.sql.SQLException
     * @see #isNull(int)
     */
    public boolean wasNull() throws SQLException {
        return isNull(lastColumnReadIndex);
    }

    /**
     * invoke org.sqlite.Statement#getString(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getString(int)
     */
    public String getString(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return stmt.getString(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getInt(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return true if value is non-zero. false if value is 0.
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getInt(int)
     */
    public boolean getBoolean(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return (stmt.getInt(columnIndex) != 0);
    }

    /**
     * invoke org.sqlite.Statement#getInt(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getInt(int)
     */
    public byte getByte(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return (byte) stmt.getInt(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getInt(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getInt(int)
     */
    public short getShort(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return (short) stmt.getInt(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getInt(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getInt(int)
     */
    public int getInt(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return stmt.getInt(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getLong(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getLong(int)
     */
    public long getLong(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return stmt.getLong(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getDouble(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getDouble(int)
     */
    public float getFloat(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return (float) stmt.getDouble(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getDouble(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getDouble(int)
     */
    public double getDouble(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return stmt.getDouble(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getString(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param scale the number of digits to the right of the decimal point
     * @return new BigDecimal(String).setScale(scale)
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getString(int)
     * @see java.math.BigDecimal#setScale(int)
     * @deprecated
     */
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        BigDecimal ret = null;
        final String val = stmt.getString(columnIndex);
        if (val != null) {
            ret = new BigDecimal(val).setScale(scale);
        }
        return ret;
    }

    /**
     * invoke org.sqlite.Statement#getBytes(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getBytes(int)
     */
    public byte[] getBytes(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        return stmt.getBytes(columnIndex);
    }

    /**
     * invoke org.sqlite.Statement#getString(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return new Date(SQLite3.parseDate(String))
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getString(int)
     * @see org.sqlite.swig.SQLite3#parseDate(String)
     */
    public Date getDate(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        final String val = stmt.getString(columnIndex);
        Date ret = null;
        if (val != null) {
            ret = new Date(SQLite3.parseDate(val));
        }
        return ret;
    }

    /**
     * invoke org.sqlite.Statement#getString(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return new Date(SQLite3.parseTime(String))
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getString(int)
     * @see org.sqlite.swig.SQLite3#parseTime(String)
     */
    public Time getTime(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        final String val = stmt.getString(columnIndex);
        Time ret = null;
        if (val != null) {
            ret = new Time(SQLite3.parseTime(val));
        }
        return ret;
    }

    /**
     * invoke org.sqlite.Statement#getString(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return new Date(SQLite3.parseTimestamp(String))
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getString(int)
     * @see org.sqlite.swig.SQLite3#parseTimestamp(String)
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        final String val = stmt.getString(columnIndex);
        Timestamp ret = null;
        if (val != null) {
            ret = new Timestamp(SQLite3.parseTimestamp(val));
        }
        return ret;
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public InputStream getAsciiStream(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     * @deprecated
     */
    public InputStream getUnicodeStream(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * invoke getBlob(int) method and JdbcBlob#getBinaryStream() method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getBlob(int)
     * @see org.sqlite.jdbc.JdbcBlob#getBinaryStream()
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return getBlob(columnIndex).getBinaryStream();
    }

    /**
     * invoke findColumn(String) method and getString(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getString(int)
     */
    public String getString(String columnLabel) throws SQLException {
        return getString(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getBoolean(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getBoolean(int)
     */
    public boolean getBoolean(String columnLabel) throws SQLException {
        return getBoolean(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getByte(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getByte(int)
     */
    public byte getByte(String columnLabel) throws SQLException {
        return getByte(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getShort(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getShort(int)
     */
    public short getShort(String columnLabel) throws SQLException {
        return getShort(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getInt(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getInt(int)
     */
    public int getInt(String columnLabel) throws SQLException {
        return getInt(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getLong(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getLong(int)
     */
    public long getLong(String columnLabel) throws SQLException {
        return getLong(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getFloat(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getFloat(int)
     */
    public float getFloat(String columnLabel) throws SQLException {
        return getFloat(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getDouble(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getDouble(int)
     */
    public double getDouble(String columnLabel) throws SQLException {
        return getDouble(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getBigDecimal(int, int) method.
     * @param columnLabel the suggested column title
     * @param scale the number of digits to the right of the decimal point
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getBigDecimal(int, int)
     * @deprecated
     */
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return getBigDecimal(findColumn(columnLabel), scale);
    }

    /**
     * invoke findColumn(String) method and getBytes(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getBytes(int)
     */
    public byte[] getBytes(String columnLabel) throws SQLException {
        return getBytes(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getDate(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getDate(int)
     */
    public Date getDate(String columnLabel) throws SQLException {
        return getDate(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getTime(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getTime(int)
     */
    public Time getTime(String columnLabel) throws SQLException {
        return getTime(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getTimestamp(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getTimestamp(int)
     */
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return getTimestamp(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getAsciiStream(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getAsciiStream(int)
     */
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return getAsciiStream(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getUnicodeStream(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getUnicodeStream(int)
     * @deprecated
     */
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return getUnicodeStream(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getBinaryStream(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getBinaryStream(int)
     */
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return getBinaryStream(findColumn(columnLabel));
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public SQLWarning getWarnings() throws SQLException {
        validateResultSetOpen();

        return null;
    }

    /**
     * 
     * @throws java.sql.SQLException
     */
    public void clearWarnings() throws SQLException {
        validateResultSetOpen();
        
        // nothing
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public String getCursorName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public JdbcResultSetMetaData getMetaData() throws SQLException {
        return new JdbcResultSetMetaData(stmt);
    }

    /**
     * 
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     */
    public Object getObject(int columnIndex) throws SQLException {
        validateResultSetOpen();

        if (columnTypes == null) {
            final int cnt = stmt.getColumnCount();
            columnTypes = new int[cnt];
            for (int i = 0; i < cnt; ++i) {
                columnTypes[i] = stmt.getColumnType(i + 1);
            }
        }
        
        final int type = columnTypes[columnIndex - 1];
        Object ret = null;
        String val = null;
        switch (type) {
            case SQLITE_INTEGER:
                val = getString(columnIndex);
                if (val != null) {
                    ret = Integer.parseInt(val);
                }
                break;

            case SQLITE_FLOAT:
                val = getString(columnIndex);
                if (val != null) {
                    ret = Double.parseDouble(val);
                }
                break;

            case SQLITE3_TEXT:
                ret = getString(columnIndex);
                break;

            case SQLITE_BLOB:
                ret = getBlob(columnIndex);
                break;

            case SQLITE_NULL:
                // nothing
                break;

            default:
                Logger.getLogger(JdbcResultSet.class.getName()).info("Unknown column type '" + type + "'.");
                ret = getString(columnIndex);
                break;
        }
        
        return ret;
    }

    /**
     * invoke findColumn(String) method and getObject(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getObject(int)
     */
    public Object getObject(String columnLabel) throws SQLException {
        return getObject(findColumn(columnLabel));
    }

    /**
     * Maps the given ResultSet suggested column title to its ResultSet column index.
     * @param columnLabel the suggested column title
     * @return column index
     * @throws java.sql.SQLException if the ResultSet object does not contain columnLabel or a database access error occurs
     * @see org.sqlite.Statement#getColumnLabel(int)
     */
    public int findColumn(String columnLabel) throws SQLException {
        validateResultSetOpen();

        if (columnLabels == null) {
            final int cnt = stmt.getColumnCount();
            columnLabels = new String[cnt];
            for (int i = 0; i < cnt; ++i) {
                columnLabels[i] = stmt.getColumnLabel(i + 1);
            }
        }
        
        for (int i = 0; i < columnLabels.length; ++i) {
            if (columnLabels[i].equalsIgnoreCase(columnLabel)) {
                return i + 1;
            }
        }
        throw new SQLException("Not found column '" + columnLabel + "'.", "42S22");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Reader getCharacterStream(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Reader getCharacterStream(String columnLabel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * invoke org.sqlite.Statement#getString(int) method.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return new BigDecimal(String)
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getString(int)
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        validateResultSetOpen();

        lastColumnReadIndex = columnIndex;
        BigDecimal ret = null;
        final String val = stmt.getString(columnIndex);
        if (val != null) {
            ret = new BigDecimal(val);
        }
        return ret;
    }

    /**
     * invoke findColumn(String) method and getBigDecimal(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getBigDecimal(findColumn(columnLabel));
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isBeforeFirst() throws SQLException {
        validateResultSetOpen();

        return isBeforeFirst;
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isAfterLast() throws SQLException {
        validateResultSetOpen();

        return isAfterLast;
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isFirst() throws SQLException {
        validateResultSetOpen();

        return isFirst;
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isLast() throws SQLException {
        if (lastRow == -1) {
            final int row = currentRow;
            afterLast();
            absolute(row);
        }
        
        return (lastRow == currentRow);
    }

    /**
     * 
     * @throws java.sql.SQLException
     */
    public void beforeFirst() throws SQLException {
        validateResultSetOpen();

        if (!isBeforeFirst) {
            stmt.reset();
            isBeforeFirst = true;
            isFirst = false;
            isAfterLast = false;
            currentRow = 0;
        }
    }

    /**
     * 
     * @throws java.sql.SQLException
     */
    public void afterLast() throws SQLException {
        while (next()) {
            // skip
        }
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean first() throws SQLException {
        beforeFirst();
        return next();
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean last() throws SQLException {
        if (lastRow == -1) {
            afterLast();
        }
        
        return absolute(lastRow);
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public int getRow() throws SQLException {
        validateResultSetOpen();

        return currentRow;
    }

    /**
     * 
     * @param row
     * @return
     * @throws java.sql.SQLException
     */
    public boolean absolute(int row) throws SQLException {
        validateResultSetOpen();
        
        boolean ret = false;
        if (row == 0) {
            beforeFirst();
            
        } else if (row > 0) {
            if (row < currentRow) {
                beforeFirst();
            }
            ret = true;
            while (currentRow < row && (ret = next())) {
                // skip
            }
        } else {
            if (lastRow == -1) {
                afterLast();
            }
            row = lastRow + (row + 1);
            if (row < 1) {
                beforeFirst();
                
            } else {
                ret = absolute(row);
            }
        }
        
        return ret;
    }

    /**
     * 
     * @param rows
     * @return
     * @throws java.sql.SQLException
     */
    public boolean relative(int rows) throws SQLException {
        validateResultSetOpen();

        boolean ret = false;
        int row = currentRow + rows;
        if (row > 0) {
            ret = absolute(row);
            
        } else {
            beforeFirst();
        }
        
        return ret;
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean previous() throws SQLException {
        validateResultSetOpen();
        
        if (isBeforeFirst) {
            return false;
        }

        if (isFirst) {
            beforeFirst();
            return false;
        }
        
        return absolute(currentRow - 1);
    }

    /**
     * Supported fetch direction is FETCH_FORWARD only.
     * @param direction
     * @throws java.sql.SQLException
     */
    public void setFetchDirection(int direction) throws SQLException {
        validateResultSetFetchDirection(direction);
        validateResultSetOpen();
        
        if (direction != FETCH_FORWARD) {
            throw new SQLException("Not supported fetch direction.", "90J23");
        }
    }

    /**
     * It always returns FETCH_FORWARD.
     * @return java.sql.ResultSet.FETCH_FORWARD
     * @throws java.sql.SQLException
     */
    public int getFetchDirection() throws SQLException {
        validateResultSetOpen();

        return ResultSet.FETCH_FORWARD;
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setFetchSize(int rows) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * It always returns 0.
     * @return 0
     * @throws java.sql.SQLException
     */
    public int getFetchSize() throws SQLException {
        validateResultSetOpen();

        return 0;
    }

    /**
     * It always returns TYPE_FORWARD_ONLY.
     * @return java.sql.ResultSet.TYPE_FORWARD_ONLY
     * @throws java.sql.SQLException
     */
    public int getType() throws SQLException {
        validateResultSetOpen();

        return TYPE_FORWARD_ONLY;
    }

    /**
     * It always returns CONCUR_READ_ONLY.
     * @return java.sql.ResultSet.CONCUR_READ_ONLY
     * @throws java.sql.SQLException
     */
    public int getConcurrency() throws SQLException {
        validateResultSetOpen();
        
        return CONCUR_READ_ONLY;
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public boolean rowUpdated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public boolean rowInserted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public boolean rowDeleted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateNull(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBoolean(int columnIndex, boolean arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateByte(int columnIndex, byte arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateShort(int columnIndex, short arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateInt(int columnIndex, int arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateLong(int columnIndex, long arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateFloat(int columnIndex, float arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateDouble(int columnIndex, double arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBigDecimal(int columnIndex, BigDecimal arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateString(int columnIndex, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBytes(int columnIndex, byte[] arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateDate(int columnIndex, Date arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateTime(int columnIndex, Time arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateTimestamp(int columnIndex, Timestamp arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateAsciiStream(int columnIndex, InputStream arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBinaryStream(int columnIndex, InputStream arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateCharacterStream(int columnIndex, Reader arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateObject(int columnIndex, Object arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateObject(int columnIndex, Object arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateNull(String columnLabel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBoolean(String columnLabel, boolean arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateByte(String columnLabel, byte arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateShort(String columnLabel, short arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateInt(String columnLabel, int arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateLong(String columnLabel, long arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateFloat(String columnLabel, float arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateDouble(String columnLabel, double arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBigDecimal(String columnLabel, BigDecimal arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateString(String columnLabel, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBytes(String columnLabel, byte[] arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateDate(String columnLabel, Date arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateTime(String columnLabel, Time arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateTimestamp(String columnLabel, Timestamp arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateAsciiStream(String columnLabel, InputStream arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBinaryStream(String columnLabel, InputStream arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateCharacterStream(String columnLabel, Reader arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateObject(String columnLabel, Object arg1, int arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateObject(String columnLabel, Object arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void insertRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void deleteRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void refreshRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void cancelRowUpdates() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void moveToInsertRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws java.sql.SQLException
     */
    public void moveToCurrentRow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public Statement getStatement() throws SQLException {
        validateResultSetOpen();
        
        return owner;
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Object getObject(int columnIndex, Map<String, Class<?>> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Ref getRef(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getBlob(int)
     * @see org.sqlite.Statement#getByteLength(int)
     */
    public JdbcBlob getBlob(int columnIndex) throws SQLException {
        validateResultSetOpen();
        
        // close the Blob that has opened.
        if (blob != null) {
            blob.close();
        }

        return (blob = new JdbcBlob(stmt, stmt.getBlob(columnIndex), stmt.getByteLength(columnIndex)));
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Clob getClob(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Array getArray(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * invoke findColumn(String) method and getObject(int, Map) method.
     * @param columnLabel the suggested column title
     * @param map a java.util.Map object that contains the mapping from SQL type names to classes in the Java programming language
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getObject(int, java.util.Map)
     */
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return getObject(findColumn(columnLabel), map);
    }

    /**
     * invoke findColumn(String) method and getRef(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getRef(int)
     */
    public Ref getRef(String columnLabel) throws SQLException {
        return getRef(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getBlob(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getBlob(int)
     */
    public JdbcBlob getBlob(String columnLabel) throws SQLException {
        return getBlob(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getClob(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getClob(int)
     */
    public Clob getClob(String columnLabel) throws SQLException {
        return getClob(findColumn(columnLabel));
    }

    /**
     * invoke findColumn(String) method and getArray(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getArray(int)
     */
    public Array getArray(String columnLabel) throws SQLException {
        return getArray(findColumn(columnLabel));
    }

    /**
     * invoke getDate(int) method and new Date(long).
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal the java.util.Calendar object to use in constructing the date
     * @return
     * @throws java.sql.SQLException
     * @see #getDate(int)
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        final Date val = getDate(columnIndex);
        Date ret = null;
        if (val != null) {
            final Calendar calendar = (Calendar) cal.clone();
            calendar.setTime(ret);
            ret = new Date(calendar.getTime().getTime());
        }
        return ret;
    }

    /**
     * invoke findColumn(String) method and getDate(int, Calendar) method.
     * @param columnLabel the suggested column title
     * @param cal the java.util.Calendar object to use in constructing the date
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getDate(int, java.util.Calendar)
     */
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return getDate(findColumn(columnLabel), cal);
    }

    /**
     * invoke getTime(int) method and new Time(long).
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal the java.util.Calendar object to use in constructing the date
     * @return
     * @throws java.sql.SQLException
     * @see #getTime(int)
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        final Time val = getTime(columnIndex);
        Time ret = null;
        if (val != null) {
            final Calendar calendar = (Calendar) cal.clone();
            calendar.setTime(ret);
            ret = new Time(calendar.getTime().getTime());
        }
        return ret;
    }

    /**
     * invoke findColumn(String) method and getTime(int, Calendar) method.
     * @param columnLabel the suggested column title
     * @param cal the java.util.Calendar object to use in constructing the date
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getTime(int, java.util.Calendar)
     */
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return getTime(findColumn(columnLabel), cal);
    }

    /**
     * invoke getTimestamp(int) method and new Timestamp(long).
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param cal the java.util.Calendar object to use in constructing the date
     * @return
     * @throws java.sql.SQLException
     * @see #getTimestamp(int)
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        final Timestamp val = getTimestamp(columnIndex);
        Timestamp ret = null;
        if (val != null) {
            final Calendar calendar = (Calendar) cal.clone();
            calendar.setTime(ret);
            ret = new Timestamp(calendar.getTime().getTime());
        }
        return ret;
    }

    /**
     * invoke findColumn(String) method and getTimestamp(int, Calendar) method.
     * @param columnLabel the suggested column title
     * @param cal the java.util.Calendar object to use in constructing the date
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return getTimestamp(findColumn(columnLabel), cal);
    }

    /**
     * invoke getString(int) method and new URL(String).
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getString(int)
     */
    public URL getURL(int columnIndex) throws SQLException {
        final String val = getString(columnIndex);
        URL ret = null;
        if (val != null) {
            try {
                ret = new URL(val);
            } catch (MalformedURLException ex) {
                Logger.getLogger(JdbcResultSet.class.getName()).throwing(JdbcResultSet.class.getName(), "getURL(int)", ex);
                throw new SQLException(ex.getMessage(), "90J09");
            }
        }
        return ret;
    }

    /**
     * invoke findColumn(String) method and getURL(int) method.
     * @param columnLabel the suggested column title
     * @return
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #getURL(int)
     */
    public URL getURL(String columnLabel) throws SQLException {
        return getURL(findColumn(columnLabel));
    }

    // TODO ResultSet.CONCUR_UPDATABLEに対応する！

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateRef(int columnIndex, Ref arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateRef(String columnLabel, Ref arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBlob(int columnIndex, Blob arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateBlob(String columnLabel, Blob arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateClob(int columnIndex, Clob arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateClob(String columnLabel, Clob arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateArray(int columnIndex, Array arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void updateArray(String columnLabel, Array arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RowId getRowId(String s) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateRowId(int i, RowId rowId) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateRowId(String s, RowId rowId) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getHoldability() throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
    // END implements

    /**
     * 
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isClosed() throws SQLException {
        return (isClosed || stmt.isClosed());
    }

    @Override
    public void updateNString(int i, String s) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNString(String s, String s1) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNClob(int i, NClob nClob) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNClob(String s, NClob nClob) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NClob getNClob(String s) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SQLXML getSQLXML(String s) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateSQLXML(String s, SQLXML sqlxml) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNString(int i) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNString(String s) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Reader getNCharacterStream(String s) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNCharacterStream(String s, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCharacterStream(String s, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBlob(int i, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBlob(String s, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateClob(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateClob(String s, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNClob(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNClob(String s, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNCharacterStream(String s, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateAsciiStream(int i, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBinaryStream(int i, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCharacterStream(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateAsciiStream(String s, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBinaryStream(String s, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateCharacterStream(String s, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBlob(int i, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateBlob(String s, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateClob(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateClob(String s, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNClob(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateNClob(String s, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 
     * @throws java.sql.SQLException
     */
    protected void validateResultSetOpen() throws SQLException {
        if (isClosed()) {
            throw new SQLException("ResultSet is already closed.", "90007");
        }
    }
    
    /**
     * 
     * @param resultSetType
     * @throws java.sql.SQLException
     */
    public static void validateResultSetType(int resultSetType) throws SQLException {
        if (resultSetType != TYPE_FORWARD_ONLY
                && resultSetType != TYPE_SCROLL_INSENSITIVE
                && resultSetType != TYPE_SCROLL_SENSITIVE) {
            // 無効な値が指定されていた場合
            throw new SQLException("Not supported result set type.", "90J24");
        }
    }

    /**
     * 
     * @param resultSetConcurrency
     * @throws java.sql.SQLException
     */
    public static void validateResultSetConcurrency(int resultSetConcurrency) throws SQLException {
        if (resultSetConcurrency != CONCUR_READ_ONLY
                && resultSetConcurrency != CONCUR_UPDATABLE) {
            // 無効な値が指定されていた場合
            throw new SQLException("Not supported result set concurrency.", "90J25");
        }
    }

    /**
     * 
     * @param resultSetHoldability
     * @throws java.sql.SQLException
     */
    public static void validateResultSetHoldability(int resultSetHoldability) throws SQLException {
        if (resultSetHoldability !=  HOLD_CURSORS_OVER_COMMIT) {
            // 無効な値が指定されていた場合
            throw new SQLException("Not supported result set holdability.", "90J21");
        }
    }

    /**
     * 
     * @param direction
     * @throws java.sql.SQLException
     */
    public static void validateResultSetFetchDirection(int direction) throws SQLException {
        if (direction != FETCH_FORWARD
                && direction != FETCH_REVERSE
                && direction != FETCH_UNKNOWN) {
            // 無効な値が指定されていた場合
            throw new SQLException("Not supported fetch direction.", "90J23");
        }
    }

    /**
     * Returns true if the Statement#getColumnType() method returns SQLITE_NULL.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @return true if the Statement#getColumnType() method returns SQLITE_NULL.
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnType(int)
     */
    public boolean isNull(int columnIndex) throws SQLException {
        validateResultSetOpen();
        
        return (stmt.getColumnType(columnIndex) == SQLITE_NULL);
    }

    /**
     * invoke findColumn(String) method and isNull(int) method.
     * @param columnLabel the suggested column title
     * @return true if the Statement#getColumnType() method returns SQLITE_NULL.
     * @throws java.sql.SQLException
     * @see #findColumn(String)
     * @see #isNull(int)
     */
    public boolean isNull(String columnLabel) throws SQLException {
        return isNull(findColumn(columnLabel));
    }

    /**
     * Open a BLOB for incremental I/O.
     * @param columnIndex the first column is 1, the second is 2, ...
     * @param rowId the ROWID
     * @param isWritable true if the BLOB is opened for read and write access.
     * @return the sqlite3_blob object
     * @throws java.sql.SQLException
     * @see org.sqlite.Database#openBlob(String, String, String, long, int)
     */
    public JdbcBlob getBlob(int columnIndex, long rowId, boolean isWritable) throws SQLException {
        validateResultSetOpen();
        
        final Database db = stmt.getDatabase();
        final String dbName = stmt.getColumnDatabaseName(columnIndex);
        final String tableName = stmt.getColumnTableName(columnIndex);
        final String columnName = stmt.getColumnName(columnIndex);
        
        return new JdbcBlob(db, db.openBlob(dbName, tableName, columnName, rowId, (isWritable ? 1 : 0)));
    }

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
