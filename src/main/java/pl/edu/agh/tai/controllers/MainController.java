package pl.edu.agh.tai.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.unbescape.html.HtmlEscape;

@Controller
public class MainController {

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/index.html";
    }

    /** Home page. */
    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    /** User zone index. */
    @RequestMapping("/user/index.html")
    public String userIndex() {
        return "user/index";
    }

    /** Administration zone index. */
    @RequestMapping("/admin/index.html")
    public String adminIndex() {
        return "admin/index";
    }

    /** Login form. */
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }


    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "error";
    }

    /** Error page. */
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

    /** Error page. */
    @RequestMapping("/403.html")
    public String forbidden() {
        return "403";
    }

}
