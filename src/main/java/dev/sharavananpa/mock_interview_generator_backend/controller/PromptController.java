package dev.sharavananpa.mock_interview_generator_backend.controller;

import org.springframework.web.bind.annotation.RestController;

import dev.sharavananpa.mock_interview_generator_backend.model.RequestPayload;
import dev.sharavananpa.mock_interview_generator_backend.model.ResponsePayload;
import dev.sharavananpa.mock_interview_generator_backend.service.MockInterviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "https://sharavananpa.dev")
@RestController
public class PromptController {

    @Autowired
    MockInterviewService mockService;

    @PostMapping("mock")
    public ResponsePayload getMockInterview(@RequestBody RequestPayload requestPayload) {
        return mockService.generateMockInterview(requestPayload);
    }
}
