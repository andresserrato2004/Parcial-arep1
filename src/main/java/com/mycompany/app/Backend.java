package com.mycompany.app;

import java.io.IOException;
import java.net.ServerSocket;

public class Backend {
 
    public static void main(String[] args) {
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5000.");
            System.exit(1);
        }

        public String add (){

        }
        
                
    }

}
