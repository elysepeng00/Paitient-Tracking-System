
import java.net.*;
import java.io.*;
public class PoolEchoServer extends Thread {
   public final static int defaultPort = 6000;
   ServerSocket theServer;
   static int num_threads = 10;
   public static void main(String[] args) {
      int port = defaultPort;
      try { port = Integer.parseInt(args[0]); }
      catch (Exception e) { }
      if (port <= 0 || port >= 65536) port = defaultPort;
      try {
         ServerSocket ss = new ServerSocket(port);
         System.out.println("Server Socket Start!!");
         for (int i = 0; i < num_threads; i++) {
            System.out.println("Create num_threads " 
                               + i + " Port:" + port);
            PoolEchoServer pes = new PoolEchoServer(ss);
            pes.start();
         }
      }
      catch (IOException e) { System.err.println(e); }
   }
   public PoolEchoServer(ServerSocket ss) { theServer = ss; }
   
   public void run() {
      while (true) {
         try {
            DataOutputStream output;
            DataInputStream input;
            Socket connection = theServer.accept();
            System.out.println("Accept Client!");

            //OutputStream os = s.getOutputStream();
            //InputStream is = s.getInputStream();
            input = new DataInputStream(
            connection.getInputStream() );
            output = new DataOutputStream(
            connection.getOutputStream() );
            //BufferedReader bf = new 
            // BufferedReader(new InputStreamReader(is));
            float isFallSum;
            System.out.println("Client Send Data-------------------- ");
            while (true) {
               String readFile = input.readUTF();
              //System.out.println("==> Input from Client: " + input.readUTF());
               String[] value = readFile.split(",");
               isFallSum = 0;
               for (int i = 0; i< 3; i++){
            	 if (i == 0){
            		 System.out.println("X: " + value[i]); 
            	 }
            	 else if (i == 1){
            		 System.out.println("Y:" + value[i]); 
            	 }
            	 else if (i == 2){
            		 System.out.println("Z:" + value[i]); 
            	 } 
            	 isFallSum += Math.sqrt(Math.round(Math.pow(Double.parseDouble(value[i]), 2)));
            	 
               }
               System.out.println("Fall Down (Yes/No)"); 
            	 if (isFallSum<=6){
            		 output.writeBoolean(true);
            		 System.out.println("Yes"); 
            	 }
            	 else if (isFallSum>6){
               	   output.writeBoolean(false);
               	   System.out.println("No"); 
                  }

              // System.out.println(  "Output to Client ==> \"Connection successful\"");
              // output.writeUTF( "Connection successful" );
               //os.write(n);
               //os.write("Hello Client!!");
               output.flush();
         		
            } // end while

         } // end try
         catch (IOException e) { }
      } // end while
   } // end run
}
    