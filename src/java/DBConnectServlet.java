/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sha
 */
public class DBConnectServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost/mdl";
    static protected String projectName = "";
    static protected String volunteerContent = "";
    static protected String serverContent = "";
    static int projectID = 0;
    static String serverBtnState = "none";
    static String volunteerBtnState = "none";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Connection conn = null;
        Statement stmt = null;
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try{
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, "root", "");
            stmt = conn.createStatement();
                        
            if(request.getParameter("method").equals("loadProjectList")){
                String sql = "SELECT project_id, project_name FROM projects";
                ResultSet rs = stmt.executeQuery(sql);
                ArrayList<Integer> projectid = new ArrayList<Integer>();
                ArrayList<String> projectN = new ArrayList<String>();
                while(rs.next()){
                    projectid.add(Integer.parseInt(rs.getString("project_id")));
                    projectN.add(rs.getString("project_name"));
                }
                serverBtnState = "none";
                volunteerBtnState = "none";
                
                JSONObject newObj = new JSONObject();
                newObj.put("projectID", projectid);
                newObj.put("projectName", projectN);
                out.println(newObj);   
            }
            
            if(request.getParameter("method").equals("loadProjectDetails")){
                String sql = "SELECT * from projects where project_id= "+request.getParameter("projectID")+"";
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next()){
                    projectID = Integer.parseInt(rs.getString("project_id"));
                    projectName = rs.getString("project_name");
                    serverContent = rs.getString("server_content");
                    volunteerContent = rs.getString("volunteer_content");
                }
                serverBtnState = "none";
                volunteerBtnState = "none";
                
                out.println(projectID);
            }
            
            if(request.getParameter("method").equals("createProject")){
                String sql = "INSERT INTO projects (project_name)" +
                         "VALUES ('"+request.getParameter("projectName")+"')";
                int status = stmt.executeUpdate(sql);  
                if(1 == status){
                    String project_ID = "SELECT project_id, project_name from projects where project_name= '"+request.getParameter("projectName")+"'";
                    ResultSet rs = stmt.executeQuery(project_ID);
                    if(rs.next()){
                        projectID = Integer.parseInt(rs.getString("project_id"));
                        projectName = rs.getString("project_name");
                        serverContent = "";
                        volunteerContent = "";
                        serverBtnState = "none";
                        volunteerBtnState = "none";
                    }
                }
                System.out.println("Creating Database.....");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "");
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE DATABASE "+request.getParameter("projectName"));
                JSONObject newObj = new JSONObject();
                newObj.put("projectID", projectID);
                newObj.put("status", status);
                out.println(newObj);
            }
            
            if(request.getParameter("method").equals("loadProject")){
                JSONObject newObj = new JSONObject();
                newObj.put("projectID", projectID);
                newObj.put("projectName", projectName);
                newObj.put("volunteerContent", volunteerContent);
                newObj.put("serverContent", serverContent);

                out.println(newObj);
            }
            
            if(request.getParameter("method").equals("saveServerContent")){
                String sql = "UPDATE projects set server_content = '"+request.getParameter("serverContent")+"' where project_id = "+request.getParameter("projectID")+"";
                int status = stmt.executeUpdate(sql);
                if(1 == status){
                    serverContent = request.getParameter("serverContent");
                }
                out.println(status);
            }
            
            if(request.getParameter("method").equals("saveVolunteerContent")){
                String sql = "UPDATE projects set volunteer_content = '"+request.getParameter("volunteerContent")+"' where project_id = "+request.getParameter("projectID")+"";
                int status = stmt.executeUpdate(sql);
                if(1 == status){
                    volunteerContent = request.getParameter("volunteerContent");
                }
                out.println(status);
            }
            
            if(request.getParameter("method").equals("saveServerBtnState")){
                serverBtnState = request.getParameter("btnStatus");
            }
            
            if(request.getParameter("method").equals("saveVolunteerBtnState")){
                volunteerBtnState = request.getParameter("btnStatus");
            }
            
            if(request.getParameter("method").equals("getBtnState")){
                JSONObject newObj = new JSONObject();
                newObj.put("projectName", projectName);
                newObj.put("serverBtn", serverBtnState);
                newObj.put("volunteerBtn", volunteerBtnState);
                out.println(newObj);
            }
            
            if(request.getParameter("method").equals("compileProject")){
                SyntaxChecker interpreter = new SyntaxChecker();
                String status = interpreter.checkSyntax(request.getParameter("volunteerContent"));
                
                JSONObject newObj = new JSONObject();      
                newObj.put("volunteerErrors", status);
                               
                out.println(newObj);               
            }
            
            if(request.getParameter("method").equals("run")){
                System.out.println("Running system...");
//                JavaInterpreter serverY = new JavaInterpreter(request.getParameter("serverContent"), "ServerY");
                JavaInterpreter volunteer = new JavaInterpreter(request.getParameter("volunteerContent"), "Main");
//                String serverYCode = serverY.javaConverted;
                String volunteerCode = volunteer.javaConverted;                

                if(volunteerCode != ""){ 
//                        /Server.fromUI = volunteerCode;
                        
//                        System.out.println("serverYcode " +serverYCode );
                        Server.database = request.getParameter("dbName");
                        Server.main(null);
//                        implementServerY(serverYCode);                       
                                             
                       
                        JSONObject newObj = new JSONObject();      
                        newObj.put("fromVolunteer", Server.responseFromVolunteer);

                         out.println(newObj);                
                }               
            }
            
            if(request.getParameter("method").equals("loadAllList")){
                String allList = "SELECT * from volunteers";
                ResultSet rs = stmt.executeQuery(allList);
                ArrayList<String> allListArr = new ArrayList<String>();
                while(rs.next()){
                    allListArr.add(rs.getString("IPAddress"));
                }
                JSONObject newObj = new JSONObject();
                newObj.put("allList", allListArr);
                
                out.println(newObj);

            }
            
            if(request.getParameter("method").equals("loadAll")){
                String allActive = "SELECT * from volunteers where status = 'active'";
                ArrayList<String> allActiveArr = new ArrayList<String>();
                ResultSet rs = stmt.executeQuery(allActive);
                while(rs.next()){
                    allActiveArr.add(rs.getString("IPAddress"));
                }
                JSONObject newObj = new JSONObject();
                newObj.put("allActive", allActiveArr);
                
                out.println(newObj);
            }
            

            if(request.getParameter("method").equals("addTable")){
                System.out.println("Creating table .....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                conn = DriverManager.getConnection(db, "root", "");
                stmt = conn.createStatement();    
                System.out.println(request.getParameter("tableName"));
                String table = "CREATE TABLE "+request.getParameter("tableName")+
                                "(taskID INTEGER NOT NULL AUTO_INCREMENT, "+
                                "dateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                                "status INTEGER NOT NULL DEFAULT 0, "+
                                "IPAddress VARCHAR(20) NULL, "+
                                "PRIMARY KEY (taskID))";
                System.out.println(table);
                int n = stmt.executeUpdate(table);
                JSONObject newObj = new JSONObject(); 
                newObj.put("rows", n);
                
                out.println(newObj);
            }
            
            if(request.getParameter("method").equals("addColumns")){
                System.out.println("Adding columns .....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                conn = DriverManager.getConnection(db, "root", "");
                stmt = conn.createStatement();    
                String tbs[] = request.getParameter("columns").split("-");
                for(int i = 0; i < tbs.length; i++){
                    System.out.println(tbs[i]);
                    String table = "ALTER TABLE "+request.getParameter("table")+" ADD "+tbs[i]+" LONGTEXT";
                    System.out.println(table);
                    stmt.execute(table);
                }
                JSONObject newObj = new JSONObject(); 
                newObj.put("rows", 0);
                
                out.println(newObj);
            }
            
            if(request.getParameter("method").equals("loadDBTables")){
                System.out.println("Retrieving tables .....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                conn = DriverManager.getConnection(db, "root", "");
                stmt = conn.createStatement();    
                DatabaseMetaData md = conn.getMetaData();
                String[] types = {"TABLE"};
                ResultSet rs = md.getTables(null, null, "%", types);
                List tables = new LinkedList();
                while(rs.next()){
//                    System.out.println(rs.getString(2));
                    tables.add(rs.getString(3));
                }
                System.out.println(tables);
                JSONObject newObj = new JSONObject();
                newObj.put("tables", tables);
                
                out.println(newObj);
            }
            
            if(request.getParameter("method").equals("loadTableContent")){
                System.out.println("Retrieving table content .....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                conn = DriverManager.getConnection(db, "root", "");
                stmt = conn.createStatement(); 
//                DatabaseMetaData md = conn.getMetaData();
//                ResultSet rs = md.getColumns(null, null, request.getParameter("table"), null);
//                List columns = new LinkedList();
//                while(rs.next()){
//                    columns.add(rs.getString(4));
//                }
                ResultSet rs = stmt.executeQuery("SELECT * FROM "+request.getParameter("table"));
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                List columns = new LinkedList();
                List data = new LinkedList();
                List result = new LinkedList();
                // The column count starts from 5
                for (int i = 5; i <= columnCount; i++ ) {
                  columns.add(rsmd.getColumnName(i));
                }
                while(rs.next()){
                    data.add(rs.getString("data"));
                    result.add(rs.getString("result"));
                }
                System.out.println(columns);
                System.out.println(data);
                System.out.println(result);
                JSONObject newObj = new JSONObject();   
                newObj.put("columns", columns);
                newObj.put("data", data);
                newObj.put("result", result);        
                
                out.println(newObj);                
            }
            if(request.getParameter("method").equals("deleteTable")){
                System.out.println("Deleting table.....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                conn = DriverManager.getConnection(db, "root", "");
                stmt = conn.createStatement(); 
                stmt.executeUpdate("DROP TABLE "+request.getParameter("table"));
  
                JSONObject newObj = new JSONObject();
                newObj.put("table", 0);
                
                out.println(newObj);                
            }
            
            if(request.getParameter("method").equals("clearTable")){
                System.out.println("Clearing table content.....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                conn = DriverManager.getConnection(db, "root", "");
                stmt = conn.createStatement(); 
                String table = "DELETE FROM "+request.getParameter("table");
                System.out.println(table);
                System.out.println(stmt.executeUpdate(table));
                JSONObject newObj = new JSONObject();
                newObj.put("columns", 0);
                
                out.println(newObj);                
            }
            
            if(request.getParameter("method").equals("storeCSV")){
                System.out.println("Uploading CSV content.....");
                String db = "jdbc:mysql://localhost/"+request.getParameter("dbName");
                System.out.println(db);
                conn = DriverManager.getConnection(db, "root", "");
                String csv[] = request.getParameter("csv").split("\n");
                System.out.println(csv.length);
                stmt = conn.createStatement(); 
                for(int i = 0; i < csv.length; i++){
                    System.out.println(csv[i]);
                    stmt.executeUpdate("INSERT INTO "+request.getParameter("table")+" (data, result) VALUES ('"+csv[i]+"', 'NULL')");               
                }
                JSONObject newObj = new JSONObject();
                newObj.put("csv", 0);
                
                out.println(newObj);                
            }
        }catch(Exception e){
            
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
