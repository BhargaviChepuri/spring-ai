package com.mss.demo.application;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIService {

	@Autowired
	AzureOpenAiChatModel aiChatModel;
	
	@Autowired
	JokeRepository jokeRepository;

	public String getBooksData(String category, String year) {
		PromptTemplate promptTemplate = new PromptTemplate(
				"""
						Please provide me best book for the given {category} and the {year}.
						Please do provide a summary of the book as well, the information should be limited and not much in depth,
						Please provide the details in the JSON format containing this information : category, book, year, review,author, summary
						""");
		promptTemplate.add("category", category);
		promptTemplate.add("year", year);
		Prompt prompt = promptTemplate.create();
		return aiChatModel.call(prompt).getResult().getOutput().getContent();
	}


	public String getJoke(String topic) {
		// Define the prompt template with a placeholder for the topic
		PromptTemplate promptTemplate = new PromptTemplate("topic");
//				I’m creating a collection of programming jokes for my website.
//				Please give me a funny programming joke specifically about {topic}.
//				""");
		promptTemplate.add("topic",topic);
		Prompt prompt = promptTemplate.create();
		String response = aiChatModel.call(prompt).getResult().getOutput().getContent();

		// Save the joke and topic to MongoDB
		JokeDocument jokeDocument = new JokeDocument(topic, response);
		jokeRepository.save(jokeDocument); // Save to MongoDB

		return response; // Return the joke response
	}

}
