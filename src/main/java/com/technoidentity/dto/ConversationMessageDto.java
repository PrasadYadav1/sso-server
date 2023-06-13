package com.technoidentity.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationMessageDto {

    @NotNull(message = "userId is mandatory")
    private UUID userId;

    @NotNull(message = "conversationId is mandatory")
    private UUID conversationId;

    @NotBlank(message = "body is mandatory")
    private String body;

    private List<String> media;

    private UUID parentMessageId;

    @NotNull(message = "conversationDate is mandatory")
    private Date conversationDate;


}
