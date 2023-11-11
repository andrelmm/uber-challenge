package com.github.andrelmm.uberchallenge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDTO {
    private String to;
    private String subject;
    private String body;
}
