import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class server implements Runnable, rfc_list, peer_list{
   private static Socket con = null;
   public static int clientnum = 0;
   public static PeerList pl = new PeerList();
   public static RFCList rl = new RFCList();
   public String split, rfcnum = null ,hostname = null,portnum = null ,title = null,splitr = null,uploadport = null;
   public int n = 0, i = 0;
   public static BufferedReader clientReader = null;
   public static PrintWriter clientWriter = null;
//   public static BufferedWriter clientWriter = null;
   public static String clientMsg = null;
   public static String msg = null;
   
   public static void main(String[] args) {
      try {
         ServerSocket s = new ServerSocket(7734);
         printServerSocketInfo(s);
         while (true) {
            Socket c = s.accept();
            printSocketInfo(c);
            server v = new server(c); 
            Thread t = new Thread(v);
            System.out.println("You are connecting to client # " + clientnum++);
            //clientWriter = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            //out = new PrintWriter(echoSocket.getOutputStream(), true);
            clientWriter = new PrintWriter(con.getOutputStream(), false);
            clientReader = new BufferedReader(new InputStreamReader(con.getInputStream()));  
            t.start();
         }
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }
   
   public server(Socket c){
      con = c;
   }
   
   public void addMsgHandler()
   {
	   try
	   {
		   System.out.println("In the ADD method");
		   while ((msg = clientReader.readLine())!= null && msg.length()!= 0) {
		   if (msg.startsWith("exit"))
           {
           		System.out.println("Breaking now from server");
           		break;
           }
		   System.out.println("The line received is:");
		   System.out.println(msg);
		   StringTokenizer st = new StringTokenizer(msg, "=' '");
		   System.out.println("Begin splitting of tokens");
		   while(st.hasMoreTokens()) {
   				split = st.nextToken();
   				i++;
   				if(i == 3)
   					rfcnum = split;
		   }
		   if (msg.startsWith("Host: "))
		   		hostname = msg.substring(6);
		   System.out.println("Hostname: " +hostname);
		   if(msg.startsWith("Port: "))
			   	portnum = msg.substring(6);
		   System.out.println("Port: " + portnum);
		   if(msg.startsWith("Title: "))
			   	title = msg.substring(7);
		   System.out.println("Title: " + title);
	   }
		
		 //clientWriter.write("Received and Processed ADD Message");
		   	clientWriter.print(clientMsg);
		   	clientWriter.append("P2P-CI/1.0 200 OK\n");
			//clientMsg = clientMsg.concat("P2P-CI/1.0 200 OK\n");
		   	clientWriter.append("RFC " + rfcnum + title + hostname + portnum);
			//clientMsg = clientMsg.concat("RFC " + rfcnum + title + hostname + portnum);
		   	clientWriter.print(clientMsg);
			
			//clientWriter.write(clientMsg, 0, clientMsg.length());
			//clientWriter.write("\n");
			//clientWriter.newLine();
			clientWriter.flush();   
		   
		   
		rl.insert(title, rfcnum, hostname);
		rl.printList();		
		}catch (IOException e) {
		         System.err.println(e.toString());
		}
   }
   
   public void lookupMsgHandler(String m)
   {
	   try
	   {
	   StringTokenizer st = new StringTokenizer(m, "=' '");
	   while ((m = clientReader.readLine())!= null && m.length()!= 0) {
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
		   		hostname = m.substring(6);
		   if(m.startsWith("Port: "))		  
			   	portnum = m.substring(6);
		   if(m.startsWith("Title: "))
		      	title = m.substring(7);
		  
	   }
		clientWriter.write("Received and Processed LOOKUP Message");
		clientMsg = clientMsg.concat("P2P-CI/1.0 200 OK\n");
		//clientMsg = clientMsg.concat("RFC " + rfcnum + title + hostname + portnum);
		String rfclist = rl.lookUp(rfcnum);
		StringTokenizer rfcl = new StringTokenizer(rfclist, "=' '"); 
		   while(rfcl.hasMoreTokens()) {
	   			splitr = rfcl.nextToken();
	   			uploadport = pl.lookUp(splitr);
	   			clientMsg = clientMsg.concat("RFC " + rfcnum + " " + title + " " + hostname + " " + uploadport);
	   			//clientWriter.append("RFC " + rfcnum + " " + title + " " + hostname + " " + uploadport);
		   }	
		clientWriter.write(clientMsg,0,clientMsg.length());
		clientWriter.write("\n");
		//clientWriter.newLine();
		clientWriter.flush();
		}catch (IOException e) {
		         System.err.println(e.toString());
		}
   }
   
   public void listMsgHandler(String m)
   {
	   try
	   {
	   StringTokenizer st = new StringTokenizer(m, "=' '"); 
	   while ((m = clientReader.readLine())!= null && m.length()!= 0) {
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
		  		hostname = m.substring(6);            	
		   if(m.startsWith("Port: "))
			   	portnum = m.substring(6);

	   }
	   	System.out.println("Received LIST Message");
		clientMsg = clientMsg.concat("P2P-CI/1.0 200 OK\n");
		clientMsg = clientMsg.concat("RFC " + rfcnum + title + hostname + portnum);
		//clientWriter.append(rl.printList());
		clientWriter.write(clientMsg,0,clientMsg.length());
		clientWriter.write("\n");
		//clientWriter.newLine();
		clientWriter.flush();
		}catch (IOException e) {
		         System.err.println(e.toString());
		}
   }
   
   public void run() { 
      try { 
    	 clientWriter.write("\n");
         //clientWriter.newLine();
         clientWriter.flush();
         while ((clientMsg = clientReader.readLine())!= null && clientMsg.length()!= 0) {
            if (clientMsg.equalsIgnoreCase("exit"))
            {
            	System.out.println("Breaking now");
            	break;
            }
            Pattern digitPattern = Pattern.compile("\\d{5}");
            System.out.println("Before receiving upload port");
            if(clientMsg.matches(digitPattern.toString()))
            {
            	System.out.println("Before Client Writer");
            	clientWriter.write("Received upload port of the client");
            	uploadport = clientMsg;
            	pl.insert(con.getInetAddress().getCanonicalHostName(), uploadport);
                pl.printList();
            }
            if (clientMsg.startsWith("ADD"))
            {
            	System.out.println("Before Entering ADD Method");
            	
            	addMsgHandler();
            	clientMsg = "";
            }
            if (clientMsg.startsWith("LOOKUP"))
            {
            	lookupMsgHandler(clientMsg);
            	clientMsg = "";
            }
            if (clientMsg.startsWith("LIST"))
            {
            	listMsgHandler(clientMsg);
            	clientMsg = "";
            }
        }     
         //clientWriter.close();
         //clientReader.close();
         //con.close(); 
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
      System.out.println("Server socket address = "+s.getInetAddress().toString());
      System.out.println("Server socket port = "+s.getLocalPort());
   } 
}