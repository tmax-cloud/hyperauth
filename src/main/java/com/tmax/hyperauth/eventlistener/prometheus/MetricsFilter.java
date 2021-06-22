package com.tmax.hyperauth.eventlistener.prometheus;

import org.jboss.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class MetricsFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOG = Logger.getLogger(MetricsFilter.class);

    private static final String METRICS_REQUEST_TIMESTAMP = "metrics.requestTimestamp";
    private static final MetricsFilter INSTANCE = new MetricsFilter();

    // relevant response content types to be measured
    private static final Set<MediaType> contentTypes = new HashSet<>();
    static {
        contentTypes.add(MediaType.APPLICATION_JSON_TYPE);
        contentTypes.add(MediaType.APPLICATION_XML_TYPE);
        contentTypes.add(MediaType.TEXT_HTML_TYPE);
    }
    private static final Set<MediaType> CONTENT_TYPES = Collections.unmodifiableSet(contentTypes);

    public static MetricsFilter instance() {
        return INSTANCE;
    }

    private MetricsFilter() { }

    @Override
    public void filter(ContainerRequestContext req) {
        req.setProperty(METRICS_REQUEST_TIMESTAMP, System.currentTimeMillis());
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        int status = res.getStatus();

        String resource = ResourceExtractor.getResource(req.getUriInfo());

        PrometheusExporter.instance().recordResponseTotal(status, req.getMethod(), resource);
        if (status >= 400) {
            PrometheusExporter.instance().recordResponseError(status, req.getMethod(), resource);
        }

        // Record request duration if timestamp property is present
        // and only if it is relevant (skip pictures)
        if (req.getProperty(METRICS_REQUEST_TIMESTAMP) != null &&
            contentTypeIsRelevant(res)) {
            long time = (long) req.getProperty(METRICS_REQUEST_TIMESTAMP);
            long dur = System.currentTimeMillis() - time;
            LOG.trace("Duration is calculated as " + dur + " ms.");
            PrometheusExporter.instance().recordRequestDuration(dur, req.getMethod(), resource);
        }
    }

    private boolean contentTypeIsRelevant(ContainerResponseContext responseContext) {
        LOG.trace("Check if is response is relevant " + responseContext.getMediaType());
        boolean ret = responseContext.getMediaType() != null &&
            CONTENT_TYPES.stream().anyMatch(type -> type.isCompatible(responseContext.getMediaType()));
        LOG.trace("Result is " + ret);
        return ret;
    }
}
