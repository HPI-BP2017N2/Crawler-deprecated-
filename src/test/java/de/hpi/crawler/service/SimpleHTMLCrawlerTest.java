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


    private Page constructTestPage(String url, String html) {
        WebURL urlOfPageToStore = new WebURL();
        urlOfPageToStore.setURL(url);
        Page pageToStore = new Page(urlOfPageToStore);
        HtmlParseData testParseData = new HtmlParseData();
        testParseData.setHtml(html);
        pageToStore.setParseData(testParseData);
        return pageToStore;
    }

    @Test
    void shouldVisitTest(){
        testUrlShouldVisit(crawler,referringPage, false, "http://www.googled.de/baleasdfsdf");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.google.de/bla.txt");
        testUrlShouldVisit(crawler, referringPage,false, "http://www.gooOOOOOOgle.dE/123.jpg");

        testUrlShouldVisit(crawler,referringPage,true, "WWW.GOOGLE.DE");
        testUrlShouldVisit(crawler, referringPage,true, "https://google.de/INDEX");
        testUrlShouldVisit(crawler, referringPage,true, "https://google.de/INDEx.html");
    }

    @Test
    void isInRootDomainTest() {
        testUrlIsInRootDomain(crawler, referringPage,false, "http://www.googled.de/baleasdfsdf");
        testUrlIsInRootDomain(crawler, referringPage,false, "http://google.com/bla");
        testUrlIsInRootDomain(crawler, referringPage,false, "http://www.calendar.gooogle.de/");
        testUrlIsInRootDomain(crawler, referringPage,false, "google.de");
        testUrlIsInRootDomain(crawler, referringPage,true, "http://google.de/");
        testUrlIsInRootDomain(crawler, referringPage,true, "https://google.de");
        testUrlIsInRootDomain(crawler, referringPage,true, "https://google.de/blablaba?asdfs");
        testUrlIsInRootDomain(crawler, referringPage,true, "www.google.de");

    }

    @Test
    void isHTMLPageTest(){
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/test.jpg");
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/blub.png?=0");
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/123.epub");
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/bla.txt");
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/javascript.js");
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/evilIdealo.js?=abc");
        testUrlIsHTMLPage(crawler, referringPage,false, "http://www.google.de/html.png");

        testUrlIsHTMLPage(crawler, referringPage,true, "http://www.google.de/");
        testUrlIsHTMLPage(crawler, referringPage,true, "https://www.google.de/");
        testUrlIsHTMLPage(crawler, referringPage,true, "http://google.de/afsdfasd");
        testUrlIsHTMLPage(crawler, referringPage,true, "http://www.calendar.google.de/");
        testUrlIsHTMLPage(crawler, referringPage,true, "http://www.calendar.google.de/");
    }

    @Test
    void convertDomainNameFileFriendlyTest() {
        assertEquals("google_de",crawler.getDomainFileFriendly("https://google.de/123/test"));
        assertEquals("google_in_co",crawler.getDomainFileFriendly("https://www.google.in.co/123/test"));
        assertEquals("google_com",crawler.getDomainFileFriendly("google.com/123/test"));
        assertEquals("google_de",crawler.getDomainFileFriendly("www.google.de/1"));


    }

    private void testUrlIsHTMLPage(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.isMIMEfiltered(webUrl));
    }

    private void testUrlIsInRootDomain(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.isInRootDomain(referringPage, webUrl));
    }

    private void testUrlShouldVisit(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.shouldVisit(referringPage, webUrl) );
    }




}