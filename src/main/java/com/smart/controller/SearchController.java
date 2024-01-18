package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.CaseRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Case;
import com.smart.entities.User;

//rest controller because it will return object(Json) and not view (html page) as controller

@RestController
public class SearchController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CaseRepository caseRepository;

	// search by client name handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal) {
		System.out.println("Query: " + query);
		User user = this.userRepository.getUserByUserCredential(principal.getName());
		List<Case> caseSuggestions = this.caseRepository.findByClientNameContainingAndUser(query, user);
		return ResponseEntity.ok(caseSuggestions);
	}
	
	// search by case name handler
		@GetMapping("/searchCase/{query}")
		public ResponseEntity<?> searchCase(@PathVariable("query") String query, Principal principal) {
			System.out.println("Query: " + query);
			User user = this.userRepository.getUserByUserCredential(principal.getName());
			List<Case> caseSuggestions = this.caseRepository.findByCaseNameContainingAndUser(query, user);
			return ResponseEntity.ok(caseSuggestions);
		}

	// search by case id handler
	@GetMapping("/searchId/{query}")
	public ResponseEntity<?> search(@PathVariable("query") Integer query, Principal principal) {
		System.out.println("Query: " + query);
		User user = this.userRepository.getUserByUserCredential(principal.getName());
		List<Case> caseSuggestions = this.caseRepository.findByCidAndUser(query, user);
		return ResponseEntity.ok(caseSuggestions);
	}
}