# NJC-LABS-Exercise
This is a Repository for NJC Labs Exercise 

***

### Database Information 
* Database Name: **games**
* Database Table: **favorite**
* Database Client: **MYSQL WorkBench** 
* Initial Contents: 
```sql
  INSERT INTO favorite (name) VALUES ("DOOM");
  INSERT INTO favorite (name) VALUES ("Vampyr");
  INSERT INTO favorite (name) VALUES ("Control");
  INSERT INTO favorite (name) VALUES ("Quantum Break");
  INSERT INTO favorite (name) VALUES ("Dead Space");
  INSERT INTO favorite (name) VALUES ("Read Dead Redemption 2");
  INSERT INTO favorite (name) VALUES ("Dead Cells");
  INSERT INTO favorite (name) VALUES ("Ruiner");
  INSERT INTO favorite (name) VALUES ("Resident Evil 2");
  INSERT INTO favorite (name) VALUES ("Superhot");
```

***

### SQL Commands to Retrieve Records 
The following SQL commands return records from table **favorite**. The SQL commands will also be used for the following steps. 
```sql
  SELECT * FROM favorite; 
  SELECT * FROM favorite WHERE id=<id>; 
  SELECT * FROM favorite WHERE name="<name>"; 
```

***

### Java Code to Retrieve Records 
The **runSQL()** function takes in a SQL Query command as a string and runs the command. The query results are then converted to JSON and returned. 
```java
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
		
  } catch(Exception e) {
    System.out.print("Error: "+e);
	}
        
  return jsonResponse.toString();   
        		
}
```
Example Call of **runSQL()**: 
```java
runSQL("SELECT * FROM favorite");
runSQL("SELECT * FROM favorite WHERE id=3");
runSQL("SELECT * FROM favorite WHERE name='DOOM'");
```
Example Return of **runSQL()**: 
```
[{"name":"DOOM","id":1},{"name":"Vampyr","id":2},{"name":"Control","id":3}, ... {"name":"Superhot","id":10}]
[{"name":"Control","id":3}]
[{"name":"DOOM","id":1}]
```

***

### REST API to Retrieve Records 
REST API has been set up to return JSON depending on the request variables. <br><br>

The URL request `http://localhost:8000/api/games` returns all games: <br>
```
[{"name":"DOOM","id":1},{"name":"Vampyr","id":2},{"name":"Control","id":3}, ... {"name":"Superhot","id":10}]
```
<br>The URL request `http://localhost:8000/api/games?id=3` returns game with id=3: <br>
```
[{"name":"Control","id":3}]
```
<br>The URL request `http://localhost:8000/api/games?name='DOOM'` returns game with name="DOOM": <br>
```
[{"name":"Control","id":3}]
```
	
  
  
  
  
