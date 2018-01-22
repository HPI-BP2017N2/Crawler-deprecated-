package de.hpi.crawler.service;

import de.hpi.crawler.dto.shopIDResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)

public class ConnectionService {

    @Getter(AccessLevel.PRIVATE) private static final String BP_BRIDGE_ROOT = "http://ts1552.byod.hpi.de:2162",
                                                             SHOPID_ROUTE = "/shopIDToUrl";

    private RestTemplate restTemplate;


    public ConnectionService(){
        setRestTemplate(new RestTemplate());
    }

    public String getURLfromShopIDfromBridge(long shopID) {
        URI connectionURItoBridge = buildURIforQuery(getBP_BRIDGE_ROOT(), getSHOPID_ROUTE(), "shopID", shopID);
        return getRestTemplate().getForObject(connectionURItoBridge, shopIDResponse.class).getShopUrl();
    }

    private URI buildURIforQuery(String rootUrl, String route, String parameterName, Object... parameter){
        return UriComponentsBuilder.fromUriString(getBP_BRIDGE_ROOT())
                .path(getSHOPID_ROUTE())
                .queryParam(parameterName, parameter)
                .build()
                .encode()
                .toUri();
    }


    public void sendStringToQueue(String string, String queueName){
        initializeConnectionToQueue();
        //TODO: Implement
    }
    private void initializeConnectionToQueue(){
        //TODO: Implement
    }
}
