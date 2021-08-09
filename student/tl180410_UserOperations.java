/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;

/**
 *
 * @author LT
 */
public class tl180410_UserOperations implements UserOperations{
    
    private final static Connection conn=DB.getInstance().getConnection();

    public static boolean isPasswordValid(String password){
        if (password==null||password.length() < 8) return false;
        boolean hasDigit = false, hasLetter = false;
        for (int i=0;i< password.length();i++) {

            if (Character.isLetter(password.charAt(i))) hasLetter = true;
            else if (Character.isDigit(password.charAt(i))) hasDigit = true;
            
            if (hasLetter&&hasDigit) return true;
        }        
        return false;
    }
    
    public static boolean userExists(String userName){
        String queryCheckUsername="select count(*) from [User] where username=?";
        try (PreparedStatement ps=conn.prepareStatement(queryCheckUsername);){
            ps.setString(1, userName);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    if(rs.getInt(1)>0)
                        return true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean courierExists(String userName){
        String queryCheckCourier="select count(*) from [Courier] where username=?";
        try(PreparedStatement ps=conn.prepareStatement(queryCheckCourier);) {
            ps.setString(1, userName);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    if(rs.getInt(1)>0)
                        return true; 
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean administratorExists(String userName){
        String queryCheckAdmin="select count(*) from [Administrator] where username=?";
        try(PreparedStatement ps=conn.prepareStatement(queryCheckAdmin);) {
            ps.setString(1, userName);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    if(rs.getInt(1)>0)
                        return true;
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    
    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password) {
        
        // check input values
        if(firstName==null || firstName.length()==0 || !Character.isUpperCase(firstName.charAt(0)) 
                || lastName==null || lastName.length()==0 || !Character.isUpperCase(lastName.charAt(0))
                || !isPasswordValid(password))
            return false;
        
        //input values ok

        if(userExists(userName))
            return false;
        
        
        String queryInsertUser="insert into [User] (username,firstName,lastname, password,countPackagesSent) values (?,?,?,?,0)";
        try (PreparedStatement ps=conn.prepareStatement(queryInsertUser);){
            ps.setString(1, userName);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, password);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;       
        
    }

    @Override
    public int declareAdmin(String userName) {
        if(!userExists(userName))return 2;
        if(administratorExists(userName))return 1; //already admin
        if(courierExists(userName))return -1;//error code is not defined but user cannot be courier and admin at the same time
        
        String queryDeclareAdmin="insert into [Administrator](username) values (?)";
        try(PreparedStatement ps=conn.prepareStatement(queryDeclareAdmin);) {
            ps.setString(1, userName);
            return (ps.executeUpdate()==1)?0:-1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public Integer getSentPackages(String... userNames) {
        if(userNames==null ||userNames.length==0)return null;
        Integer sentPackages=null;
        String userSet="(";
        for (int i=0;i<userNames.length;i++) {
            if(!userExists(userNames[i])) return null;
            if(i>0)userSet+=",";
            userSet+="?";
        }
        userSet+=")";
        
        String query="select sum(countPackagesSent) from [User] where username in "+userSet;
        try (PreparedStatement ps=conn.prepareStatement(query);){
            for (int i=0;i<userNames.length;i++) {
                ps.setString(i+1,userNames[i]);
            }
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    sentPackages=rs.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sentPackages;
    }

    @Override
    public int deleteUsers(String... userNames) {
        if(userNames==null ||userNames.length==0)return 0;
        int countDeletedUsers=0;
        String userSet="(";
        for (int i=0;i<userNames.length;i++) {
            if(i>0)userSet+=",";
            userSet+="?";
        }
        userSet+=")";
        
        String query="delete from [User] where username in "+userSet;
        try (PreparedStatement ps=conn.prepareStatement(query);){
            for (int i=0;i<userNames.length;i++) {
                ps.setString(i+1,userNames[i]);
            }
            countDeletedUsers=ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countDeletedUsers;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> users=new ArrayList<>();
        
        String query="select username from [User]";
        try (Statement stmt=conn.createStatement();
             ResultSet rs=stmt.executeQuery(query);){
            while(rs.next())
                users.add(rs.getString("username"));
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_UserOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }
    
}
