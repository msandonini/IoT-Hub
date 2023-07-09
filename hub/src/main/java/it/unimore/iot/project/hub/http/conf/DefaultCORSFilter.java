package it.unimore.iot.project.hub.http.conf;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class DefaultCORSFilter implements ContainerResponseFilter {

    public class Config {
        public static final String ALLOWED_ORIGINS = "*";
        public static final String ALLOWED_HEADERS = "*";
        public static final String ALLOWED_METHODS = "*";
        public static final String EXPOSED_HEADERS = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Access-Control-Allow-Origin";
        public static final boolean ALLOW_CREDENTIALS = false;

        public static final int ACCESS_CONTROL_MAX_AGE = 86400;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, Config.ALLOWED_ORIGINS);
        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, Config.ALLOWED_HEADERS);
        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, Config.ALLOWED_METHODS);
        headers.add(CrossOriginFilter.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, Config.EXPOSED_HEADERS);
        headers.add(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, String.valueOf(Config.ALLOW_CREDENTIALS));
        headers.add(CrossOriginFilter.ACCESS_CONTROL_MAX_AGE_HEADER, String.valueOf(Config.ACCESS_CONTROL_MAX_AGE));
    }
}