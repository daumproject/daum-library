package org.sqlite.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author calico
 */
public abstract class InputStreamAdapter extends InputStream  {
    private final Closeable owner;
    private final long last;
    private long pos;
    private long mark;
    private boolean isClosed = false;
    
    /**
     * default constructor.
     * @param owner the owner Closeable object
     * @param len the number of bytes
     */
    public InputStreamAdapter(Closeable owner, long len) {
        this.owner = owner;
        this.last = len - 1;
    }

    // START implements
    /**
     * Returns the number of bytes that can be read (or skipped over) from this input stream without blocking by the next caller of a method for this input stream.
     * @return the number of bytes that can be read from this input stream without blocking.
     * @throws java.io.IOException When this method is called on a closed stream.
     */
    @Override
    public int available() throws IOException {
        validateStreamOpen();
        
        return  (pos > last ? 0 : (int) (last - pos));
    }

    /**
     * Closes this stream.
     */
    @Override
    public void close() {
        isClosed = true;
    }

    /**
     * Marks the current position in this input stream.<br/>
     * Note: The readlimit for this class has no meaning.
     * @param readlimit ignored
     */
    @Override
    public synchronized void mark(int readlimit) {
        mark = pos;
    }

    /**
     * mark() supported.
     * @return true
     */
    @Override
    public boolean markSupported() {
        return true;
    }

    /**
     * Reads the next byte of data from the input stream.
     * @return the next byte of data, or -1 if the end of the stream is reached.
     * @throws java.io.IOException When this method is called on a closed stream.
     */
    @Override
    public int read() throws IOException {
        final byte[] b = new byte[1];
        return (read(b, 0, 1) != -1 ? b[0] : -1);
    }

    /**
     * Reads some number of bytes from the input stream and stores them into the buffer array b.
     * @param b the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or -1 is there is no more data because the end of the stream has been reached.
     * @throws java.io.IOException When this method is called on a closed stream.
     */
    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    /**
     * Reads up to len bytes of data from the input stream into an array of bytes.
     * @param b the buffer into which the data is read.
     * @param off the start offset in array b at which the data is written.
     * @param len the maximum number of bytes to read.
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of the stream has been reached.
     * @throws java.io.IOException When this method is called on a closed stream.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        validateStreamOpen();

        if (pos > last) {
            return -1;
        }
        
        if (pos + len > last) {
            len -= ((pos + len) - (last + 1));
        }
        if (len == 0) {
            return 0;
        }
        
        read(pos, b, off, len);
        pos += len;
        return len;
    }

    /**
     * Repositions this stream to the position at the time the mark method was last called on this input stream.
     * @throws java.io.IOException When this method is called on a closed stream.
     */
    @Override
    public synchronized void reset() throws IOException {
        validateStreamOpen();
        
        pos = mark;
    }

    /**
     * Skips over and discards n bytes of data from this input stream.
     * @param n the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws java.io.IOException When this method is called on a closed stream.
     */
    @Override
    public long skip(long n) throws IOException {
        validateStreamOpen();

        if (pos > last) {
            return 0;
        }
        
        pos += n;
        return (pos > last ? n - (pos - last) : n);
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

    protected abstract void read(long pos, byte[] b, int off, int len) throws IOException;
}