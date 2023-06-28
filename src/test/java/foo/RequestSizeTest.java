package foo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static jakarta.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
public class RequestSizeTest {

    private static final DropwizardAppExtension<BugConfiguration> APP = new DropwizardAppExtension<>(
            BugApplication.class
    );

    @ParameterizedTest
    @ValueSource(ints = {
            72 * 1024,
            (72 * 1024) + 1
    })
    void can_post_more_than_72K_to_the_server(int bytesToSend) throws Exception {

        var body = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                for (int i = 0; i < bytesToSend; i++) {
                    output.write(1);
                }
            }
        };

        try (Response response = new JerseyClientBuilder(APP.getEnvironment())
                .build(RandomStringUtils.randomAlphabetic(10))
                .target("http://localhost:" + APP.getLocalPort())
                .path("/test")
                .request()
                .post(Entity.entity(body, APPLICATION_OCTET_STREAM))) {

            var data = response.readEntity(String.class);
            assert response.getStatus() == 200 : data;
            assertEquals(Map.of("bytes_received", bytesToSend), new ObjectMapper().readValue(data, Map.class));
        }
    }
}
