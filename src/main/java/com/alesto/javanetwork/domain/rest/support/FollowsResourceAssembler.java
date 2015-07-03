package com.alesto.javanetwork.domain.rest.support;

import com.alesto.javanetwork.domain.entity.Follows;
import com.alesto.javanetwork.domain.rest.resource.FollowsResource;
import com.alesto.javanetwork.web.controller.rest.v1.ApiEndpoints;
import com.alesto.javanetwork.web.controller.rest.v1.RestFollowsController;
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

@Named("followsResourceAssembler")
public class FollowsResourceAssembler extends BaseResourceAssembler<Follows, Resource<FollowsResource>> {

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

    public FollowsResourceAssembler() {
        super(RestFollowsController.class);
    }
    
    /*
     * ======================================================================
     * ----- Methods
     * ======================================================================
     */

    @Override
    public Resource<FollowsResource> toResource(Follows entity) {
        Resource<FollowsResource> resource = createResourceWithId(entity.getId(), entity);

        Map<String, String> uriVariables = new HashMap<>(1);
        uriVariables.put("userId", entity.getUsername());
        Link selfLink = new Link(new UriTemplate(ApiEndpoints.FOLLOWS_INDEX).expand(uriVariables).toString() + "/" + entity.getId(), Link.REL_SELF);
        resource.add(selfLink);

        return resource;
    }

    @Override
    protected Resource<FollowsResource> createResourceWithId(Object id, Follows entity, Object... parameters) {
        Assert.notNull(entity);
        Assert.notNull(id);

        Resource<FollowsResource> instance = instantiateResource(entity);

        return instance;
    }

    @Override
    protected Resource<FollowsResource> instantiateResource(Follows entity) {
        FollowsResource followsResource = modelMapper.map(entity, FollowsResource.class);
        modelMapper.validate();

        return new Resource<FollowsResource>(followsResource);
    }

}
