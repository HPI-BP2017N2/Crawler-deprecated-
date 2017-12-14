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



class SimpleWebCrawlerTest {


    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleWebCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) StorageProvider fileSaver;




    @BeforeEach
    void setup(){
        setFileSaver(spy(new FileStorage()));

        setCrawler(spy(new SimpleWebCrawler(getFileSaver())));
        setReferringPage(mock(Page.class));
        setUrl(mock(WebURL.class));
        WebURL previousPage = new WebURL();
        previousPage.setURL("http://www.google.de/");
        when(referringPage.getWebURL()).thenReturn(previousPage);
    }

    @Test
    void visit() {
        Page validPage = constructTestPage("http://www.google.de/", "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Page Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>My First Heading</h1>\n" +
                "<p>My first paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>");
        getCrawler().visit(validPage);

        try {
            verify(getFileSaver(), times(1)).store(validPage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Page inValidPage = constructTestPage("http://www.google.de/", "bla");
        getCrawler().visit(inValidPage);

        //TODO check what non HTML content is
        //verify(getFileSaver(), never()).store(inValidPage);

    }

    @Test
    void shouldVisit(){
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

    private void testUrlShouldVisit(SimpleWebCrawler crawler, Page referringPage, boolean valid, String url) {
        WebURL webUrl = new WebURL();
        webUrl.setURL(url);
        assertEquals(valid, crawler.shouldVisit(referringPage, webUrl) );
    }


    private Page constructTestPage(String url, String html) {
        WebURL urlOfPageToStore = new WebURL();
        urlOfPageToStore.setURL(url);
        Page pageToStore = spy(new Page(urlOfPageToStore));
        HtmlParseData testParseData = spy(new HtmlParseData());
        testParseData.setHtml(html);
        testParseData.setText("");
        pageToStore.setParseData(testParseData);
        return pageToStore;
    }





}