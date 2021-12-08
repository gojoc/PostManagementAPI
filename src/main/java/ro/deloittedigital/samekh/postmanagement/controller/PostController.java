package ro.deloittedigital.samekh.postmanagement.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.deloittedigital.samekh.postmanagement.exception.EmptyFileException;
import ro.deloittedigital.samekh.postmanagement.exception.FileNotImageException;
import ro.deloittedigital.samekh.postmanagement.exception.InvalidImageException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.exception.UploadFailedException;
import ro.deloittedigital.samekh.postmanagement.model.request.PostRequest;
import ro.deloittedigital.samekh.postmanagement.model.response.PostResponse;
import ro.deloittedigital.samekh.postmanagement.service.PostService;

import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @Operation(summary = "Create post")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "The post was created successfully.",
                    response = PostResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "The request body is not valid."),
            @ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN,
                    message = "You do not have permission to access this resource."),
            @ApiResponse(code = HttpURLConnection.HTTP_INTERNAL_ERROR, message = "Failed to upload image."),
            @ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE,
                    message = "The UserManagement microservice is not available.")
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(path = "posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> post(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                             @Valid @ModelAttribute PostRequest request)
            throws ServiceUnavailableException, EmptyFileException, FileNotImageException, InvalidImageException,
            UploadFailedException {
        log.info("[PostController] post: {}", request);
        PostResponse response = postService.post(authorization, request);
        log.info("[PostController] posted: {}", response);
        return ResponseEntity.created(URI.create(String.format("posts/%s", response.getId()))).body(response);
    }

    @Operation(summary = "Get all posts")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "The posts were retrieved successfully.",
                    responseContainer = "List", response = PostResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN,
                    message = "You do not have permission to access this resource."),
            @ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE,
                    message = "The UserManagement microservice is not available.")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(path = "posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PostResponse>> getAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization)
            throws ServiceUnavailableException {
        log.info("[PostController] get all posts");
        List<PostResponse> responses = postService.getAll(authorization);
        log.info("[PostController] got all posts: {}", responses);
        return ResponseEntity.ok(responses);
    }
}
