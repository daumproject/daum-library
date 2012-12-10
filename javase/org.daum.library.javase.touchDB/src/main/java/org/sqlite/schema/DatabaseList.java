package org.sqlite.schema;

/**
 * PRAGMA database_list() value.
 * @author calico
 * @see <a href="http://www.sqlite.org/pragma.html#pragma_database_list">PRAGMA database_list;</a>
 * @see org.sqlite.jdbc.JdbcDatabaseMetaData#getDatabaseList()
 */
public class DatabaseList {
    public final int seq;
    public final String name;
    public final String file;

    public DatabaseList(int seq, String name, String file) {
        this.seq = seq;
        this.name = name;
        this.file = file;
    }
}
