package com.codingdojo.loginandregistration.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codingdojo.loginandregistration.models.User;
import com.codingdojo.loginandregistration.services.UserService;

@Controller
public class UserController {
	private final UserService userService;
 
	 public UserController(UserService userService) {
	     this.userService = userService;
	 }
	 
	 @RequestMapping("/registration")
	 public String registerForm(@ModelAttribute("user") User user) {
	     return "registrationPage.jsp";
	 }
	 @RequestMapping("/login")
	 public String login() {
	     return "loginPage.jsp";
	 }
	 
	 @RequestMapping(value="/registration", method=RequestMethod.POST)
	 public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
	     if(result.hasErrors()) {
	    	 return "registrationPage.jsp";
	     }
	     User loggedInUser = userService.registerUser(user);
	     session.setAttribute("id", loggedInUser.getId());
	     return "redirect:/home";
	 }
	 
	 @RequestMapping(value="/login", method=RequestMethod.POST)
	 public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
	     if(userService.authenticateUser(email, password)) {
	    	 User loggedInUser = userService.findByEmail(email);
	    	 session.setAttribute("id", loggedInUser.getId());
	    	 return "redirect:/home";
	     } else {
	    	 redirectAttributes.addFlashAttribute("error", "*login invalid");
	    	 return "redirect:/login";
	     }
	 }
	 
	 @RequestMapping("/home")
	 public String home(HttpSession session, Model model) {
		 Long userId = (Long)session.getAttribute("id");
		 if(userId!=null) {
			 model.addAttribute("user", userService.findUserById(userId));
			 return "homePage.jsp";
		 }
		 return "redirect:/login";
	 }
	 @RequestMapping("/logout")
	 public String logout(HttpSession session) {
	     session.removeAttribute("id");
	     return "redirect:/login";
	 }
}