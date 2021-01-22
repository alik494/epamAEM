package epam.core.workflow;

import java.util.List;
import java.util.Map;

import com.adobe.granite.workflow.exec.*;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(service = ParticipantStepChooser.class, property = {"chooser.label=" + "Dynamic Participant Step Example"})
public class DynamicParticipantStepExample implements ParticipantStepChooser {

    private final Logger logger = LoggerFactory.getLogger(DynamicParticipantStepExample.class);
    private static final String TYPE_JCR_PATH = "JCR_PATH";
    public static final Map<String, Object> WRITE_SERVICE = ImmutableMap.of(ResourceResolverFactory.SUBSERVICE, "customUserID");

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
            throws WorkflowException {
        logger.info("----------< [{}] >----------", this.getClass().getName());
        String participant = "admin";

        if (isMove(workItem, workflowSession, metaDataMap)) {
            // Setting the administrators group to the participant
            participant = "editor";
        } else {
            participant = "administrators";
        }
        logger.info("----------< Participant: {} >----------", participant);
        return participant;
    }

    private boolean isMove(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        logger.info("worflowItem {}" +
                "WorkflowSession {}" +
                "MetaDataMap {}", workItem, workflowSession, metaDataMap);
        ResourceResolver resolver = null;
        try {
            resolver = resolverFactory.getServiceResourceResolver(WRITE_SERVICE);
            WorkflowData workflowData = workItem.getWorkflowData();
            if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
                String path = workflowData.getPayload().toString() + "/jcr:content";
                Resource res = resolver.getResource(path);
                ValueMap properties = res.getValueMap();
                logger.info("properties " + properties.toString());
                if (properties.containsKey("pathToMove")) {
                    return true;
                }
            }
        } catch (LoginException e) {
            logger.error("Very big error" + e);
        }
        return false;
    }
}
