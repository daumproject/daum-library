/**
 * Original iOS version by  Jens Alfke
 * Ported to Android by Marty Schoch
 *
 * Copyright (c) 2012 Couchbase, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.couchbase.touchdb;

import java.util.EnumSet;
import java.util.List;

import com.couchbase.touchdb.TDDatabase.TDContentOptions;

/**
 * Standard query options for views.
 */
public class TDQueryOptions {

    private Object startKey = null;
    private Object endKey = null;
    private List<Object> keys = null;
    private int skip = 0;
    private int limit = Integer.MAX_VALUE;
    private int groupLevel = 0;
    private EnumSet<TDContentOptions> contentOptions = EnumSet.noneOf(TDDatabase.TDContentOptions.class);
    private boolean descending = false;
    private boolean includeDocs = false;
    private boolean updateSeq = false;
    private boolean inclusiveEnd = true;
    private boolean reduce = false;
    private boolean group = false;

    public Object getStartKey() {
        return startKey;
    }

    public void setStartKey(Object startKey) {
        this.startKey = startKey;
    }

    public Object getEndKey() {
        return endKey;
    }

    public void setEndKey(Object endKey) {
        this.endKey = endKey;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Boolean isDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

    public Boolean isIncludeDocs() {
        return includeDocs;
    }

    public void setIncludeDocs(Boolean includeDocs) {
        this.includeDocs = includeDocs;
    }

    public Boolean isUpdateSeq() {
        return updateSeq;
    }

    public void setUpdateSeq(Boolean updateSeq) {
        this.updateSeq = updateSeq;
    }

    public Boolean isInclusiveEnd() {
        return inclusiveEnd;
    }

    public void setInclusiveEnd(Boolean inclusiveEnd) {
        this.inclusiveEnd = inclusiveEnd;
    }

    public int getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    public Boolean isReduce() {
        return reduce;
    }

    public void setReduce(Boolean reduce) {
        this.reduce = reduce;
    }

    public Boolean isGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public EnumSet<TDContentOptions> getContentOptions() {
        return contentOptions;
    }

    public void setContentOptions(EnumSet<TDContentOptions> contentOptions) {
        this.contentOptions = contentOptions;
    }

    public List<Object> getKeys() {
        return keys;
    }

    public void setKeys(List<Object> keys) {
        this.keys = keys;
    }

}
