/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author LT
 */
public class tl180410_GeneralOperations implements GeneralOperations{

    private final static Connection conn = DB.getInstance().getConnection();

    @Override
    public void eraseAll() {
        String[] tables = {"TransportOffer", "CourierRequest","Drive", "Package", "District", 
    "City", "Courier", "Vehicle", "Administrator", "User"};
        for (String table : tables) {
            try ( PreparedStatement ps = conn.prepareStatement("Delete from \"" + table+"\"")) {
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
