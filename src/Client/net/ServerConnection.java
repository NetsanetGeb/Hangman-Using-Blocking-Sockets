
package Client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import Client.view.Client;


public class ServerConnection  implements Runnable{

    private final String host;
    private final int port;
    private final Client player;
    private boolean receivedWord;
    private final LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>();

    public ServerConnection(Client player,String host, int port){
         this.host= host;
         this.port= port;
         this.player= player;
     }
    
    @Override
    public void run() {
        try {
            Socket socket= new Socket("localhost",8080);
            receivedWord=false;
            int i=0;
             while (!receivedWord) {
                     i++;
                    PrintWriter printWriter= new PrintWriter(socket.getOutputStream());
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String selectedWord = bufferedReader.readLine();
                    player.connected();
                    player.updateGameGui(selectedWord);
                    byte[] toServer = strings.take().getBytes();
                String file = new String(toServer, "UTF-8");
                //System.out.println(file);
                printWriter.println(file);
                printWriter.flush();
            }
            socket.close();
        } catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
  
    
  public void storeGuessedWord(String text) {
        strings.add(text);
    }  
    
}
