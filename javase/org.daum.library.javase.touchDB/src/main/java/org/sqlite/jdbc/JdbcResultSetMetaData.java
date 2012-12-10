package org.sqlite.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.sqlite.schema.ColumnMetaData;
import org.sqlite.Statement;
import static org.sqlite.swig.SQLite3Constants.SQLITE_BLOB;
import static org.sqlite.swig.SQLite3Constants.SQLITE_FLOAT;
import static org.sqlite.swig.SQLite3Constants.SQLITE_INTEGER;
import static org.sqlite.swig.SQLite3Constants.SQLITE_NULL;
import static org.sqlite.swig.SQLite3Constants.SQLITE_RANGE;
import static org.sqlite.swig.SQLite3Constants.SQLITE3_TEXT;

/**
 *
 * @author calico
 */
public class JdbcResultSetMetaData implements ResultSetMetaData {
    private final Statement stmt;

    public JdbcResultSetMetaData(Statement stmt) {
        this.stmt = stmt;
    }
    
    // START implements
    /**
     * invoke org.sqlite.Statement#getColumnCount() method.
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnCount()
     */
    public int getColumnCount() throws SQLException {
        return stmt.getColumnCount();
    }

    /**
     * Returns the value of org.sqlite.schema.ColumnMetaData.isAutoIncrement.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getColumnMetaData(int)
     * @see org.sqlite.schema.ColumnMetaData#isAutoIncrement
     */
    public boolean isAutoIncrement(int column) throws SQLException {
        final ColumnMetaData meta = getColumnMetaData(column);
        return meta.isAutoIncrement;
    }

    /**
     * Returns true when the value of org.sqlite.schema.ColumnMetaData.collationSequenceName is 'NOCASE'.
     * @param column the first column is 1, the second is 2, ...
     * @return True when Collation Sequences is 'NOCASE'. When it is not 'NOCASE', false is returned. 
     * @throws java.sql.SQLException
     * @see #getColumnMetaData(int)
     * @see org.sqlite.schema.ColumnMetaData#collationSequenceName
     */
    public boolean isCaseSensitive(int column) throws SQLException {
        final ColumnMetaData meta = getColumnMetaData(column);
        return "NOCASE".equalsIgnoreCase(meta.collationSequenceName);
    }

    /**
     * It always returns true.
     * @param column the first column is 1, the second is 2, ...
     * @return true
     * @throws java.sql.SQLException
     * @see JdbcDatabaseMetaData#getTypeInfo()
     */
    public boolean isSearchable(int column) throws SQLException {
        stmt.validateColumnIndexRange(column);
        
        return true;
    }

    /**
     * It always returns false.
     * @param column the first column is 1, the second is 2, ...
     * @return false
     * @throws java.sql.SQLException
     */
    public boolean isCurrency(int column) throws SQLException {
        stmt.validateColumnIndexRange(column);
        
        return false;
    }

    /**
     * Returns columnNoNulls if org.sqlite.schema.ColumnMetaData.isNotNull is true.
     * @param column the first column is 1, the second is 2, ...
     * @return columnNoNulls if org.sqlite.schema.ColumnMetaData.isNotNull is true.
     * @throws java.sql.SQLException
     * @see #getColumnMetaData(int)
     * @see org.sqlite.schema.ColumnMetaData#isNotNull
     */
    public int isNullable(int column) throws SQLException {
        final ColumnMetaData meta = getColumnMetaData(column);
        return (meta.isNotNull ? columnNoNulls : columnNullable);
    }

    /**
     * invoke getSQLiteColumnType(int) and org.sqlite.jdbc.JdbcDatabaseMetaData#isSigned(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getSQLiteColumnType(int)
     * @see JdbcDatabaseMetaData#isSigned(int)
     * @see JdbcDatabaseMetaData#getTypeInfo()
     */
    public boolean isSigned(int column) throws SQLException {
        return JdbcDatabaseMetaData.isSigned(getSQLiteColumnType(column));
    }

    /**
     * invoke getSQLiteColumnType(int) and org.sqlite.jdbc.JdbcDatabaseMetaData#getColumnDisplaySize(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getSQLiteColumnType(int)
     * @see JdbcDatabaseMetaData#getColumnDisplaySize(int)
     * @see org.hsqldb.Types#getMaxDisplaySize(int)
     */
    public int getColumnDisplaySize(int column) throws SQLException {
        return JdbcDatabaseMetaData.getColumnDisplaySize(getSQLiteColumnType(column));
    }

    /**
     * invoke org.sqlite.Statement#getColumnLabel(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnLabel(int)
     */
    public String getColumnLabel(int column) throws SQLException {
        return stmt.getColumnLabel(column);
    }

    /**
     * invoke org.sqlite.Statement#getColumnName(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnName(int)
     */
    public String getColumnName(int column) throws SQLException {
        return stmt.getColumnName(column);
    }

    /**
     * invoke getDatabaseName(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return database name
     * @throws java.sql.SQLException
     * @see #getDatabaseName(int)
     */
    public String getSchemaName(int column) throws SQLException {
        return getDatabaseName(column);
    }

    /**
     * invoke getSQLiteColumnType(int) and org.sqlite.jdbc.JdbcDatabaseMetaData#getPrecision(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getSQLiteColumnType(int)
     * @see JdbcDatabaseMetaData#getPrecision(int)
     * @see JdbcDatabaseMetaData#getTypeInfo()
     */
    public int getPrecision(int column) throws SQLException {
        return JdbcDatabaseMetaData.getPrecision(getSQLiteColumnType(column));
    }

    /**
     * invoke getSQLiteColumnType(int) and org.sqlite.jdbc.JdbcDatabaseMetaData#getScale(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getSQLiteColumnType(int)
     * @see JdbcDatabaseMetaData#getScale(int)
     */
    public int getScale(int column) throws SQLException {
        return JdbcDatabaseMetaData.getScale(getSQLiteColumnType(column));
    }

    /**
     * invoke org.sqlite.Statement#getColumnTableName(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnTableName(int)
     */
    public String getTableName(int column) throws SQLException {
        return stmt.getColumnTableName(column);
    }

    /**
     * It always returns empty string.
     * @param column the first column is 1, the second is 2, ...
     * @return empty string
     * @throws java.sql.SQLException
     */
    public String getCatalogName(int column) throws SQLException {
        stmt.validateColumnIndexRange(column);
        
        return "";
    }

    /**
     * invoke getSQLiteColumnType(int) and org.sqlite.jdbc.JdbcDatabaseMetaData#getColumnType(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getSQLiteColumnType(int)
     * @see JdbcDatabaseMetaData#getColumnType(int)
     */
    public int getColumnType(int column) throws SQLException {
        return JdbcDatabaseMetaData.getColumnType(getSQLiteColumnType(column));
    }

    /**
     * invoke org.sqlite.Statement#getColumnTypeName(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnTypeName(int)
     */
    public String getColumnTypeName(int column) throws SQLException {
        return stmt.getColumnTypeName(column);
    }

    /**
     * It always returns true.
     * @param column the first column is 1, the second is 2, ...
     * @return true
     * @throws java.sql.SQLException
     */
    public boolean isReadOnly(int column) throws SQLException {
        stmt.validateColumnIndexRange(column);
        
        return true;
    }

    /**
     * It always returns false.
     * @param column the first column is 1, the second is 2, ...
     * @return false
     * @throws java.sql.SQLException
     */
    public boolean isWritable(int column) throws SQLException {
        stmt.validateColumnIndexRange(column);
        
        return false;
    }

    /**
     * It always returns false.
     * @param column the first column is 1, the second is 2, ...
     * @return false
     * @throws java.sql.SQLException
     */
    public boolean isDefinitelyWritable(int column) throws SQLException {
        stmt.validateColumnIndexRange(column);
        
        return false;
    }

    /**
     * invoke getSQLiteColumnType(int) and org.sqlite.jdbc.JdbcDatabaseMetaData#getColumnClassName(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see #getSQLiteColumnType(int)
     * @see JdbcDatabaseMetaData#getColumnClassName(int)
     */
    public String getColumnClassName(int column) throws SQLException {
        return JdbcDatabaseMetaData.getColumnClassName(getSQLiteColumnType(column));
    }
    // END implements

    /**
     * invoke org.sqlite.Statement#getColumnType(int) and org.sqlite.Statement#getColumnTypeName(int) method.
     * @param column the first column is 1, the second is 2, ...
     * @return the column data type
     * @throws java.sql.SQLException
     * @see <a href="http://sqlite.org/c3ref/c_blob.html">Fundamental Datatypes</a>
     * @see org.sqlite.Statement#getColumnType(int)
     * @see org.sqlite.Statement#getColumnTypeName(int)
     * @see <a href="http://www.sqlite.org/datatype3.html#affinity">Datatypes In SQLite Version 3 - 2. Column Affinity</a>
     */
    public int getSQLiteColumnType(int column) throws SQLException {
        try {
            return stmt.getColumnType(column);
            
        } catch (SQLException ex) {
            if (ex.getErrorCode() == SQLITE_RANGE) {
                String name = stmt.getColumnTypeName(column);
                if (name != null) {
                    name = name.toUpperCase();
                    if ("INTEGER".equals(name) || "INT".equals(name)) {
                        return SQLITE_INTEGER;

                    } else if ("TEXT".equals(name) || "VARCHAR".equals(name)
                            || "CHAR".equals(name) || "CLOB".equals(name)) {
                        return SQLITE3_TEXT;

                    } else if ("BLOB".equals(name)) {
                        return SQLITE_BLOB;

                    } else if ("REAL".equals(name) || "FLOA".equals(name) || "DOUB".equals(name)) {
                        return SQLITE_FLOAT;

                    } else if ("NULL".equals(name)) {
                        return SQLITE_NULL;
                    }
                    return SQLITE3_TEXT;
                }
            }
            throw ex;
        }
    }
    
    /**
     * Get the designated column's table's database name.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Statement#getColumnDatabaseName(int)
     */
    public String getDatabaseName(int column) throws SQLException {
        return stmt.getColumnDatabaseName(column);
    }
    
    /**
     * invoke org.sqlite.Database#getColumnMetaData(String, String, String) method.
     * @param column the first column is 1, the second is 2, ...
     * @return
     * @throws java.sql.SQLException
     * @see org.sqlite.Database#getColumnMetaData(String, String, String)
     */
    public ColumnMetaData getColumnMetaData(int column) throws SQLException {
        final String dbName = stmt.getColumnDatabaseName(column);
        final String tableName = stmt.getColumnTableName(column);
        final String columnName = stmt.getColumnName(column);
        return stmt.getDatabase().getColumnMetaData(dbName, tableName, columnName);
    }

    @Override
    public <T> T unwrap(Class<T> tClass) throws SQLException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
