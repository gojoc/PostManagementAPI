package ro.deloittedigital.samekh.postmanagement.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.deloittedigital.samekh.postmanagement.exception.EmptyFileException;
import ro.deloittedigital.samekh.postmanagement.exception.FileNotImageException;
import ro.deloittedigital.samekh.postmanagement.exception.InvalidImageException;
import ro.deloittedigital.samekh.postmanagement.exception.PostNotFoundException;
import ro.deloittedigital.samekh.postmanagement.exception.ServiceUnavailableException;
import ro.deloittedigital.samekh.postmanagement.exception.UploadFailedException;
import ro.deloittedigital.samekh.postmanagement.model.domain.Post;
import ro.deloittedigital.samekh.postmanagement.model.domain.Tag;
import ro.deloittedigital.samekh.postmanagement.model.request.PostRequest;
import ro.deloittedigital.samekh.postmanagement.model.response.PostResponse;
import ro.deloittedigital.samekh.postmanagement.model.response.ReactionResponse;
import ro.deloittedigital.samekh.postmanagement.model.response.UserResponse;
import ro.deloittedigital.samekh.postmanagement.repository.PostRepository;
import ro.deloittedigital.samekh.postmanagement.service.AmazonService;
import ro.deloittedigital.samekh.postmanagement.service.PostService;
import ro.deloittedigital.samekh.postmanagement.service.TagService;
import ro.deloittedigital.samekh.postmanagement.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.IMAGE_BMP;
import static org.apache.http.entity.ContentType.IMAGE_GIF;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;
import static org.apache.http.entity.ContentType.IMAGE_SVG;
import static org.apache.http.entity.ContentType.IMAGE_TIFF;
import static org.apache.http.entity.ContentType.IMAGE_WEBP;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImplementation implements PostService {
    private static final List<String> IMAGE_TYPES = List.of(IMAGE_PNG.getMimeType(), IMAGE_BMP.getMimeType(),
            IMAGE_GIF.getMimeType(), IMAGE_JPEG.getMimeType(), IMAGE_SVG.getMimeType(), IMAGE_TIFF.getMimeType(),
            IMAGE_WEBP.getMimeType());

    private final UserService userService;
    private final AmazonService amazonService;
    private final TagService tagService;
    private final PostRepository postRepository;

    @Value(value = "${aws.bucket}")
    private String bucket;

    @Value(value = "${aws.minutes}")
    private int minutes;

    private String getPhoto(MultipartFile photo) throws EmptyFileException, FileNotImageException, InvalidImageException,
            UploadFailedException {
        if (photo == null) {
            return null;
        }
        String name = photo.getOriginalFilename();
        if (photo.isEmpty()) {
            throw new EmptyFileException(String.format("The file: '%s' is empty.", name));
        }
        if (!IMAGE_TYPES.contains(photo.getContentType())) {
            throw new FileNotImageException(String.format("The file: '%s' is not an image.", name));
        }
        InputStream inputStream;
        try {
            inputStream = photo.getInputStream();
        } catch (IOException exception) {
            throw new InvalidImageException(String.format("The image: '%s' is not valid.", name));
        }
        return amazonService.upload(bucket, name, inputStream, photo.getContentType(), photo.getSize(), minutes);
    }

    @Override
    public void checkById(String id) throws PostNotFoundException {
        if (!postRepository.existsById(UUID.fromString(id))) {
            throw new PostNotFoundException(String.format("The post: '%s' was not found.", id));
        }
    }

    @Override
    public PostResponse post(String authorization, PostRequest request) throws ServiceUnavailableException,
            EmptyFileException, FileNotImageException, InvalidImageException, UploadFailedException {
        UserResponse userResponse = userService.getInformation(authorization);
        log.info("[PostService] {} {} posts: {}", userResponse.getFirstName(), userResponse.getLastName(), request);
        String photo = getPhoto(request.getPhoto());
        Post post = Post.builder()
                .userId(userResponse.getId())
                .firstName(userResponse.getFirstName())
                .lastName(userResponse.getLastName())
                .time(LocalDateTime.now())
                .text(request.getText())
                .photo(photo)
                .build();
        post = postRepository.save(post);
        Set<String> tags = tagService.saveAll(post.getId(), request.getTags());
        PostResponse postResponse = PostResponse.builder()
                .id(post.getId())
                .user(UserResponse.builder()
                        .id(post.getUserId())
                        .firstName(post.getFirstName())
                        .lastName(post.getLastName())
                        .build())
                .time(post.getTime())
                .text(post.getText())
                .photo(post.getPhoto())
                .tags(tags)
                .build();
        log.info("[PostService] {} {} posted: {}", userResponse.getFirstName(), userResponse.getLastName(),
                postResponse);
        return postResponse;
    }

    @Override
    public List<PostResponse> getAll(String authorization) throws ServiceUnavailableException {
        UserResponse response = userService.getInformation(authorization);
        log.info("[PostService] {} {} gets all posts", response.getFirstName(), response.getLastName());
        List<Post> posts = postRepository.getAllByUserId(response.getId());
        List<PostResponse> responses = posts.stream().map(post -> PostResponse.builder()
                .id(post.getId())
                .user(UserResponse.builder()
                        .id(post.getUserId())
                        .firstName(post.getFirstName())
                        .lastName(post.getLastName())
                        .build())
                .time(post.getTime())
                .text(post.getText())
                .photo(post.getPhoto())
                .tags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .reactions(post.getReactions().stream().map(reaction -> ReactionResponse.builder()
                        .user(UserResponse.builder()
                                .id(reaction.getUserId())
                                .firstName(reaction.getFirstName())
                                .lastName(reaction.getLastName())
                                .build())
                        .reactionType(reaction.getReactionType())
                        .build()).collect(Collectors.toSet()))
                .build()).collect(Collectors.toList());
        log.info("[PostService] {} {} got all posts", response.getFirstName(), response.getLastName());
        return responses;
    }
}
