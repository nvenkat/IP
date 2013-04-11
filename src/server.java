import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class server implements Runnable,rfc_list, peer_list{
   private static Socket con = null;
   public static int clientnum = 0;
   public static boolean flag = false;
   public static PeerList pl = new PeerList();
   public static RFCList rl = new RFCList();
   public String split, rfcnum = null ,hostname = null,portnum = null ,title = null,splitr = null,uploadport = null;
   public int n = 0, i = 0;
   
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
            //pl.insert(c.getInetAddress().toString(), c.getPort());
            //pl.printList();
            /*if(flag)
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
            }*/
            t.start();
         }
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }
   
   public server(Socket c){
      con = c;
   }
   
   public void addMsgHandler(String msg, final BufferedWriter w, final BufferedReader r)
   {
	   try
	   {
		   System.out.println("In the ADD method");
		   //msg = r.readLine();
		   System.out.println("reading now");
		   //System.out.println(msg);
		   while ((msg = r.readLine())!= null && msg.length()!= 0) {
		   
		   System.out.println("Receiving data from socket");
		   if (msg.startsWith("exit"))
           {
           	System.out.println("Breaking now");
           	break;
           }
		   StringTokenizer st = new StringTokenizer(msg, "=' '");
	   /*while(st.hasMoreTokens()) {
   			split = st.nextToken();
   			i++;
   			if(i == 3)
   				rfcnum = split;
	   }*/
	   if (msg.startsWith("Host: "))
	   {
	   		hostname = msg.substring(6);
	   		System.out.println("Hostname = " +hostname);
	   }
	   if(msg.startsWith("Port: "))
	   {
		   	portnum = msg.substring(6);
		   	System.out.println("Portnumber = " +portnum);
	   }
	   if(msg.startsWith("Title: "))
	   {
		   	title = msg.substring(7);
		   	System.out.println("Title = " +title);
	   }
	   }
		w.write("Received ADD Message");
		w.append("P2P-CI/1.0 200 OK");
		w.append("RFC xxxxx "  + title + hostname + portnum);
		rl.insert(title, rfcnum, hostname);
		w.newLine();
		w.flush();
		}catch (IOException e) {
		         System.err.println(e.toString());
		}
   }
   
   public void lookupMsgHandler(String m, BufferedWriter w, BufferedReader r)
   {
	   try
	   {
	   StringTokenizer st = new StringTokenizer(m, "=' '");
	   while ((m = r.readLine())!= null && m.length()!= 0) {
		   if (m.equalsIgnoreCase("exit"))
           {
           	System.out.println("Breaking now");
           	break;
           }
	   while(st.hasMoreTokens()) {
   			split = st.nextToken();
   			i++;
   			if(i == 3)
   				rfcnum = split;
	   }
	   if (m.startsWith("Host: "))
	   {
	   		hostname = m.substring(6);            	
	   }
	   if(m.startsWith("Port: "))
	   {
		   	portnum = m.substring(6);
	   }
	   if(m.startsWith("Title: "))
	   {
		   	title = m.substring(7);
	   }
	   }
		w.write("Received LOOKUP Message");
		w.append("P2P-CI/1.0 200 OK");
		//w.append("RFC " + rfcnum + title + hostname + portnum);
		String rfclist = rl.lookUp(rfcnum);
		StringTokenizer rfcl = new StringTokenizer(rfclist, "=' '"); 
		   while(rfcl.hasMoreTokens()) {
	   			splitr = rfcl.nextToken();
	   			uploadport = pl.lookUp(splitr);
	   			w.append("RFC " + rfcnum + " " + title + " " + hostname + " " + uploadport);
		   }		
		w.newLine();
		w.flush();
		}catch (IOException e) {
		         System.err.println(e.toString());
		}
   }
   
   public void listMsgHandler(String m, BufferedWriter w, BufferedReader r)
   {
	   try
	   {
	   StringTokenizer st = new StringTokenizer(m, "=' '"); 
	   while ((m = r.readLine())!= null && m.length()!= 0) {
		   if (m.equalsIgnoreCase("exit"))
           {
           	System.out.println("Breaking now");
           	break;
           }
	   while(st.hasMoreTokens()) {
   			split = st.nextToken();
   			i++;
   			if(i == 3)
   				rfcnum = split;
	   }
	   if (m.startsWith("Host: "))
	   {
	   		hostname = m.substring(6);            	
	   }
	   if(m.startsWith("Port: "))
	   {
		   	portnum = m.substring(6);
	   }
	   }
		w.write("Received LIST Message");
		w.append("P2P-CI/1.0 200 OK");
		rl.printList();
		w.newLine();
		w.flush();
		}catch (IOException e) {
		         System.err.println(e.toString());
		}
   }
   
   public void run() { 
      try {    	 
    	 BufferedWriter w = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
    	 //PrintWriter pw = new PrintWriter(con.getOutputStream());
    	 BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));    	   
         String m = "You are now connected to the P2P Server.";
         //pw.write(m);
         w.write(m,0,m.length());
         w.newLine();
         w.flush();
         while ((m = r.readLine())!= null && m.length()!= 0) {
            if (m.equalsIgnoreCase("exit"))
            {
            	System.out.println("Breaking now");
            	break;
            }
            if (m.startsWith("ADD") && m.endsWith("P2P-CI/1.0"))
            {
            	System.out.println("Before Entering ADD Method");
            	addMsgHandler(m,w,r);
            }
            if (m.startsWith("LOOKUP") && m.endsWith("P2P-CI/1.0"))
            {
            	addMsgHandler(m,w,r);
            }
            if (m.startsWith("LIST") && m.endsWith("P2P-CI/1.0"))
            {
            	addMsgHandler(m,w,r);
            }
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