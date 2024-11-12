package com.mss.demo.application;

import java.util.Map;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class AIController {

	@Autowired
	AIService aiService;

	@Autowired
	private AzureOpenAiChatModel chatModel;

	private final ChatClient chatClient;

	public AIController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@GetMapping("/query")
	public String getResponse(@RequestParam String category, String year) {
		return aiService.getBooksData(category, year);
	}

	@GetMapping("/ai/generate")
	public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
		return Map.of("generation", chatModel.call(message));
	}

	@GetMapping("/joke")
	public String getJoke(@RequestParam String topic) {
		return aiService.getJoke(topic);
	}
	
	@PostMapping("/chat")
	public String chat(@RequestParam String message) {
		String response = chatClient.prompt().user(message).call().content();
		return response;
	}

	@GetMapping("/stream")
	public Flux<String> chatWithStream(@RequestParam String message) {
		return chatClient.prompt().user(message).stream().content();
	}

}
