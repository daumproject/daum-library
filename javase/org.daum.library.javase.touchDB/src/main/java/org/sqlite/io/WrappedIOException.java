package org.sqlite.io;

import java.io.IOException;
import java.sql.SQLException;

/**
 * IOException class caused by SQLException.
 * @author calico
 */
public class WrappedIOException extends IOException {
    public WrappedIOException(SQLException ex) {
        super(ex.getMessage());
        initCause(ex);
    }
    
    /**
     * Returns the cause of this SQLException.
     * @return the cause of this SQLException
     */
    @Override
    public SQLException getCause() {
        return (SQLException) super.getCause();
    }
}
