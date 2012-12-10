package com.couchbase.touchdb;

/**
 * Validation block, used to approve revisions being added to the database.
 */
public interface TDValidationBlock {

    Boolean validate(TDRevision newRevision, TDValidationContext context);

}
