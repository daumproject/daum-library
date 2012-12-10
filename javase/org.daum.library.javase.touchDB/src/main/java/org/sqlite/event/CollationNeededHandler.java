package org.sqlite.event;

import org.sqlite.text.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.sqlite.Database;
import org.sqlite.callback.Callback;
import org.sqlite.jdbc.JdbcSQLException;
import static org.sqlite.swig.SQLite3.new_p_sqlite3;
import static org.sqlite.swig.SQLite3.sqlite3_clear_collation_needed;
import static org.sqlite.swig.SQLite3.sqlite3_collation_needed;
import static org.sqlite.swig.SQLite3.sqlite3_create_collation;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;
import static org.sqlite.swig.SQLite3Constants.SQLITE_OK;
import static org.sqlite.swig.SQLite3Constants.SQLITE_UTF8;

/**
 * Collation Needed Callbacks class.
 * @author calico
 * @see <a href="http://www.sqlite.org/c3ref/collation_needed.html">Collation Needed Callbacks</a>
 * @see org.sqlite.jdbc.JdbcConnection#setCollationNeededHandler(org.sqlite.event.CollationNeededHandler)
 * @see org.sqlite.jdbc.JdbcConnection#clearCollationNeededHandler()
 */
public class CollationNeededHandler extends Callback {

    /**
     * invoke sqlite3_collation_needed() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_collation_needed() function is not SQLITE_OK.
     * @see org.sqlite.Database#setCollationNeededHandler(org.sqlite.event.CollationNeededHandler)
     */
    @Override
    public final void register(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_collation_needed(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_collation_needed() function is not SQLITE_OK.
     * @see org.sqlite.Database#clearCollationNeededHandler()
     */
    @Override
    public final void unregister(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_clear_collation_needed(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
        database = null;
    }

    /**
     * owner database object.
     */
    private Database database;
    
    /**
     * Set the Database object.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the Database object
     */
    public void setDatabase(Database db) {
        this.database = db;
    }
    
    private final Map<String, Collator> collators = new HashMap<String, Collator>();
    
    /**
     * Appends the Collator object.
     * @param col the Collator object
     * @return the previous Collator object associated with collation sequence name, or null if there was no mapping for collation sequence name.
     */
    public Collator addCollator(Collator col) {
        return collators.put(col.getName().toUpperCase(), col);
    }
    
    /**
     * Removes the Collator object.
     * @param name the collation sequence name
     * @return the previous Collator object associated with collation sequence name, or null if there was no mapping for collation sequence name.
     */
    public Collator removeCollator(String name) {
        return collators.remove(name.toUpperCase());
    }
    
    /**
     * Removes the Collator object.
     * @param col the Collator object
     * @return the previous Collator object associated with collation sequence name, or null if there was no mapping for collation sequence name.
     */
    public Collator removeCollator(Collator col) {
        return removeCollator(col.getName());
    }
    
    /**
     * Register the desired collation sequence.
     * @param pDb the sqlite3* value
     * @param eTextRep the value is one of SQLITE_UTF8, SQLITE_UTF16BE, or SQLITE_UTF16LE, indicating the most desirable form of the collation sequence function required.
     * @param name the name of the required collation sequence.
     * @throws java.sql.SQLException When the return value of the sqlite3_create_collation() function is not SQLITE_OK.
     */
    protected final void xCollationNeeded(long pDb, int eTextRep, String name) throws SQLException {
        if (eTextRep == SQLITE_UTF8) {
            final Collator col = collators.get(name.toUpperCase());
            if (col != null) {
                if (database != null && !database.isClosed()) {
                    database.createCollationSequence(col);
                    
                } else {
                    SWIGTYPE_p_sqlite3 db = new_p_sqlite3(pDb);
                    final int ret = sqlite3_create_collation(db, col);
                    if (ret != SQLITE_OK) {
                        throw new JdbcSQLException(ret, db);               
                    }
                }
            }
        }
    }
}
