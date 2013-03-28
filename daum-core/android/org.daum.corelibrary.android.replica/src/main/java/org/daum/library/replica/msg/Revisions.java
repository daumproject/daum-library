package org.daum.library.replica.msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 26/03/13
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class Revisions extends  AMessage
{
    public List<Revision> snapshot = new ArrayList<Revision>();

    public List<Revision> getRevisions() {
        return snapshot;
    }

    public  void add(Revision r )
    {
        snapshot.add(r);
    }

    public void setSnapshot(List<Revision> snapshot) {
        this.snapshot = snapshot;
    }
}
