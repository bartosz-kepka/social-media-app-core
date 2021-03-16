package com.nti.socialmediaappcore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class NewPostDTO {
    @NotBlank(message = "post content cannot be blank or null")
    @ApiModelProperty(required = true, value = "Post content", example = "This is a test post content")
    @Length(max = 1024, message = "Post content cannot be longer than 1024 characters")
    private String content;
}
