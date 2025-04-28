package dev.sharavananpa.mock_interview_generator_backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import dev.sharavananpa.mock_interview_generator_backend.model.RequestBody;
import dev.sharavananpa.mock_interview_generator_backend.model.RequestPayload;
import dev.sharavananpa.mock_interview_generator_backend.model.ResponseBody;
import dev.sharavananpa.mock_interview_generator_backend.model.ResponsePayload;

@Service
public class MockInterviewService {
    private RestClient restClient = RestClient.create();

    public ResponsePayload generateMockInterview(RequestPayload payload) {

        Random seed = new Random();

        String prompt = """
                        You are generating a set of mock interview questions for a Software Development Engineer (SDE) position. The questions should be organized into three core modules as follows:

                        **Module 1: Core Computer Science Concepts**
                        Topics: %s;

                        **Module 2: Problem Solving (Data Structures & Algorithms)**
                        Topics: %s;

                        **Module 3: Behavioral**
                        Themes: %s;

                        Customization: %s;

                        **Rules:**
                        - Provide more questions than necessary so the interviewer can choose.
                        - Strictly follow the three-module structure. No mixing or adding extra sections.
                        - For each primary question:
                            - Estimate the expected time needed to answer.
                            - Generate natural follow-up questions.
                            - Include at least one conditional follow-up branch:
                            - If the candidate struggles with X, ask Y.
                            - If the candidate answers quickly or perfectly, escalate to Z.
                        - Format the output as follows:
                            **[Module Name]**
                            - **Question #**: [Primary Question]  
                                **Estimated Time**: [X minutes]
                                **Follow-Ups**: 
                                    - [Follow-up 1]
                                    - [Follow-up 2]
                                **Conditional Follow-ups**: 
                                    - If candidate struggles with [X], ask: [Y]
                                    - If candidate answers well, ask: [Z]
                        - Apply any specific instructions in "Customization".
                        - Use "Seed Value": %s to introduce controlled randomness if needed.
                        - No fluff. Only generate questions, follow-ups, and conditionals. No commentary or extra text unless requested.
                """.formatted(payload.getCoreCS(), payload.getProblemSolving(), payload.getBehavioral(), payload.getPrompt(), seed.nextLong());

        RequestBody.Part part = new RequestBody.Part();
        part.setText(prompt);

        RequestBody.Content content = new RequestBody.Content();
        content.setParts(List.of(part));

        RequestBody requestBody = new RequestBody();
        requestBody.setContents(List.of(content));

        ResponseBody responseBody = restClient.post()
                                        .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(requestBody)
                                        .retrieve()
                                        .body(ResponseBody.class);

        ResponsePayload responsePayload = new ResponsePayload();

        String defaultText = "Sorry! Couldn't generate.";

        String textToSet = Optional.ofNullable(responseBody)
                            .map(ResponseBody::getCandidates)
                            .filter(list -> !list.isEmpty())
                            .map(list -> list.get(0))
                            .map(ResponseBody.Candidate::getContent)
                            .map(ResponseBody.Candidate.Content::getParts)
                            .filter(list -> !list.isEmpty())
                            .map(list -> list.get(0))
                            .map(ResponseBody.Candidate.Part::getText)
                            .orElse(defaultText);

        responsePayload.setText(textToSet);
        return responsePayload;
    }
}
