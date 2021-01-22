package epam.core.job;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Map;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=com/test/job"
        },
        immediate = true
)
public class RemPropHandler implements JobConsumer {
    Logger log = LoggerFactory.getLogger(this.getClass());
    public static final Map<String, Object> WRITE_SERVICE = ImmutableMap.of(ResourceResolverFactory.SUBSERVICE, "customUserID");
    private static final String BASE_PATH = "/var/log/removedProperties/";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public JobResult process(Job job) {
        try {
            final String resourcePath = (String) job.getProperty("propertyPath");
            final String propertyName = (String) job.getProperty("propertyName");
            final String propertyDate = (String) job.getProperty("propertyDate");
            log.info("in jobConsumer of remProp" + propertyName);
            ResourceResolver resolver = resolverFactory.getServiceResourceResolver(WRITE_SERVICE);
            Session session = resolver.adaptTo(Session.class);
            Node node = JcrUtil.createPath(BASE_PATH + propertyDate + propertyName, JcrConstants.NT_UNSTRUCTURED, session);
            node.setProperty("name", propertyName);
            node.setProperty("path", resourcePath);
            session.save();
            return JobResult.OK;
        } catch (final Exception e) {
            log.error("Exception: " + e, e);
            return JobResult.FAILED;
        }
    }
}
