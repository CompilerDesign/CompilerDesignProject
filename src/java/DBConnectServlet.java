/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
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
                JavaInterpreter serverY = new JavaInterpreter(request.getParameter("serverContent"), "ServerY");
                JavaInterpreter volunteer = new JavaInterpreter(request.getParameter("volunteerContent"), "Main");
                String serverYCode = serverY.javaConverted;
                String volunteerCode = volunteer.javaConverted;                

                if(volunteerCode != ""){ 
//                        /Server.fromUI = volunteerCode;
                        
                        System.out.println("serverYcode " +serverYCode );
                        new Server();
//                        implementServerY(serverYCode);                       
                                             
                       
                        JSONObject newObj = new JSONObject();      
                        newObj.put("fromVolunteer", Server.responseFromVolunteer);

                         out.println(newObj);
                                
//                        Timer timer = new Timer(); 
//                        timer.scheduleAtFixedRate(new TimerTask() {
//                        @Override
//                        public void run() {
//                            System.out.println("timer");
//                            if(Server.responseFromVolunteer != ""){
//                                JSONObject newObj = new JSONObject();      
//                                newObj.put("javaConverted", Server.responseFromVolunteer);
//
//                                out.println(newObj); 
//                                
//                                timer.cancel();
//                            }
//                         }
//                        }, 0, 1000); //every second
//                    }
                    
//                    javaCode = s.responseFromVolunteer;
                
                }
//                JSONObject newObj = new JSONObject();      
//                newObj.put("javaConverted", javaCode);
//                               
//                out.println(newObj);               
            }
            
//            if(request.getParameter("method").equals("checkResult")){
//                
//            }
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
        }catch(Exception e){
            
        }
        
        
    }
//    private void implementServerY(String code){       
//        //hindi ko na po alam!!! enebe
//        new Thread(new Runnable() { 
//                    public void run() {
//                        System.out.println("enter implement server Y");
//                        System.out.println(ServerY.main());
//                        System.out.println("implement server Y done");
//                    }
//                }).start();
//    }

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
