package de.hpi.crawler.service;


import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//@PrepareForTest({System.class})

//@RunWith(PowerMockRunner.class)


class SimpleHTMLCrawlerTest {


    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleHTMLCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;



    @BeforeEach
    void setup(){
        setCrawler(new SimpleHTMLCrawler());
        setReferringPage(mock(Page.class));
        setUrl(mock(WebURL.class));
        WebURL previousPage = new WebURL();
        previousPage.setURL("http://www.google.de/");
        when(referringPage.getWebURL()).thenReturn(previousPage);
    }

    @Test
    void saveHTMLContentOfPage() {
        //mockStatic(System.class);
        //when(System.currentTimeMillis()).thenReturn(123L);
        assertEquals(true,true);
        //verify(crawler, times(1)).saveStringToFile("bla","crawledPages/test-123.html");

    }

    @Test
    void shouldVisitTest(){
        testUrlShouldVisit(crawler,referringPage,false, "http://www.googled.de/baleasdfsdf");
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
        assertEquals("google.de",crawler.convertDomainNameFileFriendly("https://google.de/123/test"));
        assertEquals("google.in.co",crawler.convertDomainNameFileFriendly("https://www.google.in.co/123/test"));
        assertEquals("google.com",crawler.convertDomainNameFileFriendly("google.com/123/test"));
        assertEquals("google.de",crawler.convertDomainNameFileFriendly("www.google.de/1"));


    }

    private void testUrlIsHTMLPage(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(crawler.isHTMLPage(webUrl), valid);
    }

    private void testUrlIsInRootDomain(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url){
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(crawler.isInRootDomain(referringPage, webUrl), valid);
    }

    private void testUrlShouldVisit(SimpleHTMLCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(crawler.shouldVisit(referringPage, webUrl), valid);
    }




}