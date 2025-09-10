package com.mycompany.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class Backend {
    
    LinkedList<String> numbers = new LinkedList<String>();
    public static void main(String[] args) {
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5000.");
            System.exit(1);
        }
    }

    public String added(String number){
        numbers.add(number);        
        String json = "{{\r\n" + 
                        "  \"status\": \"OK\",\r\n" + 
                        "  \"added\": \"number\",\r\n" + 
                        "}}";
        return json;
    }


    public LinkedList list(){
        return numbers;
    }


    public LinkedList clear(){
        numbers.clear();
        return numbers;
    }

    
}
