package org.sqlite.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.sqlite.io.BlobInputStream;
import org.sqlite.io.BlobOutputStream;
import org.sqlite.io.MemoryInputStream;
import org.sqlite.io.Closeable;
import org.sqlite.io.WrappedIOException;
import static org.sqlite.swig.SQLite3Constants.SQLITE_READONLY;
import org.sqlite.swig.SWIGTYPE_p_void;

/**
 *
 * @author calico
 */
public class JdbcBlob implements Blob, Closeable {
    private final Closeable owner;
    private final SWIGTYPE_p_void ptr;
    private final org.sqlite.Blob blob;
    private final long length;
    private boolean isClosed = false;
    
    /**
     * Create JdbcBlob object from the void pointer.
     * @param owner the owner Closeable object
     * @param blob the pointer of blob
     * @param len the number of bytes
     */
    public JdbcBlob(Closeable owner, SWIGTYPE_p_void blob, long len) {
        this.owner = owner;
        this.ptr = blob;
        this.length = len;
        this.blob = null;
    }
    
    /**
     * Create JdbcBlob object from the sqlite3_blob object.
     * @param owner the owner Closeable object
     * @param blob the sqlite3_blob object
     * @throws java.sql.SQLException When this method is called on a closed blob.
     */
    public JdbcBlob(Closeable owner, org.sqlite.Blob blob) throws SQLException {
        this.owner = owner;
        this.blob = blob;
        this.length = blob.length();
        this.ptr = null;
    }
    
    // START implements
    /**
     * Returns the number of bytes in the BLOB value designated by this Blob object.
     * @return length of the BLOB in bytes
     */
    public long length() {
        return length;
    }

    /**
     * Retrieves all or part of the BLOB  value that this Blob object represents, as an array of bytes.
     * @param pos the ordinal position of the first byte in the BLOB value to be extracted; the first byte is at position 1
     * @param len the number of consecutive bytes to be copied; the value for length must be 0 or greater
     * @return a byte array containing up to length consecutive bytes from the BLOB value designated by this Blob object, starting with the byte at position pos
     * @throws java.sql.SQLException When this method is called on a closed connection. When pos is less than 1 or length is less than 0
     */
    public byte[] getBytes(long pos, int len) throws SQLException {
        if (pos < 1) {
            throw new SQLException("Should pos is greater than or equal to 1.", "90J08");
        }
        if (len < 0) {
            throw new SQLException("Should len is greater than or equal to 0.", "90J08");
        }
        if (len == 0) {
            return new byte[0];
        }
        
        if (pos + len > length) {
            len -= ((pos + len) - length - 1);
        }
        
        final byte[] b = new byte[len];
        final InputStream in = getBinaryStream();
        try {
            in.skip(pos - 1);
            in.read(b, 0, len);
            return b;
            
        } catch (WrappedIOException ex) {
            throw ex.getCause();
            
        } catch (IOException ex) {
            final SQLException sqlex = new SQLException(ex.getMessage());
            sqlex.initCause(ex);
            throw sqlex;
        }
    }

    /**
     * Retrieves the BLOB value designated by this Blob instance as a stream.
     * @return a stream containing the BLOB data
     * @throws java.sql.SQLException
     */
    public InputStream getBinaryStream() throws SQLException {
        return (blob != null
                    ? new BlobInputStream(this, blob)
                    : new MemoryInputStream(this, ptr, length)
                );
    }

    /**
     * Retrieves by the Boyer-Moore algorithm. 
     * @param pattern the byte array for which to search
     * @param start the position at which to begin searching; the first position is 1
     * @return the position at which the pattern appears, else -1
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see <a href="http://www-igm.univ-mlv.fr/~lecroq/string/node14.html">Boyer-Moore algorithm</a>
     * @see #getBytes(long, int)
     */
    public long position(byte[] pattern, long start) throws SQLException {
        // TODO byte[] ではなく InputStream から検索できるようにする！
        int ret = BM(pattern, getBytes(start, (int) length));
        return (ret > 0 ? (ret + 1) : -1);
    }

    /**
     * invoke position(byte[], long) method.
     * @param pattern the Blob object designating the BLOB value for which to search
     * @param start the position in the BLOB value at which to begin searching; the first position is 1
     * @return the position at which the pattern begins, else -1
     * @throws java.sql.SQLException When this method is called on a closed connection.
     * @see #position(byte[], long)
     */
    public long position(Blob pattern, long start) throws SQLException {
        return position(pattern.getBytes(1, (int) pattern.length()), start);
    }

    /**
     * Writes the given array of bytes to the BLOB value that this Blob object represents, starting at position pos, and returns the number of bytes written.
     * @param pos the position in the BLOB object at which to start writing; the first position is 1
     * @param bytes the array of bytes to be written to the BLOB  value that this Blob object represents
     * @return the number of bytes written
     * @throws java.sql.SQLException
     * @see #setBytes(long, byte[], int, int)
     */
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        if (blob != null) {
            return setBytes(pos, bytes, 0, bytes.length);
        }
        throw new JdbcSQLException(SQLITE_READONLY);
    }

    /**
     * Writes all or part of the given byte array to the BLOB value that this Blob object represents and returns the number of bytes written.
     * @param pos the position in the BLOB object at which to start writing; the first position is 1
     * @param bytes the array of bytes to be written to this BLOB  object
     * @param offset the offset into the array bytes at which to start reading the bytes to be set
     * @param len the number of bytes to be written to the BLOB  value from the array of bytes bytes
     * @return the number of bytes written
     * @throws java.sql.SQLException
     * @see #setBinaryStream(long)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        if (blob != null) {
            try {
                setBinaryStream(pos).write(bytes, offset, len);
                return len;
                
            } catch (IOException ex) {
                throw (SQLException) ex.getCause();
            }
        }
        throw new JdbcSQLException(SQLITE_READONLY);
    }

    /**
     * Retrieves a stream that can be used to write to the BLOB value that this Blob object represents.
     * @param pos the position in the BLOB value at which to start writing; the first position is 1
     * @return a java.io.OutputStream object to which data can be written
     * @throws java.sql.SQLException if there is an error accessing the BLOB value or if pos is less than 1
     */
    public OutputStream setBinaryStream(long pos) throws SQLException {
        if (blob != null) {
            return new BlobOutputStream(this, blob, pos);
        }
        throw new JdbcSQLException(SQLITE_READONLY);
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public void truncate(long len) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void free() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public InputStream getBinaryStream(long l, long l1) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    // END implements

    /**
     * Closes this BLOB object.
     */
    public void close() {
        isClosed = true;
    }
    
    /**
     * Retrieves whether this BLOB object has been closed.
     * @return true if this BLOB object is closed. false if it is still open.
     */
    public boolean isClosed() {
        return (isClosed || owner.isClosed());
    }
    
    private static int[] preBmBc(byte[] x) {
        final int ASIZE = 1 + Byte.MAX_VALUE - Byte.MIN_VALUE;
        final int[] bmBc = new int[ASIZE];
        final int m = x.length;
        
        for (int i = 0; i < ASIZE; ++i) {
            bmBc[i] = m;
        }
        for (int i = 0; i < m - 1; ++i) {
            bmBc[(x[i] < 0 ? (Byte.MAX_VALUE - x[i]) : x[i])] = m - i - 1;
        }
        return bmBc;
    }

    private static int[] suffixes(byte[] x) {
        final int m = x.length;
        final int[] suff = new int[m];

        suff[m - 1] = m;
        int g = m - 1;
        int f = 0;
        for (int i = m - 2; i >= 0; --i) {
            if (i > g && suff[i + m - 1 - f] < i - g) {
                suff[i] = suff[i + m - 1 - f];
            } else {
                if (i < g) {
                    g = i;
                }
                f = i;
                while (g >= 0 && x[g] == x[g + m - 1 - f]) {
                    --g;
                }
                suff[i] = f - g;
            }
        }
        return suff;
    }

    private static int[] preBmGs(byte[] x) {
        final int[] suff = suffixes(x);
        final int m = x.length;
        final int[] bmGs = new int[m];

        for (int i = 0; i < m; ++i) {
            bmGs[i] = m;
        }
        int j = 0;
        for (int i = m - 1; i >= 0; --i) {
            if (suff[i] == i + 1) {
                for (; j < m - 1 - i; ++j) {
                    if (bmGs[j] == m) {
                        bmGs[j] = m - 1 - i;
                    }
                }
            }
        }
        for (int i = 0; i <= m - 2; ++i) {
            bmGs[m - 1 - suff[i]] = m - 1 - i;
        }
        return bmGs;
    }

    // TODO メソッドをユーティリティクラス化して外部に出す？
    private static int BM(byte[] x, byte[] y) {
        // Preprocessing
        int[] bmGs = preBmGs(x);
        int[] bmBc = preBmBc(x);

        final int m = x.length;
        final int n = y.length;
        final int end = (n - m) + 1;
        
        // Searching
        int j = 0;
        while (j < end) {
            int i = 0;
            for (i = m - 1; i >= 0 && x[i] == y[i + j]; --i) {
                // skip
            }
            if (i < 0) {
                // match
                return j;
//                j += bmGs[0];
            } else {
                final byte b = y[i + j];
                j += Math.max(
                            bmGs[i],
                            bmBc[(b < 0 ? (Byte.MAX_VALUE - b) : b)] - m + 1 + i
                        );
            }
        }
        return -1;
    }
    
}
