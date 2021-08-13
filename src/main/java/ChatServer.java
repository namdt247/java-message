import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;

public class ChatServer {

    // Toy ChatServer to illustrate multi-threading

    private final int port = 8089;
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientList;

    public ChatServer() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
        } catch (IOException e)
        {
            System.out.println(e.getStackTrace());
        }
        clientList = new ArrayList<Socket>();
    }

    public void startServer() throws IOException {
        System.out.println("Accepting clients...");
        KeyPairGenerate.genKey();

        while(true)
        {
            // wait for a client
            Socket client = serverSocket.accept();
            clientList.add(client);
            System.out.println("New client accepted..." + client.getRemoteSocketAddress());
            System.out.println("Total users: " + clientList.size());
            ChatClientHandler handler = new ChatClientHandler(client,this);
            Thread t = new Thread(handler);
            t.start();
        }
    }

    public synchronized void sendChatMessageToAll(String msg) throws IOException {
        // Tạo private key
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(KeyPairGenerate.priKey);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = factory.generatePrivate(spec);

            for(Iterator<Socket> it=clientList.iterator(); it.hasNext();)
            {
                Socket client = it.next();
                if( !client.isClosed() )
                {
                    PrintWriter pw = new PrintWriter(client.getOutputStream());

                    // Giải mã dữ liệu
                    Cipher c = Cipher.getInstance("RSA");
                    c.init(Cipher.DECRYPT_MODE, priKey);
                    byte decryptOut[] = c.doFinal(Base64.decode(msg));
                    System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
                    String str = new String(decryptOut);

                    pw.println(str);
                    pw.flush();
                    //System.out.println("Sent to: " + client.getRemoteSocketAddress());
                }
            }
        } catch (Exception ex) {

        }

    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        new ChatServer().startServer();
    }

}