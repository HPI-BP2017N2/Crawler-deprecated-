package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class FileSaverTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleHTMLCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private FileSaver fileSaver;


    @Test
    public void storePage() {
        setFileSaver(spy(new FileSaver()));

        savedPage("bla","http://www.google.de/", "google_de", true );
        savedPage("blub","https://www.google.in.co/123/test", "google_in_co", true );
        savedPage(RandomStringUtils.randomAlphabetic(10),"https://www.google.in.co/123/test", "google_in_co", true );
        savedPage(RandomStringUtils.randomAlphabetic(10),"https://google.in/123/test", "google_in", true );

        savedPage(RandomStringUtils.randomAlphabetic(10),"https://www.:google.in.co/123/test", "in_co", false );
    }

    private void savedPage(String html, String url, String fileName, Boolean valid ){
        getFileSaver().storePage(constructTestPage(url, html));
        VerificationMode mode = valid ? times(1): never();
        verify(getFileSaver(), mode).saveStringToFile(eq(html), matches(String.format("crawledPages\\/%s-[0-9]*\\.html",fileName)));
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