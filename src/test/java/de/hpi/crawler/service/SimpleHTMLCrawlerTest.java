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
    public void setup(){
        setCrawler(new SimpleHTMLCrawler());
        setReferringPage(mock(Page.class));
        setUrl( mock(WebURL.class));
    }
    @Test
    public void shouldVisitTest(){
        crawler.shouldVisit(referringPage, url);
    }

    @Test
    public void testValidUrl() {
        testUrl(url, crawler, referringPage,true, "http://www.google.de/");
        testUrl(url, crawler, referringPage,true, "https://www.google.de/");
        testUrl(url, crawler, referringPage,true, "http://google.de/afsdfasd");
        testUrl(url, crawler, referringPage,true, "http://www.calendar.google.de/");
    }

    @Test
    public void testNotHtmlUrl(){
        testUrl(url, crawler, referringPage,false, "http://www.google.de/test.jpg");
        testUrl(url, crawler, referringPage,false, "http://www.google.de/123.epub");
        testUrl(url, crawler, referringPage,false, "http://www.google.de/bla.txt");
        testUrl(url, crawler, referringPage,false, "http://www.google.de/javascript.js");
        testUrl(url, crawler, referringPage,false, "http://www.google.de/blub.png?=0");
        testUrl(url, crawler, referringPage,false, "http://www.google.de/evilIdealo.ai?=abc");
    }

    @Test
    public void testNotInRootDomain() {
        when(referringPage.getWebURL().getDomain()).thenReturn("http://www.google.de/");
        testUrl(url, crawler, referringPage,false, "http://www.googled.de/baleasdfsdf");
        testUrl(url, crawler, referringPage,false, "http://google.com/afdasd");
        testUrl(url, crawler, referringPage,false, "http://www.calendar.gooogle.de/");
    }

    private void testUrl(WebURL webUrl, SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        when(webUrl.getURL()).thenReturn(url);
        assertEquals(crawler.shouldVisit(referringPage, webUrl), valid);
    }

}