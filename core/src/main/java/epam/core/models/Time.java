package epam.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Time {
    private static final Logger log = LoggerFactory.getLogger(Time.class);

    @Inject
    @Named(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
    @Default(values = "No resourceType")
    protected String resourceType;

    private String message="The time is ";
    private  Date date = new Date();

    @Inject
    @Default(values = "/content")
    String pathf;

    @Inject
    @Source("sling-object")
    private ResourceResolver resourceResolver;



    public String getMessage() {
        return message+=date.toInstant() + "\n" +
                System.currentTimeMillis();
    }

}
