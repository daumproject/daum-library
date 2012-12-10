package org.sqlite.io;

import org.sqlite.swig.SQLite3;
import org.sqlite.swig.SWIGTYPE_p_void;

/**
 *
 * @author calico
 */
public class MemoryInputStream extends InputStreamAdapter  {
    private final SWIGTYPE_p_void blob;

    /**
     * default constructor.
     * @param owner the owner Closeable object
     * @param blob pointer of BLOB object
     * @param len the number of bytes
     */
    public MemoryInputStream(Closeable owner, SWIGTYPE_p_void blob, long len) {
        super(owner, len);
        this.blob = blob;
    }

    @Override
    protected void read(long pos, byte[] b, int off, int len) {
        SQLite3.read_blob(blob, pos, b, off, len);
    }
}
