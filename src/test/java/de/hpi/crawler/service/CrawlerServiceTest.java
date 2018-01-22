package de.hpi.crawler.service;

import org.junit.Test;

import static org.mockito.Mockito.spy;


public class CrawlerServiceTest {




    @Test
    public void crawlDomain() {
        //ConnectionService connectionService = spy(new ConnectionService());

        //doReturn("http://books.toscrape.com/").when(connectionService).getURLfromShopIDfromBridge(1234);

        CrawlerService crawlerService = spy(new CrawlerService());

        crawlerService.crawlDomain(1);


        //obeysRobotsTxt(crawlerService);

        //doReturn("").when(crawlerService.)

        //TODO: Test with a new Storage TestStorage
    }



}