/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package epam.core.models;

import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import io.wcm.testing.mock.aem.junit5.AemContext;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;
import javax.jcr.Session;

import com.day.cq.search.Query;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.mockito.Mockito.mock;


import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import com.day.cq.search.QueryBuilder;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.apache.sling.api.resource.Resource;
//import org.junit.jupiter.api.Test;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class PDFTest {

    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);


    private final AemContext ctx = new AemContext();

    @Inject
    private PDFTextFinder pdfTextFinder;


    @Mock
    private ModelFactory modelFactory;

    @Mock
    private ResourceResolver resourceResolver;

    @Before
    public void setup() throws Exception {
        ctx.addModelsForClasses(PDFTextFinder.class);
        HashMap map=new HashMap();
        Iterator iterable=map.values().iterator();
        ctx.load().json("/com/adobe/aem/guides/epam/core/models/impl/pdftext.json", "/content");
        pdfTextFinder = new PDFTextFinder();
        PrivateAccessor.setField(pdfTextFinder, "pathf", "/content/epam");
        PrivateAccessor.setField(pdfTextFinder, "api", "QueryManager");
        PrivateAccessor.setField(pdfTextFinder, "fragText", "jpg");
    }

//    @Test
//    public void testGetMessage() throws Exception {
//
//
//        Map<String, String> map = new HashMap<>();
//        map.put("group.p.or", "true");
//        map.put("group.1_property", JcrConstants.JCR_TITLE);
//        map.put("group.1_property.value", "true");
//        map.put("group.1_property.operation", "exists");
//        map.put("group.2_property.value", "true");
//        map.put("group.2_property.operation", "exists");
//        Query query = mock(Query.class);
//        QueryBuilder queryBuilder = mock(QueryBuilder.class);
//        SlingRepository repository = mock(SlingRepository.class);
//        PrivateAccessor.setField(pdfTextFinder, "queryBuilder", queryBuilder);
////        PrivateAccessor.setField(mySearchImpl, "repository", repository);
//        Mockito.when(queryBuilder.createQuery(Mockito.any(PredicateGroup.class), Mockito.any(Session.class))).thenReturn(query);
//    }


}

//        @Test
//        public void testGetName() {
//            final String expected = "Jane Doe";
//
//            ctx.currentResource("/content/pdftext");
//            PDFTextFinder byline = ctx.request().adaptTo(PDFTextFinder.class);
//
//            String actual = byline.getPathf();
//
//            assertEquals(expected, actual);
//        }




