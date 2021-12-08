package ro.deloittedigital.samekh.postmanagement.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class UserResponse {
    @ApiModelProperty(example = "Jane")
    protected String firstName;
    @ApiModelProperty(example = "Doe")
    protected String lastName;
    private UUID id;
}
