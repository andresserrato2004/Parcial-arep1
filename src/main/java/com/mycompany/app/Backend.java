package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Backend {
    
    private static List<Double> numbers = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 5000.");
            System.exit(1);
        }

        System.out.println("Backend server started on port 5000");

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            handleRequest(clientSocket);
        }
    }
    
    private static void handleRequest(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String requestLine = in.readLine();
        
        // Read headers
        String inputLine;
        while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
            // Skip headers
        }

        if (requestLine != null) {
            System.out.println("Request: " + requestLine);
            String response = processRequest(requestLine);
            out.println(response);
        }

        out.close();
        in.close();
        clientSocket.close();
    }
    
    private static String processRequest(String requestLine) {
        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            return createErrorResponse(400, "invalid_request");
        }
        
        String path = parts[1];
        
        if (path.startsWith("/add")) {
            return handleAdd(path);
        } else if (path.equals("/list")) {
            return handleList();
        } else if (path.equals("/clear")) {
            return handleClear();
        } else {
            return createErrorResponse(404, "not_found");
        }
    }
    
    private static String handleAdd(String path) {
        try {
            String[] parts = path.split("\\?");
            if (parts.length < 2) {
                return createErrorResponse(400, "invalid_number");
            }
            
            String[] params = parts[1].split("=");
            if (params.length < 2 || !params[0].equals("x")) {
                return createErrorResponse(400, "invalid_number");
            }
            
            double number = Double.parseDouble(params[1]);
            numbers.add(number);
            
            String json = "{\"status\":\"OK\",\"added\":" + number + ",\"count\":" + numbers.size() + "}";
            return createResponse(200, json);
            
        } catch (NumberFormatException e) {
            return createErrorResponse(400, "invalid_number");
        }
    }
    
    private static String handleList() {
        StringBuilder values = new StringBuilder("[");
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) values.append(",");
            values.append(numbers.get(i));
        }
        values.append("]");
        
        String json = "{\"status\":\"OK\",\"values\":" + values.toString() + "}";
        return createResponse(200, json);
    }
    
    private static String handleClear() {
        numbers.clear();
        String json = "{\"status\":\"OK\",\"message\":\"list_cleared\"}";
        return createResponse(200, json);
    }
    
    
    private static String createResponse(int statusCode, String json) {
        String statusText = statusCode == 200 ? "OK" : (statusCode == 400 ? "Bad Request" : (statusCode == 409 ? "Conflict" : "Not Found"));
        return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
               "Content-Type: application/json\r\n" +
               "\r\n" +
               json;
    }
    
    private static String createErrorResponse(int statusCode, String error) {
        String json = "{\"status\":\"ERR\",\"error\":\"" + error + "\"}";
        return createResponse(statusCode, json);
    }
}
