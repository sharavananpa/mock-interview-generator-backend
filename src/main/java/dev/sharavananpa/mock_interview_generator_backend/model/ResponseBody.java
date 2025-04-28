package dev.sharavananpa.mock_interview_generator_backend.model;

import java.util.List;

import lombok.Data;

@Data
public class ResponseBody {

    List<Candidate> candidates;
    UsageMetadata usageMetadata;
    String modelVersion;

    @Data
    public static class Candidate {

        Content content;
        String finishReason;
        CitationMetadata citationMetadata;
        Double avgLogprobs;

        @Data
        public static class Content {
            List<Part> parts;
            String role;
        }
        
        @Data
        public static class Part {
            String text;
        }

        @Data
        public static class CitationMetadata {
            List<CitationSource> citationSources;
        }

        @Data
        public static class CitationSource {
            Long startIndex;
            Long endIndex;
            String uri;
        }
    }

    @Data
    public static class UsageMetadata {
        Long promptTokenCount;
        Long candidatesTokenCount;
        Long totalTokenCount;
        List<TokensDetail> promptTokensDetails;
        List<TokensDetail> candidatesTokensDetails;

        @Data
        public static class TokensDetail {
            String modality;
            Long tokenCount;
        }

    }
}
