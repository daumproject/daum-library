package org.sqlite.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import org.sqlite.Statement;

/**
 *
 * @author calico
 */
public class JdbcParameterMetaData implements ParameterMetaData {
    private final Statement stmt;
    
    /**
     * default constructor.
     * @param stmt
     */
    public JdbcParameterMetaData(Statement stmt) {
        this.stmt = stmt;
    }
    
    // START implements
    /**
     * invoke org.sqlite.Statement#getParameterCount() method.
     * @return parameter count
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getParameterCount()
     */
    public int getParameterCount() throws SQLException {
        return stmt.getParameterCount();
    }

    /**
     * CallableStatement is not supported yet.<br/>
     * It always returns parameterNullable.
     * @param param ignored
     * @return java.sql.ParameterMetaData.parameterNullable
     */
    public int isNullable(int param) {
        return parameterNullable;
    }

    // TODO do implements. If you're so inclined.

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public boolean isSigned(int param) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public int getPrecision(int param) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public int getScale(int param) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public int getParameterType(int param) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public String getParameterTypeName(int param) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Not supporetd yet.
     * @throws UnsupportedOperationException
     */
    public String getParameterClassName(int param) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * CallableStatement is not supported yet.<br/>
     * It always returns parameterModeIn.
     * @param param ignored
     * @return java.sql.ParameterMetaData.parameterModeIn
     */
    public int getParameterMode(int param) {
        return parameterModeIn;
    }
    // END implements

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
