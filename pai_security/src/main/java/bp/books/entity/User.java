package bp.books.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userid;

    @NotNull(message = "Imię nie może być puste")
    @Size(min = 2, max = 30, message = "Imię musi mieć od 2 do 30 znaków")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$",
            message = "Imię musi zaczynać się wielką literą i zawierać tylko litery")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 30, message = "Nazwisko musi mieć od 2 do 30 znaków")
    @Pattern(regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$",
            message = "Nazwisko musi zaczynać się wielką literą i zawierać tylko litery")
    private String surname;

    @NotNull(message = "Login nie może być pusty")
    @Size(min = 3, max = 15, message = "Login musi mieć od 3 do 15 znaków")
    private String login;

    @NotNull(message = "Hasło nie może być puste")
    @Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBook> userBooks = new HashSet<>();

    public User() {
    }

    public User(String name, String surname, String login, String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

    // Standard getters and setters
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // UserBooks relationship methods
    public Set<UserBook> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(Set<UserBook> userBooks) {
        this.userBooks = userBooks;
    }

    // Helper method to get books
    public Set<Book> getBooks() {
        return userBooks.stream()
                .map(UserBook::getBook)
                .collect(Collectors.toSet());
    }

    // Helper methods for managing relationships
    public void addBook(Book book) {
        UserBook userBook = new UserBook(this, book);
        userBooks.add(userBook);
    }

    public void removeBook(Book book) {
        userBooks.removeIf(userBook -> userBook.getBook().equals(book));
    }

    // Helper method to check if a book is read by this user
    public boolean isBookRead(Book book) {
        return userBooks.stream()
                .filter(userBook -> userBook.getBook().equals(book))
                .findFirst()
                .map(UserBook::isRead)
                .orElse(false);
    }
}