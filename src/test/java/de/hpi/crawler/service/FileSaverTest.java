package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileSaverTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleHTMLCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private FileSaver fileSaver;


    @BeforeEach
    void setup(){
        setFileSaver(new FileSaver());

        setReferringPage(mock(Page.class));
        setUrl(mock(WebURL.class));
        WebURL previousPage = new WebURL();
        previousPage.setURL("http://www.google.de/");
        when(referringPage.getWebURL()).thenReturn(previousPage);
    }


    @Test
    public void storePage() {
        getFileSaver().storePage(constructTestPage("http://www.google.de/", "bla"));


        verify(crawler, times(1)).saveStringToFile(eq("bla"),matches("crawledPages\\/google_de-[0-9]*\\.html"));

        crawler.saveHTMLContentOfPage(constructTestPage("https://www.google.in.co/123/test", "blub"));
        verify(crawler, times(1)).saveStringToFile(eq("blub"),matches("crawledPages\\/google_in_co-[0-9]*\\.html"));
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