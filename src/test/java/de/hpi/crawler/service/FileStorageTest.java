package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.mockito.verification.VerificationMode;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;

public class FileStorageTest {

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private SimpleWebCrawler crawler;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private Page referringPage;
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private WebURL url;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private FileStorage fileStorage;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private long shopID = 1234;

    @Test
    public void storePage() {
        this.setFileStorage(spy(new FileStorage(shopID)));

        savedPage("bla","http://www.google.de/", "google_de", true );
        savedPage("blub","https://www.google.in.co/123/test", "google_in_co", true );
        savedPage(RandomStringUtils.randomAlphabetic(10),"https://www.google.in.co/123/test", "google_in_co", true );
        savedPage(RandomStringUtils.randomAlphabetic(10),"https://google.in/123/test", "google_in", true );

        savedPage(RandomStringUtils.randomAlphabetic(10),"https://www.:google.in.co/123/test", "in_co", false );
    }

    private void savedPage(String html, String url, String fileName, Boolean valid ){
        try {
            TestTools testTools = new TestTools();
            this.getFileStorage().store(testTools.constructTestPage(url, html), System.currentTimeMillis());
            VerificationMode mode = valid ? times(1): never();
            verify(this.getFileStorage(), mode).saveStringToFile(eq(html), matches(String.format("crawledPages\\/%s-[0-9]*\\.html",Long.toString(shopID) + "-" + fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}