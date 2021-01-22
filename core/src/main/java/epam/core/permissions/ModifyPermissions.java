package epam.core.permissions;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.felix.scr.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlList;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.models.annotations.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ModifyPermissions {
    private static final String CONTENT_SITE_FR = "/content/we-retail/fr";
    private static final Logger LOGGER= LoggerFactory.getLogger(ModifyPermissions.class);
    public static final Map<String, Object> WRITE_SERVICE = ImmutableMap.of(ResourceResolverFactory.SUBSERVICE, "customUserID");
    @Reference
    private SlingRepository repo;
    @Activate
    protected void activate(){
        LOGGER.info("ModifyPermissions activated");
        modifyPermissions();
    }


    @Reference
    private ResourceResolverFactory resolverFactory;

    private Session session;

    private void modifyPermissions() {
        Session adminSession = null;
        ResourceResolver resolver = null;
        try{
            //Don’t use loginAdministrative in real application, it’s deprecated
            //loginAdministrative requires whitelisting starting with AEM 6.3 https://sling.apache.org/documentation/the-sling-
            //engine/service-authentication.html#whitelisting-bundles-for-administrative-login
            resolver = resolverFactory.getServiceResourceResolver(WRITE_SERVICE);
            adminSession = resolver.adaptTo(Session.class);
//            adminSession= repo.loginAdministrative(null);
            UserManager userMgr=
                    ((org.apache.jackrabbit.api.JackrabbitSession)adminSession).getUserManager();
            AccessControlManager accessControlManager = adminSession.getAccessControlManager();
            Authorizable denyAccess = userMgr.getAuthorizable("deny-access");
            AccessControlPolicyIterator policyIterator =
                    accessControlManager.getApplicablePolicies(CONTENT_SITE_FR);
            AccessControlList acl;
            try{
//                acl=(JackrabbitAccessControlList)
//                        accessControlManager.getPolicies(CONTENT_SITE_FR)[0];
                acl=(JackrabbitAccessControlList)
                        policyIterator.nextAccessControlPolicy();
            }catch(Exception nse){
                acl=(JackrabbitAccessControlList)
                        accessControlManager.getPolicies(CONTENT_SITE_FR)[0];
            }
            Privilege[] privileges =
                    {accessControlManager.privilegeFromName(Privilege.JCR_READ)};
            acl.addAccessControlEntry(denyAccess.getPrincipal(), privileges);
            accessControlManager.setPolicy(CONTENT_SITE_FR, acl);
            adminSession.save();
        }catch (RepositoryException | LoginException e){
            LOGGER.error("**************************Repo Exception", e);
        }finally{
            if (adminSession != null)
                adminSession.logout();
        }
    }
}