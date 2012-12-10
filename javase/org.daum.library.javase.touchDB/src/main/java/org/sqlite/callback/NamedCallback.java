package org.sqlite.callback;

import static org.sqlite.swig.SQLite3Constants.SQLITE_UTF8;

/**
 * SQLite Named Callback function class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/create_function.html">Create Or Redefine SQL Functions</a>
 * @see <a href="http://sqlite.org/c3ref/create_collation.html">Define New Collating Sequences</a>
 */
public abstract class NamedCallback extends Callback {
    
    /** function name */
    private final String name;
    
    /** specifies what text encoding this function prefers for its parameters */
    private final int enc;
    
    /**
     * create named callback object with SQLITE_UTF8.
     * @param name the function name
     * @see org.sqlite.callback.NamedCallback#NamedCallback(String, int)
     */
    public NamedCallback(String name) {
        this(name, SQLITE_UTF8);
    }
    
    /**
     * create named callback object.
     * @param name the function name
     * @param enc the most desirable encoding may be one of the constants SQLITE_UTF8, SQLITE_UTF16LE, SQLITE_UTF16BE, SQLITE_UTF16 or SQLITE_UTF16_ALIGNED.
     * @see <a href="http://sqlite.org/c3ref/c_any.html">Text Encodings</a>
     */
    public NamedCallback(String name, int enc) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        this.name = name;
        this.enc = enc;
    }
    
    /**
     * Returns the function name.
     * @return the function name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the text encoding code.
     * @return the text encoding code
     */
    public int getEncoding() {
        return enc;
    }
}
