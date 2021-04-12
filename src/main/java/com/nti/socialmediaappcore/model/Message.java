package com.nti.socialmediaappcore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String id;

    private String senderId;

    private LocalDateTime date;

    private String content;
}
