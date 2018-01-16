package de.hpi.crawler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CrawledPage {
    long shopID;
    String url;
    String htmlSource;
}
