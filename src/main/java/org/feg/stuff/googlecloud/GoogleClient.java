package org.feg.stuff.googlecloud;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class GoogleClient {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Properties properties;
    String apiURL;

    GoogleClient(String propertyName) throws IOException, GeneralSecurityException {
        InputStream propsIn = Main.class.getClassLoader().getResourceAsStream(propertyName);

        properties = new Properties();
        properties.load(propsIn);

        apiURL = properties.getProperty("google.api.url", "");

        Compute computeService = createComputeService();
    }

    public List<String> stopInstances() throws IOException, GeneralSecurityException {

        String project = properties.getProperty("project.name", "");
        String zone = properties.getProperty("project.zone", "");
        String instance = properties.getProperty("project.instance", "");

        List<String> instances = new ArrayList<>();
        List<String> responses = new ArrayList<>();

        Compute computeService = createComputeService();

        if (instance.contains(",")){
            instances = Arrays.asList(instance.split(","));

            for (String inst : instances) {

                Compute.Instances.Stop request = computeService.instances().stop(project, zone, inst);
                Operation response = request.execute();
                responses.add(response.getStatus() + " - " + response.getStatusMessage());
            }
        } else {

            Compute.Instances.Stop request = computeService.instances().stop(project, zone, instance);
            Operation response = request.execute();
            responses.add(response.getStatus() + " - " + response.getStatusMessage());
        }

        return responses;
    }

    public List<String> startInstances() throws IOException, GeneralSecurityException {

        String project = properties.getProperty("project.name", "");
        String zone = properties.getProperty("project.zone", "");
        String instance = properties.getProperty("project.instance", "");

        List<String> instances = new ArrayList<>();
        List<String> responses = new ArrayList<>();

        Compute computeService = createComputeService();

        if (instance.contains(",")){
            instances = Arrays.asList(instance.split(","));

            for (String inst : instances) {

                Compute.Instances.Start request = computeService.instances().start(project, zone, inst);
                Operation response = request.execute();
                responses.add(response.getStatus() + " - " + response.getStatusMessage());
            }
        } else {

            Compute.Instances.Start request = computeService.instances().start(project, zone, instance);
            Operation response = request.execute();
            responses.add(response.getStatus() + " - " + response.getStatusMessage());
        }

        return responses;
    }

    public Compute createComputeService() throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleCredential credential = GoogleCredential.getApplicationDefault();
        if (credential.createScopedRequired()) {
            credential = credential.createScoped(
                    Arrays.asList(properties.getProperty("google.oaut.url", "")));
        }

        return new Compute.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Google-ComputeSample/0.1")
                .build();
    }


}
