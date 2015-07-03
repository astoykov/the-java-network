package com.alesto.javanetwork.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ImportResource(value = {"classpath:/spring/RootConfiguration.xml"})
@Import(value = {
        PropertiesConfiguration.class,
        EmbeddedProfileInfrastructureConfigurationImpl.class, MySqlProfileInfrastructureConfigurationImpl.class
})
@ComponentScan(
        basePackages = {"com.alesto.javanetwork"},
        excludeFilters = {
                @Filter(type = FilterType.REGEX, pattern = {"com.alesto.javanetwork.configuration.*", "com.alesto.javanetwork.web.*"}),
                @Filter(type = FilterType.ANNOTATION, value = {Configuration.class})
        }
)
@EnableTransactionManagement
public class RootConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper;
    }

}
