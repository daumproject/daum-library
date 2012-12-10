package org.sqlite.schema;

/**
 * PRAGMA index_list() value.
 * @author calico
 * @see <a href="http://www.sqlite.org/pragma.html#pragma_index_list">PRAGMA index_list(table-name);</a>
 * @see org.sqlite.jdbc.JdbcDatabaseMetaData#getIndexList(String)
 */
public class IndexList {
    public final int seq;
    public final String indexName;
    public final boolean isUnique;

    public IndexList(int seq, String name, int unique) {
        this.seq = seq;
        this.indexName = name;
        this.isUnique = (unique != 0);
    }
}
