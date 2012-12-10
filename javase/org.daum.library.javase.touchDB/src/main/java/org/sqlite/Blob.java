package org.sqlite;

import java.sql.SQLException;
import org.sqlite.io.Closeable;
import org.sqlite.jdbc.JdbcSQLException;
import org.sqlite.swig.SQLite3.SQLite3BlobPtrPtr;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_blob;
import static org.sqlite.swig.SQLite3.sqlite3_blob_bytes;
import static org.sqlite.swig.SQLite3.sqlite3_blob_close;
import static org.sqlite.swig.SQLite3.sqlite3_blob_read;
import static org.sqlite.swig.SQLite3.sqlite3_blob_write;
import static org.sqlite.swig.SQLite3Constants.*;

/**
 * sqlite3_blob wrapper class.<br/>
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/blob.html">A Handle To An Open BLOB</a>
 * @see Database#openBlob(String, String, String, long, int)
 */
public class Blob implements Closeable {
    private final Database db;
    private final SQLite3BlobPtrPtr ppBlob;
    private final SWIGTYPE_p_sqlite3_blob blob;
    private boolean isClosed;
    
    Blob(Database db, SQLite3BlobPtrPtr ppBlob) {
        this.db = db;
        this.ppBlob = ppBlob;
        this.blob = ppBlob.getSQLite3BlobPtr();
        this.isClosed = false;
        db.add(this);
    }
    
    public boolean isClosed() {
        return (isClosed || db.isClosed());
    }
    
    /**
     * invoke sqlite3_blob_close() function.
     * @throws java.sql.SQLException When the return value of the sqlite3_blob_close() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/blob_close.html">Close A BLOB Handle</a>
     */
    public void close() throws SQLException {
        if (!isClosed) {
            if (!db.isClosed()) {
                final int ret = sqlite3_blob_close(blob);
                if (ret != SQLITE_OK) {
                    new JdbcSQLException(ret, db.getHandle());
                }
            }
            isClosed = true;
            ppBlob.delete();
            db.remove(this);
        }
    }
    
    /**
     * invoke sqlite3_blob_bytes() function.
     * @return the size of an open blob.
     * @throws java.sql.SQLException When this method is called on a closed blob.
     * @see <a href="http://sqlite.org/c3ref/blob_bytes.html">Return The Size Of An Open BLOB</a>
     */
    public int length() throws SQLException {
        validateBlobOpen();
        
        return sqlite3_blob_bytes(blob);
    }
    
    /**
     * invoke sqlite3_blob_read() function.
     * @param b the buffer into which the data is read.
     * @param len the maximum number of bytes to read.
     * @param offset the start offset in BLOB at which the data is read.
     * @throws java.sql.SQLException When this method is called on a closed blob. When the return value of the sqlite3_blob_read() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/blob_read.html">Read Data From A BLOB Incrementally</a>
     */
    public void read(byte[] b, int len, int offset) throws SQLException {
        validateBlobOpen();
        
        final int ret = sqlite3_blob_read(blob, b, len, offset);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db.getHandle());
        }        
    }
    
    /**
     * invoke sqlite3_blob_write() function.
     * @param b the data.
     * @param len the number of bytes to write.
     * @param offset the start offset in the BLOB.
     * @throws java.sql.SQLException When the return value of the sqlite3_blob_write() function is not SQLITE_OK.
     * @see <a href="http://sqlite.org/c3ref/blob_write.html">Write Data Into A BLOB Incrementally</a>
     */
    public void write(byte[] b, int len, int offset) throws SQLException {
        validateBlobOpen();
        
        final int ret = sqlite3_blob_write(blob, b, len, offset);
        if (ret != SQLITE_OK) {
            throw new JdbcSQLException(ret, db.getHandle());
        }        
    }
    
    /**
     * 
     * @throws java.sql.SQLException When this method is called on a closed blob.
     */
    private void validateBlobOpen() throws SQLException {
        if (isClosed()) {
            throw new SQLException("Blob is already closed.", "90007");
        }
    }
}
