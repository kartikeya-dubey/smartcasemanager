package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.CaseRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Case;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/user")
public class userController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CaseRepository caseRepository;

	// to add user details to be used by any function in this class

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		// Username aka email is unique identifier of user in database
		String userCred = principal.getName();

		// get the user details using user name
		User user = userRepository.getUserByUserCredential(userCred);

		System.out.println("User " + user);

		model.addAttribute("user", user);
	}

	// Load dashboard handler with current signed in user details

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "Dashboard");
		return "normal/user_dashboard";
	}

	// Loading "add Case" handler

	@GetMapping("/add-case")
	public String openAddCaseForm(Model model) {
		model.addAttribute("title", "Add Case");
		model.addAttribute("newCase", new Case());
		return "normal/add_case_form";
	}

	// To process "add case" inputs inside database

	// @RequestParam("image") MultipartFile file
	@PostMapping("/process-case")
	public String processCase(@ModelAttribute Case newCase, HttpSession session, Principal principal) {
		try {

			System.out.println("Case Data: " + newCase);

			String userCred = principal.getName();
			User user = this.userRepository.getUserByUserCredential(userCred);

			// feeding user details in case as it is bi-directional mapping
			newCase.setUser(user);

			// Processing and uploading file to resources>static>img
			/*
			 * if(file.isEmpty()) { System.out.println("No File uploaded!"); } else {
			 * //upload file to folder and update name to case+time
			 * newCase.setImage(file.getOriginalFilename());
			 * 
			 * File saveFile=new ClassPathResource("static/img").getFile();
			 * 
			 * //getting destination path+appending file name 
			 * Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.
			 * getOriginalFilename()); //+System.currentTimeMillis()
			 * 
			 * Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			 * 
			 * System.out.println("Image uploaded successfully!"); }
			 */
			newCase.setImage("defaultPOA.jpg");
			user.getCases().add(newCase);

			this.userRepository.save(user);

			System.out.println("Case " + newCase.getCaseName() + " registered under: " + user.getName());

			// success alert (httpSession to transfer data between pages + Message is 0ur
			// helper package containing (message content,message type))
			session.setAttribute("message", new Message("Case successfully registered!", "success"));
		}

		catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			e.printStackTrace();

			// error alert (httpSession to transfer data between pages + Message is 0ur
			// helper package containing (message content,message type))
			session.setAttribute("message", new Message(e.getMessage().toString(), "danger"));
		}

		return "normal/add_case_form";
	}

	// Show All Cases List (pagination: 2 cases/page)

	@GetMapping("/show-all-cases/{page}")
	public String showCaseList(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("title", "User Case List");

		// get user details currently logged in
		String userCred = principal.getName(); // userName is actually unique user email of user currently logged in
		User userByUserCred = this.userRepository.getUserByUserCredential(userCred);

		// Simple Direct method to getAllCases of user using UserRepository
		/*
		 * List<Case> userCases =userByUserCred.getCases();
		 */

		// getAllCases using separate CaseRepository without pagination
		/*
		 * List<Case> userAllCases =
		 * this.caseRepository.findCasesByUser(userByUserCred.getId());
		 */

		// Pagination method to getAllCases using separate CaseRepository

		Pageable pageable = PageRequest.of(page, 2); // create page object containg ref of current page+rec size
		Page<Case> userAllCases = this.caseRepository.findCasesByUser(userByUserCred.getId(), pageable);

		model.addAttribute("userAllCases", userAllCases);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", userAllCases.getTotalPages());

		return "normal/show_cases";
	}

	// Show specific case details
	@GetMapping("/case/{cid}")
	public String showCaseDetails(@PathVariable("cid") Integer cid, Model model, Principal principal) {

		Optional<Case> caseDetailsById = this.caseRepository.findById(cid);

		if (!caseDetailsById.isEmpty()) {
			Case caseDetails = caseDetailsById.get();

			// Security check to ensure the user can only see cases assigned to him.
			String cred = principal.getName();
			User userByUserCredential = this.userRepository.getUserByUserCredential(cred);

			if (userByUserCredential.getId() == caseDetails.getUser().getId()) {
				model.addAttribute("caseDetails", caseDetails);
				model.addAttribute("title", caseDetails.getClientName());
			}
		}
		return "normal/case_details";
	}

	// delete case from DB

	@GetMapping("/delete/{cid}")
	public String deleteCase(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) 
	{
		Optional<Case> caseDetailsById = this.caseRepository.findById(cid);

		if (!caseDetailsById.isEmpty()) {
			Case caseDetails = caseDetailsById.get();

			// Security check to ensure the user can only delete cases assigned to him.
			String cred = principal.getName();
			User userByUserCredential = this.userRepository.getUserByUserCredential(cred);

			if (userByUserCredential.getId() == caseDetails.getUser().getId()) {

				this.caseRepository.delete(caseDetails);
				session.setAttribute("message", new Message("Case Deleted Successfully!", "success"));
			}
		}

		return "redirect:/user/show-all-cases/0";
	}

	// to update case details in DB
	@PostMapping("/update-case/{cid}")
	public String updateCase( @PathVariable("cid") Integer cid,Model model)
	{
		System.out.println("Inside update db");
		model.addAttribute("title", "Update Case Details");
		
		Case caseDetails = this.caseRepository.findById(cid).get();
		
		model.addAttribute("case", caseDetails);
		
		return "normal/update_case_form";
	}
	
	// to process case details updation after updates are submitted process-update
	
	@PostMapping("/process-update")
	public String processUpdateCase(@ModelAttribute Case caseUpdated,Model model ,HttpSession session, Principal principal) // @RequestParam("image") MultipartFile file
	{
		System.out.println("Inside process update");
		try {
			
			//get old contact details
			Case originalCaseDetails = this.caseRepository.findById(caseUpdated.getCid()).get();
			
			/*//Checking if image has been changed > rewrite new file in destination (img)
			if(!file.isEmpty())
			{
				
				//delete old img
				
				File deleteFile=new ClassPathResource("static/img").getFile();
				File file2=new File(deleteFile, originalCaseDetails.getImage());
				file2.delete();
				
				
				//update new img
				
					//get img file path
					File saveFile=new ClassPathResource("static/img").getFile();
					//getting destination path+appending file name Path
					Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					caseUpdated.setImage(file.getOriginalFilename());
			}
			else 
			{
				caseUpdated.setImage(originalCaseDetails.getImage());
			}*/
			
			
			
			//updating other caseDetails in DB
			User user=this.userRepository.getUserByUserCredential(principal.getName());
			caseUpdated.setUser(user);
			caseUpdated.setImage("defaultPOA.jpg");
			this.caseRepository.save(caseUpdated);
			
			session.setAttribute("message", new Message("Case Details updated succesfully!","success"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Case Details updation failed!","danger"));
		}
		
		System.out.println("Client Name: "+caseUpdated.getClientName());
		System.out.println("Client Id: "+caseUpdated.getCid());
		
		String contextPath = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getContextPath();
		
		return "redirect:/user/case/"+caseUpdated.getCid();
	}
	
	@GetMapping("/profile")
	public String userProfile(Model model)
	{
		model.addAttribute("title", "Profile page");
		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String settings(Model model)
	{
		model.addAttribute("title", "Profile Settings page");
		return "normal/profileSettings";
	}
	
	@GetMapping("/search")
	public String searchCase(Model model) 
	{
		model.addAttribute("title", "Lookup a case");
		return "normal/search_case";
	}
}
