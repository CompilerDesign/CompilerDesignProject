import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Volunteer extends javax.swing.JFrame implements Runnable {
        static String code="";
        Timer timer = new Timer(); 
        Date lastHeartbeat= new Date();
        
       
  public static void main(String args[]) throws IOException {
       Volunteer c = new Volunteer();
       Thread client = new Thread(c);
       client.start(); 
  }
  
  public void run() {
            try {
                this.client();
            } catch (InterruptedException ex) {
                Logger.getLogger(Volunteer.class.getName()).log(Level.SEVERE, null, ex);
            }
  }      
         
  private void client() throws InterruptedException{
        try{
            Socket s = new Socket("127.0.0.1",888); //ip of server, port
            System.out.println("Connected to server...\n");            
            receiveFromServer(s);
        } 
        catch (ConnectException connExc) {  
        System.out.println("Error: Could not connect to the server.");
        } catch (IOException e){} 
  }
    
    private void receiveFromServer( Socket s ) throws IOException, InterruptedException{
         BufferedReader keyRead = new BufferedReader(new InputStreamReader( s.getInputStream()));
            String receiveInput;
            while((receiveInput = keyRead.readLine()) != null) {
                code +=receiveInput;
//                System.out.println(code);
                //run code in secondary app
//                insertHeartbeat();
                implementCode();
                Thread.sleep(10000);//delay bcoz we wait for file to complete
                runAsThreads(s);
                //call respondToServer whenever response to server is needed
                //catches the result from the secondary app                
                              
            }

    }
    
    private void insertHeartbeat(){
            code=code.trim().replaceAll(" +", " ");
            code = code.replace("public class Main{ public static String main(){ ", //replace with
                            "public class Main { " +
                            "static Date heartbeat= new Date(); " +
                            "public static synchronized void checkHeartbeat() {heartbeat=new Date();} " +
                            "public static synchronized String main (){");
    }
    
    private void implementCode() throws FileNotFoundException{
        System.out.println("code" +code);
        PrintWriter out = new PrintWriter("src/java/Main.java");
        out.println(code);
        out.close();
    }
    
    private void runAsThreads(Socket s){
                    new Thread(new Runnable() { //heartbeat thread
                    public void run() {
                        timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
//                            Main.checkHeartbeat();
//                            System.out.println(lastHeartbeat+" "+lastHeartbeat.compareTo(Main.heartbeat)+" "+Main.heartbeat.toString());
//                            lastHeartbeat = Main.heartbeat;
                            try {
                                respondToServer(s,"lastHeartbeat: "+lastHeartbeat);
                            } catch (IOException ex) {
                                Logger.getLogger(Volunteer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                         }
                        }, 0, 5*1000);// 5 seconds   
                    }
                }).start();
                
                new Thread(new Runnable() { //main method thread
                    public void run() {
                        try {
                            respondToServer(s, Main.start().toString());
                        } catch (IOException ex) {
                            Logger.getLogger(Volunteer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
}
    
    private void respondToServer( Socket s, String response ) throws IOException {
         OutputStream ostream = s.getOutputStream(); 
         PrintStream pStream = new PrintStream(ostream);
         pStream.println(response);
    }
    

}
