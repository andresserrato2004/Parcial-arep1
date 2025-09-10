package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class HttpServer {
    
    private static final String BACKEND_HOST = "localhost";
    private static final int BACKEND_PORT = 5000;
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }

        System.out.println("Facade server started on port 36000");

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
        
        String inputLine;
        while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
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
        
        if (path.equals("/")) {
            return getClientHtml();
        } else if (path.startsWith("/add") || path.equals("/list") || path.equals("/clear") ) {
            return forwardToBackend(path);
        } else {
            return createErrorResponse(404, "not_found");
        }
    }
    
    private static String forwardToBackend(String path) {
        try {
            URL url = new URL("http://" + BACKEND_HOST + ":" + BACKEND_PORT + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream()));
            
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return createResponse(responseCode, response.toString());
            
        } catch (Exception e) {
            String json = "{\"status\":\"ERR\",\"error\":\"backend_unreachable\"}";
            return createResponse(502, json);
        }
    }
    
    private static String getClientHtml() {
        String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>Numbers API Client</title>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Numbers API Client</h1>\n" +
            "    \n" +
            "    <div>\n" +
            "        <h2>Add Number</h2>\n" +
            "        <input type=\"number\" id=\"numberInput\" value=\"3.5\" step=\"any\">\n" +
            "        <button onclick=\"addNumber()\">Add</button>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div>\n" +
            "        <h2>Operations</h2>\n" +
            "        <button onclick=\"listNumbers()\">List</button>\n" +
            "        <button onclick=\"clearNumbers()\">Clear</button>\n" +
            "        <button onclick=\"getStats()\">Stats</button>\n" +
            "    </div>\n" +
            "    \n" +
            "    <div>\n" +
            "        <h2>Response</h2>\n" +
            "        <pre id=\"response\"></pre>\n" +
            "    </div>\n" +
            "    \n" +
            "    <script>\n" +
            "        function showResponse(data) {\n" +
            "            document.getElementById('response').textContent = JSON.stringify(data, null, 2);\n" +
            "        }\n" +
            "        \n" +
            "        function addNumber() {\n" +
            "            const number = document.getElementById('numberInput').value;\n" +
            "            fetch('/add?x=' + number)\n" +
            "                .then(response => response.json())\n" +
            "                .then(data => showResponse(data))\n" +
            "                .catch(error => showResponse({status: 'ERR', error: error.message}));\n" +
            "        }\n" +
            "        \n" +
            "        function listNumbers() {\n" +
            "            fetch('/list')\n" +
            "                .then(response => response.json())\n" +
            "                .then(data => showResponse(data))\n" +
            "                .catch(error => showResponse({status: 'ERR', error: error.message}));\n" +
            "        }\n" +
            "        \n" +
            "        function clearNumbers() {\n" +
            "            fetch('/clear')\n" +
            "                .then(response => response.json())\n" +
            "                .then(data => showResponse(data))\n" +
            "                .catch(error => showResponse({status: 'ERR', error: error.message}));\n" +
            "        }\n" +
            "        \n" +
            "        function getStats() {\n" +
            "            fetch('/stats')\n" +
            "                .then(response => response.json())\n" +
            "                .then(data => showResponse(data))\n" +
            "                .catch(error => showResponse({status: 'ERR', error: error.message}));\n" +
            "        }\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>";
        
        return "HTTP/1.1 200 OK\r\n" +
               "Content-Type: text/html\r\n" +
               "\r\n" +
               html;
    }
    
    private static String createResponse(int statusCode, String content) {
        String statusText = statusCode == 200 ? "OK" : 
                           (statusCode == 400 ? "Bad Request" : 
                           (statusCode == 409 ? "Conflict" : 
                           (statusCode == 502 ? "Bad Gateway" : "Not Found")));
        
        return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
               "Content-Type: application/json\r\n" +
               "\r\n" +
               content;
    }
    
    private static String createErrorResponse(int statusCode, String error) {
        String json = "{\"status\":\"ERR\",\"error\":\"" + error + "\"}";
        return createResponse(statusCode, json);
    }
}