package org.sqlite;

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import org.sqlite.jdbc.JdbcConnection;
import org.sqlite.jdbc.Scheme;

/**
 * SQLite JDBC Driver class
 * @author calico
 */
public class Driver implements java.sql.Driver {
    static {
        try {
            System.load("/home/jed/DAUM_PROJECT/daum-library/javase/org.daum.library.javase.touchDB/src/main/resources/libsqlite3jni.so");
            DriverManager.registerDriver(new Driver());
        } catch (SQLException ex) {
            Logger.getLogger(Driver.class.getName()).throwing(Driver.class.getName(), "<clinit>", ex);
            throw new Error(ex.fillInStackTrace());
        }
    }

    /**
     * It always returns "SQLite JDBC Driver".
     * @return "SQLite JDBC Driver"
     */
    public static String getDriverName() {
        return "SQLite JDBC Driver";
    }
    
    /**
     * Returns the version of SQLite JDBC Driver.
     * @return getDriverMajorVersion() + "." + getDriverMinorVersion() + "." + getDriverReleaseVersion()
     * @see #getDriverMajorVersion()
     * @see #getDriverMinorVersion()
     * @see #getDriverReleaseVersion()
     */
    public static String getDriverVersion() {
        return getDriverMajorVersion()
                + "." + getDriverMinorVersion()
                + "." + getDriverReleaseVersion();
    }
    
    /**
     * Returns the major version number of SQLite JDBC Driver.
     * @return the major version number
     */
    public static int getDriverMajorVersion() {
        return 1;
    }

    /**
     * Returns the minor version number of SQLite JDBC Driver.
     * @return the minor version number
     */
    public static int getDriverMinorVersion() {
        return 1;
    }
    
    /**
     * Returns the release version number of SQLite JDBC Driver.
     * @return the release version number
     */
    public static int getDriverReleaseVersion() {
        return 4;
    }
    
    // START implements
    /**
     * Retrieves whether the driver thinks that it can open a connection to the given URL.
     * @param url the URL of the database
     * @return true if this driver understands the given URL; false otherwise
     * @see org.sqlite.jdbc.Scheme#JDBC_SQLITE_FILE
     * @see org.sqlite.jdbc.Scheme#JDBC_SQLITE_MEMORY
     * @see org.sqlite.jdbc.Scheme#JDBC_SQLITE
     */
    public boolean acceptsURL(String url) {
        for (Scheme scheme : Scheme.values()) {
            if (scheme.matches(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempts to make a database connection to the given URL.
     * @param url the URL of the database to which to connect
     * @param info a list of arbitrary string tag/value pairs as connection arguments.
     * @return a Connection object that represents a connection to the URL
     * @throws java.sql.SQLException if a database access error occurs
     */
    public JdbcConnection connect(String url, Properties info) throws SQLException {
        for (final Scheme scheme : Scheme.values()) {
            final Map<String, String> prop = scheme.parse(url);
            if (prop != null) {
                final String filename = prop.remove(";filename;");
                if (info != null) {
                    // put all
                    for (final Object obj : info.keySet()) {
                        final String key = obj.toString();
                        prop.put(key, info.getProperty(key));
                    }
                }
                final Database db = new Database(filename, (prop.isEmpty() ? null : prop));
                return new JdbcConnection(db, url);
            }
        }
        // unsupported URL
        return null;
    }

    /**
     * invoke getDriverMajorVersion() method.
     * @return the major version number
     * @see #getDriverMajorVersion()
     */
    public int getMajorVersion() {
        return getDriverMajorVersion();
    }

    /**
     * invoke getDriverMinorVersion() method.
     * @return the minor version number
     * @see #getDriverMinorVersion()
     */
    public int getMinorVersion() {
        return getDriverMinorVersion();
    }

    /**
     * Gets information about the possible properties for this driver.
     * @param url the URL of the database to which to connect
     * @param info a proposed list of tag/value pairs that will be sent on connect open
     * @return an array of DriverPropertyInfo objects describing possible properties.
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[] {
                    new DriverPropertyInfo("TEMP_DIR", ""),
                    new DriverPropertyInfo("READ_ONLY", ""),
                    new DriverPropertyInfo("OPEN_EXIST", ""),
                    new DriverPropertyInfo("VFS", ""),
                };
    }

    /**
     * It always returns false.
     * @return false
     */
    public boolean jdbcCompliant() {
        return false;
    }
    // END implements
    
}
