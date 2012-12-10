package org.sqlite.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.sqlite.Database;
import org.sqlite.Statement;
import org.sqlite.swig.SQLite3;
import org.sqlite.swig.SQLite3.SQLite3StmtPtrPtr;

/**
 *
 * @author calico
 */
public class JdbcPreparedStatement extends JdbcStatement implements PreparedStatement {

    private final SQLite3StmtPtrPtr ppStmt;
    private Statement stmt;
    private final String sql;
    private List<Statement> batch;
    
    public JdbcPreparedStatement(Database db, JdbcConnection conn, String sql) throws SQLException {
        super(db, conn);
        this.ppStmt = new SQLite3StmtPtrPtr();
        this.stmt = db.prepare(sql, ppStmt);
        this.sql = sql;
    }
    
    // START implements
    public JdbcResultSet executeQuery() throws SQLException {
        if (!execute()) {
            throw new SQLException("No ResultSet was produced.", "02000");
        }
        return rs;
    }

    public int executeUpdate() throws SQLException {
        if (execute()) {
            throw new SQLException("ResultSet was produced.", "02001");
        }
        return cntUpdate;
    }

    /**
     * invoke org.sqlite.Statement#bindNull(int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param sqlType ignored
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindNull(int)
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        validateStatementOpen();
  
        stmt.bindNull(parameterIndex);
    }

    /**
     * invoke org.sqlite.Statement#bindInt(int, int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindInt(int, int)
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindInt(parameterIndex, (x ? 1 : 0));
    }

    /**
     * invoke org.sqlite.Statement#bindInt(int, int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindInt(int, int)
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindInt(parameterIndex, x);
    }

    /**
     * invoke org.sqlite.Statement#bindInt(int, int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindInt(int, int)
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindInt(parameterIndex, x);
    }

    /**
     * invoke org.sqlite.Statement#bindInt(int, int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindInt(int, int)
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindInt(parameterIndex, x);
    }

    /**
     * invoke org.sqlite.Statement#bindLong(int, long) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindLong(int, long)
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindLong(parameterIndex, x);
    }

    /**
     * invoke org.sqlite.Statement#bindDouble(int, double) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindDouble(int, double)
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindDouble(parameterIndex, x);
    }

    /**
     * invoke org.sqlite.Statement#bindDouble(int, double) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindDouble(int, double)
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindDouble(parameterIndex, x);
    }

    /**
     * invoke BigDecimal#toString() and org.sqlite.Statement#bindText(int, String) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindText(int, String)
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        validateStatementOpen();
  
        stmt.bindText(parameterIndex, x.toString());
    }

    /**
     * invoke org.sqlite.Statement#bindText(int, String) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindText(int, String)
     * @see org.sqlite.Statement#bindNull(int)
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        validateStatementOpen();
  
        if (x != null) {
            stmt.bindText(parameterIndex, x);
        } else {
            stmt.bindNull(parameterIndex);
        }
    }

    /**
     * invoke org.sqlite.Statement#bindBytes(int, byte[]) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#bindBytes(int, byte[])
     * @see org.sqlite.Statement#bindNull(int)
     */
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        validateStatementOpen();
  
        if (x != null) {
            stmt.bindBytes(parameterIndex, x);
        } else {
            stmt.bindNull(parameterIndex);
        }
    }

    /**
     * invoke SQLite3#format(Date) and org.sqlite.Statement#bindText(int, String) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.swig.SQLite3#format(java.sql.Date)
     * @see org.sqlite.Statement#bindText(int, String)
     * @see org.sqlite.Statement#bindNull(int)
     */
    public void setDate(int parameterIndex, Date x) throws SQLException {
        validateStatementOpen();
  
        if (x != null) {
            stmt.bindText(parameterIndex, SQLite3.format(x));
        } else {
            stmt.bindNull(parameterIndex);
        }
    }

    /**
     * invoke SQLite3#format(Time) and org.sqlite.Statement#bindText(int, String) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.swig.SQLite3#format(java.sql.Time)
     * @see org.sqlite.Statement#bindText(int, String)
     * @see org.sqlite.Statement#bindNull(int)
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        validateStatementOpen();
  
        if (x != null) {
            stmt.bindText(parameterIndex, SQLite3.format(x));
        } else {
            stmt.bindNull(parameterIndex);
        }
    }

    /**
     * invoke SQLite3#format(Timestamp) and org.sqlite.Statement#bindText(int, String) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see org.sqlite.swig.SQLite3#format(java.sql.Timestamp)
     * @see org.sqlite.Statement#bindText(int, String)
     * @see org.sqlite.Statement#bindNull(int)
     */
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        validateStatementOpen();
  
        if (x != null) {
            stmt.bindText(parameterIndex, SQLite3.format(x));
        } else {
            stmt.bindNull(parameterIndex);
        }
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     * @deprecated
     */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @throws java.sql.SQLException
     */
    public void clearParameters() throws SQLException {
        validateStatementOpen();

        stmt.clearBinding();
    }

    /**
     * 
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @param targetSqlType
     * @param scaleOrLength
     * @throws java.sql.SQLException
     * @see #setObject(int, Object)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        validateSqlType(targetSqlType);

        if (targetSqlType == Types.DECIMAL || targetSqlType == Types.NUMERIC) {
            if (x instanceof Float) {
                x = new BigDecimal(((Number) x).floatValue(), MathContext.DECIMAL64)
                            .setScale(scaleOrLength).floatValue();

            } else if (x instanceof Double) {
                x = new BigDecimal(((Number) x).doubleValue(), MathContext.DECIMAL64)
                            .setScale(scaleOrLength).floatValue();

            } else if (x instanceof BigDecimal) {
                x = ((BigDecimal) x).setScale(scaleOrLength);
            }
        }        
        setObject(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int i, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBinaryStream(int i, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCharacterStream(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAsciiStream(int i, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBinaryStream(int i, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setCharacterStream(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNCharacterStream(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClob(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBlob(int i, InputStream inputStream) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNClob(int i, Reader reader) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * invoke setObject(parameterIndex, x, 0) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @param targetSqlType
     * @throws java.sql.SQLException
     * @see #setObject(int, Object, int, int)
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        validateSqlType(targetSqlType);

        setObject(parameterIndex, x, 0);
    }

    /**
     * 
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @throws UnsupportedOperationException
     * @see #setNull(int, int)
     * @see #setBoolean(int, boolean)
     * @see #setByte(int, byte)
     * @see #setShort(int, short)
     * @see #setInt(int, int)
     * @see #setLong(int, long)
     * @see #setFloat(int, float)
     * @see #setDouble(int, double)
     * @see #setBigDecimal(int, java.math.BigDecimal)
     * @see #setTimestamp(int, java.sql.Timestamp)
     * @see #setTime(int, java.sql.Time)
     * @see #setDate(int, java.sql.Date)
     * @see #setString(int, String)
     * @see #setBytes(int, byte[])
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, Types.NULL);

        } else if (x instanceof Boolean) {
            setBoolean(parameterIndex, (Boolean) x);
        
        } else if (x instanceof Byte) {
            setByte(parameterIndex, ((Number) x).byteValue());
        
        } else if (x instanceof Short) {
            setShort(parameterIndex, ((Number) x).shortValue());
        
        } else if (x instanceof Integer) {
            setInt(parameterIndex, ((Number) x).intValue());
        
        } else if (x instanceof Long) {
            setLong(parameterIndex, ((Number) x).longValue());
        
        } else if (x instanceof Float) {
            setFloat(parameterIndex, ((Number) x).floatValue());
        
        } else if (x instanceof Double) {
            setDouble(parameterIndex, ((Number) x).doubleValue());
        
        } else if (x instanceof BigDecimal) {
            setBigDecimal(parameterIndex, (BigDecimal) x);
        
        } else if (x instanceof Number) {
            setString(parameterIndex, String.valueOf(x));
        
        } else if (x instanceof Timestamp) {
            setTimestamp(parameterIndex, (Timestamp) x);
        
        } else if (x instanceof Time) {
            setTime(parameterIndex, (Time) x);
        
        } else if (x instanceof Date) {
            setDate(parameterIndex, (Date) x);
        
        } else if (x instanceof String) {
            setString(parameterIndex, (String) x);
        
        } else if (x instanceof byte[]) {
            setBytes(parameterIndex, (byte[]) x);
        }

        throw new UnsupportedOperationException("Not supported object.");
    }

    public boolean execute() throws SQLException {
        validateStatementOpen();

        if (stmt.producedResultSet()) {
            rs = new JdbcResultSet(this, stmt);
            cntUpdate = -1;
        } else {
            stmt.execute();
            cntUpdate = db.changes();
            rs = null;
            stmt.reset();
        }
        return (rs != null);
    }

    public void addBatch() throws SQLException {
        validateStatementOpen();
        
        if (stmt.getParameterCount() == 0) {
            super.addBatch(sql);
            
        } else {
            if (batch == null) {
                batch = new ArrayList<Statement>();
            }
            batch.add(stmt);

            // detach and renew Statement
            stmt = db.prepare(sql, ppStmt);
            stmt.reset();
            rs = null;
        }
    }

    @Override
    public void clearBatch() throws SQLException {
        super.clearBatch();

        if (batch != null) {
            for (Statement s : batch) {
                s.close();
            }
            batch = null;
        }
    }

    @Override
    public int[] executeBatch() throws SQLException {
        validateStatementOpen();

        if (stmt.getParameterCount() == 0) {
            return super.executeBatch();
            
        } else {
            if (batch == null) {
                return new int[0];
            }

            final int size = batch.size();
            int[] ret = new int[size];
            BatchUpdateException ex = null;
            for (int i = 0; i < size; ++i) {
                try {
                    ret[i] = executeUpdate(batch.get(i));
                    
                } catch (SQLException ex2) {
                    if (ex == null) {
                        ex = new BatchUpdateException(ex2.getMessage(), ex2.getSQLState(), ex2.getErrorCode(), ret);
                    }
                    ret[i] = EXECUTE_FAILED;
                }
                cntUpdate = ret[i];
                rs = null;
            }
            if (ex != null) {
                throw ex;
            }
            return ret;
        }
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setCharacterStream(int parameterIndex, Reader x, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setRef(int parameterIndex, Ref x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * invoke setBytes(int, byte[]) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @throws ArithmeticException When Blob length is greater than Integer.MAX_VALUE
     */
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        if (x.length() > Integer.MAX_VALUE) {
            throw new ArithmeticException("Blob length is greater than Integer.MAX_VALUE.");
        }
        setBytes(parameterIndex, x.getBytes(1, (int) x.length()));
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setClob(int parameterIndex, Clob x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setArray(int parameterIndex, Array x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return
     */
    public JdbcResultSetMetaData getMetaData() {
        return new JdbcResultSetMetaData(stmt);
    }

    /**
     * invoke setDate(int, Date) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @param cal the Calendar object the driver will use to construct the date
     * @throws java.sql.SQLException
     * @see #setDate(int, java.sql.Date)
     */
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        final Calendar calendar = (Calendar) cal.clone();
        calendar.setTime(x);
        setDate(parameterIndex, new Date(calendar.getTime().getTime()));
    }

    /**
     * invoke setTime(int, Time) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @param cal the Calendar object the driver will use to construct the date
     * @throws java.sql.SQLException
     * @see #setTime(int, java.sql.Time)
     */
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        final Calendar calendar = (Calendar) cal.clone();
        calendar.setTime(x);
        setTime(parameterIndex, new Time(calendar.getTime().getTime()));
    }

    /**
     * invoke setTimestamp(int, Timestamp) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param x the parameter value
     * @param cal the Calendar object the driver will use to construct the date
     * @throws java.sql.SQLException
     * @see #setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        final Calendar calendar = (Calendar) cal.clone();
        calendar.setTime(x);
        setTimestamp(parameterIndex, new Timestamp(calendar.getTime().getTime()));
    }

    /**
     * invoke setNull(int, int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @param sqlType
     * @param typeName ignore
     * @throws java.sql.SQLException
     * @see #setNull(int, int)
     */
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        setNull(parameterIndex, sqlType);
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setURL(int parameterIndex, URL x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return
     */
    public JdbcParameterMetaData getParameterMetaData() {
        return new JdbcParameterMetaData(stmt);
    }

    @Override
    public void setRowId(int i, RowId rowId) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNString(int i, String s) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNCharacterStream(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNClob(int i, NClob nClob) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClob(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setBlob(int i, InputStream inputStream, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNClob(int i, Reader reader, long l) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    // END implements

    @Override
    public boolean isClosed() {
        return (ppStmt.isDeleted() || super.isClosed());
    }
    
    @Override
    public void close() throws SQLException {
        if (!ppStmt.isDeleted()) {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            ppStmt.delete();
        }
        super.close();
    }

    /**
     * Detaches the ResultSet object from the life cycle of JdbcStatement.<br/>
     * WARNING! All parameters are unbinded.
     * @param drs ResultSet that wants to be detached
     * @throws java.sql.SQLException
     */
    @Override
    public void detach(ResultSet drs) throws SQLException {
        validateStatementOpen();
        
        if (rs != null && drs.getStatement() == this) {
            // detach ResultSet
            rs = null;

//            // free bind memory
//            stmt.clearBinding();  // clearするとstep()を呼び出しても次の値が取れなくなる

            // detach and renew Statement
            stmt = db.prepare(sql, ppStmt);
            stmt.reset();
            // MEMO Statement#transferBinding()でコピーするとstep()を呼び出しても次の値が取れなくなる
        }
    }

    /**
     * Detaches the ResultSet object from the life cycle of JdbcPreparedStatement and invoke close() method.
     * @param drs ResultSet that wants to be detached
     * @throws java.sql.SQLException
     * @see #close()
     */
    @Override
    public void close(ResultSet drs) throws SQLException {
        validateStatementOpen();
        
        if (rs != null && drs.getStatement() == this) {
            // detach ResultSet
            rs = null;
            // detach Statement
            stmt = null;
            
            close();
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * Retrieves the parameter index.<br/>
     * invoke org.sqlite.Statement#getParameterIndex(String) method.
     * @param parameterName parameter name
     * @return parameter index
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getParameterIndex(String)
     */
    public int findParameter(String parameterName) throws SQLException {
        validateStatementOpen();

        final int parameterIndex = stmt.getParameterIndex(parameterName);
        if (parameterIndex == 0) {
            throw new SQLException("Not found parameter '" + parameterName + "'.", "42S23");
        }
        return parameterIndex;
    }
    
    /**
     * invoke findParameter(String) and setNull(int, int) method.
     * @param parameterName
     * @param sqlType ignore
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setNull(int, int)
     */
    public void setNull(String parameterName, int sqlType) throws SQLException {
        setNull(findParameter(parameterName), sqlType);
    }

    /**
     * invoke findParameter(String) and setBoolean(int, boolean) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setBoolean(int, boolean)
     */
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        setBoolean(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setByte(int, byte) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setByte(int, byte)
     */
    public void setByte(String parameterName, byte x) throws SQLException {
        setByte(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setShort(int, short) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setShort(int, short)
     */
    public void setShort(String parameterName, short x) throws SQLException {
        setShort(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setInt(int, int) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setInt(int, int)
     */
    public void setInt(String parameterName, int x) throws SQLException {
        setInt(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setLong(int, long) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setLong(int, long)
     */
    public void setLong(String parameterName, long x) throws SQLException {
        setLong(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setFloat(int, float) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setFloat(int, float)
     */
    public void setFloat(String parameterName, float x) throws SQLException {
        setFloat(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setDouble(int, double) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setDouble(int, double)
     */
    public void setDouble(String parameterName, double x) throws SQLException {
        setDouble(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setBigDecimal(int, BigDecimal) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        setBigDecimal(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setString(int, String) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setString(int, String)
     */
    public void setString(String parameterName, String x) throws SQLException {
        setString(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setBytes(int, byte[]) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setBytes(int, byte[])
     */
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        setBytes(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setDate(int, Date) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setDate(int, java.sql.Date)
     */
    public void setDate(String parameterName, Date x) throws SQLException {
        setDate(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setTime(int, Time) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setTime(int, java.sql.Time)
     */
    public void setTime(String parameterName, Time x) throws SQLException {
        setTime(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setTimestamp(int, Timestamp) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        setTimestamp(findParameter(parameterName), x);
    }
    
    /**
     * invoke findParameter(String) and setBlob(int, Blob) method.
     * @param parameterName
     * @param x the parameter value
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setBlob(int, java.sql.Blob)
     */
    public void setBlob(String parameterName, Blob x) throws SQLException {
        setBlob(findParameter(parameterName), x);
    }

    /**
     * invoke findParameter(String) and setDate(int, Date, Calendar) method.
     * @param parameterName
     * @param x the parameter value
     * @param cal
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        setDate(findParameter(parameterName), x, cal);
    }

    /**
     * invoke findParameter(String) and setTime(int, Time, Calendar) method.
     * @param parameterName
     * @param x the parameter value
     * @param cal
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        setTime(findParameter(parameterName), x, cal);
    }

    /**
     * invoke findParameter(String) and setTimestamp(int, Timestamp, Calendar) method.
     * @param parameterName
     * @param x the parameter value
     * @param cal
     * @throws java.sql.SQLException
     * @see #findParameter(String)
     * @see #setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        setTimestamp(findParameter(parameterName), x, cal);
    }

    /**
     * Retrieves the parameter name.<br/>
     * invoke org.sqlite.Statement#getParameterName(int) method.
     * @param parameterIndex the first parameter is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getParameterName(int)
     */
    public String findParameterName(int parameterIndex) throws SQLException {
        validateStatementOpen();
        
        return stmt.getParameterName(parameterIndex);
    }
    
    public static void validateSqlType(int sqlType) throws SQLException {
        if (sqlType != Types.ARRAY
                && sqlType != Types.BIGINT
                && sqlType != Types.BINARY
                && sqlType != Types.BIT
                && sqlType != Types.BLOB
                && sqlType != Types.BOOLEAN
                && sqlType != Types.CHAR
                && sqlType != Types.CLOB
                && sqlType != Types.DATALINK
                && sqlType != Types.DATE
                && sqlType != Types.DECIMAL
                && sqlType != Types.DISTINCT
                && sqlType != Types.DOUBLE
                && sqlType != Types.FLOAT
                && sqlType != Types.INTEGER
                && sqlType != Types.JAVA_OBJECT
                && sqlType != Types.LONGVARBINARY
                && sqlType != Types.LONGVARCHAR
                && sqlType != Types.NULL
                && sqlType != Types.NUMERIC
                && sqlType != Types.OTHER
                && sqlType != Types.REAL
                && sqlType != Types.REF
                && sqlType != Types.SMALLINT
                && sqlType != Types.STRUCT
                && sqlType != Types.TIME
                && sqlType != Types.TIMESTAMP
                && sqlType != Types.TINYINT
                && sqlType != Types.VARBINARY
                && sqlType != Types.VARCHAR) {
            throw new SQLException("Not supported sql types.", "90J22");
        }
    }
    
    protected static int executeUpdate(Statement stmt) throws SQLException {
        if (stmt.producedResultSet()) {
            throw new SQLException("ResultSet was produced.", "02001");
        }
        stmt.execute();
        final Database db = stmt.getDatabase();
        final int cntUpdate = db.changes();
        stmt.reset();
        return cntUpdate;
    }

}
