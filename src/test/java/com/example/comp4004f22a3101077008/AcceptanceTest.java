package com.example.comp4004f22a3101077008;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class AcceptanceTest {
    @Autowired
    GameData gd;
    @Autowired
    GameLogic game;
    @LocalServerPort
    int port;
    List<WebDriver> webDrivers = new ArrayList<>();

    @BeforeEach
    public void initChromeDrivers() throws InterruptedException {
        Path path = Paths.get("src", "web-driver", "chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", path.toAbsolutePath().toString());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");


        for (int index = 0; index < 4; index ++) {
            WebDriver webDriver = new ChromeDriver(options);
            webDrivers.add(webDriver);
            webDriver.get("http://localhost:" + port);
            Thread.sleep(2000);
            webDriver.findElement(By.id("usernameBtn")).click();
        }

        Thread.sleep(1000);

    }
    @AfterEach
    public void tearDownChromeDrivers() {
        for (int index = 0; index < 4; index ++) {
            WebDriver webDriver = webDrivers.get(index);
            webDriver.close();
        }
    }

    public void setPlayerCards(String[] cardsStr) {
        for (int index = 0; index < 4; index ++) {
            Player player = gd.getPlayers().get(index);
            player.setCards(new ArrayList<>());
            String cardsStrOfPlayer = cardsStr[index];
            for (String cardStr: cardsStrOfPlayer.split(" ")) {
                String[] cardStrSplits = cardStr.split("");
                player.addCard(new Card(cardStrSplits[0], cardStrSplits[1]));
            }
        }
    }

    public void setPileCards(String[] cardsStr) {
        ArrayList<Card> pileCards = new ArrayList<>();
        for (String cardStr: cardsStr) {
            String[] cardStrSplits = cardStr.split("");
            pileCards.add(new Card(cardStrSplits[0], cardStrSplits[1]));
        }
        gd.setCards(pileCards);
    }

    public void rigTestRow25() {

        gd.setTopCard(new Card("H", "3"));
        setPlayerCards(new String[]{"C3 C7", "C4", "C5", "C6"});


    }
    @Test
    public void testRow25() throws InterruptedException {
        rigTestRow25();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // The player1 can play
        WebDriver player1 = webDrivers.get(0);
        assertTrue(player1.findElement(By.id("draw")).isEnabled());

        // player 1 play card
        player1.findElement(By.id("3C")).click();
        Thread.sleep(1000);
        assertFalse(player1.findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
    }


    public void rigTestRow27() {

        gd.setTopCard(new Card("S", "A"));
        setPlayerCards(new String[]{"HA HK", "CA CK", "DA DK", "H7 HK"});
    }
    @Test
    public void testRow27() throws InterruptedException {
        rigTestRow27();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: HA
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("AH")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        assertTrue(webDrivers.get(3).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 4", webDrivers.get(0).findElement(By.id("turnID")).getText());
        assertEquals("right", webDrivers.get(0).findElement(By.id("direction")).getText());

        // p4 plays card: H7
        webDrivers.get(3).findElement(By.id("7H")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(3).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 3", webDrivers.get(0).findElement(By.id("turnID")).getText());
        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
    }

    public void rigTestRow28() {

        gd.setTopCard(new Card("H", "Q"));
        setPlayerCards(new String[]{"CQ C2", "DQ D2", "SQ S2", "SK SA"});
    }
    @Test
    public void testRow28() throws InterruptedException {
        rigTestRow28();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: CQ
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("QC")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());

        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 3", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow29() {

        gd.setTopCard(new Card("C", "9"));
        setPlayerCards(new String[]{"C4 S1", "C6 S2", "C5 S3", "C3 S4"});
    }
    @Test
    public void testRow29() throws InterruptedException {
        rigTestRow29();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: C4
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("4C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());

        // p2 plays card: C6
        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        webDrivers.get(1).findElement(By.id("6C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(1).findElement(By.id("draw")).isEnabled());

        // p3 plays card: C5
        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
        webDrivers.get(2).findElement(By.id("5C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(2).findElement(By.id("draw")).isEnabled());

        // p4 plays card: C3
        assertTrue(webDrivers.get(3).findElement(By.id("draw")).isEnabled());
        webDrivers.get(3).findElement(By.id("3C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(3).findElement(By.id("draw")).isEnabled());

        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 1", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }


    public void rigTestRow31() {

        gd.setTopCard(new Card("C", "8"));
        setPlayerCards(new String[]{"C6 CK", "H6 HJ", "H3 H7 HJ", "HA HJ"});
    }
    @Test
    public void testRow31() throws InterruptedException {
        rigTestRow31();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: C6
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("6C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());

        // p2 plays card: H6
        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        webDrivers.get(1).findElement(By.id("6H")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(1).findElement(By.id("draw")).isEnabled());

        // p3 plays card: H3
        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
        webDrivers.get(2).findElement(By.id("3H")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(2).findElement(By.id("draw")).isEnabled());

        // p4 plays card: HA
        assertTrue(webDrivers.get(3).findElement(By.id("draw")).isEnabled());
        webDrivers.get(3).findElement(By.id("AH")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(3).findElement(By.id("draw")).isEnabled());

        // p3 plays card: H7
        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
        webDrivers.get(2).findElement(By.id("7H")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(2).findElement(By.id("draw")).isEnabled());

        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }



    public void rigTestRow32() {

        gd.setTopCard(new Card("H", "K"));
        setPlayerCards(new String[]{"H6 H7", "S6 S7", "C6 C7", "CQ CK"});
    }
    @Test
    public void testRow32() throws InterruptedException {
        rigTestRow32();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: H6
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("6H")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());

        // p2 plays card: S6
        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        webDrivers.get(1).findElement(By.id("6S")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(1).findElement(By.id("draw")).isEnabled());

        // p3 plays card: C6
        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
        webDrivers.get(2).findElement(By.id("6C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(2).findElement(By.id("draw")).isEnabled());

        // p4 plays card: CQ
        assertTrue(webDrivers.get(3).findElement(By.id("draw")).isEnabled());
        webDrivers.get(3).findElement(By.id("QC")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(3).findElement(By.id("draw")).isEnabled());


        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow35() {

        gd.setTopCard(new Card("C", "K"));
        setPlayerCards(new String[]{"HK HQ", "D4", "C4", "S4"});
    }
    @Test
    public void testRow35() throws InterruptedException {
        rigTestRow35();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: KH
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("KH")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());

        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow36() {

        gd.setTopCard(new Card("C", "K"));
        setPlayerCards(new String[]{"C7 D7", "D4", "C4", "S4"});
    }
    @Test
    public void testRow36() throws InterruptedException {
        rigTestRow36();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: 7C
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("7C")).click();
        Thread.sleep(1000);
        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());

        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow37() {

        gd.setTopCard(new Card("C", "K"));
        setPlayerCards(new String[]{"H8 D8", "D4", "C4", "S4"});
    }
    @Test
    public void testRow37() throws InterruptedException {
        rigTestRow37();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: 8H
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("8H")).click();
        Thread.sleep(1000);

        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("spade")).getAttribute("style"));
        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("heart")).getAttribute("style"));
        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("club")).getAttribute("style"));
        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("diamond")).getAttribute("style"));

    }


    public void rigTestRow38() {

        gd.setTopCard(new Card("C", "K"));
        setPlayerCards(new String[]{"S5 D5", "D4", "C4", "S4"});
    }
    @Test
    public void testRow38() throws InterruptedException {
        rigTestRow38();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 plays card: 5S
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("5S")).click();
        Thread.sleep(1000);

        Alert alert = webDrivers.get(0).switchTo().alert();
        assertEquals("Invalid Selection", alert.getText());
        alert.accept();
    }

    public void rigTestRow42() {

        gd.setTopCard(new Card("C", "7"));
        setPlayerCards(new String[]{"H3", "D3", "C3", "S3"});
    }
    @Test
    public void testRow42() throws InterruptedException {
        rigTestRow42();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        setPileCards(new String[]{"C6"});
        // p1 draw 6C
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("6C")).click();
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }


    public void rigTestRow43() {

        gd.setTopCard(new Card("C", "7"));
        setPlayerCards(new String[]{"H3", "D3", "C3", "S3"});
    }
    @Test
    public void testRow43() throws InterruptedException {
        rigTestRow43();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 draw 6D
        setPileCards(new String[]{"D6"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 draw 5C
        setPileCards(new String[]{"C5"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("5C")).click();
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow44() {

        gd.setTopCard(new Card("C", "7"));
        setPlayerCards(new String[]{"H3", "D3", "C3", "S3"});
    }
    @Test
    public void testRow44() throws InterruptedException {
        rigTestRow44();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 draw 6D
        setPileCards(new String[]{"D6"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 draw 5S
        setPileCards(new String[]{"S5"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 draw 7H
        setPileCards(new String[]{"H7"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("7H")).click();
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }


    public void rigTestRow45() {

        gd.setTopCard(new Card("C", "7"));
        setPlayerCards(new String[]{"H3", "D3", "C3", "S3"});
    }
    @Test
    public void testRow45() throws InterruptedException {
        rigTestRow45();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 draw 6D
        setPileCards(new String[]{"D6"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 draw 5S
        setPileCards(new String[]{"S5"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 draw 4H
        setPileCards(new String[]{"H4"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        assertFalse(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow46() {

        gd.setTopCard(new Card("C", "7"));
        setPlayerCards(new String[]{"H3", "D3", "C3", "S3"});
    }
    @Test
    public void testRow46() throws InterruptedException {
        rigTestRow46();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 draw 6D
        setPileCards(new String[]{"D6"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 draw 8H
        setPileCards(new String[]{"H8"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p1 play 8H
        webDrivers.get(0).findElement(By.id("8H")).click();
        Thread.sleep(1000);

        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("spade")).getAttribute("style"));
        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("heart")).getAttribute("style"));
        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("club")).getAttribute("style"));
        assertEquals("visibility: visible;", webDrivers.get(0).findElement(By.id("diamond")).getAttribute("style"));
    }

    public void rigTestRow47() {

        gd.setTopCard(new Card("C", "7"));
        setPlayerCards(new String[]{"SK C3", "D3", "S3", "H3"});
    }
    @Test
    public void testRow47() throws InterruptedException {
        rigTestRow47();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 draw 6C
        setPileCards(new String[]{"C6"});
        assertTrue(webDrivers.get(0).findElement(By.id("draw")).isEnabled());
        webDrivers.get(0).findElement(By.id("draw")).click();
        Thread.sleep(1000);


        // p1 play 6C
        webDrivers.get(0).findElement(By.id("6C")).click();
        Thread.sleep(1000);
        assertEquals("Turn: 2", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow51() {

        gd.setTopCard(new Card("C", "3"));
        setPlayerCards(new String[]{"C2 C7", "H4", "S5", "S6"});
    }
    @Test
    public void testRow51() throws InterruptedException {
        rigTestRow51();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 play 2C
        webDrivers.get(0).findElement(By.id("2C")).click();
        Thread.sleep(1000);


        // p2 draw 6C, 9D
        setPileCards(new String[]{"C6", "D9"});
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("6C")).click();
        Thread.sleep(1000);
        assertEquals("Turn: 3", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow52() {

        gd.setTopCard(new Card("C", "3"));
        setPlayerCards(new String[]{"C2 C7", "H4", "S5", "S6"});
    }
    @Test
    public void testRow52() throws InterruptedException {
        rigTestRow52();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 play 2C
        webDrivers.get(0).findElement(By.id("2C")).click();
        Thread.sleep(1000);


        // p2 draw 6S, 9D
        setPileCards(new String[]{"S6", "D9"});
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        // p2 draw 6C
        setPileCards(new String[]{"C6"});
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        webDrivers.get(1).findElement(By.id("6C")).click();
        Thread.sleep(1000);
        assertEquals("Turn: 3", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }

    public void rigTestRow53() {

        setPlayerCards(new String[]{"C2 C7", "H4", "S5", "S6"});
        gd.setTopCard(new Card("C", "3"));
        gd.setCards(new ArrayList<>());
    }
    @Test
    public void testRow53() throws InterruptedException {
        rigTestRow53();

        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        setPileCards(new String[]{"S6", "D9"});
        // p1 play 2C
        webDrivers.get(0).findElement(By.id("2C")).click();
        Thread.sleep(1000);


        // p2 auto draw 6S, 9D


        setPileCards(new String[]{"H9", "S7", "H5"});
        assertTrue(webDrivers.get(1).findElement(By.id("draw")).isEnabled());


        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("draw")).click();
        Thread.sleep(1000);

        assertFalse(webDrivers.get(1).findElement(By.id("draw")).isEnabled());
        Thread.sleep(1000);
        assertEquals("Turn: 3", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }


    public void rigTestRow55() {

        setPlayerCards(new String[]{"C2 C7", "H4", "D7", "S7"});
        gd.setTopCard(new Card("C", "3"));

    }
    @Test
    public void testRow55() throws InterruptedException {
        rigTestRow55();
        setPileCards(new String[]{"D9", "H2"});
        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 play 2C
        webDrivers.get(0).findElement(By.id("2C")).click();
        Thread.sleep(1000);
        // p2 auto draw

        setPileCards(new String[]{"S5", "D6", "H6", "C7"});

        // p2 play 2H
        webDrivers.get(1).findElement(By.id("2H")).click();
        Thread.sleep(1000);

        // p3 auto draw

        // p3 play 6H
        webDrivers.get(2).findElement(By.id("6H")).click();
        Thread.sleep(1000);

        assertEquals("Turn: 4", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }


    public void rigTestRow56() {

        setPlayerCards(new String[]{"C2 C3", "C4 C6 D9", "D3", "S4"});
        gd.setTopCard(new Card("H", "2"));

    }
    @Test
    public void testRow56() throws InterruptedException {
        rigTestRow56();
        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 play 2C
        webDrivers.get(0).findElement(By.id("2C")).click();
        Thread.sleep(1000);
        // p2 play 4C 6C
        webDrivers.get(1).findElement(By.id("4C")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("6C")).click();
        Thread.sleep(1000);


        assertTrue(webDrivers.get(2).findElement(By.id("draw")).isEnabled());
        assertEquals("Turn: 3", webDrivers.get(0).findElement(By.id("turnID")).getText());
    }


    public void rigTestRow57() {

        setPlayerCards(new String[]{"C2 C3", "C4 S4", "D3", "S4"});
        gd.setTopCard(new Card("H", "2"));

    }
    @Test
    public void testRow57() throws InterruptedException {
        rigTestRow57();
        webDrivers.get(0).findElement(By.id("startBtn")).click();
        Thread.sleep(1000);

        // p1 play 2C
        webDrivers.get(0).findElement(By.id("2C")).click();
        Thread.sleep(1000);
        // p2 play 4C 4S
        webDrivers.get(1).findElement(By.id("4C")).click();
        Thread.sleep(1000);
        webDrivers.get(1).findElement(By.id("4S")).click();
        Thread.sleep(1000);


        assertEquals(0, webDrivers.get(0).findElement(By.id("hand")).findElements(By.className("card")).size());
        assertEquals(0, webDrivers.get(1).findElement(By.id("hand")).findElements(By.className("card")).size());
        assertEquals(0, webDrivers.get(2).findElement(By.id("hand")).findElements(By.className("card")).size());
        assertEquals(0, webDrivers.get(3).findElement(By.id("hand")).findElements(By.className("card")).size());
    }
}
