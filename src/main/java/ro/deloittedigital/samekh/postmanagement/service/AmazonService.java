package ro.deloittedigital.samekh.postmanagement.service;

import ro.deloittedigital.samekh.postmanagement.exception.UploadFailedException;

import java.io.InputStream;

public interface AmazonService {
    String upload(String path, String name, InputStream inputStream, String contentType, long contentLength,
                  int minutes) throws UploadFailedException;
}
