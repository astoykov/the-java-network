package com.alesto.javanetwork.configuration;

import com.alesto.javanetwork.domain.rest.support.FollowsResourceAssembler;
import com.alesto.javanetwork.domain.rest.support.TimelineResourceAssembler;
import com.alesto.javanetwork.service.DataExchangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Import(value = {
        PropertiesConfiguration.class
})
@ComponentScan(basePackages = {"com.alesto.javanetwork.web","com.alesto.javanetwork.domain.rest.support"})
@EnableSpringDataWebSupport
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class TestWebConfiguration extends DelegatingWebMvcConfiguration {
    
    /*
     * ======================================================================
     * ----- Fields
     * ======================================================================
     */

    @Inject
    private BeanFactory beanFactory;

    @Inject
    private MessageSource messageSource;


    @Bean
    public DataExchangeService dataExchangeService()
    {
       return Mockito.mock(DataExchangeService.class);
    }

    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }

    /*
     * ======================================================================
     * ----- RequestMappingHandlerMapping
     * ======================================================================
     */

    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();

        // Enable @MatrixVariable support
        requestMappingHandlerMapping.setRemoveSemicolonContent(false);
        // Path Matching
        requestMappingHandlerMapping.setUseTrailingSlashMatch(true);

        return requestMappingHandlerMapping;
    }
    
    /*
     * ======================================================================
     * ----- Interceptors
     * ======================================================================
     */

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);

        // Cache Control is provided by Spring Security
        // use WebContentInterceptor only when Spring Security is not active for the URL(s)
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        webContentInterceptor.setUseExpiresHeader(true);
        webContentInterceptor.setUseCacheControlHeader(true);
        webContentInterceptor.setUseCacheControlNoStore(true);
        registry.addInterceptor(webContentInterceptor).addPathPatterns("/api/**");
    }
    
    /*
     * ======================================================================
     * ----- Content Negotiation
     * ======================================================================
     */

    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        super.configureContentNegotiation(configurer);

        configurer
                .favorPathExtension(true)
                .favorParameter(true)
                .parameterName("format")
                .ignoreAcceptHeader(false)
                .mediaType("html", MediaType.TEXT_HTML)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("hal", new MediaType("application", "hal+json"))
                .mediaType("xml", MediaType.APPLICATION_XML)
                .defaultContentType(MediaType.TEXT_HTML);
    }

    @Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        // Define the view resolvers and default views
        List<ViewResolver> viewResolvers = new ArrayList<>();

        // html
        InternalResourceViewResolver jspViewResolver = new InternalResourceViewResolver();
        jspViewResolver.setPrefix("/WEB-INF/views/");
        jspViewResolver.setSuffix(".jsp");
        viewResolvers.add(jspViewResolver);

        // create ContentNegotiatingViewResolver
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
        resolver.setViewResolvers(viewResolvers);

        return resolver;
    }

    /*
     * ======================================================================
     * ----- HTTP Message Converters
     * ======================================================================
     */

    private class HalMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

        public HalMappingJackson2HttpMessageConverter() {
            setSupportedMediaTypes(Arrays.asList(
                    new MediaType("application", "hal+json", Charset.defaultCharset()),
                    new MediaType("application", "*+hal+json", Charset.defaultCharset())
            ));

            ObjectMapper halObjectMapper = beanFactory.getBean("_halObjectMapper", ObjectMapper.class);
            setObjectMapper(halObjectMapper);
        }

    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        List<HttpMessageConverter<?>> allMessageConverters = new ArrayList<>();

        // --- get default HttpMessageConverters
        List<HttpMessageConverter<?>> defaultMessageConverters = new ArrayList<>();
        addDefaultHttpMessageConverters(defaultMessageConverters);
        allMessageConverters.addAll(defaultMessageConverters);

        // --- custom HttpMessageConverters
        // jsonConverter
        // https://github.com/spring-projects/spring-hateoas/issues/263
        // https://github.com/spring-projects/spring-hateoas/issues/264
        HalMappingJackson2HttpMessageConverter jsonHalConverter = new HalMappingJackson2HttpMessageConverter();
        allMessageConverters.add(jsonHalConverter);

        // --- configure HttpMessageConverters
        for (HttpMessageConverter<?> currentHttpMessageConverter : allMessageConverters) {
            if (currentHttpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                        (MappingJackson2HttpMessageConverter) currentHttpMessageConverter;

                mappingJackson2HttpMessageConverter.setPrettyPrint(true);
            }

            converters.add(currentHttpMessageConverter);
        }
    }

}
