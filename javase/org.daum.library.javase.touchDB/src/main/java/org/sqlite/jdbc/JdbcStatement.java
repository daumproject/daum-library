package org.sqlite.jdbc;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.sqlite.Database;

/**
 *
 * @author calico
 */
public class JdbcStatement implements Statement {

    /** parent Database object. */
    protected Database db;
    
    private final Connection owner;
    
    /** results of multiple statement */
    protected List<Object> results;

    /** current of result */
    protected int currentRet = -1;
    
    /** current ResultSet object. */
    protected JdbcResultSet rs;
    
    /** last update count. */
    protected int cntUpdate = -1;
    
    private List<String> batch;
    
    public JdbcStatement(Database db, Connection owner) {
        this.db = db;
        this.owner = owner;
    }
    
    // START implements
    public JdbcResultSet executeQuery(String sql) throws SQLException {
        validateStatementOpen();
        // close already opened ResultSets
        closeResultSets();

        if (!execute(db.prepare(sql))) {
            throw new SQLException("No ResultSet was produced.", "02000");
        }
        return rs;
    }

    public int executeUpdate(String sql) throws SQLException {
        validateStatementOpen();
        // close already opened ResultSets
        closeResultSets();

        if (execute(db.prepare(sql))) {
            throw new SQLException("ResultSet was produced.", "02001");
        }
        return cntUpdate;
    }

    public void close() throws SQLException {
        if (db != null) {
            db = null;
            if (rs != null) {
                rs.close();
                rs = null;
            }
            cntUpdate = -1;
            batch = null;
            closeResultSets();
        }
    }

    /**
     * It always returns 0.
     * @return 0
     * @throws java.sql.SQLException
     */
    public int getMaxFieldSize() throws SQLException {
        validateStatementOpen();

        return 0;
    }

    /**
     * Not supporetd yet.
     * @param max ignored
     * @throws UnsupportedOperationException
     */
    public void setMaxFieldSize(int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * It always returns 0.
     * @return 0
     * @throws java.sql.SQLException
     */
    public int getMaxRows() throws SQLException {
        validateStatementOpen();

        return 0;
    }

    /**
     * Not supporetd yet.
     * @param max ignored
     * @throws UnsupportedOperationException
     */
    public void setMaxRows(int max) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @param enable ignored
     * @throws UnsupportedOperationException
     */
    public void setEscapeProcessing(boolean enable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * invoke org.sqlite.Database#getBusyTimeout() method and divide by 1000.
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Database#getBusyTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        validateStatementOpen();
        
        return (db.getBusyTimeout() / 1000);
    }

    /**
     * multiply the seconds by 1000 and invoke org.sqlite.Database#setBusyTimeout(int) method.
     * @param seconds
     * @throws java.sql.SQLException
     * @see org.sqlite.Database#setBusyTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        validateStatementOpen();
        
        db.setBusyTimeout(seconds * 1000);
    }

    /**
     * invoke org.sqlite.Database#interrupt() method.
     * @throws java.sql.SQLException
     * @see org.sqlite.Database#interrupt()
     */
    public void cancel() throws SQLException {
        validateStatementOpen();

        db.interrupt();
    }

    /**
     * It always returns null.
     * @return null
     * @throws java.sql.SQLException
     */
    public SQLWarning getWarnings() throws SQLException {
        validateStatementOpen();

        return null;
    }

    /**
     * nothing.
     * @throws java.sql.SQLException
     */
    public void clearWarnings() throws SQLException {
        validateStatementOpen();

        // nothing
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setCursorName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean execute(String sql) throws SQLException {
        validateStatementOpen();
        // close already opened ResultSets
        closeResultSets();

        List<org.sqlite.Statement> list = db.prepareMultiple(sql);
        if (list.size() == 1) {
            // single statement
            return execute(list.get(0));
        }
        
        // multiple statement
        try {
            final List<Object> tmp = new  ArrayList<Object>(list.size());
            for (final org.sqlite.Statement stmt : list) {
                tmp.add((execute(stmt) ? rs : cntUpdate));
            }
            list = null;
            currentRet = 0;
            results = tmp;
            final Object result = results.get(0);
            if (result instanceof JdbcResultSet) {
                rs = (JdbcResultSet) result;
                cntUpdate = -1;
            } else {
                cntUpdate = (Integer) result;
                rs = null;
            }
        } finally {
            if (list != null) {
                // exception occurred
                for (final org.sqlite.Statement stmt : list) {
                    stmt.close();
                }
            }
        }
        return (rs != null);
    }

    public JdbcResultSet getResultSet() throws SQLException {
        validateStatementOpen();

        return rs;
    }

    public int getUpdateCount() throws SQLException {
        validateStatementOpen();

        return cntUpdate;
    }

    /**
     * invoke getMoreResults(CLOSE_CURRENT_RESULT) method.
     * @return true if the next result is a ResultSet object; false if it is an update count or there are no more results
     * @throws java.sql.SQLException
     * @see #getMoreResults(int)
     */
    public boolean getMoreResults() throws SQLException {
        return getMoreResults(CLOSE_CURRENT_RESULT);
    }

    /**
     * Supported fetch direction is FETCH_FORWARD only.
     * @param direction
     * @throws java.sql.SQLException
     */
    public void setFetchDirection(int direction) throws SQLException {
        JdbcResultSet.validateResultSetFetchDirection(direction);
        validateStatementOpen();
        
        if (direction != ResultSet.FETCH_FORWARD) {
            throw new SQLException("Not supported fetch direction.", "90J23");
        }
    }

    /**
     * It always returns FETCH_FORWARD.
     * @return java.sql.ResultSet.FETCH_FORWARD
     * @throws java.sql.SQLException
     */
    public int getFetchDirection() throws SQLException {
        validateStatementOpen();

        return ResultSet.FETCH_FORWARD;
    }

    /**
     * Not supporetd yet.
     * @param rows ignored
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
        validateStatementOpen();

        return 0;
    }

    /**
     * It always returns CONCUR_READ_ONLY.
     * @return java.sql.ResultSet.CONCUR_READ_ONLY
     * @throws java.sql.SQLException
     */
    public int getResultSetConcurrency() throws SQLException {
        validateStatementOpen();

        return ResultSet.CONCUR_READ_ONLY;
    }

    /**
     * It always returns TYPE_FORWARD_ONLY.
     * @return java.sql.ResultSet.TYPE_FORWARD_ONLY
     * @throws java.sql.SQLException
     */
    public int getResultSetType() throws SQLException {
        validateStatementOpen();

        return ResultSet.TYPE_SCROLL_INSENSITIVE;
    }

    public void addBatch(String sql) throws SQLException {
        validateStatementOpen();

        if (batch == null) {
            batch = new ArrayList<String>();
        }
        batch.add(sql);
    }

    public void clearBatch() throws SQLException {
        validateStatementOpen();

        batch = null;
    }

    public int[] executeBatch() throws SQLException {
        validateStatementOpen();

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
        }
        if (ex != null) {
            throw ex;
        }
        return ret;
    }

    public Connection getConnection() throws SQLException {
        validateStatementOpen();

        return owner;
    }

    /**
     * Moves to this Statement object's next result, deals with any current ResultSet object(s) according to the instructions specified by the given flag, and returns true if the next result is a ResultSet object.
     * @param current Statement.CLOSE_CURRENT_RESULT or Statement.KEEP_CURRENT_RESULT or Statement.CLOSE_ALL_RESULTS
     * @return true if the next result is a ResultSet object; false if it is an update count or there are no more results
     * @throws java.sql.SQLException
     */
    public boolean getMoreResults(int current) throws SQLException {
        validateCurrentResult(current);
        validateStatementOpen();
        
        if (current != KEEP_CURRENT_RESULT) {
            if (results != null && currentRet != -1 && currentRet < results.size()) {
                final int start = (current == CLOSE_ALL_RESULTS ? 0 : currentRet);
                for (int i = start; i <= currentRet; ++i) {
                    final Object result = results.get(i);
                    if (result instanceof JdbcResultSet) {
                        ((JdbcResultSet) result).close();                        
                    }
                }
            }
            if (rs != null) {
                rs.close();
                rs = null;
            }
        }
        
        if (results != null && (currentRet + 1) < results.size()) {
            final Object result = results.get(++currentRet);
            if (result instanceof JdbcResultSet) {
                rs = (JdbcResultSet) result;
                cntUpdate = -1;
            } else {
                cntUpdate = (Integer) result;
                rs = null;
            }
        } else {
            results = null;
            rs = null;
            cntUpdate = -1;
        }
        return (rs != null);
    }

    /**
     * invoke executeQuery("SELECT last_insert_rowid()") method.
     * @return
     * @throws java.sql.SQLException
     * @see <a href="http://sqlite.org/c3ref/last_insert_rowid.html">Last Insert Rowid</a>
     */
    public JdbcResultSet getGeneratedKeys() throws SQLException {
        return executeQuery("SELECT last_insert_rowid()");
    }

    /**
     * 
     * @param sql SQL to be evaluated
     * @param autoGeneratedKeys ignored
     * @return
     * @throws java.sql.SQLException
     */
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        validateAutoGeneretedKeys(autoGeneratedKeys);
        validateStatementOpen();

        return executeUpdate(sql);
    }

    /**
     * Not supporetd yet.
     *  @param sql SQL to be evaluated
     * @param columnIndexes ignored
     * @return
     * @throws UnsupportedOperationException
     */
    public int executeUpdate(String sql, int[] columnIndexes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @param sql SQL to be evaluated
     * @param columnNames ignored
     * @return
     * @throws UnsupportedOperationException
     */
    public int executeUpdate(String sql, String[] columnNames) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param sql SQL to be evaluated
     * @param autoGeneratedKeys ignored
     * @return
     * @throws java.sql.SQLException
     */
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        validateAutoGeneretedKeys(autoGeneratedKeys);
        validateStatementOpen();

        return execute(sql);
    }

    /**
     * Not supporetd yet.
     * @param sql SQL to be evaluated
     * @param columnIndexes ignored
     * @return
     * @throws UnsupportedOperationException
     */
    public boolean execute(String sql, int[] columnIndexes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @param sql SQL to be evaluated
     * @param columnNames ignored
     * @return
     * @throws UnsupportedOperationException
     */
    public boolean execute(String sql, String[] columnNames) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * It always returns CLOSE_CURSORS_AT_COMMIT.
     * @return java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT
     * @throws java.sql.SQLException
     */
    public int getResultSetHoldability() throws SQLException {
        validateStatementOpen();

        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    public boolean isClosed() {
        return (db == null || db.isClosed());
    }

    @Override
    public void setPoolable(boolean b) throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
    // END implements

    @Override
    protected void finalize() throws Throwable {
        if (db != null) {
            Logger.getLogger(JdbcStatement.class.getName()).info("Statement is not closed.");
        }
        close();
        super.finalize();
    }
    
    protected void validateStatementOpen() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Statement is already closed.", "90007");
        }
    }
    
    protected void validateStaticSQL(org.sqlite.Statement stmt) throws SQLException {
        if (stmt.getParameterCount() != 0) {
            stmt.close();
            throw new SQLException("Not static SQL.", "42000");
        }
    }
    
    public static void validateAutoGeneretedKeys(int autoGeneratedKeys) throws SQLException {
        if (autoGeneratedKeys != NO_GENERATED_KEYS
                && autoGeneratedKeys != RETURN_GENERATED_KEYS) {
            throw new SQLException("Not supported auto generated keys.", "90J26");
        }
    }
    
    public static void validateCurrentResult(int current) throws SQLException {
        if (current != CLOSE_CURRENT_RESULT
                && current != KEEP_CURRENT_RESULT
                && current != CLOSE_ALL_RESULTS) {
            throw new SQLException("Not supported current result.", "90J26");
        }
    }
    
    /**
     * Detaches the ResultSet object from the life cycle of JdbcStatement.
     * @param drs ResultSet that wants to be detached
     * @throws java.sql.SQLException
     */
    public void detach(ResultSet drs) throws SQLException {
        validateStatementOpen();

        if (rs != null && rs == drs) {
            rs = null;
        }
        if (results != null) {
            final int pos = results.indexOf(drs);
            if (pos != -1 && pos <= currentRet) {
                --currentRet;
            }
            results.remove(drs);
        }
    }
    
    /**
      * invoke detach() method and close() method.
     * @param drs ResultSet that wants to be detached
     * @throws java.sql.SQLException
     * @see #detach(java.sql.ResultSet)
     * @see #close()
     */
    public void close(ResultSet drs) throws SQLException {
        detach(drs);
        close();
    }
    
    /**
     * invoke org.sqlite.Database#lastInsertRowId() method.
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Database#lastInsertRowId()
     */
    public long getLastInsertRowId() throws SQLException {
        validateStatementOpen();
        
        return db.lastInsertRowId();
    }

    protected boolean execute(org.sqlite.Statement stmt) throws SQLException {
        try {
            rs = null;
            validateStaticSQL(stmt);
            if (stmt.producedResultSet()) {
                rs = new JdbcResultSet(this, stmt);
                cntUpdate = -1;
            } else {
                stmt.execute();
                cntUpdate = db.changes();
            }
            return (rs != null);
            
        } finally {
            if (rs == null) {
                stmt.close();
            }
        }
    }
    
    protected void closeResultSets() throws SQLException {
        if (results != null) {
            final List<Object> list = results;
            results = null;
            currentRet = -1;
            for (final Object result : list) {
                if (result instanceof JdbcResultSet) {
                    ((JdbcResultSet) result).close();
                }
            }
        }
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
