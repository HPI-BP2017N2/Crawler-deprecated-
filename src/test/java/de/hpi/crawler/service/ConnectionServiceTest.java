package de.hpi.crawler.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectionServiceTest {

    @Test
    public void getURLfromShopIDfromBridge() {
        ConnectionService connection = new ConnectionService();
        String url = connection.getURLfromShopIDfromBridge(6980);
        assertEquals( "https://www.notebooksbilliger.de", url);
    }

    @Test
    public void sendStringToQueue() {
        //TODO: Implement
    }
}