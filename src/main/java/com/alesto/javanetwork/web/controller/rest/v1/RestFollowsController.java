package com.alesto.javanetwork.web.controller.rest.v1;

import com.alesto.javanetwork.domain.entity.Follows;
import com.alesto.javanetwork.domain.rest.resource.FollowsResource;
import com.alesto.javanetwork.domain.rest.support.FollowsResourceAssembler;
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
public class RestFollowsController {
    
    /*
     * ======================================================================
     * ----- Constants
     * ======================================================================
     */

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(RestFollowsController.class);

    /*
     * ======================================================================
     * ----- Fields
     * ======================================================================
     */

    @Inject
    private FollowsResourceAssembler followsResourceAssembler;

    @Inject
    private DataExchangeService dataExchangeService;

    /*
     * ======================================================================
     * ----- Methods
     * ======================================================================
     */

    @RequestMapping(
            value = ApiEndpoints.FOLLOWS_INDEX,
            method = RequestMethod.GET,
            produces = {
                    "application/vnd.javanetwork-timeline_list-v1.0+json",
                    "application/vnd.javanetwork-timeline_list-v1.0+hal+json",
                    "application/json", // use in latest version only
                    "application/hal+json" // use in latest version only
            }
    )
    public ResponseEntity<Resources<Resource<FollowsResource>>> list(@PathVariable("userId") String userId) {
        HttpHeaders headers = new HttpHeaders();

        Resources<Resource<FollowsResource>> resources = followsResourceAssembler.toResources(dataExchangeService.getFollowedUsers(userId));

        return new ResponseEntity<Resources<Resource<FollowsResource>>>(resources, headers, HttpStatus.OK);
    }

    @RequestMapping(
            value = ApiEndpoints.FOLLOWS_INDEX,
            method = RequestMethod.POST,
            produces = {
                    "application/vnd.javanetwork-timeline_post-v1.0+json",
                    "application/vnd.javanetwork-timeline_post-v1.0+hal+json",
                    "application/json", // use in latest version only
                    "application/hal+json" // use in latest version only
            }
    )
    public ResponseEntity<Void> follows(@PathVariable("userId") String userId, @RequestParam String userToFollow) {
        HttpHeaders headers = new HttpHeaders();

        if (!userToFollow.trim().isEmpty()) {
            Follows follows = new Follows();
            follows.setCreated(new Date());
            follows.setUsername(userId);
            follows.setFollowsUser(userToFollow);
            follows = dataExchangeService.followUser(follows);
        }

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

}
