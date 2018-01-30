package de.hpi.crawler.model;


import org.junit.Assert;
import org.junit.Test;

public class CrawlerTest {

    @Test
    public void startCrawling() {

        Crawler crawler = new Crawler(100);


        TestStorage booksToScrapeStorage = new TestStorage();
        TestStorage quotesToScrapeStorage = new TestStorage();
        TestStorage mixedStorage = new TestStorage();
        crawlBooksToScrape();
        crawlQuotesToScrape();



        int booksToScrapePages = booksToScrapeStorage.getNumberOfPages();
        int quotesToScrapePages = quotesToScrapeStorage.getNumberOfPages();

        Assert.assertEquals(1350, mixedStorage.getNumberOfPages());


        //TODO: Test with a new Storage TestStorage
    }

    private void crawlBothSameStorage(Crawler crawler, TestStorage booksToScrapeStorage, TestStorage quotesToScrapeStorage, TestStorage mixedStorage) {
        crawler.startCrawling("http://books.toscrape.com/", mixedStorage);
        crawler.startCrawling("http://quotes.toscrape.com/", mixedStorage);

        while (mixedStorage.getFinished()<2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void crawlBothDifferentStorages() {
        Crawler crawler = new Crawler(100);
        TestStorage mixedStorage = new TestStorage();

        TestStorage booksToScrapeStorage = new TestStorage();
        TestStorage quotesToScrapeStorage = new TestStorage();

        crawler.startCrawling("http://books.toscrape.com/", mixedStorage);
        crawler.startCrawling("http://quotes.toscrape.com/", mixedStorage);


        while (mixedStorage.getFinished()<2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void crawlBooksToScrape() {
        Crawler crawler = new Crawler(100);


        TestStorage booksToScrapeStorage = new TestStorage();
        crawler.startCrawling("http://books.toscrape.com/", booksToScrapeStorage);
        while (booksToScrapeStorage.getFinished()<1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(1136, booksToScrapeStorage.getNumberOfPages());
    }

    @Test
    public void crawlQuotesToScrape() {

        Crawler crawler = new Crawler(100);

        TestStorage quotesToScrapeStorage = new TestStorage();
        crawler.startCrawling("http://quotes.toscrape.com/", quotesToScrapeStorage);
        while (quotesToScrapeStorage.getFinished()<1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(248, quotesToScrapeStorage.getNumberOfPages());
    }


}