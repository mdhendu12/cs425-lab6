package edu.jsu.mcis.lab6.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class AttendeeDAO {
    
    private final DAOFactory daoFactory;
    
    
    public static final String QUERY_LIST = "SELECT * FROM attendee";
    public static final String QUERY_LIST_BY_ID = "SELECT * FROM attendee WHERE id=?";
    public static final String QUERY_CREATE = "INSERT INTO attendee (firstname, lastname, displayname) VALUES (?, ?, ?)";
    public static final String QUERY_UPDATE = "UPDATE attendee SET firstname=?, lastname=?, displayname=? WHERE id = ?";
    
    AttendeeDAO(DAOFactory dao) {
        this.daoFactory = dao;
    }
    
    
    public String list() {
        
        JSONObject json = new JSONObject();
        json.put("success", false);
        JSONArray records = new JSONArray();
        
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
                    
                    JSONObject record = new JSONObject();
                    
                    record.put("id", rs.getInt("id"));
                    record.put("firstname", rs.getString("firstname"));
                    record.put("lastname", rs.getString("lastname"));
                    record.put("displayname", rs.getString("displayname"));

                    String regNum = generateRegNum(rs.getInt("id"));
                    record.put("registrationcode", regNum);
                    
                    records.add(record);
                }
                
                json.put("attendees", records);

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
    
    public String list(int attendeeid) {
        
        JSONObject json = new JSONObject();
        json.put("success", false);

        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try { 
            ps = conn.prepareStatement(QUERY_LIST_BY_ID);
            ps.setInt(1, attendeeid);

            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();

                json.put("success", hasresults);


                if (rs.next()) {
                    
                    json.put("id", rs.getInt("id"));
                    json.put("firstname", rs.getString("firstname"));
                    json.put("lastname", rs.getString("lastname"));
                    json.put("displayname", rs.getString("displayname"));

                    String regNum = generateRegNum(rs.getInt("id"));
                    json.put("registrationcode", regNum);
                    
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
    
    public String create(HashMap<String,String> hm) {
        
        JSONObject json = new JSONObject();
        json.put("success", false);
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_CREATE, Statement.RETURN_GENERATED_KEYS);
                
                ps.setString(1, hm.get("firstname"));
                ps.setString(2, hm.get("lastname"));
                ps.setString(3, hm.get("displayname"));
                
                int updateCount = ps.executeUpdate();
                boolean hasResults = updateCount > 0;
                
                ps = conn.prepareStatement("SELECT * FROM attendee WHERE firstname=?"
                        + " AND lastname=? AND displayname=?");
                
                ps.setString(1, hm.get("firstname"));
                ps.setString(2, hm.get("lastname"));
                ps.setString(3, hm.get("displayname"));
                
                hasResults = hasResults && ps.execute();
                
                if (hasResults) {
                    
                    rs = ps.getResultSet();
                    
                    json.put("success", hasResults);
                    
                    if (rs.next()) {
                        json.put("id", rs.getInt("id"));
                    }
                        
                    json.put("firstname", hm.get("firstname"));
                    json.put("lastname", hm.get("lastname"));
                    json.put("displayname", hm.get("displayname"));
                        

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

    public String update(HashMap<String,String> hm) {
        
        JSONObject json = new JSONObject();
        json.put("success", false);
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_UPDATE);

                ps.setString(1, hm.get("firstname"));
                ps.setString(2, hm.get("lastname"));
                ps.setString(3, hm.get("displayname"));
                ps.setString(4, hm.get("attendeeid"));
                
                int updateCount = ps.executeUpdate();
                boolean hasResults = updateCount > 0;
                
                if (hasResults) {
                    json.put("success", hasResults);
                    
                    json.put("attendeeid", hm.get("attendeeid"));
                    json.put("firstname", hm.get("firstname"));
                    json.put("lastname", hm.get("lastname"));
                    json.put("displayname", hm.get("displayname"));

                }
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return JSONValue.toJSONString(json);
    }

    private String generateRegNum(int attendeeid) {
        
        String q = "SELECT CONCAT(\"R\", LPAD(?, 6, 0)) AS num FROM registration";
        String regNum = null;
        
        Connection conn = daoFactory.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            ps = conn.prepareStatement(q);
            ps.setInt(1, attendeeid);

            boolean hasresults = ps.execute();

            if (hasresults) {

                rs = ps.getResultSet();
                if (rs.next()) { regNum = rs.getString("num"); }
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
        
        return regNum;
    }
}
