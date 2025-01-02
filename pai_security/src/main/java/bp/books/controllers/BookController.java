package bp.books.controllers;

import bp.books.dao.BookDao;
import bp.books.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookDao bookDao;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookDao.findAll());
        return "books/list";
    }

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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
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
            bookDao.deleteById(id);
            return "redirect:/books";
        } catch (Exception e) {
            return "redirect:/books?error=Nie można usunąć książki";
        }
    }
}