package de.hpi.crawler.service;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.crawler.*;

import static org.junit.jupiter.api.Assertions.*;


class SimpleHTMLCrawlerTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleHTMLCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @BeforeEach
     void setup(){
        setCrawler(new SimpleHTMLCrawler());
        setReferringPage(mock(Page.class));
        setUrl(mock(WebURL.class));
    }
    @Test
    void shouldVisitTest(){

    }

    @Test
    void isInRootDomainTest() {
        WebURL testDomain = new WebURL();
        testDomain.setURL("http://www.google.de/");
        when(referringPage.getWebURL()).thenReturn(testDomain);

        testInRootDomainUrl(crawler, referringPage,false, "http://www.googled.de/baleasdfsdf");
        testInRootDomainUrl(crawler, referringPage,false, "http://google.com/bla");
        testInRootDomainUrl(crawler, referringPage,false, "http://www.calendar.gooogle.de/");
        testInRootDomainUrl(crawler, referringPage,false, "google.de");

        testInRootDomainUrl(crawler, referringPage,true, "http://google.de/");
        testInRootDomainUrl(crawler, referringPage,true, "https://google.de");
        testInRootDomainUrl(crawler, referringPage,true, "https://google.de/blablaba?asdfs");
        testInRootDomainUrl(crawler, referringPage,true, "www.google.de");

    }

    @Test
    void isHTMLPageTest(){
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/test.jpg");
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/blub.png?=0");
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/123.epub");
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/bla.txt");
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/javascript.js");
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/evilIdealo.js?=abc");
        testIsHTMLPageUrl(crawler, referringPage,false, "http://www.google.de/html.png");

        testIsHTMLPageUrl(crawler, referringPage,true, "http://www.google.de/");
        testIsHTMLPageUrl(crawler, referringPage,true, "https://www.google.de/");
        testIsHTMLPageUrl(crawler, referringPage,true, "http://google.de/afsdfasd");
        testIsHTMLPageUrl(crawler, referringPage,true, "http://www.calendar.google.de/");
        testIsHTMLPageUrl(crawler, referringPage,true, "http://www.calendar.google.de/");
    }

    private void testIsHTMLPageUrl(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(crawler.isHTMLPage(webUrl), valid);
    }

    private void testInRootDomainUrl(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(crawler.isInRootDomain(referringPage, webUrl), valid);
    }




}