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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ro.deloittedigital.samekh.postmanagement.exception.PostNotFoundException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.model.request.ReactionRequest;
import ro.deloittedigital.samekh.postmanagement.model.response.ReactionResponse;
import ro.deloittedigital.samekh.postmanagement.service.ReactionService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.HttpURLConnection;

@RestController
@Validated
@AllArgsConstructor
@Slf4j
public class ReactionController {
    private final ReactionService reactionService;

    @Operation(summary = "React to post")
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "The reaction was added successfully.",
                    response = ReactionResponse.class),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "The request body is not valid."),
            @ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN,
                    message = "You do not have permission to access this resource."),
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "The post was not found."),
            @ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE,
                    message = "The UserManagement microservice is not available.")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(path = "reactions/{post}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReactionResponse> react(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                                  @NotBlank @PathVariable String post,
                                                  @Valid @RequestBody ReactionRequest request)
            throws PostNotFoundException, ServiceUnavailableException {
        log.info("[ReactionController] react: {} - {}", post, request);
        ReactionResponse response = reactionService.react(authorization, post, request);
        log.info("[ReactionController] reacted: {} - {}", post, response);
        return ResponseEntity.ok(response);
    }
}
