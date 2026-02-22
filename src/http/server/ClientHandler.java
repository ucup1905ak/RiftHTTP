/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package http.server;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Files;

/**
 *
 * @author farel
 */
public class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final String threadNumber;
    private HttpRequest request;
    private HttpResponse response;
    private final Path root;
    
    public ClientHandler(Socket client){
        this.root = Path.of("www").toAbsolutePath().normalize();
        this.clientSocket = client;
        this.threadNumber = Thread.currentThread().getName();
    }
    public ClientHandler(Socket client, Path root){
        this.root = root;
        this.clientSocket = client;
        this.threadNumber = Thread.currentThread().getName();
    }
    @Override
    public void run(){
        System.out.println("Connected to client [" + threadNumber + "]: " + clientSocket.getRemoteSocketAddress());
        try{
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            request = new HttpRequest(in);
            response = new HttpResponse(out);
            
            SiteRouter router = new SiteRouter(root);
            router.handle(request, response);
            response.send();
        }   
        catch(Exception e){e.printStackTrace();}   
        finally{
            System.out.println("Exited Thread #" +threadNumber);
            try {clientSocket.close();} catch(Exception e){}
        }
    }
}

//
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
//            
//            System.out.println("Initialized socket input and output. Thread :" + threadNumber);
//WriteOut(out, "Initialized thread "+threadNumber+ "\n\rConnected to "  + clientSocket.getRemoteSocketAddress() + "\n\n");
//
//            String recieved;
//            while(true){
//                WriteOut(out, "\n\r> $ ");
//                if((recieved = in.readLine()) == null){break;}
//                recieved = recieved.toUpperCase();
//                
//                
//                System.out.println("MSG : " +  recieved);
//                String response = respond(recieved);
//                WriteOut(out, response);
//                
//                
//                if(recieved.toUpperCase().equals("EXIT")){break;}
//            }