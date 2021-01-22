package epam.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.*;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PDFTextFinder {
    private static final Logger log = LoggerFactory.getLogger(PDFTextFinder.class);
    @Inject
    private SlingSettingsService settings;

    @Inject
    @Named(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
    @Default(values = "No resourceType")
    protected String resourceType;

    private String message;

    @Inject
    @Default(values = "/content")
    String pathf;

    @Inject
    @Default(values = "pdf")
    String fragText;

    @Inject
    @Default(values = "QueryManager")
    String api;

    @Inject
    private QueryBuilder builder;

    @Inject
    @Source("sling-object")
    private ResourceResolver resourceResolver;

    private Session session;

    private List <String> list=new ArrayList();


    @PostConstruct
    protected void init() {
        message = "\tHello World! from PDFTextFinder\n";
        message += "\tThis is instance: " + settings.getSlingId() + "\n";
        message += "\tResource type is: " + resourceType + "\n" + pathf;
    }

    public String getPathf() {
        return pathf;
    }

    public String getQuery() {
        switch (api) {
            case "QueryBuilder":
                return getPathToFileByQueryBuilder(fragText,pathf);
            case "QueryManager":
                return getPathToFileByQueryManager(fragText,pathf);
            default:
                return "no such API";
        }
    }

    public String getMessage() {
        return message;
    }

    public String getPathToFileByQueryManager(String textFragment,String path) {
        String response = "string default";
        StringBuilder sb = new StringBuilder("By QueryManager:");
        try {
            session = resourceResolver.adaptTo(Session.class);
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            String sqlStatement = "SELECT * FROM [dam:Asset] AS s  WHERE contains(s.*, \""+textFragment+"\") AND ISDESCENDANTNODE(s,\""+path+"\")";
            javax.jcr.query.Query query = queryManager.createQuery(sqlStatement, "JCR-SQL2");
            QueryResult result = query.execute();
            NodeIterator nodeIter = result.getNodes();
            while (nodeIter.hasNext()) {
                Node node = nodeIter.nextNode();
                list.add(node.getPath());
                sb.append(node.getPath()).append(System.lineSeparator());
            }
            response = sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    public String getPathToFileByQueryBuilder(String fileName, String pathToFile) {
        String response = "start ";
        StringBuilder sb = new StringBuilder("By QueryBuilder:");
        try {
            session = resourceResolver.adaptTo(Session.class);
            Map<String, String> predicate = new HashMap<>();
            predicate.put("path", pathToFile);
            predicate.put("type", "dam:Asset");
            predicate.put("fulltext",fileName);
            predicate.put("group.p.or", "true");
            Query query = builder.createQuery(PredicateGroup.create(predicate), session);
            query.setStart(0);
            query.setHitsPerPage(20);
            SearchResult searchResult = query.getResult();
            for (Hit hit : searchResult.getHits()) {
                String path = hit.getPath();
                list.add(path);
                sb.append(path).append(System.lineSeparator());
            }
            response = sb.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    public List <String> getList(){
        return list;
    }
}
