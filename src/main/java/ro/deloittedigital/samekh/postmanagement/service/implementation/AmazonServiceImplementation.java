package ro.deloittedigital.samekh.postmanagement.service.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import ro.deloittedigital.samekh.postmanagement.exception.UploadFailedException;
import ro.deloittedigital.samekh.postmanagement.service.AmazonService;

import java.io.InputStream;

@Service
@AllArgsConstructor
@Slf4j
public class AmazonServiceImplementation implements AmazonService {
    private final AmazonS3 amazonS3;

    @Override
    public String upload(String path, String name, InputStream inputStream, String contentType, long contentLength,
                         int minutes) throws UploadFailedException {
        log.info("[Amazon service] upload file: {}/{}", path, name);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(contentLength);
        try {
            amazonS3.putObject(path, name, inputStream, objectMetadata);
        } catch (AmazonServiceException exception) {
            throw new UploadFailedException(String.format("Failed to upload file: %s/%s.", path, name));
        }
        log.info("[Amazon service] uploaded file: {}/{}", path, name);
        return amazonS3.generatePresignedUrl(path, name, new DateTime().plusMinutes(minutes)
                        .toDate())
                .toExternalForm();
    }
}
