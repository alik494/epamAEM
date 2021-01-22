package epam.core.job;


import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true,
        service = EventListener.class)
public class RemPropService implements EventListener {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String BASE_PATH = "/var/log/removedProperties/";

    private Session adminSession;

    @Reference
    SlingRepository repository;

    @Reference
    private JobManager jobManager;

    /**
     * The job topic for dropbox job events.
     */
    public static final String JOB_TOPIC = "com/test/job";

    @Activate
    public void activate(ComponentContext context) throws Exception {
        log.info("activating ExampleObservation");
        try {
            adminSession = repository.loginService("customUserID", null);
            adminSession.getWorkspace().getObservationManager().addEventListener(
                    this, //handler
                    Event.PROPERTY_REMOVED, //binary combination of event types
                    "/content/epam", //path
                    true, //is Deep?
                    null, //uuids filter
                    null, //nodetypes filter
                    false);
        } catch (RepositoryException e) {
            log.error("unable to register session", e);
            throw new Exception(e);
        }
    }

    public void onEvent(EventIterator eventIterator) {
        try {
            while (eventIterator.hasNext()) {
                Event event = eventIterator.nextEvent();
                String propertyPath = event.getPath();
                String propertyName = propertyPath.substring(propertyPath.lastIndexOf("/") + 1);
                String propertyDate = Long.toString(event.getDate());

                log.info(" propertyName   : {}", propertyName);
                log.info(" propertyPath   : {}", event.getIdentifier());
                // create payload
                final Map<String, Object> payload = new HashMap<String, Object>();
                payload.put("propertyName", propertyName);
                payload.put("propertyPath", propertyPath);
                payload.put("propertyDate", propertyDate);
                // start job
                this.jobManager.addJob(JOB_TOPIC, payload);
                log.info("the remProp job has been started for: {}", propertyPath);
            }
        } catch (RepositoryException e) {
            log.error("Error while treating events", e);
        }
    }

    @Deactivate
    public void deactivate() {
        if (adminSession != null) {
            adminSession.logout();
        }
    }
}