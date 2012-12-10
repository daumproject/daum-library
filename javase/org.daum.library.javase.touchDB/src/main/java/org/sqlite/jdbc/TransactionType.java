package org.sqlite.jdbc;

/**
 * transaction type enum class.
 * @author calico
 * @see <a href="http://www.sqlite.org/lang_transaction.html">BEGIN TRANSACTION</a>
 */
public enum TransactionType {
    // TODO org.sqlite.jdbc以外のパッケージ（例えばorg.sqlite.sql等）に移動すべき？
    DEFERRED, IMMEDIATE, EXCLUSIVE;
}
