package com.agents.ScienceTeacher;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;

import io.reactivex.rxjava3.core.Flowable;


public class ScienceTeacherAgent {

    private static final String AGENT_NAME = "ScienceTeacherAgent";

    private static final String AGENT_DESCRIPTION = "An agent that helps students with science-related questions.";

    private static final String AGENT_INSTRUCTION = """
            Answer science-related questions in a clear and concise manner.
            If you don't know the answer, say 'I don't know'.

            You can also provide additional resources or explanations if relevant.
            Use simple language and avoid jargon when possible.
            If the question is too complex, break it down into simpler parts and answer each part step by step.
            Always encourage curiosity and further exploration of the topic.

            If the question is too vague or too general, ask for clarification or more specific details.

            DO NOT provide personal opinions or beliefs.
            DO NOT provide medical, legal, or financial advice.
            DO NOT provide answers that are not based on scientific facts or principles.
            """;

    public static BaseAgent ROOT_AGENT = initAgent();

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .model("gemini-1.5-flash") // Note: Updated to a common, available model
                .name(AGENT_NAME)
                .description(AGENT_DESCRIPTION)
                .instruction(AGENT_INSTRUCTION)
                .generateContentConfig(GenerateContentConfig.builder()
                        .temperature(1.0F)
                        .topP(0.9F)
                        .maxOutputTokens(1024)
                        .build())
                .build();
    }

    public static void main(String[] args) throws Exception {
        InMemoryRunner runner = new InMemoryRunner(ROOT_AGENT);

        Session session = runner.sessionService()
                                .createSession("Scientific QA", AGENT_NAME)
                                .blockingGet();

        try (Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8)){
            while (true) {
                System.out.println("\nYOU -> ");
                String userInput = sc.nextLine();

                if (userInput.equals("quit")){
                    break;
                }

                Content userMessage = Content.fromParts(Part.fromText(userInput));
                Flowable<Event> events = runner.runAsync("Scientific QA", session.id(), userMessage);
                System.out.println("\nAGENT -> ");
                events.blockingForEach(event -> System.out.println(event.stringifyContent()));


            }
        }
    }
}