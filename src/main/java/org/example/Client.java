package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",6060);

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(outputStream , true);

        Scanner scanner = new Scanner(System.in);
        writer.println(scanner.nextLine());
        System.out.println("Connected to the chat server");

        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        System.out.println(reader.readLine());
    }
}
