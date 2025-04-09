package edu.upc.dsa;

import edu.upc.dsa.exceptions.AvioNotFoundException;
import edu.upc.dsa.exceptions.VolNotFoundException;
import edu.upc.dsa.models.Shop;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ShopManagerTest {
    WebManager vm;

    @Before
    public void setUp() {
        this.vm = WebManagerImpl.getInstance();
        this.vm.addAvio("A320", "Airbus A320", "Iberia");
        this.vm.addAvio("A380", "Airbus A380", "Emirates");
        this.vm.addAvio("A350", "Airbus A350", "Lufthansa");
        this.vm.addAvio("B777", "Boeing 777", "Singapore Airlines");
        this.vm.addVol("V123", "08:30", "12:45", "A320", "Barcelona", "Madrid");
        this.vm.addVol("V010", "16:20", "23:50", "A380", "Los Ángeles", "Sídney");
        this.vm.addVol("V012", "11:15", "17:45", "A350", "París", "Seúl");
        this.vm.addVol("V015", "23:55", "06:20", "B777", "Hong Kong", "Londres");
    }

    @After
    public void tearDown() {
        // És un Singleton
        this.vm.clear();
    }

    @Test
    public void addAvions() {
        Assert.assertEquals(4, vm.sizeAvions());
        Assert.assertEquals(4, vm.sizeVols());
        this.vm.addAvio("C172", "Cessna 172", "Private Jet");
        this.vm.addVol("V014", "09:10", "10:40", "C172", "Las Vegas", "Los Ángeles");
        Assert.assertEquals(5, vm.sizeAvions());
        Assert.assertEquals(5, vm.sizeVols());
    }
    @Test
    public void addVols() {
        Assert.assertEquals(4, vm.sizeAvions());
        Assert.assertEquals(4, vm.sizeVols());
        this.vm.addAvio("C172", "Cessna 172", "Private Jet");
        this.vm.addVol("V014", "09:10", "10:40", "C172", "Las Vegas", "Los Ángeles");
        Assert.assertEquals(5, vm.sizeAvions());
        Assert.assertEquals(5, vm.sizeVols());
    }

    @Test
    public void getVolTest() throws Exception {
        Assert.assertEquals(4, vm.sizeVols());

        Shop v = this.vm.getVol("V010");
        Assert.assertEquals("Los Ángeles", v.getOrigen());
        Assert.assertEquals( "A380", v.getAvio().getId());

        v = this.vm.getVol2("V010");
        Assert.assertEquals("Los Ángeles", v.getOrigen());
        Assert.assertEquals( "A380", v.getAvio().getId());

        Assert.assertThrows(VolNotFoundException.class, () ->
                this.vm.getVol2("XXXXXXX"));

    }
    @Test
    public void getAvioTest() throws Exception {
        Assert.assertEquals(4, vm.sizeAvions());

        Avio a = this.vm.getAvio("A350");
        Assert.assertEquals("Airbus A350", a.getModel());
        Assert.assertEquals( "Lufthansa", a.getCompanyia());

        a = this.vm.getAvio2("A350");
        Assert.assertEquals("Airbus A350", a.getModel());
        Assert.assertEquals( "Lufthansa", a.getCompanyia());

        Assert.assertThrows(AvioNotFoundException.class, () ->
                this.vm.getAvio2("XXXXXXX"));

    }

    @Test
    public void getAvionsTest() {
        Assert.assertEquals(4, vm.sizeAvions());
        List<Avio> avions  = vm.findAllAvions();

        Avio a = avions.get(0);
        Assert.assertEquals("Airbus A320", a.getModel());
        Assert.assertEquals("Iberia", a.getCompanyia());

        a = avions.get(1);
        Assert.assertEquals("Airbus A380", a.getModel());
        Assert.assertEquals("Emirates", a.getCompanyia());

        a = avions.get(2);
        Assert.assertEquals("Airbus A350", a.getModel());
        Assert.assertEquals("Lufthansa", a.getCompanyia());

        a = avions.get(3);
        Assert.assertEquals("Boeing 777", a.getModel());
        Assert.assertEquals("Singapore Airlines", a.getCompanyia());

        Assert.assertEquals(4, vm.sizeAvions());

    }
    @Test
    public void getVolsTest() {
        Assert.assertEquals(4, vm.sizeVols());
        List<Shop> shops = vm.findAllVols();

        Shop v = shops.get(0);
        Assert.assertEquals("V123", v.getId());
        Assert.assertEquals("08:30", v.getHoraSortida());
        Assert.assertEquals("12:45", v.getHoraArribada());
        Assert.assertEquals("A320", v.getAvio().getId());
        Assert.assertEquals("Barcelona", v.getOrigen());
        Assert.assertEquals("Madrid", v.getDesti());

        v = shops.get(1);
        Assert.assertEquals("V010", v.getId());
        Assert.assertEquals("16:20", v.getHoraSortida());
        Assert.assertEquals("23:50", v.getHoraArribada());
        Assert.assertEquals("A380", v.getAvio().getId());
        Assert.assertEquals("Los Ángeles", v.getOrigen());
        Assert.assertEquals("Sídney", v.getDesti());

        v = shops.get(2);
        Assert.assertEquals("V012", v.getId());
        Assert.assertEquals("11:15", v.getHoraSortida());
        Assert.assertEquals("17:45", v.getHoraArribada());
        Assert.assertEquals("A350", v.getAvio().getId());
        Assert.assertEquals("París", v.getOrigen());
        Assert.assertEquals("Seúl", v.getDesti());

        v = shops.get(3);
        Assert.assertEquals("V015", v.getId());
        Assert.assertEquals("23:55", v.getHoraSortida());
        Assert.assertEquals("06:20", v.getHoraArribada());
        Assert.assertEquals("B777", v.getAvio().getId());
        Assert.assertEquals("Hong Kong", v.getOrigen());
        Assert.assertEquals("Londres", v.getDesti());

        Assert.assertEquals(4, vm.sizeAvions());

    }

    @Test
    public void updateVolTest() {
        Assert.assertEquals(4, vm.sizeVols());
        Shop v = this.vm.getVol("V010");
        Assert.assertEquals("Los Ángeles", v.getOrigen());
        Assert.assertEquals( "A380", v.getAvio().getId());

        v.setOrigen("Chicago");
        this.vm.updateVol(v);

        v = this.vm.getVol("V010");
        Assert.assertEquals("Chicago", v.getOrigen());
        Assert.assertEquals( "A380", v.getAvio().getId());
    }

    @Test
    public void updateAvioTest() {
        Assert.assertEquals(4, vm.sizeAvions());
        Avio a = this.vm.getAvio("A380");
        Assert.assertEquals("Airbus A380", a.getModel());
        Assert.assertEquals( "Emirates", a.getCompanyia());

        a.setCompanyia("Jan's Air");
        this.vm.updateAvio(a);

        a = this.vm.getAvio("A380");
        Assert.assertEquals("Airbus A380", a.getModel());
        Assert.assertEquals( "Jan's Air", a.getCompanyia());
    }


    @Test
    public void deleteVolTest() {
        Assert.assertEquals(4, vm.sizeVols());
        this.vm.deleteVol("V010");
        Assert.assertEquals(3, vm.sizeVols());

        Assert.assertThrows(VolNotFoundException.class, () ->
                this.vm.getVol2("V010"));
    }

    @Test
    public void deleteAvioTest() {
        Assert.assertEquals(4, vm.sizeAvions());
        this.vm.deleteAvio("A380");
        Assert.assertEquals(3, vm.sizeAvions());

        Assert.assertThrows(AvioNotFoundException.class, () ->
                this.vm.getAvio2("A380"));
    }
}
