package dev.sharavananpa.mock_interview_generator_backend.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponsePayload {
    String text;
    List<String> citationSources;
}
