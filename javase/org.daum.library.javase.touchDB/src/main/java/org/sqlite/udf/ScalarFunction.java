package org.sqlite.udf;

/**
 * User-Defined scalar function class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/create_function.html">Create Or Redefine SQL Functions</a>
 * @see org.sqlite.jdbc.JdbcConnection#createFunction(org.sqlite.udf.Function)
 * @see org.sqlite.jdbc.JdbcConnection#dropFunction(org.sqlite.udf.Function)
 */
public abstract class ScalarFunction extends Function {
    /**
     * create scalar function object.
     * @param name the function name
     * @see org.sqlite.udf.Function#Function(String)
     */
    public ScalarFunction(String name) {
        super(name);
    }
    
    /**
     * create scalar function object.
     * @param name the function name
     * @param argc the number of arguments
     * @see org.sqlite.udf.Function#Function(String, int)
     */
    public ScalarFunction(String name, int argc) {
        super(name, argc);
    }
    
    /**
     * create scalar function object.
     * @param name the function name
     * @param argc the number of arguments
     * @param enc the specifies what text encoding this function prefers for its parameters
     * @see <a href="http://sqlite.org/c3ref/c_any.html">Text Encodings</a>
     * @see org.sqlite.udf.Function#Function(String, int, int)
     */
    public ScalarFunction(String name, int argc, int enc) {
        super(name, argc, enc);
    }
}
