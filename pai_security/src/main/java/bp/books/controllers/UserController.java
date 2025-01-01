package bp.books.controllers;

import bp.books.dao.UserDao;
import bp.books.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao dao;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model m) {
        m.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerPagePOST(@Valid User user, BindingResult binding) {
        if (binding.hasErrors()) {
            return "register";
        }
        if (dao.findByLogin(user.getLogin()) != null) {
            binding.rejectValue("login", "error.user", "Użytkownik o podanym loginie już istnieje");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
        return "login";
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal) {
        User user = dao.findByLogin(principal.getName());
        if (user != null) {
            m.addAttribute("user", user);
            return "profile";
        }
        return "redirect:/login"; // Redirect to login if the user is not found
    }

    @GetMapping("/users")
    public String usersPage(Model m, Principal principal) {
        m.addAttribute("users", dao.findAll());
        return "users";
    }

    @GetMapping("/edit")
    public String editPage(Model m, Principal principal) {
        User user = dao.findByLogin(principal.getName());
        if (user != null) {
            m.addAttribute("user", user);
            return "edit";
        }
        return "redirect:/login"; // Redirect to login if the user is not found
    }

    @PostMapping("/edit")
    public String editPagePOST(@Valid @ModelAttribute User user, BindingResult binding, Principal principal) {
        User existingUser = dao.findByLogin(principal.getName());

        if (existingUser == null) {
            binding.rejectValue("login", "error.user", "Użytkownik nie został znaleziony");
            return "edit"; // Return to edit form with error
        }

        // Check if the new login is different and already exists
        if (!existingUser.getLogin().equals(user.getLogin()) && dao.findByLogin(user.getLogin()) != null) {
            binding.rejectValue("login", "error.user", "Użytkownik o podanym loginie już istnieje");
            return "edit"; // Return to edit form with error
        }

        // Update user information
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());

        // Only update the login if it's changed
        existingUser.setLogin(user.getLogin());

        // Only update the password if it's not empty
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Save changes to the database
        dao.save(existingUser);

        // Redirect to the profile page after saving changes
        return "redirect:/profile"; // Redirect to the profile page
    }

    @GetMapping("/delete")
    public String deletePage(Model m, Principal principal) {
        User user = dao.findByLogin(principal.getName());
        if (user != null) {
            m.addAttribute("user", user);
            return "delete";
        }
        return "redirect:/login"; // Redirect to login if the user is not found
    }

    @PostMapping("/delete")
    public String deleteAccount(Principal principal) {
        User existingUser = dao.findByLogin(principal.getName());

        if (existingUser != null) {
            dao.delete(existingUser);
        }

        SecurityContextHolder.clearContext();
        return "redirect:/login"; // Redirect to login page after deletion
    }
}
