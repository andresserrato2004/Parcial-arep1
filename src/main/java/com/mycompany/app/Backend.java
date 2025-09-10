package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Backend {
    
    LinkedList<String> numbers = new LinkedList<String>();
    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5000.");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                System.out.println("http://localhost:5000");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

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
