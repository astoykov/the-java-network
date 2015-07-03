package com.alesto.javanetwork.domain.rest.support;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.alesto.javanetwork.domain.entity.Timeline;
import com.alesto.javanetwork.domain.rest.resource.TimelineResource;
import com.alesto.javanetwork.web.controller.rest.v1.ApiEndpoints;
import com.alesto.javanetwork.web.controller.rest.v1.RestTimelineController;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.UriTemplate;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named("timelineResourceAssembler")
public class TimelineResourceAssembler extends BaseResourceAssembler<Timeline, Resource<TimelineResource>> {
    
    /*
     * ======================================================================
     * ----- Constants
     * ======================================================================
     */

    /*
     * ======================================================================
     * ----- Fields
     * ======================================================================
     */

    @Inject
    private ModelMapper modelMapper;
    
    /*
     * ======================================================================
     * ----- Constructors
     * ======================================================================
     */

    public TimelineResourceAssembler() {
        super(RestTimelineController.class);
    }
    
    /*
     * ======================================================================
     * ----- Methods
     * ======================================================================
     */

    @Override
    public Resource<TimelineResource> toResource(Timeline entity) {
        Resource<TimelineResource> resource = createResourceWithId(entity.getId(), entity);

        Map<String, String> uriVariables = new HashMap<>(1);
        uriVariables.put("userId", entity.getUsername());
        Link selfLink = new Link(new UriTemplate(ApiEndpoints.TIMELINE_INDEX).expand(uriVariables).toString() + "/" + entity.getId(), Link.REL_SELF);
        resource.add(selfLink);

        return resource;
    }

    @Override
    protected Resource<TimelineResource> createResourceWithId(Object id, Timeline entity, Object... parameters) {
        Assert.notNull(entity);
        Assert.notNull(id);

        Resource<TimelineResource> instance = instantiateResource(entity);

        return instance;
    }

    @Override
    protected Resource<TimelineResource> instantiateResource(Timeline entity) {
        TimelineResource entityResource = modelMapper.map(entity, TimelineResource.class);
        modelMapper.validate();

        return new Resource<TimelineResource>(entityResource);
    }

}
