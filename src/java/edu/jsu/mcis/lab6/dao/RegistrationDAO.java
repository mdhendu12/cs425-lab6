package edu.jsu.mcis.lab6.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.simple.*;

public class RegistrationDAO {
    
    private final DAOFactory daoFactory;
    
    private final String QUERY_SELECT_BY_ID = "SELECT * FROM "
            + "((registration JOIN attendee ON registration.attendeeid = attendee.id) "
            + "JOIN `session` ON registration.sessionid = `session`.id) "
            + "WHERE `session`.id = ? AND attendee.id = ?";
    
    private final String QUERY_SELECT_BY_SESSION_ID = "SELECT * FROM "
            + "((registration JOIN attendee ON registration.attendeeid = attendee.id) "
            + "JOIN `session` ON registration.sessionid = `session`.id) "
            + "WHERE `session`.id = ?";
    private final String QUERY_CREATE = "INSERT INTO registration (sessionid, attendeeid) VALUES (?, ?)";
    private final String QUERY_UPDATE = "UPDATE registration SET sessionid=? WHERE attendeeid=?";
    private final String QUERY_DELETE = "DELETE FROM registration WHERE attendeeid = ?";
    
    RegistrationDAO(DAOFactory dao) {
        this.daoFactory = dao;
    }
    
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
    
    
    public String find(int sessionid) {

        JSONObject json = new JSONObject();
        json.put("success", false);
        JSONArray records = new JSONArray();

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(QUERY_SELECT_BY_SESSION_ID);
            ps.setInt(1, sessionid);
            
            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();
                
                json.put("success", hasresults);
                
                while (rs.next()) {
                    
                    JSONObject record = new JSONObject();
                    
                    record.put("attendeeid", rs.getInt("attendeeid"));
                    record.put("sessionid", rs.getInt("sessionid"));
                    record.put("firstname", rs.getString("firstname"));
                    record.put("lastname", rs.getString("lastname"));
                    record.put("displayname", rs.getString("displayname"));
                    record.put("session", rs.getString("description"));
                                        
                    records.add(record);
                }

                json.put("registrations", records);
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
    
    public String create(int sessionid, int attendeeid) {

        JSONObject json = new JSONObject();
        json.put("success", false);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_CREATE);
                ps.setInt(1, sessionid);
                ps.setInt(2, attendeeid);
                
                int updateCount = ps.executeUpdate();
                boolean hasResults = updateCount > 0;
                if (hasResults) {
            
                    json.put("success", hasResults);
                    
                    json.put("attendeeid", attendeeid);
                    json.put("sessionid", sessionid);

                }
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return JSONValue.toJSONString(json);
    }
    
    public String update(int sessionid, int attendeeid) {
        
        JSONObject json = new JSONObject();
        json.put("success", false);
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_UPDATE);
                ps.setInt(1, sessionid);
                ps.setInt(2, attendeeid);
                
                int updateCount = ps.executeUpdate();
                boolean hasResults = updateCount > 0;
                
                if (hasResults) {
            
                    json.put("success", hasResults);
                    
                    json.put("attendeeid", attendeeid);
                    json.put("sessionid", sessionid);

                }
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return JSONValue.toJSONString(json);
    }
    
    public String delete(int sessionid, int attendeeid) {
        
        boolean result = false;
        JSONObject json = new JSONObject();
        json.put("success", false);
        
        PreparedStatement ps = null;
        
        try { 
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_DELETE);
                ps.setInt(1, attendeeid);
                
                int updateCount = ps.executeUpdate();
                
                if (updateCount > 0) {
            
                    result = true;

                }
                json.put("success", true);
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return JSONValue.toJSONString(json);
    }
}
