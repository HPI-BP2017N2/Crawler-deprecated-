package de.hpi.crawler.service;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

public class ConnectionService {

    @Getter(AccessLevel.PRIVATE) private static final String BP_BRIDGE_ROOT = "http://ts1552.byod.hpi.de:2162",
                                                             SHOPID_ROUTE = "/shopIDToUrl";



    public String getURLfromShopIDfromBridge(long shopID) {
        return UriComponentsBuilder.fromUriString(getBP_BRIDGE_ROOT())
                .path(getSHOPID_ROUTE())
                .queryParam("shopID", shopID)
                .build()
                .encode()
                .toUri().toString();
    }

    public void sendStringToQueue(String string){
        initializeConnectionToQueue();
        //TODO: Implement
    }
    private void initializeConnectionToQueue(){
        //TODO: Implement
    }
}
