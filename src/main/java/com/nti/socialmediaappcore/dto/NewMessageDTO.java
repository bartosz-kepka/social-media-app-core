package com.nti.socialmediaappcore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class NewMessageDTO {
    @NotBlank(message = "message content cannot be blank or null")
    @ApiModelProperty(required = true, value = "Message content", example = "This is a test message content")
    @Length(max = 1024, message = "Message content cannot be longer than 1024 characters")
    private String content;
}
