/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
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

/**
 *
 * @author LT
 */
public class tl180410_CourierOperations implements CourierOperations{
    private final Connection conn = DB.getInstance().getConnection();
    
    @Override
    public boolean insertCourier(String courierUserName, String licencePlateNumber) {
        String query="{ call spInsertCourier(?,?) }";
        try (
            CallableStatement cs=conn.prepareCall(query);){
            cs.setString(1,courierUserName);
            cs.setString(2, licencePlateNumber);
            return cs.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String courierUserName) {
        String query="delete from Courier where username=?";

        try(PreparedStatement ps=conn.prepareStatement(query);){
            ps.setString(1, courierUserName);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int statusOfCourier) {
        String query="select username from Courier where status=?";
        List<String> couriers=new ArrayList<>();
        try(PreparedStatement ps=conn.prepareStatement(query);) {
            ps.setInt(1,statusOfCourier);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next())
                    couriers.add(rs.getString("username"));
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return couriers;
    }

    @Override
    public List<String> getAllCouriers() {
        String query="select username from Courier order by profit DESC";
        List<String> couriers=new ArrayList<>();
        try (
            Statement stmt =conn.createStatement();
            ResultSet rs=stmt.executeQuery(query);    ){
            while(rs.next())
                couriers.add(rs.getString("username"));
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return couriers;
        
    }

    @Override
    public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
        String query="select coalesce(avg(profit),convert(decimal(10,3),0)) from Courier where countPackagesDelivered>=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setInt(1, numberOfDeliveries);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    return rs.getBigDecimal(1);
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_CourierOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(BigInteger.ZERO);        
    }
    
}
