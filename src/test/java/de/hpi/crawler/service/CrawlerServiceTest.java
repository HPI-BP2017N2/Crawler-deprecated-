package de.hpi.crawler.service;

import org.junit.Test;

import static org.mockito.Mockito.spy;

public class CrawlerServiceTest {


    @Test
    public void crawlDomain() {
        CrawlerService crawlerService = spy(new CrawlerService());

        obeysRobotsTxt(crawlerService);

        //doReturn("").when(crawlerService.)

        //TODO: Test with a new Storage TestStorage
    }

    private void obeysRobotsTxt(CrawlerService crawlerService) {

    }
}