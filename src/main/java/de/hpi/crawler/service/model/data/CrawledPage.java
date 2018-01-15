package de.hpi.crawler.service.model.data;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CrawledPage {
    long shopID;
    String url;
    String htmlSource;
}
