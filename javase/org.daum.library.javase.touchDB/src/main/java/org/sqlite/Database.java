package org.sqlite;

import org.sqlite.jdbc.TransactionType;
import org.sqlite.schema.ColumnMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.sqlite.auth.Authorizer;
import org.sqlite.callback.Callback;
import org.sqlite.callback.ExecCallback;
import org.sqlite.event.BusyHandler;
import org.sqlite.event.CommitHook;
import org.sqlite.event.ProgressHandler;
import org.sqlite.event.RollbackHook;
import org.sqlite.event.UpdateHook;
import org.sqlite.jdbc.JdbcSQLException;
import org.sqlite.swig.SWIGTYPE_p_int;
import org.sqlite.swig.SWIGTYPE_p_p_char;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;
import org.sqlite.event.CollationNeededHandler;
import org.sqlite.io.Closeable;
import org.sqlite.profiler.Profiler;
import org.sqlite.profiler.Tracer;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_stmt;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_vfs;
import org.sqlite.swig.SWIGTYPE_p_void;
import org.sqlite.text.Collator;
import org.sqlite.udf.Function;
import static org.sqlite.swig.SQLite3.*;

/**
 * sqlite3 wrapper class.<br/>
 * @author calico
 */
public class Database implements Closeable {
    /**
     * database properties
     */
    protected final Map<String, String> info;
    
    private final boolean isInMemory;
    private final SQLite3PtrPtr ppDb;
    private final boolean isReadOnly;
    private List<Closeable> handles;
    private List<Function> functions;
    private Authorizer authorizer;
    private BusyHandler busyHandler;
    private CollationNeededHandler collNeeded;
    private ProgressHandler progressHandler;
    
    /** timeout(ms) : sqlite3_busy_timeout */
    private int timeout;
    
    /**
     * open database.
     * @param filename database file path
     * @param info database properties. Changes the temporary directory by "TEMP_DIR" property.
     * @throws java.sql.SQLException When the return value of the sqlite3_open() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/temp_directory.html">Name Of The Folder Holding Temporary Files</a>
     */
    public Database(String filename, Map<String, String> info) throws SQLException {
        this.info = info;
        this.isInMemory
                = (filename == null || getInMemoryFileName().equals(filename));
        this.ppDb = new SQLite3PtrPtr();

        if (info != null) {
            // set temporary directory
            if (info.containsKey("TEMP_DIR")) {
                set_sqlite3_temp_directory(info.get("TEMP_DIR"));
            }
            this.isReadOnly = info.containsKey("READ_ONLY");
            
        } else {
            this.isReadOnly = false;
        }
        
        // open database
        open(filename);
        
        // execute PRAGMA commands
        if (info != null && !info.isEmpty()) {
            final StringBuilder pragmas = new StringBuilder();
            for (final String key : info.keySet()) {
                if (key.startsWith("PRAGMA")) {
                    pragmas.append(key);
                    final String value = info.get(key);
                    if (value.length() != 0) {
                        pragmas.append("=").append(value);
                    }
                    pragmas.append(";");
                }
            }
            if (pragmas.length() != 0) {
                execute(pragmas.toString());
            }
        }
    }
    
    /**
     * It always returns "SQLite".
     * @return "SQLite"
     */
    public static String getProductName() {
        return "SQLite";
    }
    
    /**
     * true is returned for the In-Memory mode.
     * @return true is returned for the In-Memory mode.
     */
    public boolean isInMemoryMode() {
        return isInMemory;
    }
    
    /**
     * Returns the database handle.
     * @return the database handle.
     * @see <a href="http://sqlite.org/c3ref/sqlite3.html">Database Connection Handle</a>
     */
    SWIGTYPE_p_sqlite3 getHandle() {
        return ppDb.getSQLite3Ptr();
    }
    
    /**
     * invoke sqlite3_open() or sqlite3_open_v2() function.
     * @param filename database file path
     * @throws java.sql.SQLException When the return value of the sqlite3_open() or sqlite3_open_v2() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/open.html">Opening A New Database Connection</a>
     */
    protected void open(String filename) throws SQLException {
        int ret = 0;
        if (info == null) {
            ret = sqlite3_open(filename, ppDb);
            
        } else {
            final int flag
                    = (isReadOnly
                        ? SQLITE_OPEN_READONLY
                        : (info.containsKey("OPEN_EXIST")
                            ? SQLITE_OPEN_READWRITE
                            : SQLITE_OPEN_READWRITE | SQLITE_OPEN_CREATE));
            final String vfs = info.get("VFS");
            ret = sqlite3_open_v2(filename, ppDb, flag, vfs);
        }
        ppDb.allocateHandle();
        if (ret != SQLITE_OK) {
            SWIGTYPE_p_sqlite3 db = getHandle();
            SQLException ex = new JdbcSQLException(ret, db);
            ppDb.delete();
            throw ex;
        }
    }
    
    /**
     * Retrieves whether this database is in read-only mode.
     * @return true if so; false otherwise
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }
    
    /**
     * Retrieves whether this Database object has been closed.
     * @return true if this Database object is closed. false if it is still open.
     */
    public boolean isClosed() {
        return (ppDb.isDeleted());
    }
    
    /**
     * invoke sqlite3_close() function.
     * @throws java.sql.SQLException When the return value of the sqlite3_close() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/close.html">Closing A Database Connection</a>
     */
    public void close() throws SQLException {
        if (!isClosed()) {
            // close all handles
            closeHandles();
            // clear callback object
            clearCallbacks();
            
            // SQLITE_ENABLE_MEMORY_MANAGEMENTが定義された状態でビルドされたSQLiteを使用している場合、
            // sqlite3_open()を呼び出したスレッドとは異なるスレッドからsqlite3_close()を呼び出すと
            // ACCESS_VIOLATION例外が発生してJVMがクラッシュするので要注意！
            // 尚、SQLITE_ENABLE_MEMORY_MANAGEMENTを定義してビルドする必要がある環境は組み込みデバイスなどのメモリが極小の環境のみ。
            // @see http://readlist.com/lists/sqlite.org/sqlite-users/0/572.html
            final SWIGTYPE_p_sqlite3 db = getHandle();
            int ret = sqlite3_close(db);
            if (ret != SQLITE_OK) {
                throw new JdbcSQLException(ret, db);
            }
            ppDb.delete();
            
            // delete callbacks object
            deleteCallbacks();
            // clear temporary directory
            if (info != null && info.containsKey("TEMP_DIR")) {
                set_sqlite3_temp_directory(null);
            }
        }
    }
    
    /**
     * invoke sqlite3_get_autocommit() function.
     * @return  true if auto commit mode.
     * @see <a href="http://sqlite.org/c3ref/get_autocommit.html">Test To See If The Database Is In Auto-Commit Mode</a>
     */
    public boolean getAutoCommit() {
        return (sqlite3_get_autocommit(getHandle()) != 0);
    }
    
    /**
     * invoke sqlite3_busy_timeout() function.
     * @param ms milliseconds
     * @throws java.sql.SQLException When the return value of the sqlite3_busy_timeout() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/busy_timeout.html">Set A Busy Timeout</a>
     */
    public void setBusyTimeout(int ms) throws SQLException {
        clearBusyHandler();

        final SWIGTYPE_p_sqlite3 db = getHandle();
        int ret = sqlite3_busy_timeout(db, ms);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
        timeout = (ms < 1 ? 0 : ms);
    }

    /**
     * Returns the value of timeout(ms).
     * @return  timeout(ms) value.
     */
    public int getBusyTimeout() {
        return timeout;
    }
    
    /**
     * invoke sqlite3_exec() function.
     * @param sql SQL to be evaluated
     * @throws java.sql.SQLException When the return value of the sqlite3_exec() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/exec.html">One-Step Query Execution Interface</a>
     * @see #execute(String, org.sqlite.callback.ExecCallback, org.sqlite.swig.SWIGTYPE_p_p_char)
     */
    public void execute(String sql) throws SQLException {
        execute(sql, null, null);
    }
    
    /**
     * invoke sqlite3_exec() function.
     * @param sql SQL to be evaluated
     * @param callback callback object
     * @param errmsg Error message written here
     * @throws java.sql.SQLException When the return value of the sqlite3_exec() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/exec.html">One-Step Query Execution Interface</a>
     */
    public void execute(String sql, ExecCallback callback, SWIGTYPE_p_p_char errmsg) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        int ret = 0;
        while (((ret = sqlite3_exec(db, sql, callback, errmsg)) == SQLITE_BUSY)
                && (timeout == 0)) {
            // waiting...
        }
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * execute PRAGMA commands by sqlite3_exec() finction.
     * @param commands the command list <em>without semicolon</em>
     * @throws java.sql.SQLException When the return value of the sqlite3_exec() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/pragma.html">Pragma statements supported by SQLite</a>
     * @see #execute(String)
     */
    public void pragma(String[] commands) throws SQLException {
        final StringBuilder sql = new StringBuilder();
        for (final String cmd : commands) {
            sql.append("PRAGMA ").append(cmd).append(";");
        }
        execute(sql.toString());
    }
    
    /**
     * begin transaction.
     * @param type transaction type.
     * @throws java.sql.SQLException When the return value of the sqlite3_exec() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/lang_transaction.html">BEGIN TRANSACTION</a>
     */
    public void beginTransaction(TransactionType type) throws SQLException {
        closeHandles();
        if (type == null) {
            execute("BEGIN");
        } else {
            execute("BEGIN " + type);
        }
    }
    
    /**
     * commit toransaction.
     * @throws java.sql.SQLException When the return value of the sqlite3_exec() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/lang_transaction.html">BEGIN TRANSACTION</a>
     */
    public void commitTransaction() throws SQLException {
        closeHandles();
        execute("COMMIT");
    }
    
    /**
     * rollback transaction.
     * @throws java.sql.SQLException When the return value of the sqlite3_exec() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/lang_transaction.html">BEGIN TRANSACTION</a>
     */
    public void rollbackTransaction() throws SQLException {
        closeHandles();
        execute("ROLLBACK");
    }
    
    /**
     * create MANAGED Statement instance.
     * @param sql SQL to be evaluated
     * @param ppStmt SQLite3StmtPtrPtr object
     * @return Statement object
     * @throws java.sql.SQLException When the return value of the sqlite3_prepare_v2() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/prepare.html">Compiling An SQL Statement</a>
     */
    public Statement prepare(String sql, SQLite3StmtPtrPtr ppStmt) throws SQLException {
        if (sql == null) {
            throw new NullPointerException("sql is null.");
        }
        if (ppStmt == null) {
            throw new NullPointerException("ppStmt is null.");
        }
        
        final SWIGTYPE_p_sqlite3 db = getHandle();
        int ret = sqlite3_prepare(db, sql, -1, ppStmt, null);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
        // TODO UTF-16系のメソッドに対応したStatement16を作成し、「PRAGMA encoding;」の値に対応したStatementクラスを返すようにする！
        return new Statement(this, ppStmt.getSQLite3StmtPtr());
    }
    
    /**
     * create UNMANAGED Statement instance.
     * @param sql SQL to be evaluated
     * @return Statement object
     * @throws java.sql.SQLException When the return value of the sqlite3_prepare_v2() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/prepare.html">Compiling An SQL Statement</a>
     */
    public Statement prepare(String sql) throws SQLException {
        if (sql == null) {
            throw new NullPointerException("sql is null.");
        }

        final SWIGTYPE_p_sqlite3 db = getHandle();
        final SQLite3StmtPtrPtr ppStmt = new SQLite3StmtPtrPtr();
        int ret = sqlite3_prepare(db, sql, -1, ppStmt, null);
        if (ret != SQLITE_OK) {
            ppStmt.delete();
            throw new JdbcSQLException(ret, db);
        }
        // TODO UTF-16系のメソッドに対応したStatement16を作成し、「PRAGMA encoding;」の値に対応したStatementクラスを返すようにする！
        return new Statement(this, ppStmt);
    }
    
    /**
     * create multiple UNMANAGED Statement instance.
     * @param sql SQL to be evaluated
     * @return array of Statement
     * @throws java.sql.SQLException When the return value of the sqlite3_prepare_v2() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/prepare.html">Compiling An SQL Statement</a>
     */
    public List<Statement> prepareMultiple(String sql) throws SQLException {
        if (sql == null) {
            throw new NullPointerException("sql is null.");
        }

        final SWIGTYPE_p_sqlite3 db = getHandle();
        final List<SQLite3StmtPtrPtr> stmts = new ArrayList<SQLite3StmtPtrPtr>();
        final String[] tail = new String[1];
        do {
            final SQLite3StmtPtrPtr ppStmt = new SQLite3StmtPtrPtr();
            stmts.add(ppStmt);
            final int ret = sqlite3_prepare(db, sql, -1, ppStmt, tail);
            if (ret != SQLITE_OK) {
                for (final SQLite3StmtPtrPtr stmt : stmts) {
                    stmt.delete();
                }
                throw new JdbcSQLException(ret, db);
            }
            sql = tail[0].trim();
        } while (sql.length() > 0);
        
        // TODO UTF-16系のメソッドに対応したStatement16を作成し、「PRAGMA encoding;」の値に対応したStatementクラスを返すようにする！
        final List<Statement> ret = new  ArrayList<Statement>(stmts.size());
        for (final SQLite3StmtPtrPtr stmt : stmts) {
            ret.add(new Statement(this, stmt));
        }
        return ret;
    }
    
    /**
     * invoke sqlite3_interrupt() function.
     * @see <a href="http://sqlite.org/c3ref/interrupt.html">Interrupt A Long-Running Query</a>
     */
    public void interrupt() {
        sqlite3_interrupt(getHandle());
    }
    
    /**
     * invoke sqlite3_changes() function.
     * @return the number of database rows that were changed or inserted or deleted by the most recently completed SQL statement on the connection specified.
     * @see <a href="http://sqlite.org/c3ref/changes.html">Count The Number Of Rows Modified</a>
     * @see #totalChanges()
     */
    public int changes() {
        return sqlite3_changes(getHandle());
    }
    
    /**
     * invoke sqlite3_total_changes() function.
     * @return the number of row changes caused by INSERT, UPDATE or DELETE statements since the database handle was opened.
     * @see <a href="http://sqlite.org/c3ref/total_changes.html">Total Number Of Rows Modified</a>
     * @see #changes()
     */
    public int totalChanges() {
        return sqlite3_total_changes(getHandle());
    }
    
    /**
     * invoke sqlite3_last_insert_rowid() function.
     * @return the rowid of the most recent successful INSERT into the database from the database connection.
     * @see <a href="http://sqlite.org/c3ref/last_insert_rowid.html">Last Insert Rowid</a>
     */
    public long lastInsertRowId() {
        return sqlite3_last_insert_rowid(getHandle());
    }
    
    /**
     * invoke sqlite3_table_column_metadata() function.
     * @param dbName database name
     * @param tableName table name
     * @param columnName column name
     * @return the meta-data about a specific column of a specific database table accessible using the connection.
     * @throws java.sql.SQLException When the return value of the sqlite3_table_column_metadata() function is not SQLITE_OK.
     * @see <a href="http://www.sqlite.org/c3ref/table_column_metadata.html">Extract Metadata About A Column Of A Table</a>
     */
    public ColumnMetaData getColumnMetaData(String dbName, String tableName, String columnName) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        SWIGTYPE_p_p_char dataType = null;
        SWIGTYPE_p_p_char collSeq = null;
        SWIGTYPE_p_int notNull = null;
        SWIGTYPE_p_int primaryKey = null;
        SWIGTYPE_p_int autoInc = null;
        try {
            dataType = new_p_p_char();
            collSeq = new_p_p_char();
            notNull = new_p_int();
            primaryKey = new_p_int();
            autoInc = new_p_int();
        
            int ret = sqlite3_table_column_metadata(
                            db, dbName, tableName, columnName,
                            dataType, collSeq, notNull, primaryKey, autoInc
                        );
            if (ret != SQLITE_OK) {
                throw new JdbcSQLException(ret, db);
            }
            
            return new ColumnMetaData(
                            get_p_char(dataType),
                            get_p_char(collSeq),
                            get_int(notNull),
                            get_int(primaryKey),
                            get_int(autoInc)
                        );
        } finally {
            if (dataType != null) {
                delete_p_p_char(dataType);
            }
            if (collSeq != null) {
                delete_p_p_char(collSeq);
            }
            if (notNull != null) {
                delete_p_int(notNull);
            }
            if (primaryKey != null) {
                delete_p_int(primaryKey);
            }
            if (autoInc != null) {
                delete_p_int(autoInc);
            }
        }
    }
    
    /**
     * invoke from Statement#&lt;init&gt;() and Blob#&lt;init&gt;().
     * @param handle the closeable object
     */
    void add(Closeable handle) {
        if (handles == null) {
            handles = new ArrayList<Closeable>();
        }
        if (handles.contains(handle)) {
            throw new IllegalArgumentException("handle is already exist.");
        }
        handles.add(handle);
    }
    
    /**
     * invoke from Statement#close() and Blob#close().
     * @param handle the closeable object
     */
    void remove(Closeable handle) {
        if (handles != null) {
            if (!handles.remove(handle)) {
                throw new IllegalArgumentException("handle is not exist.");
            }
        }
    }
    
    /**
     * close all handle object.
     */
    private void closeHandles() {
        if (handles != null) {
            final List<Closeable> list = handles;
            handles = null;
            for (final Closeable handle : list) {
                try {
                    handle.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Database.class.getName()).info(ex.toString());
                }
            }
        }
    }
    
    /**
     * clear callback objects.
     */
    private void clearCallbacks() {
        clearCommitHook();
        clearProfiler();
        clearRollbackHook();
        clearTracer();
        clearUpdateHook();
    }
    
    /**
     * delete callback objects.
     */
    private void deleteCallbacks() {
        final List<Callback> callbacks = new ArrayList<Callback>();
        if (functions != null) {
            callbacks.addAll(functions);
            functions = null;
        }
        if (authorizer != null) {
            callbacks.add(authorizer);
            authorizer = null;
        }
        if (busyHandler != null) {
            callbacks.add(busyHandler);
            busyHandler = null;
        }
        if (collNeeded != null) {
            callbacks.add(collNeeded);
            collNeeded = null;
        }
        if (progressHandler != null) {
            callbacks.add(progressHandler);
            progressHandler = null;
        }
        
        // delete all callbacks object
        for (final Callback callback : callbacks) {
            callback.delete();
        }
    }
    
    /**
     * invoke org.sqlite.udf.Function#register() method.
     * @param func User-Defined function
     * @throws java.sql.SQLException When the return value of the sqlite3_create_function() function is not SQLITE_OK.
     * @see org.sqlite.udf.Function#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void createFunction(Function func) throws SQLException {
        func.register(getHandle());
        if (functions == null) {
            functions = new ArrayList<Function>();
        }
        functions.add(func);
    }
    
    /**
     * invoke org.sqlite.udf.Function#unregister() method.
     * @param func User-Defined function
     * @throws java.sql.SQLException When the return value of the sqlite3_create_function() function is not SQLITE_OK.
     * @see org.sqlite.udf.Function#unregister(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void dropFunction(Function func) throws SQLException {
        func.unregister(getHandle());
        if (functions != null) {
            functions.remove(func);
        }
    }
    
    /**
     * invoke org.sqlite.text.Collator#register() method.
     * @param col User-Defined Collating Sequences
     * @throws java.sql.SQLException When the return value of the sqlite3_create_collation_v2() function is not SQLITE_OK.
     * @see org.sqlite.text.Collator#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void createCollationSequence(Collator col) throws SQLException {
        col.register(getHandle());
    }
    
    /**
     * invoke org.sqlite.text.Collator#unregister() method.
     * @param col User-Defined Collating Sequences
     * @throws java.sql.SQLException When the return value of the sqlite3_create_collation() function is not SQLITE_OK.
     * @see org.sqlite.text.Collator#unregister(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void dropCollationSequence(Collator col) throws SQLException {
        col.unregister(getHandle());
    }
    
    /**
     * invoke org.sqlite.auth.Authorizer#register() method.
     * @param auth authorizer
     * @throws java.sql.SQLException When the return value of the sqlite3_set_authorizer() function is not SQLITE_OK.
     * @see org.sqlite.auth.Authorizer#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setAuthorizer(Authorizer auth) throws SQLException {
        clearAuthorizer();
        
        auth.register(getHandle());
        authorizer = auth;
    }
    
    /**
     * invoke org.sqlite.auth.Authorizer#unregister() function.
     * @throws java.sql.SQLException When the return value of the sqlite3_set_authorizer() function is not SQLITE_OK.
     * @see org.sqlite.auth.Authorizer#unregister(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearAuthorizer() throws SQLException {
        if (authorizer != null) {
            authorizer.unregister(getHandle());
            authorizer = null;
        }
    }
    
    /**
     * invoke org.sqlite.event.BusyHandler#register() method.
     * @param busy busy handler
     * @throws java.sql.SQLException When the return value of the sqlite3_busy_handler() function is not SQLITE_OK.
     * @see org.sqlite.event.BusyHandler#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setBusyHandler(BusyHandler busy) throws SQLException {
        clearBusyHandler();
        
        busy.register(getHandle());
        busyHandler = busy;
        timeout = -1;
    }
    
    /**
     * invoke org.sqlite.event.BusyHandler#unregister() function.
     * @throws java.sql.SQLException When the return value of the sqlite3_busy_handler() function is not SQLITE_OK.
     * @see org.sqlite.event.BusyHandler#unregister(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearBusyHandler() throws SQLException {
        if (busyHandler != null) {
            busyHandler.unregister(getHandle());
            busyHandler = null;
        }
    }
    
    /**
     * invoke org.sqlite.event.CollationNeededHandler#register() method.
     * @param needed the CollationNeededHandler object
     * @throws java.sql.SQLException When the return value of the sqlite3_collation_needed() function is not SQLITE_OK.
     * @see org.sqlite.event.CollationNeededHandler#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     * @see org.sqlite.event.CollationNeededHandler#setDatabase(org.sqlite.Database)
     */
    public void setCollationNeededHandler(CollationNeededHandler needed) throws SQLException {
        clearCollationNeededHandler();
        
        needed.register(getHandle());
        collNeeded = needed;
        collNeeded.setDatabase(this);
    }
    
    /**
     * invoke org.sqlite.event.CollationNeededHandler#unregister(() method.
     * @throws java.sql.SQLException When the return value of the sqlite3_collation_needed() function is not SQLITE_OK.
     * @see org.sqlite.event.CollationNeededHandler#unregister(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearCollationNeededHandler() throws SQLException {
        if (collNeeded != null) {
            collNeeded.unregister(getHandle());
            collNeeded = null;
        }
    }
    
    /**
     * invoke org.sqlite.event.ProgressHandler#register() method.
     * @param prog progress handler
     * @see org.sqlite.event.ProgressHandler#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setProgressHandler(ProgressHandler prog) {
        clearProgressHandler();

        prog.register(getHandle());
        progressHandler = prog;
    }
    
    /**
     * invoke org.sqlite.event.ProgressHandler#unregister() method.
     * @see org.sqlite.event.ProgressHandler#unregister(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearProgressHandler() {
        if (progressHandler != null) {
            progressHandler.unregister(getHandle());
            progressHandler = null;
        }
    }
    
    /**
     * invoke org.sqlite.event.CommitHook#register() method.
     * @param hook commit hoot
     * @see org.sqlite.event.CommitHook#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setCommitHook(CommitHook hook) {
        hook.register(getHandle());
    }
    
    /**
     * invoke org.sqlite.event.CommitHook#clear() method.
     * @see org.sqlite.event.CommitHook#clear(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearCommitHook() {
        CommitHook.clear(getHandle());
    }
    
    /**
     * invoke org.sqlite.event.RollbackHook#register() method.
     * @param hook rollback hoot
     * @see org.sqlite.event.RollbackHook#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setRollbackHook(RollbackHook hook) {
        hook.register(getHandle());
    }
    
    /**
     * invoke org.sqlite.event.RollbackHook#clear() function.
     * @see org.sqlite.event.RollbackHook#clear(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearRollbackHook() {
        RollbackHook.clear(getHandle());
    }
    
    /**
     * invoke org.sqlite.event.UpdateHook#register() method.
     * @param hook update hoot
     * @see org.sqlite.event.UpdateHook#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setUpdateHook(UpdateHook hook) {
        hook.register(getHandle());
    }
    
    /**
     * invoke org.sqlite.event.UpdateHook#clear() method.
     * @see org.sqlite.event.UpdateHook#clear(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearUpdateHook() {
        UpdateHook.clear(getHandle());
    }
    
    /**
     * invoke org.sqlite.profiler.Profiler#register() method.
     * @param profiler profiler
     * @see org.sqlite.profiler.Profiler#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setProfiler(Profiler profiler) {
        profiler.register(getHandle());
    }
    
    /**
     * invoke org.sqlite.profiler.Profiler#clear() method.
     * @see org.sqlite.profiler.Profiler#clear(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearProfiler() {
        Profiler.clear(getHandle());
    }
    
    /**
     * invoke org.sqlite.profiler.Tracer#register() method.
     * @param tracer tracer
     * @see org.sqlite.profiler.Tracer#register(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void setTracer(Tracer tracer) {
        tracer.register(getHandle());
    }
    
    /**
     * invoke org.sqlite.profiler.Tracer#clear() method.
     * @see org.sqlite.profiler.Tracer#clear(org.sqlite.swig.SWIGTYPE_p_sqlite3)
     */
    public void clearTracer() {
        Tracer.clear(getHandle());
    }
    
    /**
     * invoke sqlite3_enable_shared_cache(on) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_enable_shared_cache() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/enable_shared_cache.html">Enable Or Disable Shared Pager Cache</a>
     */
    public static void enableSharedCache() throws SQLException {
        final int ret = sqlite3_enable_shared_cache(1);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret);
        }
    }
    
    /**
     * invoke sqlite3_enable_shared_cache(off) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_enable_shared_cache() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/enable_shared_cache.html">Enable Or Disable Shared Pager Cache</a>
     */
    public static void disableSharedCache() throws SQLException {
        final int ret = sqlite3_enable_shared_cache(0);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret);
        }
    }
    
    /**
     * invoke sqlite3_errcode() function.
     * @return the numeric result code or extended result code for the most recent failed sqlite3_* API call associated with sqlite3 handle 'db'.
     * @see <a href="http://sqlite.org/c3ref/errcode.html">Error Codes And Messages</a>
     * @see <a href="http://sqlite.org/c3ref/c_abort.html">Result Codes</a>
     * @see <a href="http://sqlite.org/c3ref/c_ioerr_blocked.html">Extended Result Codes</a>
     * @see #enableExtendedResultCodes()
     * @see #disableExtendedResultCodes()
     */
    public int getLastError() {
        return sqlite3_errcode(getHandle());
    }
    
    /**
     * invoke sqlite3_errmsg() function.
     * @return the numeric result code or extended result code for the most recent failed sqlite3_* API call associated with sqlite3 handle 'db'.
     * @see <a href="http://sqlite.org/c3ref/errcode.html">Error Codes And Messages</a>
     * @see #getLastError()
     * @see #enableExtendedResultCodes()
     * @see #disableExtendedResultCodes()
     */
    public String getLastErrorMessage() {
        return sqlite3_errmsg(getHandle());
    }
    
    /**
     * invoke sqlite3_get_table() function.
     * @param sql SQL to be evaluated
     * @param errmsg Error message written here
     * @return Results of the query
     * @throws java.sql.SQLException When the return value of the sqlite3_get_table() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/free_table.html">Convenience Routines For Running Queries</a>
     */
    public List<String[]> getTable(String sql, SWIGTYPE_p_p_char errmsg) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final List<String[]> result = new ArrayList<String[]>();
        int ret = 0;
        while (((ret = sqlite3_get_table(db, sql, result, errmsg)) == SQLITE_BUSY)
                && (timeout == 0)) {
            // waiting...
        }
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
        return result;
    }
    
    /**
     * invoke sqlite3_extended_result_codes(on) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_extended_result_codes() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/extended_result_codes.html">Enable Or Disable Extended Result Codes</a>
     */
    public void enableExtendedResultCodes() throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final int ret = sqlite3_extended_result_codes(db, 1);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * invoke sqlite3_extended_result_codes(off) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_extended_result_codes() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/extended_result_codes.html">Enable Or Disable Extended Result Codes</a>
     */
    public void disableExtendedResultCodes() throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final int ret = sqlite3_extended_result_codes(db, 0);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * invoke sqlite3_enable_load_extension(on) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_enable_load_extension() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/enable_load_extension.html">Enable Or Disable Extension Loading</a>
     */
    public void enableLoadExtention() throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final int ret = sqlite3_enable_load_extension(db, 1);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * invoke sqlite3_enable_load_extension(off) function.
     * @throws java.sql.SQLException When the return value of the sqlite3_enable_load_extension() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/enable_load_extension.html">Enable Or Disable Extension Loading</a>
     */
    public void disableLoadExtention() throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final int ret = sqlite3_enable_load_extension(db, 0);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * invoke sqlite3_load_extension() function.
     * @param filename the Name of the shared library containing extension
     * @param entryPoint the Entry point. Use "sqlite3_extension_init" if null.
     * @param errmsg Error message written here
     * @throws java.sql.SQLException When the return value of the sqlite3_load_extension() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/load_extension.html">Load An Extension</a>
     */
    public void loadExtention(String filename, String entryPoint, SWIGTYPE_p_p_char errmsg) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final int ret = sqlite3_load_extension(db, filename, entryPoint, errmsg);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * invoke sqlite3_reset_auto_extension() function.
     * @see <a href="http://www.sqlite.org/c3ref/reset_auto_extension.html">Reset Automatic Extension Loading</a>
     */
    public static void resetAutoExtention() {
        sqlite3_reset_auto_extension();
    }
    
    /**
     * invoke sqlite3_threadsafe() function.
     * @return true if the library is threadsafe
     * @see <a href="http://sqlite.org/c3ref/threadsafe.html">Test To See If The Library Is Threadsafe</a>
     */
    public static boolean isThreadSafe() {
        return (sqlite3_threadsafe() != 0);
    }
    
    /**
     * invoke sqlite3_memory_highwater() function.
     * @param reset true if the memory highwater mark is reset.
     * @return the maximum value of sqlite3_memory_used() since the highwater mark was last reset.
     * @see <a href="http://sqlite.org/c3ref/memory_highwater.html">Memory Allocator Statistics</a>
     * @see #status(int, boolean)
     * @deprecated
     */
    public static long highwaterMemory(boolean reset) {
        return sqlite3_memory_highwater((reset ? 1 : 0));
    }
    
    /**
     * invoke sqlite3_memory_used() function.
     * @return the number of bytes of memory currently outstanding (malloced but not freed).
     * @see <a href="http://sqlite.org/c3ref/memory_highwater.html">Memory Allocator Statistics</a>
     * @see #status(int, boolean)
     * @deprecated
     */
    public static long usedMemory() {
        return sqlite3_memory_used();
    }
    
    /**
     * invoke sqlite3_vfs_find() function.
     * @param vfsName the VFS name are case sensitive.
     * @return the pointer to a VFS. if vfsName is null then the default VFS is returned.
     * @see <a href="http://sqlite.org/c3ref/vfs_find.html">Virtual File System Objects</a>
     * @see #registerVFS(org.sqlite.swig.SWIGTYPE_p_sqlite3_vfs, boolean)
     * @see #unregisterVFS(org.sqlite.swig.SWIGTYPE_p_sqlite3_vfs)
     */
    public static SWIGTYPE_p_sqlite3_vfs findVFS(String vfsName) {
        return sqlite3_vfs_find(vfsName);
    }
    
    /**
     * invoke sqlite3_vfs_register() function.
     * @param vfs the VFS object.
     * @param makeDefault Each new VFS becomes the default VFS if the makeDefault flag is true.
     * @throws java.sql.SQLException When the return value of the sqlite3_vfs_register() function is not SQLITE_OK.
     * @see #findVFS(String)
     * @see #unregisterVFS(org.sqlite.swig.SWIGTYPE_p_sqlite3_vfs)
     */
    public static void registerVFS(SWIGTYPE_p_sqlite3_vfs vfs, boolean makeDefault) throws SQLException {
        final int ret = sqlite3_vfs_register(vfs, (makeDefault ? 1 : 0));
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret);
        }
    }
    
    /**
     * invoke sqlite3_vfs_unregister() function.
     * @param vfs the registered VFS object.
     * @throws java.sql.SQLException When the return value of the sqlite3_vfs_unregister() function is not SQLITE_OK.
     * @see #registerVFS(org.sqlite.swig.SWIGTYPE_p_sqlite3_vfs, boolean)
     */
    public static void unregisterVFS(SWIGTYPE_p_sqlite3_vfs vfs) throws SQLException {
        final int ret = sqlite3_vfs_unregister(vfs);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret);
        }
    }

    /**
     * invoke sqlite3_file_control() function.
     * @param dbName the name "main" or a null.
     * @param op the parameters to this routine are passed directly through to the second parameters of the xFileControl method.
     * @param arg the parameters to this routine are passed directly through to the third parameters of the xFileControl method.
     * @throws java.sql.SQLException When the return value of the sqlite3_file_control() function is not SQLITE_OK.
     */
    public void fileControl(String dbName, int op, SWIGTYPE_p_void arg) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final int ret = sqlite3_file_control(db, dbName, op, arg);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * invoke sqlite3_blob_open() function.
     * @param dbName the database name
     * @param tableName the table name
     * @param columnName the column name
     * @param rowId the ROWID
     * @param flag If the flags parameter is non-zero, the blob is opened for read and write access. If it is zero, the blob is opened for read access. 
     * @return the sqlite3_blob object
     * @throws java.sql.SQLException When the return value of the sqlite3_blob_open() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/blob_open.html">Open A BLOB For Incremental I/O</a>
     */
    public Blob openBlob(String dbName, String tableName, String columnName, long rowId, int flag) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final SQLite3BlobPtrPtr ppBlob = new SQLite3BlobPtrPtr();
        final int ret = sqlite3_blob_open(db, dbName, tableName, columnName, rowId, flag, ppBlob);
        if (ret != SQLITE_OK) {
            ppBlob.delete();
            throw new JdbcSQLException(ret, db);
        }
        return new Blob(this, ppBlob);
    }
    
    /**
     * invoke sqlite3_limit() function.
     * @param id an one of the limit categories that define a class of constructs to be size limited
     * @param newVal the new limit value
     * @return the old limit value
     * @see <a href="http://www.sqlite.org/c3ref/limit.html">Run-time Limits</a>
     * @see <a href="http://www.sqlite.org/c3ref/c_limit_attached.html">Run-Time Limit Categories</a>
     */
    public int limit(int id, int newVal) {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        return sqlite3_limit(db, id, newVal);
    }
    
    /**
     * invoke sqlite3_next_stmt() function.
     * @param stmt Statement object (null allowed)
     * @return returns the next prepared statement after stmt associated with the database connection
     * @see <a href="http://sqlite.org/c3ref/next_stmt.html">Find the next prepared statement</a>
     */
    public Statement nextStatement(Statement stmt) throws SQLException {
        final SWIGTYPE_p_sqlite3 db = getHandle();
        final SWIGTYPE_p_sqlite3_stmt pstmt
                = (stmt != null ? stmt.getHandle() : null);
        final SWIGTYPE_p_sqlite3_stmt next = sqlite3_next_stmt(db, pstmt);
        if (next != null) {
            return new Statement(this, next);
        }
        return null;
    }

    /**
     * invoke sqlite3_status() function.
     * @param op status parameter
     * @param reset true if the memory highwater mark is reset.
     * @return Retrieves runtime status information about the preformance of SQLite, and optionally to reset various highwater marks
     * @throws java.sql.SQLException When the return value of the sqlite3_status() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/status.html">SQLite Runtime Status</a>
     * @see <a href="http://sqlite.org/c3ref/c_status_malloc_size.html">Status Parameters</a>
     */
    public static int[] status(int op, boolean reset) throws SQLException {
        SWIGTYPE_p_int current = null;
        SWIGTYPE_p_int highwater = null;
        try {
            current = new_p_int();
            highwater = new_p_int();
            final int ret = sqlite3_status(op, current, highwater, (reset ? 1 : 0));
            if (ret != SQLITE_OK) {
                throw new JdbcSQLException(ret);
            }
            return new int[] { get_int(current), get_int(highwater) };

        } finally {
            if (current != null) {
                delete_p_int(current);
            }
            if (highwater != null) {
                delete_p_int(highwater);
            }
        }
    }

    /**
     * invoke sqlite3_db_status() function.
     * @param op status parameter
     * @param reset true if the memory highwater mark is reset.
     * @return Retrieves runtime status information about a single database connection
     * @throws java.sql.SQLException When the return value of the sqlite3_db_status() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/db_status.html">Database Connection Status</a>
     * @see <a href="http://sqlite.org/c3ref/c_dbstatus_lookaside_used.html">Status Parameters for database connections</a>
     * @see <a href="http://sqlite.org/c3ref/status.html">SQLite Runtime Status</a>
     */
    public int[] connectionStatus(int op, boolean reset) throws SQLException {
        SWIGTYPE_p_int current = null;
        SWIGTYPE_p_int highwater = null;
        try {
            current = new_p_int();
            highwater = new_p_int();
            final SWIGTYPE_p_sqlite3 db = getHandle();
            final int ret = sqlite3_db_status(db, op, current, highwater, (reset ? 1 : 0));
            if (ret != SQLITE_OK) {
                throw new JdbcSQLException(ret, db);
            }
            return new int[] { get_int(current), get_int(highwater) };

        } finally {
            if (current != null) {
                delete_p_int(current);
            }
            if (highwater != null) {
                delete_p_int(highwater);
            }
        }
    }

    /**
     * Close database if database is not closed yet.
     * @throws Throwable
     * @see #close()
     */
    @Override
    protected void finalize() throws Throwable {
        if (!isClosed()) {
            Logger.getLogger(Database.class.getName()).severe("Database connection has leaked!");
            close();
        }
        super.finalize();
    }
    
}