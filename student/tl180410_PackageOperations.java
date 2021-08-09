/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;

/**
 *
 * @author LT
 */
public class tl180410_PackageOperations implements PackageOperations{
    
    private static final Connection conn = DB.getInstance().getConnection();
    
    //for offers
    public static class tl180410_Pair<Integer, BigDecimal> implements PackageOperations.Pair<Integer, BigDecimal>{
        
        private Integer idTransportOffer;
        private BigDecimal pricePercentage;

        public tl180410_Pair(Integer idTransportOffer, BigDecimal pricePercentage) {
            this.idTransportOffer = idTransportOffer;
            this.pricePercentage = pricePercentage;
        }

        @Override
        public Integer getFirstParam() {
            return this.idTransportOffer;
        }

        @Override
        public BigDecimal getSecondParam() {
            return this.pricePercentage;
        }

        public static boolean equals(Pair a, Pair b) {
            return a.getFirstParam().equals(b.getFirstParam()) && a.getSecondParam().equals(b.getSecondParam());
        }
        
        
        
    }

    public static boolean packageExists(int packageId){
        String query="select * from Package where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(query);){
            ps.setInt(1, packageId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    return true;
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean changeUserSentPackagesNumber(String username, int difference){
        String updatePackageSentCount="update [User] set countPackagesSent=countPackagesSent+? where username=?";
        try (PreparedStatement ps=conn.prepareStatement(updatePackageSentCount);){
            ps.setInt(1, difference);
            ps.setString(2, username);
            return ps.executeUpdate()==1;            
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }  
    
    public static BigDecimal calculatePackagePriceMoja(int packageType,BigDecimal packageWeight, double distance,BigDecimal pricePercentage){
        BigDecimal basePrice=new BigDecimal(BigInteger.ZERO);
        BigDecimal weightFact=new BigDecimal(BigInteger.ZERO);
        BigDecimal pricePerKG=new BigDecimal(BigInteger.ZERO);
        BigDecimal priceFact=pricePercentage.divide(new BigDecimal(100)).add(new BigDecimal(BigInteger.ONE));
        switch(packageType){
            case 0:{
                basePrice=new BigDecimal(10);
                weightFact=new BigDecimal(0);
                pricePerKG=new BigDecimal(0);
                break;
            }
            case 1:{
                basePrice=new BigDecimal(25);
                weightFact=new BigDecimal(1);
                pricePerKG=new BigDecimal(100);
                break;
            }
            case 2:{
                basePrice=new BigDecimal(75);
                weightFact=new BigDecimal(2);
                pricePerKG=new BigDecimal(300);
            }
        }
        
        //return (basePrice+(weightFact*packageWeight.doubleValue())*pricePerKG)*distance*priceFact;
        return basePrice.add(weightFact.multiply(packageWeight).multiply(pricePerKG)).multiply(new BigDecimal(distance));
    }
    
    // uzeto iz testova kako bi bila potpuno ista vrednost
    static BigDecimal calculatePackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
        percentage = percentage.divide(new BigDecimal(100));
        switch (type) {
          case 0:
            return (new BigDecimal(10.0D * distance)).multiply(percentage.add(new BigDecimal(1)));
          case 1:
            return (new BigDecimal((25.0D + weight.doubleValue() * 100.0D) * distance)).multiply(percentage.add(new BigDecimal(1)));
          case 2:
            return (new BigDecimal((75.0D + weight.doubleValue() * 300.0D) * distance)).multiply(percentage.add(new BigDecimal(1)));
        } 
        return null;
    }
    
    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
        if(packageType<0||packageType>2) return -1;
        if(!tl180410_DistrictOperations.districtExists(districtFrom)||!tl180410_DistrictOperations.districtExists(districtTo)
                ||!tl180410_UserOperations.userExists(userName))return -1;
        
        String queryInsertPackage="insert into Package (idDistrictFrom,idDistrictTo,type,weight,senderUserName,courierUsername,status,timePackageAccepted,price) "
                + "values (?,?,?,?,?,null,0,null,null)";
        try(PreparedStatement ps=conn.prepareStatement(queryInsertPackage,PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, districtFrom);
            ps.setInt(2, districtTo);
            ps.setInt(3, packageType);
            ps.setBigDecimal(4, weight);
            ps.setString(5, userName);
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys();){
                if(rs.next()){
                    int ret=rs.getInt(1);
                    changeUserSentPackagesNumber(userName,1);
                    return ret;
                }
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    @Override
    public boolean changeType(int packageId, int newType) {
        if(newType<0||newType>2) return false;
        
        String queryChangeType="update Package set type=? where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryChangeType);){
            ps.setInt(1, newType);
            ps.setInt(2, packageId);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }    
    
    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {      
        String queryChangeType="update Package set weight=? where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryChangeType);){
            ps.setBigDecimal(1, newWeight);
            ps.setInt(2, packageId);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public List<Integer> getAllPackages() {
        String querySelectAllPackages="select idPackage from Package";
        List<Integer> packages=new ArrayList<>();
        try (Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(querySelectAllPackages);){
            while(rs.next())
                packages.add(rs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packages;
    }
    
    @Override
    public List<Integer> getAllPackagesWithSpecificType(int type) {
        List<Integer> packages=new ArrayList<>();
        String querySelectAllPackages="select idPackage from Package where type=?";
        try (PreparedStatement ps=conn.prepareStatement(querySelectAllPackages);){
            ps.setInt(1, type);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next())
                    packages.add(rs.getInt(1));
            }
            catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packages;
    }

    @Override
    public Integer getDeliveryStatus(int packageId) {
        String queryGetDeliveryStatus="select status from Package where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryGetDeliveryStatus)){
            ps.setInt(1, packageId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    return rs.getInt(1);
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public Date getAcceptanceTime(int packageId) {
        String queryGetDeliveryStatus="select timePackageAccepted from Package where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryGetDeliveryStatus)){
            ps.setInt(1, packageId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())                    
                    return rs.getDate(1);
                
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        String queryGetDeliveryStatus="select price from Package where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryGetDeliveryStatus)){
            ps.setInt(1, packageId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())                    
                    return rs.getBigDecimal(1);
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    


    @Override
    public int insertTransportOffer(String couriersUserName, int packageId, BigDecimal pricePercentage) {
        if(!packageExists(packageId)||!tl180410_UserOperations.courierExists(couriersUserName))return -1;
        if(pricePercentage==null){
            pricePercentage=new BigDecimal( -10D + new Random().nextDouble()*20D);
        }
        String queryOffer="insert into TransportOffer(idPackage,username,pricePercentage)values(?,?,?)";
        try (PreparedStatement ps=conn.prepareStatement(queryOffer,PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setInt(1,packageId);
            ps.setString(2,couriersUserName);
            ps.setBigDecimal(3, pricePercentage);
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys();){
                if(rs.next())
                    return rs.getInt(1);
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    @Override
    public List<Integer> getAllOffers() {
        String querySelectAllTransportOffers="select idTransportOffer from TransportOffer";
        List<Integer> offers=new ArrayList<>();
        try (Statement stmt=conn.createStatement();
                ResultSet rs=stmt.executeQuery(querySelectAllTransportOffers);){
            while(rs.next())
                offers.add(rs.getInt(1));
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return offers;
    }
    
    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        List<Pair<Integer, BigDecimal>> offers=new ArrayList<>();
        String querySelectTransportOffers="select idTransportOffer,pricePercentage from TransportOffer where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(querySelectTransportOffers);){
            ps.setInt(1, packageId);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next())
                    offers.add(new tl180410_Pair<>(rs.getInt(1),rs.getBigDecimal(2)));
            }
            catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return offers;
    }

    @Override
    public boolean deletePackage(int packageId) {
        String senderUserName;
        String queryGetSender="select senderUserName from Package where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryGetSender);){
            ps.setInt(1, packageId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next())
                    senderUserName=rs.getString(1);
                else return false;
            }
            catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        String queryDeletePackage="delete from Package where idPackage=?";
        try (PreparedStatement ps=conn.prepareStatement(queryDeletePackage);){
            ps.setInt(1, packageId);
            if(ps.executeUpdate()==1){
                if(changeUserSentPackagesNumber(senderUserName,-1))
                    return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
        
    @Override
    public boolean acceptAnOffer(int offerId) {
        //trigger is triggered on updating field timePackageAccepted in table Package
        int packageId;
        BigDecimal packageWeight;
        int packageType;
        BigDecimal pricePercentage;
        int x1Cord,y1Cord,x2Cord,y2Cord;
        String courierUsername;
        
        String queryGetOffer="select Package.idPackage,Package.[weight],Package.[type], TransportOffer.username, TransportOffer.pricePercentage, d1.xCord,d1.yCord,d2.xCord,d2.yCord from TransportOffer join Package on TransportOffer.idPackage=Package.idPackage \n" +
                            "join District d1 on Package.idDistrictFrom=d1.idDistrict join District d2 on Package.idDistrictTo=d2.idDistrict where idTransportOffer=?";
        try (PreparedStatement ps=conn.prepareStatement(queryGetOffer);){
            ps.setInt(1, offerId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()){
                    packageId=rs.getInt(1);
                    packageWeight=rs.getBigDecimal(2);
                    packageType=rs.getInt(3);
                    courierUsername=rs.getString(4);
                    pricePercentage=rs.getBigDecimal(5);
                    x1Cord=rs.getInt(6);
                    y1Cord=rs.getInt(7);
                    x2Cord=rs.getInt(8);
                    y2Cord=rs.getInt(9);
                }
                else return false;
            }
            catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        double distance=Math.sqrt(Math.pow(x1Cord - x2Cord,2) + Math.pow(y1Cord - y2Cord,2));
        
        BigDecimal totalPrice=calculatePackagePrice(packageType, packageWeight, distance, pricePercentage);
        
        String queryUpdatePackage = "update Package set courierUsername=?, status=1, timePackageAccepted=GETDATE(), price=? where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(queryUpdatePackage)) {
            ps.setString(1, courierUsername);
            ps.setBigDecimal(2, totalPrice);
            ps.setInt(3, packageId);
            return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;        
        
    }

    @Override
    public List<Integer> getDrive(String username) {
        List<Integer> packages = new ArrayList<>();
        String query = "select * from Package where courierUsername=? and status=2";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next()){
                    packages.add(rs.getInt("idPackage"));
                }
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }            
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        /*if(packages.size()>0){
            
            String packagesSet="(";
            for(int i=0;i<packages.size();i++){
                if(i>0)packagesSet+=",";
                packagesSet+="?";
            }
            packagesSet+=")";
            String queryUpdatePackageStatus = "update Package set status=2 where idPackage in "+packagesSet;
            try (PreparedStatement ps = conn.prepareStatement(queryUpdatePackageStatus)) {
                for(int i=0;i<packages.size();i++){
                    ps.setInt(i+1, packages.get(i));
                }
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
           
            String queryUpdateCourierStatus = "update Courier set status=1 where username=?";
            try (PreparedStatement ps = conn.prepareStatement(queryUpdateCourierStatus)) {
                ps.setString(1, username);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }*/
        if(packages.size()==0){
            /*String queryInDriveCheck="select * from Drive where username=?";
            try(PreparedStatement ps=conn.prepareStatement(queryInDriveCheck);) {
                ps.setString(1, username);
                try(ResultSet rs = ps.executeQuery();){
                    if(rs.next()){
                        return packages;
                    }
                    else
                        return null;                  
                }
                catch (SQLException ex) {
                    Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex); 
                }             
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            return null;
        }        
        return packages;
    }

    
    
    @Override
    public int driveNextPackage(String courierUserName) {
        
        int pickedPackageId=-1;
        boolean lastPackageInDrive=false;
        int status;
        //provera da li je voznja pocela ako jeste uzme paket
        String queryNextPackage="select * from Package where courierUsername=? and status<3 order by status desc, timePackageAccepted";
        try(PreparedStatement ps=conn.prepareStatement(queryNextPackage);) {
            ps.setString(1, courierUserName);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()){
                    pickedPackageId=rs.getInt("idPackage");
                    status=rs.getInt("status");
                }
                else
                    return -1;                  
            }
            catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }             
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        
        if(status==1){
            //voznja nije pocela startuj voznju
            // promena statusa paketa na pokupljen za sve pakete u voznji
            String queryUpdatePackageStatus = "update Package set status=2 where courierUsername=? and status=1";
            try (PreparedStatement ps = conn.prepareStatement(queryUpdatePackageStatus)) {
                ps.setString(1, courierUserName);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }
            String queryUpdateCourierStatus = "update Courier set status=1 where username=?";
            try (PreparedStatement ps = conn.prepareStatement(queryUpdateCourierStatus)) {
                ps.setString(1, courierUserName);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        // promena statusa paketa na isporucen
        String queryUpdatePackageStatus = "update Package set status=3 where idPackage=?";
        try (PreparedStatement ps = conn.prepareStatement(queryUpdatePackageStatus)) {
            ps.setInt(1, pickedPackageId);
            ps.executeUpdate();
        } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
        }
        
        
        
        
        //ubaci u voznju paket        
        String queryInsertPackageDrive = "insert into Drive(username,idPackage) values(?,?)";
        try (PreparedStatement ps = conn.prepareStatement(queryInsertPackageDrive)) {
            ps.setString(1, courierUserName);
            ps.setInt(2, pickedPackageId);
            ps.executeUpdate();
        } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
        }
        
        //proveri da li je poslednja
        queryNextPackage="select * from Package where courierUsername=? and status=2";
        try(PreparedStatement ps=conn.prepareStatement(queryNextPackage);) {
            ps.setString(1, courierUserName);
            try(ResultSet rs = ps.executeQuery();){
                if(!rs.next()){
                    lastPackageInDrive=true;
                }                   
            }
            catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return -2;
            }             
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return -2;
        }
        
        //update kurir broj isporucenih
        String queryUpdateCourierPackagesDelivered = "update Courier set countPackagesDelivered=countPackagesDelivered+1 where username=?";
        try (PreparedStatement ps = conn.prepareStatement(queryUpdateCourierPackagesDelivered)) {
                ps.setString(1, courierUserName);
                ps.executeUpdate();
        } catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //racunanje profita
        if(lastPackageInDrive)finishDrive(courierUserName);
        return pickedPackageId;
        
    }
    private volatile int startX=-1,startY=-1; 
    
    public boolean finishDrive(String courierUserName){
        //postavi status kuriru da vise ne vozi
        BigDecimal totalDistance=new BigDecimal(BigInteger.ZERO);
        BigDecimal totalIncome=new BigDecimal(BigInteger.ZERO);
        String queryUpdateCourierStatus = "update Courier set status=0 where username=?";
        try (PreparedStatement ps = conn.prepareStatement(queryUpdateCourierStatus)) {
            ps.setString(1, courierUserName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        String querySelectPackagesInDrive="SELECT d1.xCord, d1.yCord,d2.xCord,d2.yCord, Package.price from Drive join Package on Drive.idPackage=Package.idPackage join District d1 on Package.idDistrictFrom=d1.idDistrict join District d2 on Package.idDistrictTo=d2.idDistrict \n" +
                                            "where Drive.username=? order by Drive.idDrive";
        try (PreparedStatement ps = conn.prepareStatement(querySelectPackagesInDrive)) {
            ps.setString(1, courierUserName);
            try(ResultSet rs=ps.executeQuery();){
                while(rs.next()){
                    if(startX>0){
                        totalDistance=totalDistance.add(new BigDecimal(Math.sqrt(Math.pow(rs.getInt(1) - startX,2) + Math.pow(rs.getInt(2) -startY,2))));
                    }
                    totalDistance=totalDistance.add(new BigDecimal(Math.sqrt(Math.pow(rs.getInt(1) - rs.getInt(3),2) + Math.pow(rs.getInt(2) - rs.getInt(4),2))));
                    startX=rs.getInt(3);
                    startY=rs.getInt(4);
                    
                    totalIncome=totalIncome.add(rs.getBigDecimal(5));
                }
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        BigDecimal fuelConsumtion=new BigDecimal(BigInteger.ZERO);
        int fuelType=0;
        String queryFuelPricePerDrive="select fuelType,fuelConsumtion from Courier join Vehicle on Courier.licencePlateNumber=Vehicle.licencePlateNumber where username=?";
        try (PreparedStatement ps = conn.prepareStatement(queryFuelPricePerDrive)) {
            ps.setString(1, courierUserName);
            try(ResultSet rs=ps.executeQuery();){
                if(rs.next()){
                    fuelType=rs.getInt(1);
                    fuelConsumtion=rs.getBigDecimal(2);
                }
            }catch (SQLException ex) {
                Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        
        double fuelPrice=0;
        switch(fuelType){
            case 0:{
                fuelPrice=15;
                break;
            }
            case 1:{
                fuelPrice=36;
                break;
            }
            case 2:{
                fuelPrice=32;
                break;
            }
        }
        BigDecimal loss=fuelConsumtion.multiply(new BigDecimal(fuelPrice)).multiply(totalDistance);
        BigDecimal earnedMoney=totalIncome.subtract(loss);
        
        String queryUpdateCourierProfit="update Courier set profit=profit+? where username=?";
        try (PreparedStatement ps=conn.prepareStatement(queryUpdateCourierProfit);){
            ps.setBigDecimal(1, earnedMoney);
            ps.setString(2, courierUserName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String queryDeleteLastDrive="delete from Drive where username=?";
        try (PreparedStatement ps=conn.prepareStatement(queryDeleteLastDrive);){
            ps.setString(1, courierUserName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(tl180410_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
        
    }
    
    


    
}
