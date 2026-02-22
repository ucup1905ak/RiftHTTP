
package http.server;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class HTTPServer{
    private static final int MAX_CLIENT = 3;
    private static final int PORT = 80;
    private static final ThreadPoolExecutor pool =  new ThreadPoolExecutor(
            MAX_CLIENT,
            MAX_CLIENT,
            0L,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<>()
    );
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Running on port " + server.getLocalPort());
        while(true){
            Socket ClientSocket = server.accept();
            try{
                pool.execute(new ClientHandler(ClientSocket));
            }catch(RejectedExecutionException e){
                System.out.println("Server is full. Rejecting Client");
                ClientSocket.close();
            }
        }
    }

}
