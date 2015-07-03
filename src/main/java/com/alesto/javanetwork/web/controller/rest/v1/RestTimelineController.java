package com.alesto.javanetwork.web.controller.rest.v1;

import com.alesto.javanetwork.domain.entity.Timeline;
import com.alesto.javanetwork.domain.rest.resource.TimelineResource;
import com.alesto.javanetwork.domain.rest.support.TimelineResourceAssembler;
import com.alesto.javanetwork.service.DataExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.Date;

@Controller
public class RestTimelineController {
    
    /*
     * ======================================================================
     * ----- Constants
     * ======================================================================
     */

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(RestTimelineController.class);

    /*
     * ======================================================================
     * ----- Fields
     * ======================================================================
     */

    @Inject
    private TimelineResourceAssembler timelineResourceAssembler;

    @Inject
    private DataExchangeService dataExchangeService;

    /*
     * ======================================================================
     * ----- Methods
     * ======================================================================
     */

    @RequestMapping(
            value = ApiEndpoints.TIMELINE_INDEX,
            method = RequestMethod.GET,
            produces = {
                    "application/vnd.javanetwork-timeline_list-v1.0+json",
                    "application/vnd.javanetwork-timeline_list-v1.0+hal+json",
                    "application/json", // use in latest version only
                    "application/hal+json" // use in latest version only
            }
    )
    public ResponseEntity<Resources<Resource<TimelineResource>>> list(@PathVariable("userId") String userId) {
        HttpHeaders headers = new HttpHeaders();

        Resources<Resource<TimelineResource>> resources = timelineResourceAssembler.toResources(dataExchangeService.getTimelineMessages(userId));

        return new ResponseEntity<Resources<Resource<TimelineResource>>>(resources, headers, HttpStatus.OK);
    }

    @RequestMapping(
            value = ApiEndpoints.WALL_INDEX,
            method = RequestMethod.GET,
            produces = {
                    "application/vnd.javanetwork-timeline_list-v1.0+json",
                    "application/vnd.javanetwork-timeline_list-v1.0+hal+json",
                    "application/json", // use in latest version only
                    "application/hal+json" // use in latest version only
            }
    )
    public ResponseEntity<Resources<Resource<TimelineResource>>> wall(@PathVariable("userId") String userId) {
        HttpHeaders headers = new HttpHeaders();

        Resources<Resource<TimelineResource>> resources = timelineResourceAssembler.toResources(dataExchangeService.getWallMessages(userId));

        return new ResponseEntity<Resources<Resource<TimelineResource>>>(resources, headers, HttpStatus.OK);
    }

    @RequestMapping(
            value = ApiEndpoints.TIMELINE_INDEX,
            method = RequestMethod.POST,
            produces = {
                    "application/vnd.javanetwork-timeline_post-v1.0+json",
                    "application/vnd.javanetwork-timeline_post-v1.0+hal+json",
                    "application/json", // use in latest version only
                    "application/hal+json" // use in latest version only
            }
    )
    public ResponseEntity<Void> newMessage(@PathVariable("userId") String userId, @RequestParam String message) {
        HttpHeaders headers = new HttpHeaders();

        if (!message.trim().isEmpty()) {
            Timeline timeline = new Timeline();
            timeline.setCreated(new Date());
            timeline.setUsername(userId);
            timeline.setMessage(message);
            timeline = dataExchangeService.postTimelineMessage(timeline);
        }

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

}
