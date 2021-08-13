import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

public class ChatClient implements Runnable {

    // why is the ChatClient Multi-threaded?

    private Socket link;
    private PrintWriter outputStream;
    private Scanner inputStream;
    private int port = 8089;
    private String nick;

    public ChatClient() throws IOException {
        initialize();
    }

    private void initialize() throws IOException {
        // get server address
        Scanner keyboard = new Scanner(System.in);
//        System.out.println("What is the chat server's ip address?");
//        String str = keyboard.next();

        // get user nick
        System.out.println("What is your nick?");
        nick = keyboard.next();

        // connect to server
        InetAddress host = null;
        try {
//            host = InetAddress.getByName(str);
            host = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e1) {
            System.out.println("Host not found");
        }
        System.out
                .println("You are now connected to: " + host.getHostAddress());

        link = null;
        try {
            link = new Socket(host, port);
            link.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("not found");
        }
        inputStream = new Scanner(link.getInputStream());
        outputStream = new PrintWriter(link.getOutputStream());

        // start new thread to listen from server
        // one runnable, two threads... in which cases is this legal?
        Thread t = new Thread(this);
        t.start();

        // Tạo public key
        try {
            KeyPairGenerate.genKey();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(KeyPairGenerate.pubKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = factory.generatePublic(spec);

            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, pubKey);

            // continuously listen your user input
            while (keyboard.hasNextLine()) {
                String msg = keyboard.nextLine();

                byte encryptOut[] = c.doFinal(msg.getBytes());
                String strEncrypt = Base64.encode(encryptOut);
                System.out.println(nick + " says: " + msg);
                outputStream.println(nick + " says: " + msg);
                outputStream.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatClient();
    }

    @Override
    public void run() {
        while (true) {
            if (inputStream.hasNextLine())
                System.out.println(inputStream.nextLine());
        }
    }
}