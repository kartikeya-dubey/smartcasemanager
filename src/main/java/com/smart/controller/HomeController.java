package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home - Smart Case Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About - Smart Case Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register - Smart Case Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	//handler for registering user
	
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bResult, @RequestParam(value="agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session)
	{
		try {
			if(!agreement)
			{
				throw new Exception("You have not agreed to Terms & Conditions");
			}
			
			//To check error list of server side validations
			if(bResult.hasErrors())
			{
				System.out.println("ERROR: "+bResult.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			//setting user values to user object for Database entry
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			//Encrypting the password before storing it in database 
			user.setPassword(new BCryptPasswordEncoder(10).encode(user.getPassword()));
			
			User result=this.userRepository.save(user);
			
			session.setAttribute("message", new Message("Successfully registered", "alert-success"));
			
			model.addAttribute("user", new User());
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user" , user);
			session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
		}
		return "signup";
	}
	
	@GetMapping("/signin")
	public String loginUser(Model model)
	{
		System.out.println("1");
		model.addAttribute("title","Login - Smart Case Manager");
		System.out.println("2");
		return "login";
	}
}
