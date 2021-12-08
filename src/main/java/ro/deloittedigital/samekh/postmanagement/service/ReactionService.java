package ro.deloittedigital.samekh.postmanagement.service;

import ro.deloittedigital.samekh.postmanagement.exception.PostNotFoundException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.model.request.ReactionRequest;
import ro.deloittedigital.samekh.postmanagement.model.response.ReactionResponse;

public interface ReactionService {
    ReactionResponse react(String authorization, String post, ReactionRequest request) throws PostNotFoundException,
            ServiceUnavailableException;
}
