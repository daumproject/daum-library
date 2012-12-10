package org.sqlite.jdbc;

import java.sql.*;
import java.util.List;
import org.sqlite.Database;
import java.util.Map;
import java.util.Properties;

import org.sqlite.auth.Authorizer;
import org.sqlite.event.BusyHandler;
import org.sqlite.event.CollationNeededHandler;
import org.sqlite.event.CommitHook;
import org.sqlite.event.ProgressHandler;
import org.sqlite.event.RollbackHook;
import org.sqlite.event.UpdateHook;
import org.sqlite.profiler.Profiler;
import org.sqlite.profiler.Tracer;
import org.sqlite.swig.SWIGTYPE_p_p_char;
import org.sqlite.text.Collator;
import org.sqlite.udf.Function;

/**
 *
 * @author calico
 */
public class JdbcConnection implements Connection {

    private Database db;
    private TransactionType type;
    private final String url;
    
    public JdbcConnection(Database db, String url) throws SQLException {
        this.db = db;
        db.pragma(
                new String[] {
//                    "encoding = \"UTF-8\"",
                    "case_sensitive_like = on",
//                    "short_column_names = on",
//                    "count_changes = off",
//                    "empty_result_callbacks = off",
//                    "short_column_names = off",
//                    "full_column_names = on",
//                    "show_datatypes = on", for SQLite ver 2.x
                }
            );
        this.url = url;
    }
    
    // START implements
    public JdbcStatement createStatement() throws SQLException {
        validateConnectionOpen();
        
        return new JdbcStatement(db, this);
    }

    public JdbcPreparedStatement prepareStatement(String sql) throws SQLException {
        validateConnectionOpen();
        
        return new JdbcPreparedStatement(db, this, sql);
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public CallableStatement prepareCall(String sql) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public String nativeSQL(String sql) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        validateConnectionOpen();
        
        if (!db.getAutoCommit()) {
            rollback();
        }
        if (!autoCommit && db.getAutoCommit()) {
            db.beginTransaction(type);
        }
    }

    public boolean getAutoCommit() throws SQLException {
        validateConnectionOpen();
        
        return db.getAutoCommit();
    }

    public void commit() throws SQLException {
        validateConnectionOpen();
        
        if (db.getAutoCommit()) {
            throw new SQLException("Connection is auto commit mode.", "90J10");
        }
        db.commitTransaction();
    }

    public void rollback() throws SQLException {
        validateConnectionOpen();
        
        if (db.getAutoCommit()) {
            throw new SQLException("Connection is auto commit mode.", "90J10");
        }
        db.rollbackTransaction();
    }

    public void close() throws SQLException {
        if (!isClosed()) {
            if (!db.getAutoCommit()) {
                rollback();
            }
            db.close();
            db = null;
        }
    }

    public boolean isClosed() throws SQLException {
        return (db == null || db.isClosed());
    }

    public JdbcDatabaseMetaData getMetaData() throws SQLException {
        return new JdbcDatabaseMetaData(db, this, url);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        validateConnectionOpen();
        
        if (!db.getAutoCommit()) {
            throw new SQLException("Connection is transaction mode.", "90J11");
        }
        if (readOnly != db.isReadOnly()) {
            throw new UnsupportedOperationException("Not supported the change access mode.");
        }
    }

    /**
     * Retrieves whether this Connection object is in read-only mode.
     * @return true if this Connection object is read-only; false otherwise
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        validateConnectionOpen();
        
        return db.isReadOnly();
    }

    /**
     * Catalog is not supported yet.
     * @param catalog ignored
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public void setCatalog(String catalog) throws SQLException {
        validateConnectionOpen();
        
        // nothing
    }

    /**
     * Catalog is not supported yet.
     * It always returns null.
     * @return null
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public String getCatalog() throws SQLException {
        validateConnectionOpen();
        
        return null;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        validateConnectionOpen();
        
        if (level != TRANSACTION_SERIALIZABLE) {
            throw new SQLException("Not supported isolation level.", "90J20");
        }
        // nothing
    }

    /**
     * It always returns TRANSACTION_SERIALIZABLE.
     * @return java.sql.Connection.TRANSACTION_SERIALIZABLE
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public int getTransactionIsolation() throws SQLException {
        validateConnectionOpen();
        
        return TRANSACTION_SERIALIZABLE;
    }

    /**
     * SQL Warning is not supported yet.
     * It always returns null.
     * @return null
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public SQLWarning getWarnings() throws SQLException {
        validateConnectionOpen();
        
        return null;
    }

    /**
     * SQL Warning is not supported yet.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public void clearWarnings() throws SQLException {
        validateConnectionOpen();
        
        // nothing
    }

    public JdbcStatement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        JdbcResultSet.validateResultSetType(resultSetType);
        JdbcResultSet.validateResultSetConcurrency(resultSetConcurrency);
        validateConnectionOpen();
        
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
            throw new SQLException("Not supported result set type.", "90J24");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLException("Not supported result set concurrency.", "90J25");
        }
        
        return new JdbcStatement(db, this);
    }

    public JdbcPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        JdbcResultSet.validateResultSetType(resultSetType);
        JdbcResultSet.validateResultSetConcurrency(resultSetConcurrency);
        validateConnectionOpen();
        
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
            throw new SQLException("Not supported result set type.", "90J24");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLException("Not supported result set concurrency.", "90J25");
        }
        
        return new JdbcPreparedStatement(db, this, sql);        
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Map<String, Class<?>> getTypeMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void setTypeMap(Map<String, Class<?>> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Supported result set holdability is ResultSet.CLOSE_CURSORS_AT_COMMIT only.
     * @param holdability
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public void setHoldability(int holdability) throws SQLException {
        validateConnectionOpen();

        if (holdability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw new SQLException("Not supported result set holdability.", "90J21");
        }
        // nothing
    }

    /**
     * It always returns CLOSE_CURSORS_AT_COMMIT.
     * @return java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public int getHoldability() throws SQLException {
        validateConnectionOpen();

        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Savepoint setSavepoint() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public Savepoint setSavepoint(String name)  {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void rollback(Savepoint savepoint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void releaseSavepoint(Savepoint savepoint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JdbcStatement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        JdbcResultSet.validateResultSetType(resultSetType);
        JdbcResultSet.validateResultSetConcurrency(resultSetConcurrency);
        JdbcResultSet.validateResultSetHoldability(resultSetHoldability);
        validateConnectionOpen();

        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
            throw new SQLException("Not supported result set type.", "90J24");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLException("Not supported result set concurrency.", "90J25");
        }
        if (resultSetHoldability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw new SQLException("Not supported result set holdability.", "90J21");
        }
        
        return new JdbcStatement(db, this);
    }

    public JdbcPreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        JdbcResultSet.validateResultSetType(resultSetType);
        JdbcResultSet.validateResultSetConcurrency(resultSetConcurrency);
        JdbcResultSet.validateResultSetHoldability(resultSetHoldability);
        validateConnectionOpen();
        
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
            throw new SQLException("Not supported result set type.", "90J24");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLException("Not supported result set concurrency.", "90J25");
        }
        if (resultSetHoldability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw new SQLException("Not supported result set holdability.", "90J21");
        }
        
        return new JdbcPreparedStatement(db, this, sql);        
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param sql SQL to be evaluated
     * @param autoGeneratedKeys ignored
     * @return
     * @throws java.sql.SQLException When this method is called on a closed connection.
     */
    public JdbcPreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        JdbcStatement.validateAutoGeneretedKeys(autoGeneratedKeys);
        validateConnectionOpen();

        return new JdbcPreparedStatement(db, this, sql);
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public JdbcPreparedStatement prepareStatement(String sql, int[] columnIndexes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public JdbcPreparedStatement prepareStatement(String sql, String[] columnNames) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Clob createClob() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Blob createBlob() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public NClob createNClob() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isValid(int i) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClientInfo(String s, String s1) throws SQLClientInfoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getClientInfo(String s) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Array createArrayOf(String s, Object[] objects) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Struct createStruct(String s, Object[] objects) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    // END implements
    
    protected void validateConnectionOpen() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Connection is already closed.", "90007");
        }    
    }

    /**
     * 
     * @param type  'DEFERRED', 'IMMEDIATE', 'EXCLUSIVE'
     * @see <a href="http://www.sqlite.org/lang_transaction.html">BEGIN TRANSACTION</a>
     */
    public void setTransactionType(TransactionType type) throws SQLException {
        validateConnectionOpen();

        this.type = type;
    }

    /**
     * 
     * @return transaction type
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://www.sqlite.org/lang_transaction.html">BEGIN TRANSACTION</a>
     */
    public TransactionType getTransactionType() throws SQLException {
        validateConnectionOpen();

        return type;
    }

    /**
     * invoke sqlite3_create_function() function.
     * @param func User-Defined function
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_create_function() function is not SQLITE_OK.
     * @see org.sqlite.Database#createFunction(org.sqlite.udf.Function)
     */
    public void createFunction(Function func) throws SQLException {
        validateConnectionOpen();

        db.createFunction(func);
    }
    
    /**
     * invoke sqlite3_create_function() function.
     * @param func User-Defined function
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_create_function() function is not SQLITE_OK.
     * @see org.sqlite.Database#dropFunction(org.sqlite.udf.Function)
     */
    public void dropFunction(Function func) throws SQLException {
        validateConnectionOpen();

        db.dropFunction(func);
    }
    
    /**
     * invoke sqlite3_create_collation() function.
     * @param col User-Defined Collating Sequences
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_create_collation() function is not SQLITE_OK.
     * @see org.sqlite.Database#createCollationSequence(org.sqlite.text.Collator)
     */
    public void createCollationSequence(Collator col) throws SQLException {
        validateConnectionOpen();

        db.createCollationSequence(col);
    }
    
    /**
     * invoke sqlite3_create_function() function.
     * @param col User-Defined Collating Sequences
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_create_collation() function is not SQLITE_OK.
     * @see org.sqlite.Database#dropCollationSequence(org.sqlite.text.Collator)
     */
    public void dropCollationSequence(Collator col) throws SQLException {
        validateConnectionOpen();

        db.dropCollationSequence(col);
    }
    
    /**
     * invoke sqlite3_set_authorizer() function.
     * @param auth authorizer
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_set_authorizer() function is not SQLITE_OK.
     * @see org.sqlite.Database#setAuthorizer(org.sqlite.auth.Authorizer)
     */
    public void setAuthorizer(Authorizer auth) throws SQLException {
        validateConnectionOpen();

        db.setAuthorizer(auth);
    }
    
    /**
     * invoke sqlite3_set_authorizer() function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_set_authorizer() function is not SQLITE_OK.
     * @see org.sqlite.Database#clearAuthorizer()
     */
    public void clearAuthorizer() throws SQLException {
        validateConnectionOpen();

        db.clearAuthorizer();
    }
    
    /**
     * invoke sqlite3_busy_timeout() function.
     * @param ms milliseconds
     * @throws java.sql.SQLException When this method is called on a closed connection. 
     * @see org.sqlite.Database#setBusyTimeout(int)
     */
    public void setBusyTimeout(int ms) throws SQLException {
        validateConnectionOpen();

        db.setBusyTimeout(ms);
    }

    /**
     * Returns the timeout(ms) value.
     * @return timeout(ms) value.
     * @throws java.sql.SQLException When this method is called on a closed connection. 
     * @see org.sqlite.Database#getBusyTimeout()
     */
    public int getBusyTimeout() throws SQLException {
        validateConnectionOpen();

        return db.getBusyTimeout();
    }

    /**
     * invoke sqlite3_busy_handler() function.
     * @param busy busy handler
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_busy_handler() function is not SQLITE_OK.
     * @see org.sqlite.Database#setBusyHandler(BusyHandler)
     */
    public void setBusyHandler(BusyHandler busy) throws SQLException {
        validateConnectionOpen();

        db.setBusyHandler(busy);
    }
    
    /**
     * invoke sqlite3_busy_handler() function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_busy_handler() function is not SQLITE_OK.
     * @see org.sqlite.Database#clearBusyHandler()
     */
    public void clearBusyHandler() throws SQLException {
        validateConnectionOpen();

        db.clearBusyHandler();
    }

    /**
     * invoke sqlite3_collation_needed() function.
     * @param needed collation needed handler
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_collation_needed() function is not SQLITE_OK.
     * @see org.sqlite.Database#setCollationNeededHandler(CollationNeededHandler)
     */
    public void setCollationNeededHandler(CollationNeededHandler needed) throws SQLException {
        validateConnectionOpen();

        db.setCollationNeededHandler(needed);
    }
    
    /**
     * invoke sqlite3_collation_needed() function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_collation_needed() function is not SQLITE_OK.
     * @see org.sqlite.Database#clearCollationNeededHandler()
     */
    public void clearCollationNeededHandler() throws SQLException {
        validateConnectionOpen();

        db.clearCollationNeededHandler();
    }

    /**
     * invoke sqlite3_progress_handler() function.
     * @param prog progress handler
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#setProgressHandler(ProgressHandler)
     */
    public void setProgressHandler(ProgressHandler prog) throws SQLException {
        validateConnectionOpen();

        db.setProgressHandler(prog);
    }
    
    /**
     * invoke sqlite3_progress_handler() function.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#clearProgressHandler()
     */
    public void clearProgressHandler() throws SQLException {
        validateConnectionOpen();

        db.clearProgressHandler();
    }
    
    /**
     * invoke sqlite3_commit_hook() function.
     * @param hook commit hoot
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#setCommitHook(CommitHook)
     */
    public void setCommitHook(CommitHook hook) throws SQLException {
        validateConnectionOpen();

        db.setCommitHook(hook);
    }
    
    /**
     * invoke sqlite3_commit_hook() function.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#clearCommitHook()
     */
    public void clearCommitHook() throws SQLException {
        validateConnectionOpen();

        db.clearCommitHook();
    }
    
    /**
     * invoke sqlite3_rollback_hook() function.
     * @param hook rollback hoot
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#setRollbackHook(RollbackHook)
     */
    public void setRollbackHook(RollbackHook hook) throws SQLException {
        validateConnectionOpen();

        db.setRollbackHook(hook);
    }
    
    /**
     * invoke sqlite3_rollback_hook() function.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#clearRollbackHook()
     */
    public void clearRollbackHook() throws SQLException {
        validateConnectionOpen();

        db.clearRollbackHook();
    }
    
    /**
     * invoke sqlite3_update_hook() function.
     * @param hook update hoot
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#setUpdateHook(UpdateHook)
     */
    public void setUpdateHook(UpdateHook hook) throws SQLException {
        validateConnectionOpen();

        db.setUpdateHook(hook);
    }
    
    /**
     * invoke sqlite3_update_hook() function.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#clearUpdateHook()
     */
    public void clearUpdateHook() throws SQLException {
        validateConnectionOpen();

        db.clearUpdateHook();
    }
    
    /**
     * invoke sqlite3_profile() function.
     * @param profiler profiler
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#setProfiler(org.sqlite.profiler.Profiler)
     */
    public void setProfiler(Profiler profiler) throws SQLException {
        validateConnectionOpen();

        db.setProfiler(profiler);
    }
    
    /**
     * invoke sqlite3_profile() function.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#clearProfiler()
     */
    public void clearProfiler() throws SQLException {
        validateConnectionOpen();

        db.clearProfiler();
    }
    
    /**
     * invoke sqlite3_trace() function.
     * @param tracer tracer
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#setTracer(org.sqlite.profiler.Tracer)
     */
    public void setTracer(Tracer tracer) throws SQLException {
        validateConnectionOpen();

        db.setTracer(tracer);
    }
    
    /**
     * invoke sqlite3_trace() function.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#clearTracer()
     */
    public void clearTracer() throws SQLException {
        validateConnectionOpen();

        db.clearTracer();
    }
    
    /**
     * invoke sqlite3_enable_shared_cache(on) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_enable_shared_cache() function is not SQLITE_OK.
     * @see org.sqlite.Database#enableSharedCache()
     */
    public static void enableSharedCache() throws SQLException {
        Database.enableSharedCache();
    }
    
    /**
     * invoke sqlite3_enable_shared_cache(off) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_enable_shared_cache() function is not SQLITE_OK.
     * @see org.sqlite.Database#disableSharedCache()
     */
    public static void disableSharedCache() throws SQLException {
        Database.disableSharedCache();
    }
    
    /**
     * invoke sqlite3_errcode() function.
     * @return the numeric result code or extended result code for the most recent failed sqlite3_* API call associated with sqlite3 handle 'db'.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#getLastError()
     * @see #enableExtendedResultCodes()
     * @see #disableExtendedResultCodes()
     */
    public int getLastError() throws SQLException {
        validateConnectionOpen();

        return db.getLastError();
    }
    
    /**
     * invoke sqlite3_errmsg() function.
     * @return the numeric result code or extended result code for the most recent failed sqlite3_* API call associated with sqlite3 handle 'db'.
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#getLastErrorMessage()
     * @see #getLastError()
     * @see #enableExtendedResultCodes()
     * @see #disableExtendedResultCodes()
     */
    public String getLastErrorMessage() throws SQLException {
        validateConnectionOpen();

        return db.getLastErrorMessage();
    }
    
    /**
     * invoke sqlite3_get_table() function.
     * @param sql SQL to be evaluated
     * @param errmsg Error message written here
     * @return Results of the query
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_get_table() function is not SQLITE_OK.
     * @see org.sqlite.Database#getTable(String, org.sqlite.swig.SWIGTYPE_p_p_char)
     */
    public List<String[]> getTable(String sql, SWIGTYPE_p_p_char errmsg) throws SQLException {
        validateConnectionOpen();

        return db.getTable(sql, errmsg);
    }
    
    /**
     * invoke sqlite3_extended_result_codes(on) function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_extended_result_codes() function is not SQLITE_OK.
     * @see org.sqlite.Database#enableExtendedResultCodes()
     */
    public void enableExtendedResultCodes() throws SQLException {
        validateConnectionOpen();

        db.enableExtendedResultCodes();
    }
    
    /**
     * invoke sqlite3_extended_result_codes(off) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_extended_result_codes() function is not SQLITE_OK.
     * @see org.sqlite.Database#disableExtendedResultCodes()
     */
    public void disableExtendedResultCodes() throws SQLException {
        validateConnectionOpen();

        db.disableExtendedResultCodes();
    }
    
    /**
     * invoke sqlite3_enable_load_extension(on) function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_enable_load_extension() function is not SQLITE_OK.
     * @see org.sqlite.Database#enableLoadExtention()
     */
    public void enableLoadExtention() throws SQLException {
        validateConnectionOpen();

        db.enableLoadExtention();
    }
    
    /**
     * invoke sqlite3_enable_load_extension(off) function.
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_enable_load_extension() function is not SQLITE_OK.
     * @see org.sqlite.Database#disableLoadExtention()
     */
    public void disableLoadExtention() throws SQLException {
        validateConnectionOpen();

        db.disableLoadExtention();
    }
    
    /**
     * invoke sqlite3_load_extension() function.
     * @param filename the Name of the shared library containing extension
     * @param entryPoint the Entry point. Use "sqlite3_extension_init" if null.
     * @param errmsg Error message written here
     * @throws java.sql.SQLException When this method is called on a closed connection. When the return value of the sqlite3_load_extension() function is not SQLITE_OK.
     * @see org.sqlite.Database#loadExtention(String, String, org.sqlite.swig.SWIGTYPE_p_p_char)
     */
    public void loadExtention(String filename, String entryPoint, SWIGTYPE_p_p_char errmsg) throws SQLException {
        validateConnectionOpen();

        db.loadExtention(filename, entryPoint, errmsg);
    }
    
    /**
     * invoke sqlite3_reset_auto_extension() function.
     * @see org.sqlite.Database#resetAutoExtention()
     */
    public static void resetAutoExtention() {
        Database.resetAutoExtention();
    }
    
    /**
     * invoke sqlite3_limit() function.
     * @param id an one of the limit categories that define a class of constructs to be size limited
     * @param newVal the new limit value
     * @return the old limit value
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see org.sqlite.Database#limit(int, int)
     */
    public int limit(int id, int newVal) throws SQLException {
        validateConnectionOpen();

        return db.limit(id, newVal);
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
