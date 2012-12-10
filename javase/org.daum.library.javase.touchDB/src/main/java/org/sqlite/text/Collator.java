package org.sqlite.text;

import java.sql.SQLException;
import java.util.Comparator;
import org.sqlite.callback.NamedCallback;
import org.sqlite.jdbc.JdbcSQLException;
import static org.sqlite.swig.SQLite3.sqlite3_create_collation;
import static org.sqlite.swig.SQLite3.sqlite3_drop_collation;
import org.sqlite.swig.SWIGTYPE_p_sqlite3;
import static org.sqlite.swig.SQLite3Constants.SQLITE_OK;

/**
 * User-Defined Collating Sequence class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/create_collation.html">Define New Collating Sequences</a>
 * @see org.sqlite.jdbc.JdbcConnection#createCollationSequence(Collator)
 * @see org.sqlite.jdbc.JdbcConnection#dropCollationSequence(Collator)
 */
public abstract class Collator extends NamedCallback implements Comparator<String> {

    /**
     * invoke sqlite3_create_collation_v2() function and this object is registered in the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_create_collation_v2() function is not SQLITE_OK.
     * @see org.sqlite.Database#createCollationSequence(org.sqlite.text.Collator)
     */
    @Override
    public void register(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_create_collation(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }

    /**
     * Unregister this object from the database.<br/>
     * WARNING! Do not use this method because it is called internally.
     * @param db the database handle.
     * @throws java.sql.SQLException When the return value of the sqlite3_create_collation() function is not SQLITE_OK.
     * @see org.sqlite.Database#dropCollationSequence(org.sqlite.text.Collator)
     */
    @Override
    public void unregister(SWIGTYPE_p_sqlite3 db) throws SQLException {
        final int ret = sqlite3_drop_collation(db, this);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db);
        }
    }
    
    /**
     * Create new collation sequence with SQLITE_UTF8.
     * @param name the name of the new collation sequence.
     * @see NamedCallback#NamedCallback(String)
     */
    public Collator(String name) {
        super(name);
    }
    
    /**
     * Create new collation sequence.
     * @param name the name of the new collation sequence.
     * @param enc the most desirable encoding may be one of the constants SQLITE_UTF8, SQLITE_UTF16LE, SQLITE_UTF16BE or SQLITE_UTF16_ALIGNED.
     * @see <a href="http://sqlite.org/c3ref/c_any.html">Text Encodings</a>
     * @see NamedCallback#NamedCallback(String, int)
     */
    public Collator(String name, int enc) {
        super(name, enc);
    }
}
