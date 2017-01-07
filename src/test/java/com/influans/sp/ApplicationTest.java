package com.influans.sp;

import com.mongodb.BasicDBObject;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.mockito.MockitoAnnotations.initMocks;

@ActiveProfiles("test")
@WebAppConfiguration
@IntegrationTest
public abstract class ApplicationTest extends SpringTest {

    @Value("${server.address:localhost}")
    protected String serverAddress;
    @Value("${server.port:8080}")
    protected String serverPort;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception {
        clearMongo();
        initMocks(this);
    }

    /**
     * Remove all data from collections (expect system collections). Faster than
     * a simple dropDatabase.
     */
    private void clearMongo() throws Exception {

        mongoTemplate.getDb().getCollectionNames().stream().filter(name -> !name.startsWith("system.")).forEach(name -> {
            mongoTemplate.getDb().getCollection(name).remove(new BasicDBObject());
        });
    }

    protected String serverUrl() {
        return "http://" + serverAddress + ":" + serverPort + "/";
    }

}
