package com.alesto.javanetwork.domain.rest.support;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Based on {@link org.springframework.hateoas.mvc.ResourceAssemblerSupport ResourceAssemblerSupport}.
 */
public abstract class BaseResourceAssembler<T, D extends Resource<?>> implements ResourceAssembler<T, D> {
    
    /*
     * ======================================================================
     * ----- Constants
     * ======================================================================
     */

    private final Class<?> controllerClass;
    
    /*
     * ======================================================================
     * ----- Constructors
     * ======================================================================
     */

    /**
     * Creates a new {@link BaseResourceAssembler} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     */
    public BaseResourceAssembler(Class<?> controllerClass) {
        Assert.notNull(controllerClass);

        this.controllerClass = controllerClass;
    }
    
    /*
     * ======================================================================
     * ----- Methods
     * ======================================================================
     */

    /**
     * Converts all given entities into {@link Resources}.
     *
     * @param entities must not be {@literal null}.
     * @return
     * @see {@link ResourceAssembler#toResource(Object) ResourceAssembler#toResource(Object)}
     */
    public Resources<D> toResources(Iterable<T> entities) {
        Assert.notNull(entities);

        List<D> result = new ArrayList<D>();
        for (T entity : entities) {
            result.add(toResource(entity));
        }

        return new Resources<D>(result);
    }

    /**
     * Creates a new resource with a self link to the given id.
     *
     * @param id     must not be {@literal null}.
     * @param entity must not be {@literal null}.
     * @return
     */
    protected D createResourceWithId(Object id, T entity) {
        return createResourceWithId(id, entity, new Object[0]);
    }

    protected D createResourceWithId(Object id, T entity, Object... parameters) {
        Assert.notNull(entity);
        Assert.notNull(id);

        D instance = instantiateResource(entity);

        instance.add(linkTo(controllerClass, parameters).slash(id).withSelfRel());

        return instance;
    }

    /**
     * Instantiates the resource object.
     *
     * @param entity must not be {@literal null}.
     * @return
     */
    protected abstract D instantiateResource(T entity);

}
