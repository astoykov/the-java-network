package com.alesto.javanetwork.web.controller.rest.v1;

public final class ApiEndpoints {

    // Prevents instantiation
    private ApiEndpoints() {
    }

    public static final String API_INDEX = "/api/v1";

    public static final String TIMELINE_INDEX = API_INDEX + "/{userId}/timeline";

    public static final String FOLLOWS_INDEX = API_INDEX + "/{userId}/follows";

    public static final String WALL_INDEX = API_INDEX + "/{userId}/wall";
}
