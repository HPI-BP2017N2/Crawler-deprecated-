package de.hpi.crawler.service;


import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



class SimpleHTMLCrawlerTest {


    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleHTMLCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;



    @BeforeEach
    void setup(){
        setCrawler(spy(new SimpleHTMLCrawler(new FileSaver())));
        setReferringPage(mock(Page.class));
        setUrl(mock(WebURL.class));
        WebURL previousPage = new WebURL();
        previousPage.setURL("http://www.google.de/");
        when(referringPage.getWebURL()).thenReturn(previousPage);
    }


    @Test
    void shouldVisitTest(){
        isInRootDomainTest();
        isHTMLPageTest();
    }

    private void isInRootDomainTest() {
        testUrlShouldVisit(crawler, referringPage,false, "http://www.googled.de/INDEX.html");
        testUrlShouldVisit(crawler, referringPage,false, "http://GOOGLE.com/bla");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.calendar.gooogle.de/");
        testUrlShouldVisit(crawler, referringPage,false, "google.de");

        testUrlShouldVisit(crawler, referringPage,true, "http://google.de/INDEX");
        testUrlShouldVisit(crawler, referringPage,true, "https://GOOGLE.de");
        testUrlShouldVisit(crawler, referringPage,true, "https://google.de/blablaba?asdfs");
        testUrlShouldVisit(crawler, referringPage,true, "www.google.de");

    }

    private void isHTMLPageTest(){
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/test.jpg");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/blub.png?=0");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.goog000le.de/123.epub");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/bla.txt");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/javascript.js");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/evilIdealo.js?=abc");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/html.png");

        testUrlShouldVisit(crawler, referringPage,true, "http://www.google.de/");
        testUrlShouldVisit(crawler, referringPage,true, "https://www.google.de/");
        testUrlShouldVisit(crawler, referringPage,true, "http://google.de/afsdfasd");
        testUrlShouldVisit(crawler, referringPage,true, "http://www.calendar.google.de/");
        testUrlShouldVisit(crawler, referringPage,true, "http://www.calendar.google.de/");
    }

    private void testUrlShouldVisit(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.shouldVisit(referringPage, webUrl) );
    }


    private Page constructTestPage(String url, String html) {
        WebURL urlOfPageToStore = new WebURL();
        urlOfPageToStore.setURL(url);
        Page pageToStore = new Page(urlOfPageToStore);
        HtmlParseData testParseData = new HtmlParseData();
        testParseData.setHtml(html);
        pageToStore.setParseData(testParseData);
        return pageToStore;
    }








}