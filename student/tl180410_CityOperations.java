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
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author LT
 */
public class tl180410_CityOperations implements CityOperations{

    private final static Connection conn = DB.getInstance().getConnection();
    
    @Override
    public int insertCity(String name, String postalCode) {
        String query1= "select * from city where postalCode=? or name=?";
        try (
            PreparedStatement ps=conn.prepareStatement(query1);){
            ps.setString(1, postalCode);
            ps.setString(2, name);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next())
                    return -1;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query2= "insert into city (name, postalCode)values(?,?)";
        try (
            PreparedStatement ps=conn.prepareStatement(query2,PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, name);
            ps.setString(2, postalCode);
            ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){
                if(rs.next())
                    return rs.getInt(1);
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public int deleteCity(String... names) {
        int countDeletedCities=0;
        if(names==null||names.length==0)return countDeletedCities;
        
        String query="delete from city where name in (";
        for(int i=0;i<names.length;i++){
            if(i>0)query+=",";
            query+="?";
        }
        query+=")";
        
        try (
            PreparedStatement ps=conn.prepareStatement(query);){
            int paramIndex=1;
            for(String name : names){
                ps.setString(paramIndex++, name);
            }
            countDeletedCities = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countDeletedCities;
    }

    @Override
    public boolean deleteCity(int idCity) {
        
        String query="delete from city where idCity=?";
        try (
            PreparedStatement ps=conn.prepareStatement(query);){
            ps.setInt(1,idCity);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        List<Integer> listOfIdCity=new ArrayList<>();
        String query="select idCity from city";
        try (
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);){
            while(rs.next())
                listOfIdCity.add(rs.getInt(1));            
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listOfIdCity;
        
    }
    
}
