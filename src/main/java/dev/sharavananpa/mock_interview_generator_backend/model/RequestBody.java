package dev.sharavananpa.mock_interview_generator_backend.model;

import java.util.List;

import lombok.Data;

@Data
public class RequestBody {

    List<Content> contents;

    @Data
    public static class Content {
        List<Part> parts;
    }
    
    @Data
    public static class Part {
        String text;
    }
}
