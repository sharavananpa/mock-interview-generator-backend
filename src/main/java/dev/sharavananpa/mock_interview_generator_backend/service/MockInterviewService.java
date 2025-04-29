package dev.sharavananpa.mock_interview_generator_backend.service;

import java.util.List;
import java.util.Optional;

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

        String prompt = """
You are tasked with generating a structured set of mock interview questions for a Software Development Engineer (SDE) position.

**Objective:** Create a realistic and challenging question set following the specified modules, formatting, and constraints.

**Input Parameters:**
- **Module 1:** Core Computer Science Concepts
	- Topics = %s (ignore module if empty array)
- **Module 2:** Problem Solving (Data Structures & Algorithms)
	- Topics = %s (ignore module if empty array)
- **Module 3:** Behavioral
	- Themes: %s (ignore module if empty array)
- **Customization & Constraints:** %s

**Rules & Formatting:**
1. **Strict Structure:** Adhere strictly to the three distinct modules (Module 1, Module 2, Module 3). Do not combine them or add other sections.
2. **Precise Formatting:** Use the following format EXACTLY for each question:

    **Module #[Number]: [Module Name]**

    **Question #[Number]: [Primary Question Text]**

    **Estimated Time**: [X minutes]
    **Focus Area**: [Briefly state the core concept/skill being tested, e.g., "Process Synchronization", "Tree Traversal", "Conflict Resolution"]
    **Follow-Ups**:
        - [Follow-up Question 1]
        - [Follow-up Question 2]
        - [...]
    **Hints (If candidate struggles)**:
        - [Hint 1, potentially simplifying the problem or pointing towards a concept]
        - [Hint 2]
    **Deeper Dive (If candidate excels)**:
        - [Advanced follow-up 1, e.g., asking about complexity, trade-offs, alternative solutions, scaling]
        - [Advanced follow-up 2]

3. **Content Relevance:** Questions must align with the specified `Topics` (Modules 1 & 2) and `Themes` (Module 3). **If the Topics / Themes array is empty, skip the module entirely!**
4. **Customization Implementation:** Incorporate all instructions from the `Customization & Constraints` section.
5. **No Extraneous Text:** Generate *only* the structured questions as specified. Do not include introductory sentences, concluding remarks, explanations of the process, or any commentary outside the defined question format.
6. **Markdown Usage:** Ensure proper Markdown formatting, especially for lists and bold text, with adequate newlines for readability.
                """.formatted(payload.getCoreCS(), payload.getProblemSolving(), payload.getBehavioral(), payload.getPrompt());

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
