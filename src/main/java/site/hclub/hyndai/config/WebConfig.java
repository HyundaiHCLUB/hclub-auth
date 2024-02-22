package site.hclub.hyndai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;



import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

@Configuration
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{
                DataConfig.class,
                AuthenticationConfig.class,
                
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{
                ServletContextConfig.class,
                CorsConfig.class,
        
                // SecurityConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
 
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        
        CorsConfig corsConfig = new CorsConfig();
     
        return new Filter[]{
                characterEncodingFilter,
                corsConfig.corsFilter()
        };
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
       
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                null, 20_971_520, 41_943_040, 20_971_520);
        registration.setMultipartConfig(multipartConfig);
    }
}
