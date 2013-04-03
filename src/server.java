import java.io.*;
import java.net.*;


public class server implements Runnable,rfc_list, peer_list{
   private Socket con = null;
   public static int clientnum = 0;
   public static boolean flag = false;
   
   public static void main(String[] args) {
      try {
         ServerSocket s = new ServerSocket(8888);
         printServerSocketInfo(s);
         while (true) {
            Socket c = s.accept();
            printSocketInfo(c);
            server v = new server(c);
            Thread t = new Thread(v);
            System.out.println("You are connecting to client # " + clientnum++);
            PeerList pl = new PeerList();
            RFCList rl = new RFCList();
            pl.insert(c.getInetAddress().toString(), c.getPort());
            pl.printList();
            if(flag)
            {
            	rl.insert("Title 1", 1234, c.getInetAddress().toString());
            	rl.insert("Title 2", 1234, c.getInetAddress().toString());
            	rl.printList();
            	flag = false;
            }
            else
            {
            	rl.insert("Title 3", 4321, c.getInetAddress().toString());
            	rl.printList();
            	flag = true;
            }
            t.start();
         }
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }
   public server(Socket c){
      con = c;
   }
   
   public void run() { 
      try {
         BufferedWriter w = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
         BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
         String m = "Welcome to Server.Please type in some words.";
         w.write(m,0,m.length());
         w.newLine();
         w.flush();
         while ((m=r.readLine())!= null) {
            if (m.equals(".")) break;
            char[] a = m.toCharArray();
            int n = a.length;
            for (int i=0; i<n/2; i++) {
               char t = a[i];
               a[i] = a[n-1-i];
               a[n-i-1] = t;
            }
            w.write(a,0,n);
            w.newLine();
            w.flush();
         }
         w.close();
         r.close();
         con.close();
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }
   
   private static void printSocketInfo(Socket s) {
      System.out.println("Remote address = "+s.getInetAddress().toString());
      System.out.println("Remote port = "+s.getPort());
      System.out.println("Local socket address = "+s.getLocalSocketAddress().toString());
      System.out.println("Local address = "+s.getLocalAddress().toString());
      System.out.println("Local port = "+s.getLocalPort());
   }
   
   private static void printServerSocketInfo(ServerSocket s) {
      System.out.println("Server socker address = "+s.getInetAddress().toString());
      System.out.println("Server socker port = "+s.getLocalPort());
   } 
}