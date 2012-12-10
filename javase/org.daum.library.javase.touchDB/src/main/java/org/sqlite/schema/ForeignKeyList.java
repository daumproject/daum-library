package org.sqlite.schema;

/**
 * PRAGMA foreign_key_list() value.
 * @author calico
 * @see <a href="http://www.sqlite.org/pragma.html#pragma_foreign_key_list">PRAGMA foreign_key_list(table-name);</a>
 * @see org.sqlite.jdbc.JdbcDatabaseMetaData#getForeignKeyList(String)
 */
public class ForeignKeyList {
    public final int id;
    public final int seq;
    public final String table;
    public final String from;
    public final String to;
    
    public ForeignKeyList(int id, int seq, String table, String from, String to) {
        this.id = id;
        this.seq = seq;
        this.table = table;
        this.from = from;
        this.to = to;
    }
}
