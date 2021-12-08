package ro.deloittedigital.samekh.postmanagement.service;

import ro.deloittedigital.samekh.postmanagement.exception.EmptyFileException;
import ro.deloittedigital.samekh.postmanagement.exception.FileNotImageException;
import ro.deloittedigital.samekh.postmanagement.exception.InvalidImageException;
import ro.deloittedigital.samekh.postmanagement.exception.PostNotFoundException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.exception.UploadFailedException;
import ro.deloittedigital.samekh.postmanagement.model.request.PostRequest;
import ro.deloittedigital.samekh.postmanagement.model.response.PostResponse;

import java.util.List;

public interface PostService {
    void checkById(String id) throws PostNotFoundException;

    PostResponse post(String authorization, PostRequest request) throws ServiceUnavailableException, EmptyFileException,
            FileNotImageException, InvalidImageException, UploadFailedException;

    List<PostResponse> getAll(String authorization) throws ServiceUnavailableException;
}
