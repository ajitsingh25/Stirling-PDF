package stirling.software.SPDF.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import lombok.RequiredArgsConstructor;

import stirling.software.common.configuration.InstallationPathConfig;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final EndpointInterceptor endpointInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(endpointInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handler for new Vue.js frontend
        registry.addResourceHandler("/**")
                .addResourceLocations("file:/app/frontend/dist/")
                .resourceChain(true)
                .addResolver(
                        new PathResourceResolver() {
                            @Override
                            protected Resource getResource(String resourcePath, Resource location)
                                    throws IOException {
                                Resource requestedResource = location.createRelative(resourcePath);
                                return requestedResource.exists() && requestedResource.isReadable()
                                        ? requestedResource
                                        : new FileSystemResource("/app/frontend/dist/index.html");
                            }
                        });

        // Handler for legacy static resources
        registry.addResourceHandler("/legacy/**")
                .addResourceLocations(
                        "file:" + InstallationPathConfig.getStaticPath(), "classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward the root path to the new frontend's index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}
