package com.nti.socialmediaappcore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class NewChatDTO {
    @NotNull(message = "property membersIds is required")
    @Size(min = 1, message = "Chat must have at least 2 members")
    @ApiModelProperty(required = true, value = "Chat members")
    private Set<String> membersIds;
}
