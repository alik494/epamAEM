package epam.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.*;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sample workflow process that sets an <code>approve</code> property to the payload based on the process argument value.
 */
@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label" + "=My Sample Workflow Process"
        }
)
public class MyProcess implements WorkflowProcess {

    private final Logger logger = LoggerFactory.getLogger(MyProcess.class);
    private static final String TYPE_JCR_PATH = "JCR_PATH";
    public static final Map<String, Object> WRITE_SERVICE = ImmutableMap.of(ResourceResolverFactory.SUBSERVICE, "customUserID");

    @Reference
    private ResourceResolverFactory resolverFactory;

    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        WorkflowData workflowData = item.getWorkflowData();
        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            String path = workflowData.getPayload().toString();
            String pathToContent = path + "/jcr:content";
            try {
                Session jcrSession = session.adaptTo(Session.class);
                Workflow workflow = item.getWorkflow();
                List<HistoryItem> workflowHistory = session.getHistory(workflow);
                String newPath = null;
                for (HistoryItem wor : workflowHistory
                ) {
                    newPath = wor.getWorkItem().getMetaDataMap().get("pathf", String.class);
                    if (newPath != null && !newPath.equals("null")) {
                        Node node = (Node) jcrSession.getItem(pathToContent);
                        if (node != null && !node.hasProperty("pathToMove")) {
                            node.setProperty("pathToMove", newPath);
                            jcrSession.save();
                            movePage(path,newPath);
                        }
                    }
                }
            } catch (RepositoryException e) {
                throw new WorkflowException(e.getMessage(), e);
            }
        }
    }

    public void movePage(String pagePath, String newPagePath) {
        if (newPagePath != null && !newPagePath.equals("null") && !newPagePath.equals("")) {
            Page newPage;
            PageManager pageManager;
            ResourceResolver resolver = null;
            String newPageName = "";
            try {
                resolver = resolverFactory.getServiceResourceResolver(WRITE_SERVICE);
                Resource res = resolver.getResource(pagePath);
                Page page = res.adaptTo(Page.class);
                pageManager = resolver.adaptTo(PageManager.class);
                newPage = pageManager.move(page, newPagePath, null, false, true, null);
                if (newPage != null) {
                    newPageName = newPage.getPageTitle();
                    logger.info("*** PAGE NAME IS " + newPageName);
                } else {
                    logger.info("*** PAGE CANNOT BE MOVED -- ERROR");
                }
            } catch (Exception e) {
                logger.info("ERROR" + e.getMessage());
            }
        }
    }

    boolean isValidPath(String path){
        String newPath=new String(path);
        Pattern pattern = Pattern.compile("([-/A-Za-z])+");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            newPath.substring(matcher.start(), matcher.end());
        }
        return newPath.length() == path.length();
    }
}
