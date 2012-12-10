package org.sqlite.udf;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * User-Defined aggregate function class.
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/create_function.html">Create Or Redefine SQL Functions</a>
 * @see org.sqlite.jdbc.JdbcConnection#createFunction(Function)
 * @see org.sqlite.jdbc.JdbcConnection#dropFunction(Function)
 */
public abstract class AggregateFunction<T> extends Function {
    /**
     * create aggregate function object.
     * @param name the function name
     * @see Function#Function(String)
     */
    public AggregateFunction(String name) {
        super(name);
    }
    
    /**
     * create aggregate function object.
     * @param name the function name
     * @param argc the number of arguments that the aggregate takes.
     * @see Function#Function(String, int)
     */
    public AggregateFunction(String name, int argc) {
        super(name, argc);
    }
    
    /**
     * create aggregate function object.
     * @param name the function name
     * @param argc the number of arguments that the aggregate takes.
     * @param enc the specifies what text encoding this function prefers for its parameters
     * @see <a href="http://sqlite.org/c3ref/c_any.html">Text Encodings</a>
     * @see Function#Function(String, int, int)
     */
    public AggregateFunction(String name, int argc, int enc) {
        super(name, argc, enc);
    }
    
    /**
     * invoke xStep() method.
     * @param ctx sqlite3_context wrapper object
     * @throws java.sql.SQLException
     * @see #xStep(org.sqlite.udf.Context)
     */
    @Override
    protected final void xFunc(Context ctx) throws SQLException {
        xStep(ctx);
    }
    
    /**
     * Called from the sqlite3_step() function.
     * @param context
     * @see #xFinal(org.sqlite.udf.Context)
     */
    protected final void xFinal(long context) {
        // TODO Mysaifu JVMのBug#11980が解決したらアクセス修飾子をprivateに戻すこと！
        // @see http://sourceforge.jp/tracker/index.php?func=detail&aid=11980&group_id=1890&atid=7027
        Context ctx = null;
        try {
            ctx = new Context(context);
            xFinal(ctx);
            
        } catch (Throwable th) {
            if (ctx != null) {
                final String msg = th.toString();
                ctx.resultError((msg != null ? msg : "Unknown error."));
                
            } else {
                Logger.getLogger(AggregateFunction.class.getName()).fine("Exception occurred: " + th.toString());
            }
        }
    }

    /** thread local storage */
    protected final ThreadLocal<T> tls
            = new ThreadLocal<T>() {
                    @Override
                    protected T initialValue() {
                        return internalInitialValue();
                    }
                };
    
    private T internalInitialValue() {
        return initialValue();
    }
    
    /**
     * Returns the current thread's "initial value" for this thread-local variable. 
     * @return the initial value for this thread-local
     * @see ThreadLocal#initialValue()
     */
    protected abstract T initialValue();
    
    /**
     * Returns the value in the current thread's copy of this thread-local variable.
     * @return the current thread's value of this thread-local
     * @see ThreadLocal#get()
     */
    protected T get() {
        return tls.get();
    }
    
    /**
     * Removes the current thread's value for this thread-local variable.
     * @see ThreadLocal#remove()
     */
    protected void remove() {
        tls.remove();
    }
    
    /**
     * Sets the current thread's copy of this thread-local variable to the specified value.
     * @param value the value to be stored in the current thread's copy of this thread-local.
     * @see ThreadLocal#set(Object)
     */
    protected void set(T value) {
        tls.set(value);
    }
    
    protected abstract void xStep(Context ctx) throws SQLException;

    protected abstract void xFinal(Context ctx) throws SQLException;
}
