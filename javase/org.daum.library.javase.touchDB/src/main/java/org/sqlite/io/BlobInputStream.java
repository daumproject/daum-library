package org.sqlite.io;

import java.io.IOException;
import java.sql.SQLException;
import org.sqlite.Blob;

/**
 *
 * @author calico
 */
public class BlobInputStream extends InputStreamAdapter {

    private final Blob blob;
    
    /**
     * default constructor.
     * @param owner the owner Closeable object
     * @param blob the sqlite3_blob object.
     * @throws java.sql.SQLException When this method is called on a closed blob.
     */
    public BlobInputStream(Closeable owner, Blob blob) throws SQLException {
        super(owner, blob.length());
        this.blob = blob;
    }
    
    @Override
    protected void read(long pos, byte[] b, int off, int len) throws IOException {
        try {
            if (off == 0) {
                blob.read(b, len, (int) pos);
                
            } else {
                byte[] buff = new byte[len];
                blob.read(buff, len, (int) pos);
                System.arraycopy(buff, 0, b, off, len);
            }
        } catch (SQLException ex) {
            throw new WrappedIOException(ex);
        }
    }

}
