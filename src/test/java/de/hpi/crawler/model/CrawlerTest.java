package de.hpi.crawler.model;


import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.spy;

public class CrawlerTest {

    @Test
    public void startCrawling() {


        Crawler crawler = spy(new Crawler());


        TestStorage booksToScrapeStorage = new TestStorage();
        TestStorage quotesToScrapeStorage = new TestStorage();
        TestStorage mixedStorage = new TestStorage();

        crawlBooksToScrape(crawler, booksToScrapeStorage);
        crawlQuotesToScrape(crawler, quotesToScrapeStorage);

        crawlBoth(crawler, booksToScrapeStorage, quotesToScrapeStorage, mixedStorage);

        int booksToScrapePages = booksToScrapeStorage.getNumberOfPages();
        int quotesToScrapePages = quotesToScrapeStorage.getNumberOfPages();

        Assert.assertEquals(booksToScrapePages+quotesToScrapePages, mixedStorage.getNumberOfPages());

        System.out.println(booksToScrapePages);
        System.out.println(quotesToScrapePages);



        //TODO: Test with a new Storage TestStorage
    }

    private void crawlBoth(Crawler crawler, TestStorage booksToScrapeStorage, TestStorage quotesToScrapeStorage, TestStorage mixedStorage) {
        crawler.startCrawling("http://books.toscrape.com/", mixedStorage);
        crawler.startCrawling("http://qoutes.toscrape.com/", mixedStorage);

        while (!quotesToScrapeStorage.isFinished() && !booksToScrapeStorage.isFinished()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void crawlBooksToScrape(Crawler crawler, TestStorage booksToScrapeStorage) {
        crawler.startCrawling("http://books.toscrape.com/", booksToScrapeStorage);
        while (!booksToScrapeStorage.isFinished()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void crawlQuotesToScrape(Crawler crawler, TestStorage quotesToScrapeStorage) {
        crawler.startCrawling("http://qoutes.toscrape.com/", quotesToScrapeStorage);
        while (!quotesToScrapeStorage.isFinished()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}