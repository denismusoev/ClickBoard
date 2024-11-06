package com.coursework.clickboard.Services;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private String serviceKey = "292a6012292a6012292a6012d92a3213ae2292a292a60124f7def0bc83a1353c93ac08f";
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Async("taskExecutor")
//    public CompletableFuture<VkApiResponse> exchangeSilentAuthToken(String silentToken, String uuid) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                String urlTemplate = "https://api.vk.com/method/auth.exchangeSilentAuthToken?v=5.131&token=%s&access_token=%s&uuid=%s";
//                String url = String.format(urlTemplate, silentToken, serviceKey, uuid);
//                logger.info("Request URL: {}", url);
//
//                HttpGet request = new HttpGet(url);
//                HttpResponse response = httpClient.execute(request);
//                String json = EntityUtils.toString(response.getEntity());
//                logger.info("Received response: {}", json);
//
//                return objectMapper.readValue(json, VkApiResponse.class);
//            } catch (Exception e) {
//                throw new AuthenticationFailureException(e.getMessage());
//            }
//        });
//    }
//
//    @Async("taskExecutor")
//    public CompletableFuture<VkProfileInfo> getProfileInfo(String accessToken) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                String urlTemplate = "https://api.vk.com/method/users.get";
//                String url = UriComponentsBuilder.fromHttpUrl(urlTemplate)
//                        .queryParam("access_token", accessToken)
//                        .queryParam("v", "5.131")
//                        .queryParam("fields", "last_name,first_name")
//                        .build()
//                        .toUriString();
//                logger.info("Request URL: {}", url);
//
//                HttpGet request = new HttpGet(url);
//                HttpResponse response = httpClient.execute(request);
//                String json = EntityUtils.toString(response.getEntity());
//                logger.info("Received response: {}", json);
//
//                return objectMapper.readValue(json, VkProfileInfo.class);
//            } catch (Exception e) {
//                throw new AuthenticationFailureException(e.getMessage());
//            }
//        });
//    }
}