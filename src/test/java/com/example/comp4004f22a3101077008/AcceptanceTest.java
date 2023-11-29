package com.example.comp4004f22a3101077008;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
}
