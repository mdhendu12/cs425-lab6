package edu.jsu.mcis.lab6.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class TrainingSessionDAO {
    
    private final DAOFactory daoFactory;
    
    private static final String QUERY_LIST = "SELECT * FROM session";
    private static final String QUERY_LIST_ATTENDEES = "SELECT * FROM attendee WHERE id IN (SELECT attendeeid FROM registration WHERE sessionid = ?)";
    private static final String QUERY_FIND = "SELECT * FROM session WHERE id = ?";
    
   /* private final String QUERY_SELECT_BY_ID = "SELECT * FROM "
            + "((registration JOIN attendee ON registration.attendeeid = attendee.id) "
            + "JOIN `session` ON registration.sessionid = `session`.id) "
            + "WHERE `session`.id = ? AND attendee.id = ?"; */
    
    TrainingSessionDAO(DAOFactory dao) {
        this.daoFactory = dao;
    }
    
    public String list() {
                
        JSONObject json = new JSONObject();
        json.put("success", false);
        JSONArray sessions = new JSONArray();

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try { 
            ps = conn.prepareStatement(QUERY_LIST);
            
            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();
                
                json.put("success", hasresults);
                
                while (rs.next()) {
                    
                    JSONObject session = new JSONObject();
                    
                    session.put("id", rs.getInt("id"));
                    session.put("description", rs.getString("description"));
                    
                    sessions.add(session);
                                                            
                }
                                
                json.put("sessions", sessions);
            }
        }
        catch (Exception e) { 
            e.printStackTrace(); 
        }
        finally { 
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
        return JSONValue.toJSONString(json);
    }
    
    public String list(int id) {
        JSONObject json = new JSONObject();
        json.put("success", false);
        JSONArray attendees = new JSONArray();
        
        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try { 
            ps = conn.prepareStatement(QUERY_LIST_ATTENDEES);
            ps.setInt(1, id);
            
            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();
                
                json.put("success", hasresults);
                
                while (rs.next()) {
                    JSONObject attendee = new JSONObject();
                    
                    attendee.put("id", rs.getInt("id"));
                    attendee.put("firstname", rs.getString("firstname"));
                    attendee.put("lastname", rs.getString("lastname"));
                    attendee.put("displayname", rs.getString("displayname"));
                    
                    attendees.add(attendee);
                                                            
                }
                                
                json.put("attendees", attendees);
            }
        }
        catch (Exception e) { 
            e.printStackTrace(); 
        }
        finally { 
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
        }
        return JSONValue.toJSONString(json);
    }
}

/*

public class RegistrationDAO {
    
    
    public String find(int sessionid, int attendeeid) {

        JSONObject json = new JSONObject();
        json.put("success", false);

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(QUERY_SELECT_BY_ID);
            ps.setInt(1, sessionid);
            ps.setInt(2, attendeeid);
            
            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();
                
                if (rs.next()) {
                    
                    json.put("success", hasresults);
                    
                    json.put("attendeeid", rs.getInt("attendeeid"));
                    json.put("sessionid", rs.getInt("sessionid"));
                    json.put("firstname", rs.getString("firstname"));
                    json.put("lastname", rs.getString("lastname"));
                    json.put("displayname", rs.getString("displayname"));
                    json.put("session", rs.getString("description"));
                                        
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                }
                catch (Exception e) { e.printStackTrace(); }
            }

        }

        return JSONValue.toJSONString(json);

    }
    
}
*/