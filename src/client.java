import java.io.*;
import java.net.*;

public class client /*implements Runnable*/ {
	
private static Socket pcon = null;
   public static void main(String[] args) {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      PrintStream out = System.out;
      
      try {
    	  //Try an upload server listening on the local port
         Socket c = new Socket("localhost",8888);
         printSocketInfo(c);
         System.out.println("You are the client");
         BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
         //r Reader-reads data from socket from server
         BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
         String m = null;
         
         while ((m=in.readLine())!= null) {
        	 if (m.equalsIgnoreCase("exit"))
             {
             	System.out.println("Breaking now");
             	w.write(m,0,m.length());
             	break;
             }
            //System.out.println("Printing Server contents");
        	//out.println(m);
        	 //System.out.println("now reading from console");
            //m = in.readLine();
        	 //System.out.println("Write to w buffer to socket");
            w.write(m,0,m.length());            
            w.newLine();
            w.flush();
         }
         while ((m=r.readLine())!= null) {
        	 if (m.equalsIgnoreCase("exit"))
             {
             	System.out.println("Breaking now");
             	break;
             }
        	 System.out.println("Reading Response from Server");
        	 out.println(m);
         }
         w.close();
         r.close();
         c.close();
         
       // Now check if a peer is trying to connect, if yes, use the get message to get filename and send the file
         //client pc = new client(c);
         //Thread pt = new Thread(pc);
         
       //Else if we want a RFC, use list from the server and connect to corresponding peer and upload port
         
      System.out.println("Done now");
      
      }
      
      catch (IOException e) {
         System.err.println(e.toString());
      }
   }
   
   public client(Socket c){
	      pcon = c;
	   }
   
   private static void printSocketInfo(Socket s) {
      System.out.println("Remote address = " +s.getInetAddress().toString());
      System.out.println("Remote port = " +s.getPort());
      System.out.println("Local socket address = " +s.getLocalSocketAddress().toString());
      System.out.println("Local address = " +s.getLocalAddress().toString());
      System.out.println("Local port = " +s.getLocalPort());
   }
}