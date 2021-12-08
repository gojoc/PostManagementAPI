package ro.deloittedigital.samekh.postmanagement.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.model.response.UserResponse;
import ro.deloittedigital.samekh.postmanagement.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
    @Value(value = "${userManagement.url}")
    private String userManagementUrl;

    @Override
    public UserResponse getInformation(String authorization) throws ServiceUnavailableException {
        try {
            return WebClient.create(userManagementUrl)
                    .get()
                    .header(HttpHeaders.AUTHORIZATION, authorization)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        } catch (WebClientResponseException exception) {
            throw new ServiceUnavailableException("The UserManagement microservice is not available.");
        }
    }
}
