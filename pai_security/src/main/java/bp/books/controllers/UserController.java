package bp.books.controllers;

import bp.books.dao.UserDao;
import bp.books.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

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
        Optional<User> existingUser = dao.findByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            binding.rejectValue("login", "error.user", "Użytkownik o podanym loginie już istnieje");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.save(user);
        return "login";
    }

    @GetMapping("/profile")
    public String profilePage(Model m, Principal principal) {
        Optional<User> userOpt = dao.findByLogin(principal.getName());
        if (!userOpt.isPresent()) {
            return "redirect:/login";
        }
        m.addAttribute("user", userOpt.get());
        return "profile";
    }

    @GetMapping("/users")
    public String usersPage(Model m, Principal principal) {
        m.addAttribute("users", dao.findAll());
        return "users";
    }

    @GetMapping("/edit")
    public String editPage(Model m, Principal principal) {
        Optional<User> userOpt = dao.findByLogin(principal.getName());
        if (!userOpt.isPresent()) {
            return "redirect:/login";
        }
        m.addAttribute("user", userOpt.get());
        return "edit";
    }

    @PostMapping("/edit")
    public String editPagePOST(@Valid @ModelAttribute User user, BindingResult binding, Principal principal) {
        Optional<User> existingUserOpt = dao.findByLogin(principal.getName());
        if (!existingUserOpt.isPresent()) {
            binding.rejectValue("login", "error.user", "Użytkownik nie został znaleziony");
            return "edit";
        }
        User existingUser = existingUserOpt.get();

        // Check if the new login is different and already exists
        if (!existingUser.getLogin().equals(user.getLogin()) &&
                dao.findByLogin(user.getLogin()).isPresent()) {
            binding.rejectValue("login", "error.user", "Użytkownik o podanym loginie już istnieje");
            return "edit";
        }

        // Update user information
        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setLogin(user.getLogin());

        // Only update the password if it's not empty
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        dao.save(existingUser);
        return "redirect:/profile";
    }

    @GetMapping("/delete")
    public String deletePage(Model m, Principal principal) {
        Optional<User> userOpt = dao.findByLogin(principal.getName());
        if (!userOpt.isPresent()) {
            return "redirect:/login";
        }
        m.addAttribute("user", userOpt.get());
        return "delete";
    }

    @PostMapping("/delete")
    public String deleteAccount(Principal principal) {
        Optional<User> existingUserOpt = dao.findByLogin(principal.getName());
        if (existingUserOpt.isPresent()) {
            dao.delete(existingUserOpt.get());
            SecurityContextHolder.clearContext();
        }
        return "redirect:/login";
    }
}