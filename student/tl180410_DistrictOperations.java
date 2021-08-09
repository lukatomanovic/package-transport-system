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
import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author LT
 */
public class tl180410_DistrictOperations implements DistrictOperations{
    
    private static final Connection conn = DB.getInstance().getConnection();

    public static boolean districtExists(String name, int cityId){
        String query="select count(*) from District where idCity=? and name=?";
        try(PreparedStatement ps=conn.prepareStatement(query);) {
            ps.setInt(1, cityId);
            ps.setString(2, name);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    return rs.getInt(1)>0;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        
    }
    
    public static boolean districtExists(int idDistrict){
        String query="select count(*) from District where idDistrict=?";
        try(PreparedStatement ps=conn.prepareStatement(query);) {
            ps.setInt(1, idDistrict);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next())
                    return rs.getInt(1)>0;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        
    }
    
    @Override
    public int insertDistrict(String name, int cityId, int xCord, int yCord) {
        if(districtExists(name, cityId))return -1;
        
        String query="insert into District(name,idCity,xCord,yCord)values(?,?,?,?)";
        try (PreparedStatement ps=conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, name);
            ps.setInt(2, cityId);
            ps.setInt(3, xCord);
            ps.setInt(4, yCord);
            ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys();){
                if(rs.next())
                    return rs.getInt(1);
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int deleteDistricts(String... names) {
        if(names==null||names.length==0)return 0;
        int deletedCount=0;
        
        String districtSet="(";
        for(int i=0;i<names.length;i++){
            if(i>0)districtSet+=",";
            districtSet+="?";
        }
        districtSet+=")";
        
        String query="delete from District where name in "+districtSet;
        try (PreparedStatement ps=conn.prepareStatement(query);){
            for(int i=0;i<names.length;i++){
                ps.setString(i+1, names[i]);
            }
            deletedCount= ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deletedCount;
        
    }

    @Override
    public boolean deleteDistrict(int idDistrict) {
        String query="delete from District where idDistrict=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setInt(1, idDistrict);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String nameOfTheCity) {
        int deletedCount=0;
        List<Integer> citiesIds=new ArrayList<>();
        String queryFindCityId="select idCity from City where name=?";
        try (PreparedStatement ps=conn.prepareStatement(queryFindCityId);){
            ps.setString(1, nameOfTheCity);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next())
                    citiesIds.add(rs.getInt(1));
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(citiesIds.size()==0)return 0;
        
        String citiesSet="(";
        for(int i=0;i<citiesIds.size();i++){
            if(i>0)citiesSet+=",";
            citiesSet+="?";
        }
        citiesSet+=")";
        
        String queryDeleteDistricts="delete from District where idCity in "+citiesSet;
        try (PreparedStatement ps=conn.prepareStatement(queryDeleteDistricts);){
            for(int i=0;i<citiesIds.size();i++){
                ps.setInt(i+1, citiesIds.get(i));
            }
            deletedCount= ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deletedCount;
        
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int idCity) {
        String query0= "select * from city where idCity=?";
        try (
            PreparedStatement ps=conn.prepareStatement(query0);){
            ps.setInt(1, idCity);
            try(ResultSet rs=ps.executeQuery()){
                if(!rs.next())
                    return null;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        List<Integer> districts=new ArrayList<>();
        String query="select idDistrict from District where idCity=?";
        try(PreparedStatement ps=conn.prepareStatement(query);) {
            ps.setInt(1, idCity);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next())
                    districts.add(rs.getInt(1));
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return districts;
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> districts=new ArrayList<>();
        String query="select idDistrict from District";
        try(Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(query);) {
            while(rs.next()){
                districts.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_DistrictOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return districts;
    }
    
    
}
