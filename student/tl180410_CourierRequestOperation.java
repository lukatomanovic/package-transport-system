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
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CourierRequestOperation;

/**
 *
 * @author LT
 */
public class tl180410_CourierRequestOperation implements CourierRequestOperation{
    
    private static final Connection conn=DB.getInstance().getConnection();
    
    @Override
    public boolean insertCourierRequest(String userName, String licencePlateNumber) {
        
        //check is user already courier
        if(tl180410_UserOperations.courierExists(userName))return false;
        
        //check is user administrator
        if(tl180410_UserOperations.administratorExists(userName))return false;
        
        //check does user exists
        if(!tl180410_UserOperations.userExists(userName))return false;
        
        //check VehicleExists
        if(!tl180410_VehicleOperations.vehicleExists(licencePlateNumber))return false;
        
        //check is vehicle used
        if(vehicleUsed(licencePlateNumber))return false;
        
        //check is request set before
        String queryCheckExists="select count(*) from CourierRequest where username=?";
        try (PreparedStatement ps=conn.prepareStatement(queryCheckExists);){
            ps.setString(1, userName);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    if(rs.getInt(1)>0)
                        return false;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String queryInsertRequest="insert into CourierRequest(username,licencePlateNumber) values (?,?)";
        try (PreparedStatement ps=conn.prepareStatement(queryInsertRequest);){
           ps.setString(1, userName);
           ps.setString(2, licencePlateNumber);
           return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteCourierRequest(String userName) {
        String query="delete from CourierRequest where username=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
           ps.setString(1, userName);
           return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String userName, String licencePlateNumber) {
        
        if(!tl180410_VehicleOperations.vehicleExists(licencePlateNumber))return false;
        if(vehicleUsed(licencePlateNumber))return false;
        
        String query="update CourierRequest set licencePlateNumber=? where username=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setString(1, licencePlateNumber);
            ps.setString(2, userName);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getAllCourierRequests() {
        
        List<String> requests=new ArrayList<>();
        String query="select * from CourierRequest";
        try (Statement ps=conn.createStatement();
                ResultSet rs=ps.executeQuery(query);){
            while(rs.next())
                requests.add(rs.getString("username") + " " +rs.getString("licencePlateNumber"));
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return requests;
    }

    @Override
    public boolean grantRequest(String username) {
        String licencePlateNumber="";
        String queryCheckRequestExists="select licencePlateNumber from CourierRequest where username=?";
        try (PreparedStatement ps=conn.prepareStatement(queryCheckRequestExists);){
            ps.setString(1, username);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    licencePlateNumber=rs.getString(1);
                else return false;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(vehicleUsed(licencePlateNumber))return false;
        CourierOperations courierOperations = new tl180410_CourierOperations();
        if(courierOperations.insertCourier(username, licencePlateNumber)){
            return deleteCourierRequest(username);
        }
        return false;
    }
    
    public static boolean vehicleUsed(String licencePlateNumber){
        String query="select count(*) from Courier where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setString(1, licencePlateNumber);
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
    
}
