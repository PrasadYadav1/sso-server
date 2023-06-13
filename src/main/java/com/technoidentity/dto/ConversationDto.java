package com.technoidentity.dto;

import com.technoidentity.enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDto {

    @Valid
    private ConversationType conversationType;

    private String name;

    private List<ParticipantDto> participants= new ArrayList<>();



}
