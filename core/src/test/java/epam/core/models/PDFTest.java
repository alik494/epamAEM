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

import io.wcm.testing.mock.aem.junit5.AemContext;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import static org.mockito.Mockito.mock;


import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

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
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
public class PDFTest {

    private final AemContext ctx = new AemContext();

    @Inject
    private PDFTextFinder pdfTextFinder;


    @Mock
    private ModelFactory modelFactory;

    @Before
    public void setup() throws Exception {
        ctx.addModelsForClasses(PDFTextFinder.class);
        ctx.load().json("/com/adobe/aem/guides/epam/core/models/impl/pdftext.json", "/content");
        pdfTextFinder = new PDFTextFinder();
        PrivateAccessor.setField(pdfTextFinder, "pathf", "/content/epam");
        PrivateAccessor.setField(pdfTextFinder, "api", "QueryManager");
        PrivateAccessor.setField(pdfTextFinder, "fragText", "jpg");
    }

//    @Test
//    public void testGetMessage() throws Exception {
//        // some very basic junit tests
//        String msg =  pdfTextFinder.getQuery();
//        assertNotNull(msg);
//        assertTrue(msg.length() > 0);
//    }

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

}
