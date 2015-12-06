package webserver;

import java.io.* ;
import java.net.* ;
import java.util.Properties;


public class WebServer2 extends Thread {
    private ServerSocket socket = null;
    private Socket connection = null;
    private Properties config = null;
    private String root = "images/win.png";

    public WebServer2(ServerSocket s, Properties config) {
        this.socket = s;
        this.config = config;
        this.root = this.config.getProperty("root");        
    }
    
    public WebServer2(ServerSocket s) {
        this.socket = s;   
    }

    public void run() {   


        OutputStream out = null;
        InputStream in = null;
        
        try {
        	connection = socket.accept();
            out = connection.getOutputStream();
            in = connection.getInputStream();
            PrintWriter writer = new PrintWriter(out, true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            
            String request = reader.readLine();
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");          
            writer.println();

            // Search for filename 
            int slash = request.indexOf("/"); //find first occurrence of slash
            int emptySpace = request.indexOf(" ", slash); //find first space after slash
            String filename = request.substring(slash, emptySpace); //return filename 
            
//          writer.println("<b>" + filename + "</b>");

            try {
     
                File initialFile = new File(root);
                InputStream targetStream = new FileInputStream(initialFile);
                
                int toWrite;
                while((toWrite = targetStream.read()) != -1) {
                	out.write(toWrite);
                }
                
                targetStream.close();
            }
            catch (MalformedURLException e) {
                System.err.println("Don't know about host: " + filename);
            }
            catch (IOException e) {
                  System.err.println("Couldn't get I/O for the connection to: " + filename);
            }
//          reader.close();
            writer.close();
            socket.close();
        }
        catch(IOException e) {
            System.out.println("Error: " + e);          
        }

        finally {
            try {
//              in.close() ;
                out.close() ;
                socket.close();
            }
            catch(IOException e) {
                System.out.println("Error: " + e);          
            }
        }
    }
    
    public static void main(String...args) {
    	try {
			WebServer2 myServer = new WebServer2(new ServerSocket(5055));
			myServer.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 }