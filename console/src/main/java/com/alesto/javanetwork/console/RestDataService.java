package com.alesto.javanetwork.console;

/**
 * Created by Alex on 27/06/2015.
 */

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.hateoas.Resources;


import java.lang.reflect.Array;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RestDataService
{
    public final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyyyy HH:mm:ss",Locale.UK);

    private final RestTemplate restTemplate;

    private final String baseURL = "http://localhost:8080/javanetwork/api/v1/";

    public RestDataService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public List<HashMap<?,?>> getTimeline(String userName) {
        List<HashMap<?, ?>> messages = new ArrayList<HashMap<?, ?>>(restTemplate.getForObject(baseURL + userName + "/timeline.json", Resources.class).getContent());
        return messages;
    }

    public List<HashMap<?,?>> getWall(String userName) {
        List<HashMap<?, ?>> messages = new ArrayList<HashMap<?, ?>>(restTemplate.getForObject(baseURL + userName + "/wall.json", Resources.class).getContent());
        return messages;
    }

    public boolean postMessage(String userName, String message) {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("message", message);

        Object response = this.restTemplate.postForObject(baseURL + userName + "/timeline.json", parts, String.class);

       return true;
    }

    public boolean follow(String userName, String userToFollow) {

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("userToFollow", userToFollow);

        Object response = this.restTemplate.postForObject(baseURL + userName + "/follows.json", parts, String.class);

        return true;
    }

    public List<String> follows(String userName) {

        List<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>(restTemplate.getForObject(baseURL + userName + "/follows.json", Resources.class).getContent());

        LocalDateTime ldtNow = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<String> following = new ArrayList<>();

        for (HashMap<String, String> mapMessage : messages) {
            following.add(mapMessage.get("followsUser"));
        }

        return following;
    }

}
