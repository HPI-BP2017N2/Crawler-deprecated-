package de.hpi.crawler.model;

import edu.uci.ics.crawler4j.crawler.Page;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class TestStorage implements StorageProvider {

    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) volatile boolean finished = false;

    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE) int numberOfPages = 0;

    @Override
    public void store(Page page, long timestamp) throws Exception {
        setNumberOfPages(getNumberOfPages()+1);
    }

    @Override
    public void finishedCrawling() {
        setFinished(true);
    }
}
