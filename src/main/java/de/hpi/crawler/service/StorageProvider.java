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
     */

    void store(Page page) throws Exception;

}
