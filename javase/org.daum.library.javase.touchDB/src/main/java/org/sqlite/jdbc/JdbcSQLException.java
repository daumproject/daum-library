package org.sqlite.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.sqlite.swig.SQLite3.*;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_stmt;

/**
 *
 * @author calico
 */
public class JdbcSQLException extends SQLException {

    public JdbcSQLException(int resultCode) {
        this(sqlite3_get_errmsg(resultCode), resultCode);
    }

    public JdbcSQLException(String reason, int vendorCode) {
        super(reason, getSQLState(vendorCode), vendorCode);
        
        log(reason, vendorCode);
    }
    
    public JdbcSQLException(int resultCode, SWIGTYPE_p_sqlite3 db) {
        this(resultCode, sqlite3_get_errmsg(resultCode), db);
    }
    
    public JdbcSQLException(int resultCode, SWIGTYPE_p_sqlite3_stmt stmt) {
        this(resultCode, sqlite3_db_handle(stmt));
    }
    
    private JdbcSQLException(int resultCode, String message, SWIGTYPE_p_sqlite3 db) {
        super(message, getSQLState(resultCode), resultCode);
        
        log(message, resultCode);
        
        final int errorCode = sqlite3_errcode(db);
        if (errorCode != 0 && errorCode != resultCode) {
            final JdbcSQLException ex = new JdbcSQLException(sqlite3_errmsg(db), errorCode);
            ex.initCause(this);
            setNextException(ex);
        }
    }
    
    private static final List<Integer> NOT_FATAL_ERROR;
    static {
        final int[] NOT_FATAL_ERROR_CODES
                = new int[] {
                    SQLITE_OK,
                    SQLITE_PERM,
                    SQLITE_ABORT,
                    SQLITE_BUSY,
                    SQLITE_LOCKED,
                    SQLITE_READONLY,
                    SQLITE_INTERRUPT,
                    SQLITE_CANTOPEN,
                    SQLITE_SCHEMA,
                    SQLITE_CONSTRAINT,
                    SQLITE_AUTH,
                    SQLITE_ROW,
                    SQLITE_DONE,
                };
        NOT_FATAL_ERROR = new ArrayList<Integer>();
        for (int code : NOT_FATAL_ERROR_CODES) {
            NOT_FATAL_ERROR.add(code);
        }
    }
    
    private void log(String reason, int vendorCode) {
        if (!NOT_FATAL_ERROR.contains(vendorCode)) {
            final Logger logger = Logger.getLogger(JdbcSQLException.class.getName());
            if (logger.isLoggable(Level.FINE)) {
                final StringBuilder message = new StringBuilder(reason);
                message.append("\n");
                for (final StackTraceElement trace : getStackTrace()) {
                    message.append("\tat ").append(trace.toString()).append("\n");
                }
                logger.severe(message.toString());
            }
        }
    }
    
    /**
     * Convert SQLite result codes to SQL/99 SQLSTATE codes.
     * @param errcode SQLite Result Codes
     * @return SQL/99 SQLSTATE codes.
     * @see <a href="http://www.sqlite.org/c3ref/c_abort.html">Result Codes</a>
     */
    public static String getSQLState(int errcode) {
        final String subclass = ("000" + errcode).substring(0, 3);
        switch (errcode) {
            case SQLITE_OK:
            case SQLITE_ROW:
            case SQLITE_DONE:
                return "00" + subclass;

            case SQLITE_ABORT:
            case SQLITE_INTERRUPT:
                return "02" + subclass;

            case SQLITE_ERROR:
            case SQLITE_INTERNAL:
            case SQLITE_PERM:
            case SQLITE_BUSY:
            case SQLITE_LOCKED:
            case SQLITE_NOMEM:
                return "HY" + subclass;

            case SQLITE_READONLY:
            case SQLITE_IOERR:
            case SQLITE_CORRUPT:
            case SQLITE_FULL:
            case SQLITE_EMPTY:
            case SQLITE_SCHEMA:
            case SQLITE_TOOBIG:
            case SQLITE_MISMATCH:
            case SQLITE_NOLFS:
            case SQLITE_AUTH:
            case SQLITE_FORMAT:
            case SQLITE_RANGE:
            case SQLITE_NOTADB:
                return "90" + subclass;

            case SQLITE_CONSTRAINT:
                return "23" + subclass;

            case SQLITE_CANTOPEN:
                return "08" + subclass;

            case SQLITE_MISUSE:
                return "42" + subclass;
        }
        return null;
    }

}
