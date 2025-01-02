package bp.books.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userid;

    @NotNull(message = "Imię nie może być puste")
    @Size(min = 2, max = 30, message = "Imię musi mieć od 2 do 30 znaków")
    private String name;

    @NotNull(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 30, message = "Nazwisko musi mieć od 2 do 30 znaków")
    private String surname;

    @NotNull(message = "Login nie może być pusty")
    @Size(min = 3, max = 15, message = "Login musi mieć od 3 do 15 znaków")
    private String login;

    @NotNull(message = "Hasło nie może być puste")
    @Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<Book> books = new HashSet<>();

    public User() {
    }

    public User(String name, String surname, String login,
                String password) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
    }

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

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    // Helper methods
    public void addBook(Book book) {
        this.books.add(book);
        book.getUsers().add(this);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
        book.getUsers().remove(this);
    }
}