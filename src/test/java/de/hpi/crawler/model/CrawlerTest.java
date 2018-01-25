package de.hpi.crawler.model;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class CrawlerTest {

    @Test
    public void startCrawling() {


        Crawler crawler = spy(new Crawler());

        StorageProvider storageProvider = mock(StorageProvider.class);

        crawler.startCrawling("http://books.toscrape.com/", storageProvider);



        //obeysRobotsTxt(crawlerService);

        //doReturn("").when(crawlerService.)

        //TODO: Test with a new Storage TestStorage
    }
}