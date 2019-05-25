import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;
public class  ChatServer {
  Vector<String> users = new Vector<String>();
  Vector<HandleClient> clients = new Vector<HandleClient>();
  public void process() throws Exception  {
      ServerSocket server = new ServerSocket(9999,10);
      out.println("Server Started...");
      while( true) {
 		 Socket client = server.accept();
 		 HandleClient c = new HandleClient(client);
  		 clients.add(c); 
     }  
  }
  public static void main(String ... args) throws Exception {
      new ChatServer().process();
  }   
  public void bcast(String user, String message)  {
	    for ( HandleClient c : clients )
	    	c.sendmessage(user,message); 
  }
  class  HandleClient extends Thread {
        String name = "";
	BufferedReader input;
	PrintWriter output;
	public HandleClient(Socket  client) throws Exception {
	 input = new BufferedReader( new InputStreamReader( client.getInputStream())) ;
	 output = new PrintWriter ( client.getOutputStream(),true);
	 name  = input.readLine();
	 users.add(name); 
	 start();
        }
        public void sendmessage(String uname,String  msg)  {
	    output.println( uname + ":" + msg);
	}
		
        public String getUserName() {  
            return name; 
        }
        public void run()  {
    	     String line;
	     try    {
                while(true)   {
		 line = input.readLine();
		 if ( line.equals("end") ) {
		   clients.remove(this);
		   users.remove(name);
		   break;
                 }
		 bcast(name,line); 
	       } 
	     } 
	     catch(Exception ex) {
	       System.out.println(ex.getMessage());
	     }
        } 
   } 
} 