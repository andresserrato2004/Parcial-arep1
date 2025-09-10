package com.mycompany.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws IOException {

        int PORT_backend = 5000;  

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                System.out.println("http://localhost:36000");
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

            

            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    +"<!DOCTYPE html>\n"
                    +"<html>\n"
                    +"<head>\n"
                    +"    <title>Form Example</title>\n"
                    +"    <meta charset=\"UTF-8\">\n"
                    +"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                    +"</head>\n"
                    +"<body>\n"
                    +"    <h1>Form with GET</h1>\n"
                    +"    <form action=\"/add\">\n"
                    +"        <label for=\"name\">add</label><br>\n"
                    +"        <input type=\"text\" id=\"number\" name=\"number\" value=\"3.5\"><br><br>\n"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getadd()\">\n"
                    +"    </form>\n"
                    +"    <div id=\"getrespmsg\"></div>\n"
                    +"    <form action=\"/list\">\n"
                    +"        <label for=\"name\">list</label><br>\n"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getlist()\">\n"
                    +"    </form>\n"
                    +"    <div id=\"getrespmsg\"></div>\n"
                    +"    <form action=\"/clear\">\n"
                    +"        <label for=\"name\">clear</label><br>\n"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getclear()\">\n"
                    +"    </form>\n"
                    +"    <div id=\"getrespmsg\"></div>\n"
                    +"    <form action=\"/stats\">\n"
                    +"        <label for=\"name\">stats:</label><br>\n"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getstats()\">\n"
                    +"    </form>\n"
                    +"    <div id=\"getrespmsg\"></div>\n"
                    +"    <script>\n"
                    +"        function getlist() {\n"
                    +"            let nameVar = document.getElementById(\"number\").value;\n"
                    +"            const xhttp = new XMLHttpRequest();\n"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);\n"
                    +"            xhttp.send();\n"
                    +"        }\n"
                    +"        function getadd() {\n"
                    +"            let nameVar = document.getElementById(\"number\").value;\n"
                    +"            const xhttp = new XMLHttpRequest();\n"
                    +"            xhttp.onload = function () {\n"
                    +"                document.getElementById(\"getrespmsg\").innerHTML =\n"
                    +"                    this.responseText;\n"
                    +"            }\n"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);\n"
                    +"            xhttp.send();\n"
                    +"        }\n"
                    +"        function getclear() {\n"
                    +"            let nameVar = document.getElementById(\"number\").value;\n"
                    +"            const xhttp = new XMLHttpRequest();\n"
                    +"            xhttp.onload = function () {\n"
                    +"                document.getElementById(\"getrespmsg\").innerHTML =\n"
                    +"                    this.responseText;\n"
                    +"            }\n"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);\n"
                    +"            xhttp.send();\n"
                    +"        }\n"
                    +"        function getstats() {\n"
                    +"            let nameVar = document.getElementById(\"number\").value;\n"
                    +"            const xhttp = new XMLHttpRequest();\n"
                    +"            xhttp.onload = function () {\n"
                    +"                document.getElementById(\"getrespmsg\").innerHTML =\n"
                    +"                    this.responseText;\n"
                    +"            }\n"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);\n"
                    +"            xhttp.send();\n"
                    +"        }\n"
                    +"    </script>\n"
                    +"</body>\n"
                    +"</html>\n";
            
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
    }
}