import javax.swing.*;
import java.net.*; 
import java.io.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{ 
 protected static Socket clientSocket;
 public static PrintStream pStream = null;

 static ArrayList<Socket> arraySocket = new ArrayList<Socket>();
// static String code="";
 public static String responseFromVolunteer = "";
 
 public static String fromUI = "";
 public static String database = "";
 public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException {
//    new ServerY();
    ServerSocket serverSocket = null; 
    try { 
         serverSocket = new ServerSocket(888); 
         System.out.println ("Waiting for client(s)...");
         try { 
              while (true){                 
                  new Server (serverSocket.accept()); 
              }
         } 
         catch (IOException e){ 
             // System.exit(1); 
        } 
    } 
    catch (IOException e){ 
        // System.exit(1); 
        } 
    finally{
        try {
             serverSocket.close(); 
        }
        catch (IOException e){ 
            System.out.println(e);
        } 
    }
  }
 private Server(Socket client)
   {
    arraySocket.add(client);
    clientSocket = client;
    start();
   }


 public void run(){
     //heartbeat
    InetAddress ip = clientSocket.getInetAddress();
    String ipAdd = ip.getHostAddress();
    OutputStream ostream = null;
    System.out.println (ipAdd+ " connected...");
    try {
        String volCode = ServerY.updateTable("");
        ostream = clientSocket.getOutputStream();
        pStream = new PrintStream(ostream);
        pStream.println(volCode);
//        System.out.println(volCode);
     } catch (IOException ex) {
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
     } catch (SQLException ex) {
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
     }
  
    try { 
         BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 
         String inputLine = ""; 
         while ((inputLine = in.readLine()) != null){ 
              System.out.println (ipAdd+": " + inputLine); 
              responseFromVolunteer = inputLine;
              if(!inputLine.split(" ")[0].equals("lastHeartbeat:")){
//                inputLine = inputLine.replaceAll("'", "\"");
                String status = ServerY.updateTable(inputLine);
                System.out.println(status);
              }
              
         }
         in.close(); 
         clientSocket.close(); 
        } 
    catch (IOException e){ 
          System.out.println (ipAdd+ " disconnected...");
        } catch (SQLException ex) { 
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
     } 
    }
 
// public static void sendToVolunteer(String code){
//      OutputStream ostream = null;
//           try {
//              for(int i = 0; i < arraySocket.size(); i++){
//                 ostream = arraySocket.get(i).getOutputStream();
//                 pStream = new PrintStream(ostream);
//                 code = code.replace("\n"," ");
//                 pStream.println(code);
//                 System.out.println(arraySocket.get(i));                                                                 
//              }
//           } catch (IOException ex) {  }
// }
 
} 


