
package Server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Server.controller.SlowConnectionHandler;


public class Server {
    
    public static void main(String [] args) throws IOException{
        int port= 8080;
        int poolsize=5;
        ServerSocket serversocket=null;
        try{
           serversocket= new ServerSocket(port);
           System.out.println(" Server Running");
        }catch(IOException ex){
            System.out.println(ex);   
        }
       ExecutorService exe= Executors.newFixedThreadPool(poolsize);
        
        while(true){  
            Socket socket= serversocket.accept();
            System.out.println(" Server accepting");
             exe.execute(new SlowConnectionHandler(socket));  
           
        } 
    }
    
}
