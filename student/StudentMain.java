package student;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.*;
import student.tl180410_CityOperations;
import rs.etf.sab.tests.*;



public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new tl180410_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new tl180410_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new tl180410_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new tl180410_CourierRequestOperation();
        GeneralOperations generalOperations = new tl180410_GeneralOperations();
        UserOperations userOperations = new tl180410_UserOperations();
        VehicleOperations vehicleOperations = new tl180410_VehicleOperations();
        PackageOperations packageOperations = new tl180410_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations
        );

        TestRunner.runTests();
        
        /*
        
        //*************POCETAK TESTOVA ZA GRAD*********************
        System.out.println(cityOperations.insertCity("Kragujevac", "27000"));
        System.out.println(cityOperations.insertCity("Krusevac", "28000"));
        
        //System.out.println(cityOperations.deleteCity("Krusevac","Kragujevac"));
        System.out.println(cityOperations.deleteCity(10));
        
        List<Integer> allCities = cityOperations.getAllCities();
        for (Integer city : allCities) {
            System.out.println("idCity: "+city);
        }
        //*************KRAJ TESTOVA ZA GRAD*********************
        */
        /*
        //*************POCETAK TESTOVA ZA KURIRA*********************
        System.out.println(courierOperations.deleteCourier("zika")==true?"uspesno obrisno":"neuspesno obrisano");
        System.out.println(courierOperations.insertCourier("zika","456")==true?"uspesno dodato":"neuspesno dodato");
        List<String> couriers = courierOperations.getAllCouriers();
        for (String courier : couriers) {
        System.out.println("kurir: "+courier);
        }
        System.out.println("za 3:"+courierOperations.getAverageCourierProfit(3));
        System.out.println("za 2:"+courierOperations.getAverageCourierProfit(2));
        System.out.println("za 1:"+courierOperations.getAverageCourierProfit(1));
        System.out.println();
        System.out.println("Kuriri sa statusom 0");
        couriers = courierOperations.getCouriersWithStatus(0);
        for (String courier : couriers) {
        System.out.println("kurir: "+courier);
        }
        System.out.println();
        System.out.println("Kuriri sa statusom 1");
        couriers = courierOperations.getCouriersWithStatus(1);
        for (String courier : couriers) {
        System.out.println("kurir: "+courier);
        }
        //*************KRAJ TESTOVA ZA KURIRA*********************
         */
        
        /*
        //*************POCETAK TESTOVA ZA KORISNIKA*********************
        
        //userOperations.deleteUsers("luka1","luka2","luka3","luka4");
        
        System.out.println();
        System.out.println("Korisnici");
        List<String> allUsers = userOperations.getAllUsers();
        for(String username:allUsers){
            System.out.println(username);
        }
        
        System.out.println();
        System.out.println("Dodavanje korisnika");
        System.out.println(userOperations.insertUser("luka1","luka", "Tomanovic","Mojasifra1" ));
        System.out.println(userOperations.insertUser("luka2","Luka", "tomanovic","Mojasifra2" ));
        System.out.println(userOperations.insertUser("luka3","Luka", "Tomanovic","mojas3" ));
        System.out.println(userOperations.insertUser("luka4","Luka", "Tomanovic","Mojasifra4" ));
        System.out.println(userOperations.insertUser("luka5","Luka", "Tomanovic","Mojasifra5" ));
        
        
        System.out.println();
        System.out.println("Deklarisanje za admina");
        System.out.println(userOperations.declareAdmin("luka3"));
        System.out.println(userOperations.declareAdmin("luka4"));
        System.out.println(userOperations.declareAdmin("luka4"));
        
        System.out.println();
        System.out.println("Korisnici");
        allUsers = userOperations.getAllUsers();
        for(String username:allUsers){
            System.out.println(username);
        }
        System.out.println();
        System.out.println("Broj poslatih paketa");
        System.out.println(userOperations.getSentPackages("luka4","luka5"));
        System.out.println();
        System.out.println("Broj poslatih paketa");
        System.out.println(userOperations.getSentPackages("luka3","luka5"));
        
        //*************KRAJ TESTOVA ZA KORISNIKA*********************
        
        
        */
        /*
        //*************POCETAK TESTOVA ZA OPSTINU*********************
        
        System.out.println();
        System.out.println("Ubacivanje gradova");
        int g1,g2;
        System.out.println(g1=cityOperations.insertCity("Beograd", "11000"));
        System.out.println(g2=cityOperations.insertCity("Novi Sad", "21000"));
        
        int o1,o2,o3,o4,o5,o6;
        o1=districtOperations.insertDistrict("Vozdovac", g1, 500, 1000);
        o2=districtOperations.insertDistrict("Vozdovac", g1, 500, 700);
        o3=districtOperations.insertDistrict("NBG", g1, 500, 900);
        o4=districtOperations.insertDistrict("Karaula", g2, 700, 550);
        o5=districtOperations.insertDistrict("Karaula", g2, 400, 550);
        o6=districtOperations.insertDistrict("Mikula", g2, 450, 550);
        System.out.println();
        System.out.println("Ubacivanje opstina");
        System.out.println("o1:"+o1);
        System.out.println("o2:"+o2);
        System.out.println("o3:"+o3);
        System.out.println("o4:"+o4);
        System.out.println("o5:"+o5);
        System.out.println("o6:"+o6);
        
        System.out.println();
        System.out.println("Ispis svih opstina");
        List<Integer> districts=new ArrayList<>();
        districts=districtOperations.getAllDistricts();
        for (Integer district : districts) {
            System.out.println(district);
        }
        
        
        System.out.println();
        System.out.println("Ispis opstina za grad "+g1);
        districts=new ArrayList<>();
        districts=districtOperations.getAllDistrictsFromCity(g1);
        for (Integer district : districts) {
            System.out.println(district);
        }
        
        System.out.println();
        System.out.println("Ispis opstina za grad "+g2);
        districts=new ArrayList<>();
        districts=districtOperations.getAllDistrictsFromCity(g2);
        for (Integer district : districts) {
            System.out.println(district);
        }
        
        System.out.println();
        System.out.println("Brisanje opstine");
        System.out.println(districtOperations.deleteDistrict(o3));
        System.out.println(districtOperations.deleteDistrict(o2));
        System.out.println(districtOperations.deleteDistricts("Mikula","Sikula"));
           
        System.out.println();
        System.out.println("Ispis opstina za grad "+g1);
        districts=new ArrayList<>();
        districts=districtOperations.getAllDistrictsFromCity(g1);
        for (Integer district : districts) {
            System.out.println(district);
        }
        
        System.out.println();
        System.out.println("Brisanje opstine za grad "+g2);
        System.out.println(districtOperations.deleteAllDistrictsFromCity("Novi Sad"));
        
        System.out.println();
        System.out.println("Ispis svih opstina");
        districts=new ArrayList<>();
        districts=districtOperations.getAllDistricts();
        for (Integer district : districts) {
            System.out.println(district);
        }
        
        System.out.println();
        System.out.println("Brisanje opstine za grad "+g1);
        System.out.println(districtOperations.deleteAllDistrictsFromCity("Beograd"));
        
        System.out.println();
        System.out.println("Ispis svih opstina");
        districts=new ArrayList<>();
        districts=districtOperations.getAllDistricts();
        for (Integer district : districts) {
            System.out.println(district);
        }
        
        cityOperations.deleteCity(g1);
        cityOperations.deleteCity(g2);
        //*************KRAJ TESTOVA ZA OPSTINU*********************
        */
        
        
        /*
        //*************POCETAK TESTOVA ZA VOZILO*********************
        System.out.println();
        System.out.println("Ubacivanje vozila");
        System.out.println(vehicleOperations.insertVehicle("12345",2, new BigDecimal(10.3)));
        System.out.println(vehicleOperations.insertVehicle("12345",0, new BigDecimal(7.43)));
        System.out.println(vehicleOperations.insertVehicle("67890",0, new BigDecimal(9.45)));
        System.out.println(vehicleOperations.insertVehicle("67890",0, new BigDecimal(11.11)));
        
        
        System.out.println();
        System.out.println("Ispis vozila");
        List<String> allVehichles = vehicleOperations.getAllVehichles();
        for (String vehicle : allVehichles) {
            System.out.println(vehicle);            
        }
        
        System.out.println();
        System.out.println("Promena tipa i potrosnje");
        System.out.println(vehicleOperations.changeFuelType("12345", 5));
        System.out.println(vehicleOperations.changeFuelType("67890",1 ));
        System.out.println(vehicleOperations.changeConsumption("12345", new BigDecimal(8.88)));
        
        
        System.out.println();
        System.out.println("Ispis vozila");
        allVehichles = vehicleOperations.getAllVehichles();
        for (String vehicle : allVehichles) {
            System.out.println(vehicle);            
        }
        
        vehicleOperations.deleteVehicles("12345","67890","11111");
        
        System.out.println();
        System.out.println("Ispis vozila");
        allVehichles = vehicleOperations.getAllVehichles();
        for (String vehicle : allVehichles) {
            System.out.println(vehicle);            
        }
        //*************KRAJ TESTOVA ZA VOZILO*********************
        */
        
       /*
        //*************POCETAK TESTOVA ZA ZAHTEVE ZA KURIRA*********************
        
        
        System.out.println();
        System.out.println("Dodavanje korisnika");
        System.out.println(userOperations.insertUser("lukaKurir1","Luka", "Tomanovic","Mojasifra4" ));
        System.out.println(userOperations.insertUser("lukaKurir2","Luka", "Tomanovic","Mojasifra5" ));
        
        System.out.println();
        System.out.println("Ubacivanje vozila");
        System.out.println(vehicleOperations.insertVehicle("12345",2, new BigDecimal(10.3)));
        System.out.println(vehicleOperations.insertVehicle("12345",0, new BigDecimal(7.43)));
        System.out.println(vehicleOperations.insertVehicle("67890",0, new BigDecimal(9.45)));
        System.out.println(vehicleOperations.insertVehicle("67890",0, new BigDecimal(11.11)));
        
        
        System.out.println();
        System.out.println("Korisnici");
        List<String> allUsers = userOperations.getAllUsers();
        for(String username:allUsers){
            System.out.println(username);
        }
        
        System.out.println();
        System.out.println("Ispis vozila");
        List<String> allVehichles = vehicleOperations.getAllVehichles();
        for (String vehicle : allVehichles) {
            System.out.println(vehicle);            
        }
        
        System.out.println();
        System.out.println("Kuriri");
        List<String> couriers = courierOperations.getAllCouriers();
        for (String courier : couriers) {
            System.out.println("kurir: "+courier);
        }
        
        System.out.println(courierRequestOperation.insertCourierRequest("lukaKurir1", "12345"));
        System.out.println(courierRequestOperation.insertCourierRequest("lukaKurir1", "67890"));
        System.out.println(courierRequestOperation.changeVehicleInCourierRequest("lukaKurir1", "67890"));
        
        System.out.println();
        System.out.println("Ispis zahteva");
        List<String> requests = courierRequestOperation.getAllCourierRequests();
        for (String req : requests) {
            System.out.println(req);
        }
        
        System.out.println(courierRequestOperation.grantRequest("lukaKurir1"));
        System.out.println(courierRequestOperation.insertCourierRequest("lukaKurir1", "12345"));
        
        System.out.println();
        System.out.println("Ispis zahteva");
        requests = courierRequestOperation.getAllCourierRequests();
        for (String req : requests) {
            System.out.println(req);
        }
        
        
         //*************KRAJ TESTOVA ZA ZAHTEVE ZA KURIRA*********************
        
        */
      /* 
        //*************POCETAK TESTOVA ZA PAKET*********************
        System.out.println();
        System.out.println("Dodavanje korisnika");
        System.out.println(userOperations.insertUser("posiljalac","Luka", "Tomanovic","Mojasifra4" ));
        System.out.println(userOperations.insertUser("kurir1","Pera", "Peric","Mojasifra5" ));
        System.out.println(userOperations.insertUser("kurir2","Mika", "Mikic","Mojasifra5" ));
        
        
        System.out.println();
        System.out.println("Ubacivanje vozila");
        System.out.println(vehicleOperations.insertVehicle("12345",2, new BigDecimal(10.3)));
        System.out.println(vehicleOperations.insertVehicle("67890",0, new BigDecimal(11.11)));
        
        
        
        System.out.println();
        System.out.println("Ubacivanje kurira");
        System.out.println(courierOperations.insertCourier("kurir1","12345")==true?"uspesno dodat kurir":"kurir nije dodat");
        System.out.println(courierOperations.insertCourier("kurir2","67890")==true?"uspesno dodat kurir":"kurir nije dodat");
        
        System.out.println();
        System.out.println("Ubacivanje gradova");
        int g1,g2;
        System.out.println(g1=cityOperations.insertCity("Beograd", "11000"));
        System.out.println(g2=cityOperations.insertCity("Novi Sad", "21000"));
        
        
        System.out.println();
        System.out.println("Ubacivanje opstina");
        int o1,o2,o3,o4,o5,o6;
        o1=districtOperations.insertDistrict("Vozdovac", g1, 120, 130);
        o2=districtOperations.insertDistrict("Vozdovac", g1, 500, 700);
        o3=districtOperations.insertDistrict("NBG", g1, 120, 125);
        o4=districtOperations.insertDistrict("Karaula", g2, 225, 227);
        o5=districtOperations.insertDistrict("Karaula", g2, 223, 229);
        o6=districtOperations.insertDistrict("Mikula", g2, 220, 225);
        System.out.println(o1);
        System.out.println(o2);
        System.out.println(o3);
        System.out.println(o4);
        System.out.println(o5);
        System.out.println(o6);
        
               
        System.out.println();
        System.out.println("Ubacivanje paketa");
        packageOperations=new tl180410_PackageOperations();    
        int p1,p2,p3;
        System.out.println(p1=packageOperations.insertPackage(o2, o5, "posiljalac", 0, new BigDecimal(100.5)));
        System.out.println(p2=packageOperations.insertPackage(o1, o6, "posiljalac", 1, new BigDecimal(100.5)));
        System.out.println(p3=packageOperations.insertPackage(o1, o6, "posiljala", 1, new BigDecimal(100.5)));
        
        
        System.out.println();
        System.out.println("Ispis paketa odredjenog tipa");
        List<Integer> packages=packageOperations.getAllPackagesWithSpecificType(1);
        for (Integer packageId : packages) {
            System.out.println(packageId);
        }
        
        System.out.println();
        System.out.println("Promena tezine i tipa paketa");
        System.out.println(packageOperations.changeType(p1, 5));
        System.out.println(packageOperations.changeType(p2, 5));
        System.out.println(packageOperations.changeType(p2, 2));        
        System.out.println(packageOperations.changeWeight(p3, new BigDecimal(77.75)));
        System.out.println(packageOperations.changeWeight(p2, new BigDecimal(59.75)));
        
        
        System.out.println();
        System.out.println("Ispis svih paketa");
        packages=packageOperations.getAllPackages();
        for (Integer packageId : packages) {
            System.out.println(packageId);
        }
        
        System.out.println();
        System.out.println("Ispis paketa odredjenog tipa");
        packages=packageOperations.getAllPackagesWithSpecificType(1);
        for (Integer packageId : packages) {
            System.out.println(packageId);
        }
        
        System.out.println();
        System.out.println("Ispis statusa isporuke paketa");
        System.out.println(packageOperations.getDeliveryStatus(p1));
        System.out.println(packageOperations.getDeliveryStatus(p2));  
        System.out.println(packageOperations.getDeliveryStatus(p3));       
        
        
        
        System.out.println();
        System.out.println("Ispis vremena prihvatanja");
        System.out.println(packageOperations.getAcceptanceTime(p1));
        System.out.println(packageOperations.getAcceptanceTime(p2));  
        System.out.println(packageOperations.getAcceptanceTime(p3));  
        
        System.out.println();
        System.out.println("Ispis cene paketa");
        System.out.println(packageOperations.getPriceOfDelivery(p1));
        System.out.println(packageOperations.getPriceOfDelivery(p2));  
        System.out.println(packageOperations.getPriceOfDelivery(p3));  
        
        
        System.out.println();
        System.out.println("Davanje ponuda");
        int off1,off2,off3,off4;
        off1=packageOperations.insertTransportOffer("nema kurira", p2, null);
        off2=packageOperations.insertTransportOffer("kurir1", p1, null);
        off3=packageOperations.insertTransportOffer("kurir1", p2, null);
        off4=packageOperations.insertTransportOffer("kurir2", p2, new BigDecimal(8.54));
        System.out.println(off1);
        System.out.println(off2);
        System.out.println(off3);
        System.out.println(off4);
        
        
        System.out.println();
        System.out.println("Prikaz svih ponuda");
        List<Integer> offers=packageOperations.getAllOffers();
        for (Integer offer : offers) {
            System.out.println(offer);
        }
        
        System.out.println();
        System.out.println("Prikaz ponuda za paket");
        List<PackageOperations.Pair<Integer,BigDecimal>> offersForPackage=packageOperations.getAllOffersForPackage(p2);
        for (PackageOperations.Pair<Integer,BigDecimal> offer : offersForPackage) {
            System.out.println(offer.getFirstParam() + " "+offer.getSecondParam());
        }
        
        System.out.println(packageOperations.acceptAnOffer(off1));
        System.out.println(packageOperations.acceptAnOffer(off2));
        System.out.println(packageOperations.acceptAnOffer(off3));
        System.out.println(packageOperations.acceptAnOffer(off4));
        packageOperations.getDrive("kurir1");
        packageOperations.getDrive("kurir2");
        
        packageOperations.driveNextPackage("kurir1");
        System.out.println();
        System.out.println("Brisanje paketa");
        System.out.println(packageOperations.deletePackage(p2));
        
        */
      
      //***PUBLIC MODULE TEST
      
       /* String courierLastName = "Ckalja";
        String courierFirstName = "Pero";
        String courierUsername = "perkan";
        String password = "sabi2018";
        System.out.println(userOperations.insertUser(courierUsername, courierFirstName, courierLastName, password));
        String licencePlate = "BG323WE";
        int fuelType = 0;
        BigDecimal fuelConsumption = new BigDecimal(8.3D);
        System.out.println(vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption));
        System.out.println(courierRequestOperation.insertCourierRequest(courierUsername, licencePlate));
        System.out.println(courierRequestOperation.grantRequest(courierUsername));
    
        //Assert.assertTrue(this.testHandler.getCourierOperations().getAllCouriers().contains(courierUsername));
        
        String senderUsername = "masa";
        String senderFirstName = "Masana";
        String senderLastName = "Leposava";
        password = "lepasampasta1";
        System.out.println(userOperations.insertUser(senderUsername, senderFirstName, senderLastName, password));
        int cityId = cityOperations.insertCity("Novo Milosevo", "21234");
        int cordXd1 = 10;
        int cordYd1 = 2;
        int districtIdOne = districtOperations.insertDistrict("Novo Milosevo", cityId, cordXd1, cordYd1);
        int cordXd2 = 2;
        int cordYd2 = 10;
        int districtIdTwo = districtOperations.insertDistrict("Vojinovica", cityId, cordXd2, cordYd2);
        
        int type1 = 0;
        BigDecimal weight1 = new BigDecimal(123);
        int packageId1 = packageOperations.insertPackage(districtIdOne, districtIdTwo, courierUsername, type1, weight1);
        BigDecimal packageOnePrice = getPackagePrice(type1, weight1, 
        euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));        
        int offerId = packageOperations.insertTransportOffer(courierUsername, packageId1, new BigDecimal(5));
        System.out.println(packageOperations.acceptAnOffer(offerId));
        
        int type2 = 1;
        BigDecimal weight2 = new BigDecimal(321);
        int packageId2 = packageOperations.insertPackage(districtIdTwo, districtIdOne, courierUsername, type2, weight2);
        BigDecimal packageTwoPrice = getPackagePrice(type2, weight2, 
        euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = packageOperations.insertTransportOffer(courierUsername, packageId2, new BigDecimal(5));
        System.out.println(packageOperations.acceptAnOffer(offerId));
        
        int type3 = 1;
        BigDecimal weight3 = new BigDecimal(222);
        int packageId3 = packageOperations.insertPackage(districtIdTwo, districtIdOne, courierUsername, type3, weight3);
        BigDecimal packageThreePrice = getPackagePrice(type3, weight3, 
        euclidean(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = packageOperations.insertTransportOffer(courierUsername, packageId3, new BigDecimal(5));
        System.out.println(packageOperations.acceptAnOffer(offerId));
        
        //Assert.assertEquals(1L, this.testHandler
        //.getPackageOperations().getDeliveryStatus(packageId1).intValue());
        System.out.println(packageOperations.getDeliveryStatus(packageId1));//treba da bude 1
        
        //Assert.assertEquals(packageId1, this.testHandler
        //.getPackageOperations().driveNextPackage(courierUsername));
        System.out.println(packageOperations.driveNextPackage(courierUsername));// treba da bude jednako packageId1
        
        //Assert.assertEquals(3L, this.testHandler
        //.getPackageOperations().getDeliveryStatus(packageId1).intValue());
        System.out.println(packageOperations.getDeliveryStatus(packageId1));//treba da bude 3
        
        //Assert.assertEquals(2L, this.testHandler
        //.getPackageOperations().getDeliveryStatus(packageId2).intValue());
        System.out.println(packageOperations.getDeliveryStatus(packageId2));//treba da bude 2
        
        //Assert.assertEquals(packageId2, this.testHandler
        //.getPackageOperations().driveNextPackage(courierUsername));
        System.out.println(packageOperations.driveNextPackage(courierUsername));// treba da bude jednako packageId2
        
        //Assert.assertEquals(3L, this.testHandler
        //.getPackageOperations().getDeliveryStatus(packageId2).intValue());
        System.out.println(packageOperations.getDeliveryStatus(packageId2));//treba da bude 3
        
        //Assert.assertEquals(2L, this.testHandler
        //.getPackageOperations().getDeliveryStatus(packageId3).intValue());
        System.out.println(packageOperations.getDeliveryStatus(packageId3));//treba da bude 2
        
        //Assert.assertEquals(packageId3, this.testHandler
        //.getPackageOperations().driveNextPackage(courierUsername));
        System.out.println(packageOperations.driveNextPackage(courierUsername));// treba da bude jednako packageId3
        
        
        //Assert.assertEquals(3L, this.testHandler
        //.getPackageOperations().getDeliveryStatus(packageId3).intValue());
        System.out.println(packageOperations.getDeliveryStatus(packageId3));//treba da bude 3
        
        BigDecimal gain = packageOnePrice.add(packageTwoPrice).add(packageThreePrice);
        
        BigDecimal loss = (new BigDecimal(euclidean(cordXd1, cordYd1, cordXd2, cordYd2) * 4.0D * 15.0D)).multiply(fuelConsumption);
        BigDecimal actual = courierOperations.getAverageCourierProfit(0);
        
        
        //Assert.assertTrue((gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(1.001D))) < 0));
        System.out.println(gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(1.001D))));//treba da bude manje od 0 za true
        

        //Assert.assertTrue((gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(0.999D))) > 0));
        System.out.println(gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(0.999D))));
        */
    }
    static double euclidean(int x1, int y1, int x2, int y2) {
        return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }
  
    static BigDecimal getPackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
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
    
}
