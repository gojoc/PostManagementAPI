package ro.deloittedigital.samekh.postmanagement.service.implementation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.deloittedigital.samekh.postmanagement.exception.PostNotFoundException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.model.domain.Reaction;
import ro.deloittedigital.samekh.postmanagement.model.request.ReactionRequest;
import ro.deloittedigital.samekh.postmanagement.model.response.ReactionResponse;
import ro.deloittedigital.samekh.postmanagement.model.response.UserResponse;
import ro.deloittedigital.samekh.postmanagement.repository.ReactionRepository;
import ro.deloittedigital.samekh.postmanagement.service.PostService;
import ro.deloittedigital.samekh.postmanagement.service.ReactionService;
import ro.deloittedigital.samekh.postmanagement.service.UserService;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ReactionServiceImplementation implements ReactionService {
    private final PostService postService;
    private final UserService userService;
    private final ReactionRepository reactionRepository;

    @Override
    public ReactionResponse react(String authorization, String post, ReactionRequest request)
            throws PostNotFoundException, ServiceUnavailableException {
        UserResponse userResponse = userService.getInformation(authorization);
        log.info("[ReactionService] {} {} reacts: {} - {}", userResponse.getFirstName(), userResponse.getLastName(),
                post, request);
        postService.checkById(post);
        Reaction reaction = reactionRepository.getByPostIdAndUserId(UUID.fromString(post), userResponse.getId()).
                orElse(Reaction.builder()
                        .postId(UUID.fromString(post))
                        .userId(userResponse.getId())
                        .firstName(userResponse.getFirstName())
                        .lastName(userResponse.getLastName())
                        .build());
        reaction.setReactionType(request.getReactionType());
        reaction = reactionRepository.save(reaction);
        ReactionResponse reactionResponse = ReactionResponse.builder()
                .user(UserResponse.builder()
                        .id(reaction.getUserId())
                        .firstName(reaction.getFirstName())
                        .lastName(reaction.getLastName())
                        .build())
                .reactionType(reaction.getReactionType())
                .build();
        log.info("[ReactionService] {} {} reacted: {} - {}", userResponse.getFirstName(), userResponse.getLastName(),
                post, reactionResponse);
        return reactionResponse;
    }
}
