package epam.core.listeners;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Component(
        service = ResourceChangeListener.class,
        property = {
                // filter the notifications by path in the repo. Can be array and supports globs
                ResourceChangeListener.PATHS + "=" + "/content/epam",
                //The type of change you want to listen to.
                //Possible values at https://sling.apache.org/apidocs/sling9/org/apache/sling/api/resource/observation/ResourceChange.ChangeType.html.
                ResourceChangeListener.CHANGES + "=" + "ADDED",
                ResourceChangeListener.CHANGES + "=" + "REMOVED",
//                ResourceChangeListener.CHANGES + "=" + "CHANGED"
                //PS: If you want to declare multiple values for a prop, you repeat it in OSGI R6 annotations.
                //https://stackoverflow.com/questions/41243873/osgi-r6-service-component-annotations-property-list#answer-41248826
        }
)
public class SampleResourceChangeListener implements ResourceChangeListener { // Use ExternalResourceChangeListener to listen for changes that happen in a different node
    public static final Logger LOGGER = LoggerFactory.getLogger(SampleResourceChangeListener.class);
    public static final Map<String, Object> WRITE_SERVICE = ImmutableMap.of(ResourceResolverFactory.SUBSERVICE, "customUserID");

    //This method will be called with paths and types of change that occurred
    //The task in this should be fast. In case it takes more time, trigger a sling job from here.
    //The listener can be blacklisted if it's slow [it does for event handler, should be same for ResourceListener IMHO]

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void onChange(List<ResourceChange> list) {
        list.forEach((change) -> {
            LOGGER.info("test " + change.getPath());
            LOGGER.info(change.getType().toString());
            ResourceResolver resolver = null;
            try {
                resolver = resolverFactory.getServiceResourceResolver(WRITE_SERVICE);
                Resource res = resolver.getResource("/content/epam/");
                Iterator<Resource> it = res.listChildren();
                while (it.hasNext()) {
                    res = it.next();
                    ValueMap properties = res.getValueMap();
                    if (properties.get("jcr:primaryType", String.class).equals("cq:Page")) {
                        Resource resInner = resolver.getResource(res.getPath());
                        Iterator<Resource> itInner = resInner.listChildren();
                        while (itInner.hasNext()) {
                            resInner = itInner.next();
                            ValueMap propPage = resInner.getValueMap();
                            if (propPage.containsKey("jcr:description")) {
                               LOGGER.info("description not empty inner");
                               PageManager pageManager = resolver.adaptTo(PageManager.class);
                               Page page = pageManager.getContainingPage(resInner);
                               LOGGER.info(page.getTitle());
                               LOGGER.info(page.getDescription());
                               pageManager.createRevision(page);
                               LOGGER.info(page.getTitle() + " revision created");
                            }
                        }
                    }
                }
            } catch (LoginException e) {
                LOGGER.error("LoginException", e);
            } catch (WCMException e) {
                LOGGER.error("WCMException", e);
            } finally {
                if (resolver != null && resolver.isLive()) {
                    resolver.close();
                }
            }
        });
    }
}