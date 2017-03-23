
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ServerY {

	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	 final static String DB_URL = "jdbc:mysql://localhost/hi";
	 static Connection conn = null;
	 static Statement stmt = null;
	 String sql="";
	 
	 //static String fromVolunteer="";
        static String fromVolunteer = "harvesting~1~resultNewest";

        static String modifiedCode = "";
	 	 
	public ServerY(String type) throws ClassNotFoundException, SQLException{
		fromVolunteer = type;
		Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, "root", "");
                 stmt = conn.createStatement();	
                        updateTable();
	}
	
	public static void updateTable(){
		if(fromVolunteer.equals("")){
                    
                    String volunteerCode = "link = \"\" asdasdasdasd   taskID = \"\" kjjlkjlkj executionType = \"\"";
                    String execTypeReplace = "";
                    String dataReplace = "";
                    String taskIDReplace = "";
                    String dataType = ""; //will contain either "link" or "content"
                    String isHarvesting = "";
                    
                    try{
			Class.forName("com.mysql.jdbc.Driver");
                        conn = DriverManager.getConnection(DB_URL, "root", "");
                        stmt = conn.createStatement();	
			String query = "SELECT * FROM harvesting where status = 0";
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs!=null){                            
                            isHarvesting = "true";
                            rs.next();
                            taskIDReplace = "taskID = "+rs.getString("taskID");
                            dataReplace = rs.getString("data");
                        }else{
                            query = "SELECT * FROM sentimentAnalysis where status = 0";
                            rs = stmt.executeQuery(query);
                            if(rs!=null){ 
                                isHarvesting = "false";
                                rs.next();
                                taskIDReplace = "taskID = "+rs.getString("taskID");
                                dataReplace = rs.getString("data"); 
                            }
                            
                        }

            
                    }catch(SQLException se){
                        se.printStackTrace();
                    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
                    }
                    


                    //if table1 was accessed (note: "true" is just there to not make it error)
                    if (isHarvesting.equals("true")){
                       execTypeReplace = "harvesting";
                       dataType = "link";
                       System.out.println("HARVESTING PROCESS ONGOING." + modifiedCode);

                    //else if table2 was accessed (note: "false" is just there to not make it error
                    }else if(isHarvesting.equals("false")){
                        execTypeReplace = "sentimentAnalysis";
                        dataType = "content";
                        System.out.println("HARVESTING ALREADY COMPLETE. SENTIMENT ANALYSIS PROCESS ONGOING." + modifiedCode);
                    }else if(isHarvesting.equals("")){
                         System.out.println("HARVESTING AND SENTIMENT ANALYSIS ALREADY COMPLETE." + modifiedCode);
                         return;
                    }

                    
                    //modifying the volunteer code
                    modifiedCode = volunteerCode.replace(dataType+" = \\\"\\\"", dataType + " = \\\"" +dataReplace+"\\\"");
                    modifiedCode = modifiedCode.replace("taskID = \\\"\\\"", taskIDReplace);
                    modifiedCode = modifiedCode.replace("executionType = \\\"\\\"", "executionType = \\\"" +execTypeReplace+"\\\"");

                    System.out.println("MODIFIED CODE: " + modifiedCode);
		}else{
			
			String[] response= new String[5];
			response = fromVolunteer.split("~");
			String executionType= response[0];
			String taskID= response[1];
			String result= response[2];
			
			System.out.println(executionType+" "+taskID+" "+result);
			
			try{
			
			String update = "UPDATE "+executionType+" SET result = '"+result+"', status=1 where taskID = "+taskID+"";
                        stmt.executeUpdate(update);
            
            if(executionType.equals("harvesting")){
            	
            	String addtoTable2 = "INSERT INTO sentimentanalysis (data)" +
                         "VALUES ('"+result+"')";
                stmt.executeUpdate(addtoTable2);
            }
                        
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
}
