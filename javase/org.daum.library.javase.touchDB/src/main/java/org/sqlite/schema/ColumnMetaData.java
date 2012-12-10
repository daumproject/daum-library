package org.sqlite.schema;

/**
 * sqlite3_table_column_metadata() output values.
 * @author calico
 * @see <a href="http://www.sqlite.org/c3ref/table_column_metadata.html">Extract Metadata About A Column Of A Table</a>
 * @see org.sqlite.Database#getColumnMetaData(String, String, String)
 * @see org.sqlite.jdbc.JdbcResultSetMetaData#getColumnMetaData(int)
 */
public class ColumnMetaData {
    public final String dataType;
    public final String collationSequenceName;
    public final boolean isNotNull;
    public final boolean isPrimaryKey;
    public final boolean isAutoIncrement;
    
    public ColumnMetaData(String dataType, String collSeq, int notNull, int primaryKey, int autinc) {
        this.dataType = dataType;
        this.collationSequenceName = collSeq;
        this.isNotNull = (notNull != 0);
        this.isPrimaryKey = (primaryKey != 0);
        this.isAutoIncrement = (autinc != 0);
    }
}
