package de.hpi.crawler.service;

import edu.uci.ics.crawler4j.crawler.Page;

/**
 * This interface is to provide a Storage Provider to save HTML Pages crawled by a crawler
 * @author jonaspohlmann
 *
 */

public interface StorageProvider {
    /**
     * @param page A page crawled by the crawler4j
     * @param timestamp The timestamp when the page was crawled
     *
     */

    void store(Page page, long timestamp) throws Exception;

}
