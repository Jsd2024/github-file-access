package github.file.access.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
public class YamlController {

    @GetMapping("/fetch-yaml")
    public Map<String, Object> fetchYamlFromGitHub(@RequestParam String url) {
        try {
            // Fetch YAML content from GitHub
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Failed to fetch YAML file from GitHub. Status code: " + response.statusCode());
            }

            String yamlContent = response.body();

            // Parse YAML into a Map
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(yamlContent, Map.class);

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching or parsing YAML: " + e.getMessage(), e);
        }
    }
}
