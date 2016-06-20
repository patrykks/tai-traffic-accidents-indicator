package pl.edu.agh.tai.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.unbescape.html.HtmlEscape;
import pl.edu.agh.tai.model.User;
import pl.edu.agh.tai.secuirty.register.UserDTO;
import pl.edu.agh.tai.secuirty.exceptions.EmailExistsException;
import pl.edu.agh.tai.secuirty.register.IUserService;

@Controller
public class MainController {

    @Autowired
    IUserService userService;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/index.html";
    }

    /**
     * Home page.
     */
    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    /**
     * User zone index.w
     */
    @RequestMapping("/user/index.html")
    public String userIndex() {
        return "user/index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDTO accountDto,
                                            BindingResult result, WebRequest request, Errors errors) {
        System.out.println("DTO: " + accountDto);
        System.out.println(result);
        User registered = new User();
        if (!result.hasErrors()) {
            registered = createUserAccount(accountDto, result);
        }

        if (errors.hasGlobalErrors()) {
            ModelAndView model = new ModelAndView("register", "user", accountDto);
            model.getModel().put("passwordNotMatchError", true);
            return model;
        }

        if (result.hasErrors()) {
            return new ModelAndView("register", "user", accountDto);
        }
        else {
            return new ModelAndView("login");
        }
    }

    private User createUserAccount(UserDTO accountDto, BindingResult result)   {
        User registered = null;
        try {
            registered = userService.registerNewUserAccount(accountDto);
        } catch (EmailExistsException e) {
            System.out.println("User with email" + accountDto.getEmail() + " doesnt exist");
            return null;
        }
        return registered;
    }

    /**
     * Administration zone index.
     */
    @RequestMapping("/admin/index.html")
    public String adminIndex() {
        return "admin/index";
    }

    /**
     * Login form.
     */
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "error";
    }

    /**
     * Error page.
     */
    @RequestMapping("/error.html")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        System.out.println(errorMessage.toString());
        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }

    /**
     * Error page.
     */
    @RequestMapping("/403.html")
    public String forbidden() {
        return "403";
    }

}
