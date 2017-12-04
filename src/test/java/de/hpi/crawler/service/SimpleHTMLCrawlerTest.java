package de.hpi.crawler.service;


import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class SimpleHTMLCrawlerTest {

    private SimpleHTMLCrawler crawler;
    private Page referringPage;
    private WebURL url;

    @BeforeEach
    void setup() {
        crawler = new SimpleHTMLCrawler();
        referringPage = mock(Page.class);
        url = mock(WebURL.class);

        WebURL testDomain = new WebURL();
        testDomain.setURL("http://www.google.de/");
        when(referringPage.getWebURL()).thenReturn(testDomain);
    }

    @Test // NOTE - ParameterizedTest with input value pairs are still not possible, but will be cool
    void shouldVisitTest() {
        testUrlShouldVisit(crawler, referringPage, false, "http://www.googled.de/baleasdfsdf");
        testUrlShouldVisit(crawler, referringPage, false, "http://www.google.de/bla.txt");
        testUrlShouldVisit(crawler, referringPage, false, "http://www.gooOOOOOOgle.dE/123.jpg");

        testUrlShouldVisit(crawler, referringPage, true, "WWW.GOOGLE.DE");
        testUrlShouldVisit(crawler, referringPage, true, "https://google.de/INDEX");
        testUrlShouldVisit(crawler, referringPage, true, "https://google.de/INDEx.html");
    }

    @Test
    void isInRootDomainTest() {
        testUrlIsInRootDomain(crawler, referringPage, false, "http://www.googled.de/baleasdfsdf");
        testUrlIsInRootDomain(crawler, referringPage, false, "http://google.com/bla");
        testUrlIsInRootDomain(crawler, referringPage, false, "http://www.calendar.gooogle.de/");
        testUrlIsInRootDomain(crawler, referringPage, false, "google.de");
        testUrlIsInRootDomain(crawler, referringPage, true, "http://google.de/");
        testUrlIsInRootDomain(crawler, referringPage, true, "https://google.de");
        testUrlIsInRootDomain(crawler, referringPage, true, "https://google.de/blablaba?asdfs");
        testUrlIsInRootDomain(crawler, referringPage, true, "www.google.de");

    }

    @Test
    void isHTMLPageTest() {
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/test.jpg");
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/blub.png?=0");
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/123.epub");
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/bla.txt");
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/javascript.js");
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/evilIdealo.js?=abc");
        testUrlIsHTMLPage(crawler, referringPage, false, "http://www.google.de/html.png");

        testUrlIsHTMLPage(crawler, referringPage, true, "http://www.google.de/");
        testUrlIsHTMLPage(crawler, referringPage, true, "https://www.google.de/");
        testUrlIsHTMLPage(crawler, referringPage, true, "http://google.de/afsdfasd");
        testUrlIsHTMLPage(crawler, referringPage, true, "http://www.calendar.google.de/");
        testUrlIsHTMLPage(crawler, referringPage, true, "http://www.calendar.google.de/");
    }

    private void testUrlIsHTMLPage(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.isHTMLPage(webUrl)); // assertSomething(expected, actual)
    }

    private void testUrlIsInRootDomain(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.isInRootDomain(referringPage, webUrl));
    }

    private void testUrlShouldVisit(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.shouldVisit(referringPage, webUrl));
    }
}