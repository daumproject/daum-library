package org.daum.library.replicav2;

/**
 * Created with IntelliJ IDEA.
 * User: jed
 * Date: 27/03/13
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class Node
{
  public String name;

    public Node(String nodename){
        this.name= nodename;
    }


    public Node clone(){

        return  new Node(name);
    }
}
