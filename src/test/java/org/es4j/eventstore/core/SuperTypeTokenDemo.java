package org.es4j.eventstore.core;

import java.util.ArrayList;


public class SuperTypeTokenDemo {

    public static void main(String[] args) {

        // Note that an anonymous sublcass instance of TypeReference is being
        // created. Because TypeReference itself is an abstract class, I should
        // get the instance of the subclass.
        TypeReference tr1 = new TypeReference<ArrayList<String>>() {};
        TypeReference tr2 = new TypeReference<String>() {};
    }
}