import javax.swing.*;
import java.net.*; 
import java.io.*; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread{ 
 protected static Socket clientSocket;
 public static PrintStream pStream = null;

 static ArrayList<Socket> arraySocket = new ArrayList<Socket>();
// static String code="";
 public static String responseFromVolunteer = "";
 
 public static String fromUI = "";
 
  
// public static void fromUI(){
//  JFrame frame= new JFrame("Send to client");
//  JPanel panel = new JPanel();
//  JButton send = new JButton("SEND");  
//  JTextArea contents= new JTextArea(20, 50);
//  JScrollPane scrollingArea = new JScrollPane(contents);
//  String file = "Java Code.txt";
//   try {
//            FileReader fileReader = new FileReader(file);
//            contents.read(fileReader, file);              		
//   } catch (IOException ex) {  
//            System.out.println("File not found.");
//    }
//        
//     send.addActionListener(new ActionListener(){     
//	      public void actionPerformed(ActionEvent actionEvent) {
//                  code = contents.getText(); 
//                  sendToVolunteer();
////                  contents.setText("");               
//	      }
//     });
//       
//     panel.add(scrollingArea);
//     panel.add(send);
//     
//     frame.add(panel);
//     frame.pack();
//     frame.setVisible(true);
// }
 
 
 public Server() throws IOException 
   {
//    fromUI();
    ServerSocket serverSocket = null; 
    try { 
         serverSocket = new ServerSocket(888); 
         System.out.println ("Waiting for client(s)...");
         while (true){                 
            connect(serverSocket.accept()); 
         }
    } 
    finally{
        serverSocket.close();        
    }
   }

 public static void connect (Socket client){
    arraySocket.add(client);
    clientSocket = client;
    threadStart();
 }

 public static void threadStart(){
     //heartbeat
    InetAddress ip = clientSocket.getInetAddress();
    String ipAdd = ip.getHostAddress();
    OutputStream ostream = null;
    System.out.println (ipAdd+ " connected...");
    try {
         ostream = clientSocket.getOutputStream();
         pStream = new PrintStream(ostream);
        fromUI = fromUI.replace("\n"," ");
        pStream.println(fromUI);
     } catch (IOException ex) {
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
     }
  
    try { 
         BufferedReader in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream())); 
         String inputLine = ""; 
         while ((inputLine = in.readLine()) != null){ 
              System.out.println (ipAdd+": " + inputLine); 
              responseFromVolunteer = inputLine;
         }
         in.close(); 
         clientSocket.close(); 
        } 
    catch (IOException e){ 
          System.out.println (ipAdd+ " disconnected...");
        } 
    }
 
 public static void sendToVolunteer(String code){
      OutputStream ostream = null;
           try {
              for(int i = 0; i < arraySocket.size(); i++){
                 ostream = arraySocket.get(i).getOutputStream();
                 pStream = new PrintStream(ostream);
                 code = code.replace("\n"," ");
                 pStream.println(code);
                 System.out.println(arraySocket.get(i));                                                                 
              }
           } catch (IOException ex) {  }
 }
 
} 


