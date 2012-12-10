package org.sqlite;

import static org.sqlite.swig.SQLite3.sqlite3_mutex_alloc;
import static org.sqlite.swig.SQLite3.sqlite3_mutex_enter;
import static org.sqlite.swig.SQLite3.sqlite3_mutex_free;
import static org.sqlite.swig.SQLite3.sqlite3_mutex_leave;
import static org.sqlite.swig.SQLite3.sqlite3_mutex_try;
import org.sqlite.swig.SWIGTYPE_p_sqlite3_mutex;

/**
 * sqlite3_mutex wrapper class.<br/>
 * @author calico
 * @see <a href="http://sqlite.org/c3ref/mutex_alloc.html">Mutexes</a>
 */
public class Mutex {
    private final SWIGTYPE_p_sqlite3_mutex mutex;
    private boolean isDeleted;
    
    /**
     * invoke sqlite3_mutex_alloc() function.
     * @param type the mutex type.
     * @see <a href="http://sqlite.org/c3ref/c_mutex_fast.html">Mutex Types</a>
     * @see <a href="http://sqlite.org/c3ref/mutex_alloc.html">Mutexes</a>
     */
    public Mutex(int type) {
        this.mutex = sqlite3_mutex_alloc(type);
        if (this.mutex == null) {
            throw new NullPointerException("Failed sqlite3_mutex_alloc(" + type + ")");
        }
        this.isDeleted = false;
    }
    
    /**
     * invoke sqlite3_mutex_enter() function.
     * @see <a href="http://sqlite.org/c3ref/mutex_alloc.html">Mutexes</a>
     */
    public void enter() {
        if (isDeleted) {
            throw new IllegalStateException("Mutex is already deleted.");
        }
        sqlite3_mutex_enter(mutex);
    }
    
    /**
     * invoke sqlite3_mutex_free() function.
     * @see <a href="http://sqlite.org/c3ref/mutex_alloc.html">Mutexes</a>
     */
    public synchronized void free() {
        if (!isDeleted) {
            sqlite3_mutex_free(mutex);
            isDeleted = true;
        }
    }

    /**
     * invoke sqlite3_mutex_leave() function.
     * @see <a href="http://sqlite.org/c3ref/mutex_alloc.html">Mutexes</a>
     */
    public void leave() {
        if (isDeleted) {
            throw new IllegalStateException("Mutex is already deleted.");
        }
        sqlite3_mutex_leave(mutex);
    }
    
    /**
     * invoke sqlite3_mutex_try() function.
     * @see <a href="http://sqlite.org/c3ref/mutex_alloc.html">Mutexes</a>
     */
    public int try_() {
        if (isDeleted) {
            throw new IllegalStateException("Mutex is already deleted.");
        }
        return sqlite3_mutex_try(mutex);
    }

    /**
     * invoke free() method.
     * @throws Throwable
     * @see #free()
     */
    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }
}
