package dev.sharavananpa.mock_interview_generator_backend.model;

import java.util.List;

import lombok.Data;

@Data
public class RequestPayload {
    String prompt;
    List<String> coreCS;
    List<String> problemSolving;
    List<String> behavioral;
}
