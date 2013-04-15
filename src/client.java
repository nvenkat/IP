import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.net.*;
import java.util.*;

/*
 * Listen on upload port in parallel in an infinite loop + accept connections + Have a separate thread for every new connection you create with a peer
 * Send upload port number to server after connection establishment
 * push this value into list in server
 * Then adding your RFC's to list ---- DONE
 * List all RFC's ---- Implement response on the Server
 * Look up specific RFC ---- Should return the Upload Port to connect to specific Peer + Other details
 * Then connect to specific peer on that Upload Port and download the RFC
 * when peer tries to connect to you on your upload port, check the RFC it asks for and then send the file across the socket
 */

public class client implements Runnable {
	
	public static String serverMsg = null;
	public static String peerMsg = null;
	public static String serverRecvMsg;
	public static BufferedWriter serverWriter = null;
	public static BufferedReader serverReader = null;
	public static BufferedWriter peerWriter = null;
	public static BufferedReader peerReader = null;
	public static ServerSocket peerServerSocket = null;
	public static Socket serverSocket = null;
	public static String rfcnum = null, title = null;
	public static Socket peer = null;
	
   public static void main(String[] args) {
      BufferedReader consoleInputReader = new BufferedReader(new InputStreamReader(System.in));
      PrintStream out = System.out;
           
      try {
    	   	 serverSocket = new Socket("localhost",7734);
    	   	 System.out.println("Printing server socket information");
	         printSocketInfo(serverSocket);
	         System.out.println("You are the client. You are now connected to the server on port 7734");
	         Thread peerThread = new Thread(new client());
	         peerThread.start();
	         serverWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
	         serverReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
	        	 
	         while ((serverMsg=consoleInputReader.readLine())!= null) {
	        	 if (serverMsg.equalsIgnoreCase("exit"))
	             {
	             	System.out.println("Breaking now");
	             	serverWriter.write(serverMsg,0,serverMsg.length());
	             	//serverSocket.close();
	             	break;
	             }
	        	 System.out.println("Type ADD to Add all the RFC's to the server");
	        	 if(serverMsg.startsWith("ADD"))
	        	 {
	        		 	System.out.println("YOU are a DOUCHEBAG!!!");
	        		 	serverMsg = "";
	        		 	//serverWriter.write(serverMsg, 0, serverMsg.length());
	        		 	//serverWriter.write("You are HERE ----------------");
	        		 	
	        		 	addMessageBuilder();
	        		 	out.println(serverReader.readLine());
	        	 }
	        	 //serverWriter.flush();
	        	 System.out.println("ADDing the available RFC's to the server");
	        	 System.out.println("Type LIST to all RFC's");
	        	 if(serverMsg.equalsIgnoreCase("LIST"))
	        	 {
	        		 serverMsg = "";
	        		 listMsgBuilder();
		        	 serverMsg = "";
	        	 }	        		 
	        	 System.out.println("Type LOOKUP to lookup a specific RFC");
	        	 if(serverMsg.equalsIgnoreCase("LOOKUP"))
	        	 {
	        		 serverMsg = "";
		        	 System.out.println("Enter the RFC number");
		        	 rfcnum = consoleInputReader.readLine();
		        	 System.out.println("Enter the Title");
		        	 title = consoleInputReader.readLine();
	        	     lookupMsgBuilder(rfcnum,title);
	        	     serverMsg = "";
	        	 }
	        	serverWriter.flush();
	            serverWriter.write(serverMsg,0,serverMsg.length());            
	            serverWriter.newLine();
	            serverWriter.flush();
	         }
	         while ((serverRecvMsg=serverReader.readLine())!= null) {
	        	 if (serverRecvMsg.equalsIgnoreCase("exit"))
	             {
	             	System.out.println("Breaking now");
	             	//serverSocket.close();
	             	break;
	             }
	        	 else
	        	 {
	        		 System.out.println("Before server response");
	        		 System.out.println(serverRecvMsg);
	        	 }
	         }
	         //serverWriter.close();
	         //serverReader.close();
	         //serverSocket.close();     
	      }
	      
	      catch (IOException e) {
	         System.err.println(e.toString());
	      }
   }

   public static void addMessageBuilder()
   {	  
	   System.out.println("Inside Add Message Builder function");
	   try
	   {
		   int i = 0; 
		   String line = null;
		   StringTokenizer st;
		   BufferedReader fileReader = new BufferedReader(new FileReader("E:\\Workspace\\P2P\\IP\\src\\list.txt"));
		    try {
		        while ((line = fileReader.readLine()) != null) {
		        	serverMsg = serverMsg.concat("ADD");
		            st = new StringTokenizer(line, "=' '");
		            while(st.hasMoreTokens()) {
		       			//split = st.nextToken(" ");
		       			i++;
		       			serverMsg = serverMsg.concat(" ");
		       			serverMsg = serverMsg.concat(st.nextToken());
		       			if(i == 2)
		       			{
		       				i = 0;
		       				break;
		       			}
		            }
		            serverMsg = serverMsg.concat(" P2P-CI/1.0\n");
		            serverMsg = serverMsg.concat("Host: " + serverSocket.getInetAddress().getCanonicalHostName() + "\n");
		            serverMsg = serverMsg.concat("Port: " + peerServerSocket.getLocalPort() + "\n");
		            serverMsg = serverMsg.concat("Title: " + st.nextToken() + "\n");
		        }
		        
		    } finally {
		    	fileReader.close();
		    	System.out.println("Final ADD msg:");
		    	System.out.println(serverMsg);
		    	serverWriter.write(serverMsg,0,serverMsg.length());
		        serverWriter.newLine();
	            //serverWriter.flush();
		    }
	   }
	   catch (IOException | NullPointerException n) {
	       System.err.println(n);
	   }
   }
   
   public static void listMsgBuilder() throws IOException
   {
	   System.out.println("Inside List Message Builder function");
	   serverMsg = serverMsg.concat("LIST ALL P2P-CI/1.0\n");
	   serverMsg = serverMsg.concat("Host: " + serverSocket.getInetAddress().getCanonicalHostName() + "\n");
	   serverMsg = serverMsg.concat("Port: " + peerServerSocket.getLocalPort() + "\n");
	   System.out.println("Final LIST Msg: ");
	   System.out.println(serverMsg);
	   serverWriter.write(serverMsg,0,serverMsg.length());
	   serverWriter.newLine();
       //serverWriter.flush();
   }
   

   public static void lookupMsgBuilder(String rfcnum, String title) throws IOException
   {
	   System.out.println("Inside Lookup Message Builder function");
	   serverMsg = serverMsg.concat("LOOKUP RFC " + rfcnum);
	   serverMsg = serverMsg.concat(" P2P-CI/1.0\n");
	   serverMsg = serverMsg.concat("Host: " + serverSocket.getInetAddress().getCanonicalHostName() + "\n");
	   serverMsg = serverMsg.concat("Port: " + peerServerSocket.getLocalPort() + "\n");
	   serverMsg = serverMsg.concat("Title: " + title + "\n");
	   System.out.println("Final LOOKUP Msg: ");
	   System.out.println(serverMsg);
	   serverWriter.write(serverMsg,0,serverMsg.length());
	   serverWriter.newLine();
       //serverWriter.flush();
   }
   
   public static String uploadServer(String uploadPort)
   {
	   System.out.println("Sent upload port to server");
	   return uploadPort;
   }
   
   public void run()
   {
	   try {
		   		System.out.println("Inside Run. Client thread has begun.");
		   		peerServerSocket = new ServerSocket(0);
		   		int uploadPort = peerServerSocket.getLocalPort();
		   		System.out.println("Printing client socket info running on upload port");
		   		printServerSocketInfo(peerServerSocket);
		   		System.out.println("Client is running on UploadPort = " + uploadPort);
		   		serverMsg = uploadServer(Integer.toString(uploadPort));
		   		serverWriter.write(serverMsg,0,serverMsg.length());
		   		serverWriter.newLine();
	            serverWriter.flush();
	            //System.out.println("Press enter");
		   		while(true)
		   		{
		   			peer = peerServerSocket.accept();
		   			Thread clientThread = new Thread(new peer( ));
		   			clientThread.start();
		   			clientThread.run();
		   		}
	   	}
	   
	   catch (IOException e) {
	         System.err.println(e.toString());
	     }
   }
   
   //To request for download
   public void getMsgBuilder()
   {
	   
   }
   
   public void getMsgHandler()
   {
	   
   }
   
   /*
    * 	GET RFC 1234 P2P-CI/1.0
		Host: somehost.csc.ncsu.edu
		OS: Mac OS 10.4.1
    */
   
   /*
    * 	Response Codes
    * 	• 200 OK
		• 400 Bad Request
		• 404 Not Found
		• 505 P2P-CI Version Not Supported
    */
   
   public class peer implements Runnable
   {
	   public void run()
	   { 
		   try
		   {	//give data only to other peer
			   while ((peerMsg = peerReader.readLine()) != null) {
				   if(peerMsg.startsWith("GET"))
				   {
					   System.out.println("Inside the peer to peer run to connect to a new peer");
					   peerWriter = new BufferedWriter(new OutputStreamWriter(peer.getOutputStream()));
					   peerReader = new BufferedReader(new InputStreamReader(peer.getInputStream()));
					   peerWriter.write("Write to peer");				   
					   System.out.println("Handle the GET message");
					   if(peerMsg.equalsIgnoreCase("exit"))
						   break;
					   StringTokenizer st = new StringTokenizer(peerMsg, "=' '");
					   System.out.println("Begin splitting the GET tokens");
					   String[] split = peerMsg.split("\\s+");
					   String line;
					   String filesplit;
					   int i = 0;
					   BufferedReader fileReader = new BufferedReader(new FileReader("E:\\Workspace\\P2P\\IP\\src\\list.txt"));
					   while ((line = fileReader.readLine()) != null) {
						   		st = new StringTokenizer(line, "=' '");
						   			while(st.hasMoreTokens()) {
				       					filesplit = st.nextToken(" ");
				       					i++;
				       					if(filesplit.equalsIgnoreCase(split[2]))
				       						peerMsg.concat("P2P-CI/1.0 200 OK");
				       					
						       			if(i == 2)
						       			{
						       				i = 0;
						       				break;
						       			}
						   			}
					   }
					   fileReader.close();
					}					   
				   }
		   }		   
		   catch (IOException e) {
		         System.err.println(e.toString());
		     }
	   }
   }
   
   
   public static void printSocketInfo(Socket s) {
      System.out.println("Remote address = " +s.getInetAddress().toString());
      System.out.println("Remote port = " +s.getPort());
      System.out.println("Local socket address = " +s.getLocalSocketAddress().toString());
      System.out.println("Local address = " +s.getLocalAddress().toString());
      System.out.println("Local port = " +s.getLocalPort());
   }
   
   private static void printServerSocketInfo(ServerSocket s) {
	      System.out.println("Server socket address = "+s.getInetAddress().toString());
	      System.out.println("Server socket port = "+s.getLocalPort());
	   }
   
}