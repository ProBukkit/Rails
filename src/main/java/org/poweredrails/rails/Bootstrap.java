package org.poweredrails.rails;


public class Bootstrap {

    private static final float JAVA_VERSION = Float.parseFloat(System.getProperty("java.class.version"));

    /*TODO: IF IMPLEMENTED, COMPILE IN JAVA 6*/
    public static void main(String[] args) {
        if (JAVA_VERSION < 52.0) {
            System.out.println("Rails requires Java 8 inorder to run properly. Download the latest version of Java @ http://www.oracle.com/technetwork/java/javase/downloads/index.html");
            System.out.println("Shutting down...");
            System.exit(0);
        } else {
            Main.main(args);
        }
    }
}
