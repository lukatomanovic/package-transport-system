/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.VehicleOperations;

/**
 *
 * @author LT
 */
public class tl180410_VehicleOperations implements VehicleOperations{

    private final static Connection conn=DB.getInstance().getConnection();
    
    public static boolean vehicleExists(String licencePlateNumber){
        String queryCheckExists="select count(*) from Vehicle where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(queryCheckExists);){
            ps.setString(1, licencePlateNumber);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    if(rs.getInt(1)>0)
                        return true;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumption) {
        if(fuelType<0||fuelType>2)return false;
        /*String queryCheckExists="select count(*) from Vehicle where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(queryCheckExists);){
            ps.setString(1, licencePlateNumber);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    if(rs.getInt(1)>0)
                        return false;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        if(vehicleExists(licencePlateNumber))return false;
        
        String queryInsertVehicle="insert into Vehicle(licencePlateNumber,fuelType,fuelConsumtion) values (?,?,?)";
        try (PreparedStatement ps=conn.prepareStatement(queryInsertVehicle);){
           ps.setString(1, licencePlateNumber);
           ps.setInt(2, fuelType);
           ps.setBigDecimal(3, fuelConsumption);
           return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... licencePlateNumbers) {
        if(licencePlateNumbers==null||licencePlateNumbers.length==0) return 0;
        String vehiclesSet="(";
        for(int i=0;i<licencePlateNumbers.length;i++){
            if(i>0)vehiclesSet+=",";
            vehiclesSet+="?";
        }
        vehiclesSet+=")";
        
        String query="delete from Vehicle where licencePlateNumber in "+vehiclesSet;
        
        try (PreparedStatement ps=conn.prepareStatement(query);){
            for(int i=0;i<licencePlateNumbers.length;i++){
                ps.setString(i+1, licencePlateNumbers[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> vehicles=new ArrayList<>();
        String query="select licencePlateNumber from Vehicle";
        try (Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(query)){
            while(rs.next())
                vehicles.add(rs.getString(1));
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vehicles;
    }

    @Override
    public boolean changeFuelType(String licensePlateNumber, int fuelType) {
        /*String query="select * from Vehicle where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setString(1, licensePlateNumber);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()){                
                    rs.updateInt("fuelType", fuelType);
                    rs.updateRow();
                    return true;
                }
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;*/
        if(fuelType<0||fuelType>2)return false;
        String query="update Vehicle set fuelType=? where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setInt(1, fuelType);
            ps.setString(2, licensePlateNumber);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }

    @Override
    public boolean changeConsumption(String licensePlateNumber, BigDecimal fuelConsumption) {
        /*String query="select * from Vehicle where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setString(1, licensePlateNumber);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()){                
                    rs.updateBigDecimal("fuelConsumtion", fuelConsumption);
                    rs.updateRow();
                    return true;
                }
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;*/
        String query="update Vehicle set fuelConsumtion=? where licencePlateNumber=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setBigDecimal(1,fuelConsumption);
            ps.setString(2, licensePlateNumber);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_VehicleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
