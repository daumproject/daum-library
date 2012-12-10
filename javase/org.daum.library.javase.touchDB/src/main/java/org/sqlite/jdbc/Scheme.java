package org.sqlite.jdbc;

import java.util.HashMap;
import java.util.Map;
import org.sqlite.swig.SQLite3;

/**
 * JDBC URL Schemes.
 * @author calico
 */
public enum Scheme {
    /**
     * JDBC URL Scheme "jdbc:sqlite:file:".
     */
    JDBC_SQLITE_FILE("jdbc:sqlite:file:"),
    
    /**
     * JDBC URL Scheme "jdbc:sqlite:mem:".
     */
    JDBC_SQLITE_MEMORY("jdbc:sqlite:mem:"),
    
    /**
     * JDBC URL Scheme "jdbc:sqlite:".
     */
    JDBC_SQLITE("jdbc:sqlite:");
    
    /* URL Schemes for SQLite.JDBCDriver  */
//    CHRISTIAN_JDBC_SQLITE("jdbc:sqlite:/"),
//    CHRISTIAN_SQLITE("sqlite:/");

    private final String scheme;

    private Scheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return scheme;
    }

    public boolean matches(String url) {
        return url.startsWith(scheme);
    }

    public Map<String, String> parse(String url) {
        Map<String, String> prop = null;
        if (matches(url)) {
            prop = new HashMap<String, String>();
            final String[] props = url.substring(scheme.length()).split(";");
            // set filename
            prop.put(
                    ";filename;",
                    (this != JDBC_SQLITE_MEMORY
                        ? props[0]
                        : SQLite3.getInMemoryFileName())
                );
            // set properties
            for (int i = 1; i < props.length; ++i) {
                final String[] option = props[i].split("=", 2);
                prop.put(option[0], (option.length == 2 ? option[1] : ""));
            }
        }
        return prop;
        
        // for example:
        // open exist file and read only mode:
        //      jdbc:sqlite:file:\My Documents\sqlite\database;OPEN_MODE=READONLY
        // open exist file and read/write mode:
        //      jdbc:sqlite:file:\My Documents\sqlite\database;OPEN_MODE=READWRITE
        // open exist file or create file and read/write mode (DEFAULT):
        //      jdbc:sqlite:file:\My Documents\sqlite\database;OPEN_MODE=CREATE
        // open in-memory database:
        //      jdbc:sqlite:mem:
    }
}
