package org.sqlite.io;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import org.sqlite.Blob;

/**
 *
 * @author calico
 */
public class BlobOutputStream extends OutputStream {
    private final Closeable owner;
    private final Blob blob;
    private long pos;
    private boolean isClosed = false;

    /**
     * create BLOB output stream.
     * @param owner the owner object
     * @param blob sqlite3_blob wrapper object
     * @param pos the position in the BLOB object at which to start writing; the first position is 1
     * @throws java.sql.SQLException if pos is less than 1
     */
    public BlobOutputStream(Closeable owner, Blob blob, long pos) throws SQLException {
        if (pos < 1) {
            throw new SQLException("Should pos is greater than or equal to 1.", "90J08");
        }
        this.owner = owner;
        this.blob = blob;
        this.pos = pos - 1;
    }
    
    // START implements
    /**
     * Closes this stream.
     */
    @Override
    public void close() {
        isClosed = true;
    }

    /**
     * Writes b.length bytes from the specified byte array to this output stream.
     * @param b the data.
     * @throws java.io.IOException
     * @see #write(byte[], int, int)
     */
    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /**
     * Writes len bytes from the specified byte array starting at offset off to this output stream.
     * @param b the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     * @throws java.io.IOException
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        validateStreamOpen();
        
        try {
            if (off == 0) {
                blob.write(b, len, (int) pos);
                
            } else {
                byte[] buff = new byte[len];
                System.arraycopy(buff, 0, b, off, len);
                blob.write(buff, len, (int) pos);
            }
            pos += len;
            
        } catch (SQLException ex) {
            throw new WrappedIOException(ex);
        }
    }

    /**
     * Writes the specified byte to this output stream. The general contract for write is that one byte is written to the output stream.
     * @param b the byte.
     * @throws java.io.IOException
     * @see #write(byte[], int, int)
     */
    @Override
    public void write(int b) throws IOException {
        write(new byte[] { (byte)b }, 0, 1);
    }
    // END implements
    
    /**
     * 
     * @throws java.io.IOException
     */
    private void validateStreamOpen() throws IOException {
        if (isClosed || owner.isClosed()) {
            throw new IOException("Stream is already closed.");
        }
    }
}
