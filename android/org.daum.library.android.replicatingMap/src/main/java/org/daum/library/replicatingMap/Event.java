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
		ADD, DELETE,UPDATE
	}

	public Operation op;
	public String key;
	public Object value;

}