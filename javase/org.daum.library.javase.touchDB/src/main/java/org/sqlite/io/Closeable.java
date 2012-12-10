package org.sqlite.io;

import java.sql.SQLException;

/**
 * An Interface extended a java.io.Closeable interface.
 * @author calico
 */
public interface Closeable {

    /**
     * Closes this object and releases any system resources associated with it.
     * @throws java.sql.SQLException if a database access error occurs
     */
    void close() throws SQLException;

    /**
     * Retrieves whether this object has been closed.
     * @return true if this object is closed; false if it is still open
     */
    boolean isClosed();
}
