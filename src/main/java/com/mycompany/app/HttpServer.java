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
                    +"<!DOCTYPE html>"
                    +"<html>"
                    +"<head>"
                    +"    <title>Form Example</title>"
                    +"    <meta charset=\"UTF-8\">"
                    +"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    +"</head>"
                    +"<body>"
                    +"    <h1>Form with GET</h1>"
                    +"    <form action=\"/add\">"
                    +"        <label for=\"name\">add</label><br>"
                    +"        <input type=\"text\" id=\"number\" name=\"number\" value=\"3.5\"><br><br>"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getadd()\">"
                    +"    </form>"
                    +"    <div id=\"getrespmsg\"></div>"
                    +"    <form action=\"/list\">"
                    +"        <label for=\"name\">list</label><br>"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getlist()\">"
                    +"    </form>"
                    +"    <div id=\"getrespmsg\"></div>"
                    +"    <form action=\"/clear\">"
                    +"        <label for=\"name\">clear</label><br>"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getclear()\">"
                    +"    </form>"
                    +"    <div id=\"getrespmsg\"></div>"
                    +"    <form action=\"/stats\">"
                    +"        <label for=\"name\">stats:</label><br>"
                    +"        <input type=\"button\" value=\"Submit\" onclick=\"getstats()\">"
                    +"    </form>"
                    +"    <div id=\"getrespmsg\"></div>"
                    +"    <script>"
                    +"        function getlist() {"
                    +"            let nameVar = document.getElementById(\"number\").value;"
                    +"            const xhttp = new XMLHttpRequest();"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);"
                    +"            xhttp.send();"
                    +"        }"
                    +"        function getadd() {"
                    +"            let nameVar = document.getElementById(\"number\").value;"
                    +"            const xhttp = new XMLHttpRequest();"
                    +"            xhttp.onload = function () {"
                    +"                document.getElementById(\"getrespmsg\").innerHTML ="
                    +"                    this.responseText;"
                    +"            }"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);"
                    +"            xhttp.send();"
                    +"        }"
                    +"        function getclear() {"
                    +"            let nameVar = document.getElementById(\"number\").value;"
                    +"            const xhttp = new XMLHttpRequest();"
                    +"            xhttp.onload = function () {"
                    +"                document.getElementById(\"getrespmsg\").innerHTML ="
                    +"                    this.responseText;"
                    +"            }"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);"
                    +"            xhttp.send();"
                    +"        }"
                    +"        function getstats() {"
                    +"            let nameVar = document.getElementById(\"number\").value;"
                    +"            const xhttp = new XMLHttpRequest();"
                    +"            xhttp.onload = function () {"
                    +"                document.getElementById(\"getrespmsg\").innerHTML ="
                    +"                    this.responseText;"
                    +"            }"
                    +"            xhttp.open(\"GET\", \"/hello?name=\" + nameVar);"
                    +"            xhttp.send();"
                    +"        }"
                    +"    </script>"
                    +"</body>"
                    +"</html>";
            
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
    }
}