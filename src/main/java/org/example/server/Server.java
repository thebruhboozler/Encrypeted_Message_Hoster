package org.example.server;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;


public class Server {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());



        try (ServerSocket serverSocket = new ServerSocket(6060)){
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                Thread userThread = new Thread(()->{
                    OutputStream output = null;
                    try {
                        output = socket.getOutputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    PrintWriter writer = new PrintWriter(output, true);
                    try {
                        InputStream input = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));





                        String command = reader.readLine();
                        String[] splited = command.split(" ");
                        System.out.println(splited[0]);
                        System.out.println(splited[1]);
                        System.out.println(splited[2]);
                        if(splited[0] == "e"){
                            String result = encrypt(splited[2],splited[1]);
                            System.out.println(result);
                            writer.println(result);
                        }else{
                            String result = decrypt(splited[2],splited[1]);
                            System.out.println(result);
                            writer.println(result);
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        writer.println(e.getMessage());
                    }
                });
                userThread.start();

            }
        }
    }

    public static String encrypt(String string, String keyString) throws Exception {

        byte[] key = keyString.substring(0,Math.min(16,keyString.length() )).getBytes();


        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = cipher.doFinal(string.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    public static String decrypt(String string, String keyString) throws Exception{
        byte[] key = keyString.substring(0,Math.min(16,keyString.length() )).getBytes();


        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = Base64.getDecoder().decode(string);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
