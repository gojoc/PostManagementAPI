package ro.deloittedigital.samekh.postmanagement.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class PostRequest {
    @ApiModelProperty(example = "This is a text.")
    private String text;

    private MultipartFile photo;

    private Set<String> tags;

    @Override
    public String toString() {
        return String.format("PostRequest(text=%s, photo=%s, tags=%s)", text, photo.getOriginalFilename(), tags);
    }
}
