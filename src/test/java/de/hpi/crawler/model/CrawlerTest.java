package de.hpi.crawler.model;


import org.junit.Assert;
import org.junit.Test;

public class CrawlerTest {

    @Test
    public void startCrawling() {

        //TODO: Test with a new Storage TestStorage
    }

    @Test
    public void crawlBothSameStorage() {
        Crawler crawler = new Crawler(50);
        TestStorage mixedStorage = new TestStorage();


        crawler.startCrawling("http://books.toscrape.com/", mixedStorage);
        crawler.startCrawling("http://quotes.toscrape.com/", mixedStorage);

        while (mixedStorage.getFinished()<2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Assert.assertEquals(1350, mixedStorage.getNumberOfPages());
        //TODO: Look why the number keeps being 1064
    }

    @Test
    public void crawlBothDifferentStorages() {
        Crawler crawler = new Crawler(50);
        TestStorage mixedStorage = new TestStorage();

        TestStorage booksToScrapeStorage = new TestStorage();
        TestStorage quotesToScrapeStorage = new TestStorage();

        crawler.startCrawling("http://books.toscrape.com/", booksToScrapeStorage);
        crawler.startCrawling("http://quotes.toscrape.com/", quotesToScrapeStorage);


        while (booksToScrapeStorage.getFinished()<1 || quotesToScrapeStorage.getFinished()<1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Assert.assertEquals(214, quotesToScrapeStorage.getNumberOfPages());
        Assert.assertEquals(1136, booksToScrapeStorage.getNumberOfPages());

    }

    @Test
    public void crawlBooksToScrape() {
        Crawler crawler = new Crawler(50);


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

        Crawler crawler = new Crawler(50);

        TestStorage quotesToScrapeStorage = new TestStorage();
        crawler.startCrawling("http://quotes.toscrape.com/", quotesToScrapeStorage);
        while (quotesToScrapeStorage.getFinished()<1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(214, quotesToScrapeStorage.getNumberOfPages());
    }


}