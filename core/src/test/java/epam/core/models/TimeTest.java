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

import static org.junit.jupiter.api.Assertions.assertEquals;
import junitx.util.PrivateAccessor;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
public class TimeTest {

    @Inject
    private Time time;
    Date date;


    @Before
    public void setup() throws Exception {
        time = new Time();
        date= new Date();
}
    
//    @Test
//    public void testGetMessage() throws Exception {
//        String message="The time is "+date.toInstant() + "\n" +
//                System.currentTimeMillis();
//        // some very basic junit tests
//        String msg = time.getMessage();
//        assertNotNull(msg);
//        assertEquals(msg,message);
////        assertTrue(msg.length() > 0);
//    }

}
