// HTTPS Local Server Libraries 
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;


// SQL Connection Related Libraries 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

 
// JSON Libraries  
import org.json.*;


// Extra Java Libraries  
import java.util.Map;
import java.util.HashMap; 

 
class Main {
	
	public static Connection conn; 
	
	public static void establishConnectiontoDB() {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/games","root", "MyNewPass");
		
		}
		catch(Exception e) {
			System.out.print("Error: "+e);
		}
		
	}
	
	public static String runSQL(String query) {
			
	    JSONArray jsonResponse = new JSONArray(); 
		
        try {
			
        	Statement st = conn.createStatement();   
    	    ResultSet rs = st.executeQuery(query);
    		    	    
            while (rs.next()) {
            	
    	        int id = rs.getInt("id");
    	        String name = rs.getString("name");
    	        jsonResponse.put(new JSONObject().put("name", name).put("id", id));  
    	        
            }
            
            st.close(); 
		
		}
		catch(Exception e) {
			System.out.print("Error: "+e);
		}
        
        return jsonResponse.toString();   
        		
	}
	
	public static void closeConnectiontoDB() {
		
		try {
			
        	conn.close(); 
		
		}
		catch(Exception e) {
			System.out.print("Error: "+e); 
		}
				
	}
	
	public static Map<String, String> queryToMap(String query) {
		
	    Map<String, String> result = new HashMap<>();
	    
	    for (String param : query.split("&")) {
	    	
	        String[] entry = param.split("=");
	        
	        if (entry.length > 1) {
	            result.put(entry[0], entry[1]);
	        } else {
	            result.put(entry[0], "");
	        }
	        
	    }
	    
	    return result;
	
	}
	
	public static void main (String[] args) throws IOException{
		
		establishConnectiontoDB(); 
		
		int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        
        server.createContext("/api/games", (exchange -> {
        	
            String headerParameters = exchange.getRequestURI().getQuery(); 
            String respText = ""; 
            
            if (headerParameters != null) {
            	
            	Map<String, String> params = queryToMap(headerParameters);  
            	
            	if (params.containsKey("id")) {
            		respText = runSQL("SELECT * FROM favorite WHERE id=" + params.get("id")); 
            	} else if (params.containsKey("name")) {
            		respText = runSQL("SELECT * FROM favorite WHERE name=" + params.get("name"));
            	}
            	 
            } else {
            	respText = runSQL("SELECT * FROM favorite"); 
            }
            
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close(); 
        	
        }));
        
        server.setExecutor(null); 
        server.start();
        
	}
	
}
