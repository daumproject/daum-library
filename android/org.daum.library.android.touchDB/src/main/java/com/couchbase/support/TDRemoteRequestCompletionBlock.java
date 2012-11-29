package com.couchbase.support;


public interface TDRemoteRequestCompletionBlock {

    public void onCompletion(Object result, Throwable e);

}
