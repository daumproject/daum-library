package org.daum.javase.webportal.client;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/07/12
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class DynamicChart extends TreeNode {

    public DynamicChart(String id, String parentId, String title) {
        setID(id);
        if (parentId != null) {
            setParentID(parentId);
        }
        setTitle(title);
    }

    public static DynamicChart[] getData() {
        return new DynamicChart[] {
                new DynamicChart("sum", null, "All Years"),
                new DynamicChart("2002", "sum", "2002"),
                new DynamicChart("2003", "sum", "2002"),
                new DynamicChart("2004", "sum", "2004"),
                new DynamicChart("Q1-2002", "2002", "Q1-2002"),
                new DynamicChart("Q2-2002", "2002", "Q2-2002"),
                new DynamicChart("Q3-2002", "2002", "Q3-2002"),
                new DynamicChart("Q4-2002", "2002", "Q4-2002"),
                new DynamicChart("Q1-2003", "2003", "Q1-2003"),
                new DynamicChart("Q2-2003", "2003", "Q2-2003"),
                new DynamicChart("Q3-2003", "2003", "Q3-2003"),
                new DynamicChart("Q4-2003", "2003", "Q4-2003"),
                new DynamicChart("Q1-2004", "2004", "Q1-2004"),
                new DynamicChart("Q2-2004", "2004", "Q2-2004"),
                new DynamicChart("Q3-2004", "2004", "Q3-2004"),
                new DynamicChart("Q4-2004", "2004", "Q4-2004"),
                new DynamicChart("1/1/2002", "Q1-2002", "1/1/2002"),
                new DynamicChart("2/1/2002", "Q1-2002", "2/1/2002"),
                new DynamicChart("3/1/2002", "Q1-2002", "3/1/2002"),
                new DynamicChart("4/1/2002", "Q2-2002", "4/1/2002"),
                new DynamicChart("5/1/2002", "Q2-2002", "5/1/2002"),
                new DynamicChart("6/1/2002", "Q2-2002", "6/1/2002"),
                new DynamicChart("7/1/2002", "Q3-2002", "7/1/2002"),
                new DynamicChart("8/1/2002", "Q3-2002", "8/1/2002"),
                new DynamicChart("9/1/2002", "Q3-2002", "9/1/2002"),
                new DynamicChart("10/1/2002", "Q4-2002", "10/1/2002"),
                new DynamicChart("11/1/2002", "Q4-2002", "11/1/2002"),
                new DynamicChart("12/1/2002", "Q4-2002", "12/1/2002"),
                new DynamicChart("1/1/2003", "Q1-2003", "1/1/2003"),
                new DynamicChart("2/1/2003", "Q1-2003", "2/1/2003"),
                new DynamicChart("3/1/2003", "Q1-2003", "3/1/2003"),
                new DynamicChart("4/1/2003", "Q2-2003", "4/1/2003"),
                new DynamicChart("5/1/2003", "Q2-2003", "5/1/2003"),
                new DynamicChart("6/1/2003", "Q2-2003", "6/1/2003"),
                new DynamicChart("7/1/2003", "Q3-2003", "7/1/2003"),
                new DynamicChart("8/1/2003", "Q3-2003", "8/1/2003"),
                new DynamicChart("9/1/2003", "Q3-2003", "9/1/2003"),
                new DynamicChart("10/1/2003", "Q4-2003", "10/1/2003"),
                new DynamicChart("11/1/2003", "Q4-2003", "11/1/2003"),
                new DynamicChart("12/1/2003", "Q4-2003", "12/1/2003"),
                new DynamicChart("1/1/2004", "Q1-2004", "1/1/2004"),
                new DynamicChart("2/1/2004", "Q1-2004", "2/1/2004"),
                new DynamicChart("3/1/2004", "Q1-2004", "3/1/2004"),
                new DynamicChart("4/1/2004", "Q2-2004", "4/1/2004"),
                new DynamicChart("5/1/2004", "Q2-2004", "5/1/2004"),
                new DynamicChart("6/1/2004", "Q2-2004", "6/1/2004"),
                new DynamicChart("7/1/2004", "Q3-2004", "7/1/2004"),
                new DynamicChart("8/1/2004", "Q3-2004", "8/1/2004"),
                new DynamicChart("9/1/2004", "Q3-2004", "9/1/2004"),
                new DynamicChart("10/1/2004", "Q4-2004", "10/1/2004"),
                new DynamicChart("11/1/2004", "Q4-2004", "11/1/2004"),
                new DynamicChart("12/1/2004", "Q4-2004", "12/1/2004"),
        };
    }

}  