package org.daum.library.replicatingMap;

/**
 * Created by jed
 * User: jedartois@gmail.com
 * Date: 22/05/12
 * Time: 17:51
 */
import java.io.Serializable;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Operation {
		ADD, DELETE,UPDATE,SNAPSHOT
	}

    public int count;
	public Operation op;
	public Object key;
	public Object value;
    public String cache;
    public  String source;
}