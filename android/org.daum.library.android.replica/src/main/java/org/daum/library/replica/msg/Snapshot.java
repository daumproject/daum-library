package org.daum.library.replica.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 25/05/12
 * Time: 11:56
 */
public class Snapshot extends AMessage {

    public List<Update> snapshot = new ArrayList<Update>();

    public List<Update> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(List<Update> snapshot) {
        this.snapshot = snapshot;
    }
}
