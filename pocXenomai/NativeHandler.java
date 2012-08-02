class NativeHandler {

 public native void start();
 public native void stop();
 public native void update();

 static { System.load("testjni"); }

public static void main(String[] args){

NativeHandler t  = new NativeHandler();

t.start();

}




}
