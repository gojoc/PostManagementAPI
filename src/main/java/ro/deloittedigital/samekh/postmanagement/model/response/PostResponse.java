package ro.deloittedigital.samekh.postmanagement.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class PostResponse {
    private UUID id;

    private UserResponse user;

    private LocalDateTime time;

    @ApiModelProperty(example = "This is a text.")
    private String text;

    @ApiModelProperty(example = "https://samekh-photos.s3.eu-central-1.amazonaws.com/photo.jpeg")
    private String photo;

    private Set<String> tags;

    private Set<ReactionResponse> reactions;
}
