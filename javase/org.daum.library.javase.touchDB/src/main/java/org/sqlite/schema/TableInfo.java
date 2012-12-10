package org.sqlite.schema;

/**
 * PRAGMA table_info() value.
 * @author calico
 * @see <a href="http://www.sqlite.org/pragma.html#pragma_table_info">PRAGMA table_info(table-name);</a>
 * @see org.sqlite.jdbc.JdbcDatabaseMetaData#getTableInfo(String)
 */
public class TableInfo {
    public final int columnIndex;
    public final String columnName;
    public final String dataType;
    public final boolean isNotNull;
    /**
     * @see org.sqlite.swig.SQLite3#unescapeSingleQuotedString(String)
     */
    public final String defaultValue;
    public final boolean isPrimaryKey;

    public TableInfo(int cin, String name, String type, int notnull, String dflt_value, int pk) {
        this.columnIndex = cin;
        this.columnName = name;
        this.dataType = type;
        this.isNotNull = (notnull != 0);
        this.defaultValue = dflt_value;
        this.isPrimaryKey = (pk != 0);
    }
}
