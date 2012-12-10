package com.couchbase.touchdb;

/**
 * Filter block, used in changes feeds and replication.
 */
public interface TDFilterBlock {

    Boolean filter(TDRevision revision);

}
