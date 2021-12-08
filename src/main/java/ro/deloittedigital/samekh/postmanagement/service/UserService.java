package ro.deloittedigital.samekh.postmanagement.service;

import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.model.response.UserResponse;

public interface UserService {
    UserResponse getInformation(String authorization) throws ServiceUnavailableException;
}
