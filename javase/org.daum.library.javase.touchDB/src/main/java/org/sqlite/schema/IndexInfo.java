package org.sqlite.schema;

/**
 * PRAGMA index_info() value.
 * @author calico
 * @see <a href="http://www.sqlite.org/pragma.html#pragma_index_info">PRAGMA index_info(index-name);</a>
 * @see org.sqlite.jdbc.JdbcDatabaseMetaData#getIndexInfo(String)
 */
public class IndexInfo {
    public final int seqNo;
    public final int columnIndex;
    public final String columnName;

    public IndexInfo(int seqno, int cin, String name) {
        this.seqNo = seqno;
        this.columnIndex = cin;
        this.columnName = name;
    }
}
