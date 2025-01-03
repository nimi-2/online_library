package bp.books.controllers;

import bp.books.dao.BookDao;
import bp.books.dao.UserDao;
import bp.books.entity.Book;
import bp.books.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private UserDao userDao;

    @GetMapping
    public String listBooks(Model model, Principal principal) {
        model.addAttribute("books", bookDao.findAll());
        if (principal != null) {
            Optional<User> userOpt = userDao.findByLogin(principal.getName());
            if (!userOpt.isPresent()) {
                throw new IllegalStateException("Nie znaleziono użytkownika");
            }
            model.addAttribute("userBooks", userOpt.get().getBooks());
        }
        return "books/list";
    }

    @PostMapping("/{id}/addToUser")
    public String addBookToUser(@PathVariable Long id, Principal principal) {
        try {
            Optional<User> userOpt = userDao.findByLogin(principal.getName());
            if (!userOpt.isPresent()) {
                throw new IllegalStateException("Nie znaleziono użytkownika");
            }
            User user = userOpt.get();

            Optional<Book> bookOpt = bookDao.findById(id);
            if (!bookOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid book Id:" + id);
            }
            Book book = bookOpt.get();

            book.addUser(user);
            bookDao.save(book);
            return "redirect:/books";
        } catch (Exception e) {
            return "redirect:/books?error=Nie można dodać książki do kolekcji";
        }
    }

    @PostMapping("/{id}/removeFromUser")
    public String removeBookFromUser(@PathVariable Long id, Principal principal) {
        try {
            Optional<User> userOpt = userDao.findByLogin(principal.getName());
            if (!userOpt.isPresent()) {
                throw new IllegalStateException("Nie znaleziono użytkownika");
            }
            User user = userOpt.get();

            Optional<Book> bookOpt = bookDao.findById(id);
            if (!bookOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid book Id:" + id);
            }
            Book book = bookOpt.get();

            book.removeUser(user);
            bookDao.save(book);
            return "redirect:/books";
        } catch (Exception e) {
            return "redirect:/books?error=Nie można usunąć książki z kolekcji";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Book> bookOpt = bookDao.findById(id);
        if (!bookOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid book Id:" + id);
        }
        model.addAttribute("book", bookOpt.get());
        return "books/form";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id,
                             @ModelAttribute("book") @Valid Book book,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            book.setId(id);
            return "books/form";
        }
        try {
            Optional<Book> existingBookOpt = bookDao.findById(id);
            if (!existingBookOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid book Id:" + id);
            }
            Book existingBook = existingBookOpt.get();
            book.setUsers(existingBook.getUsers());
            book.setId(id);
            bookDao.save(book);
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Wystąpił błąd podczas aktualizacji książki: " + e.getMessage());
            return "books/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        try {
            Optional<Book> bookOpt = bookDao.findById(id);
            if (!bookOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid book Id:" + id);
            }
            Book book = bookOpt.get();
            book.getUsers().forEach(user -> book.removeUser(user));
            bookDao.deleteById(id);
            return "redirect:/books";
        } catch (Exception e) {
            return "redirect:/books?error=Nie można usunąć książki";
        }
    }

    // Pozostałe metody bez zmian...
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book(
                "Tytuł",
                "Autor",
                0,
                "Gatunek",
                0.0
        ));
        return "books/form";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") @Valid Book book,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            return "books/form";
        }
        try {
            bookDao.save(book);
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Wystąpił błąd podczas zapisywania książki: " + e.getMessage());
            return "books/form";
        }
    }

    @GetMapping("/my-collection")
    public String showUserCollection(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userDao.findByLogin(principal.getName());
        if (!userOpt.isPresent()) {
            throw new IllegalStateException("Nie znaleziono użytkownika");
        }

        User user = userOpt.get();
        model.addAttribute("userBooks", user.getBooks());
        return "books/user-books";
    }
}