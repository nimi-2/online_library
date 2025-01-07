package bp.books.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tytuł nie może być pusty")
    @Size(min = 2, max = 100, message = "Tytuł musi mieć od 2 do 100 znaków")
    private String title;

    @NotBlank(message = "Autor nie może być pusty")
    @Size(min = 2, max = 100, message = "Autor musi mieć od 2 do 100 znaków")
    @Pattern(regexp = "^[\\p{L}\\s.'-]+$", message = "Autor może zawierać tylko litery, spacje, kropki, apostrofy i myślniki")
    private String author;

    @NotNull(message = "Rok wydania nie może być pusty")
    @Min(value = 1000, message = "Rok wydania musi być większy niż 1000")
    @Max(value = 2100, message = "Rok wydania nie może być większy niż 2100")
    private Integer publicationYear;

    @Pattern(regexp = "^[\\p{L}\\s-]*$", message = "Gatunek może zawierać tylko litery, spacje i myślniki")
    @Size(min = 2, max = 50, message = "Gatunek musi mieć od 2 do 50 znaków")
    private String genre;

    @NotNull(message = "Cena nie może być pusta")
    @DecimalMin(value = "0.01", message = "Cena musi być większa niż 0")
    @DecimalMax(value = "99999.99", message = "Cena nie może być większa niż 99999.99")
    @Digits(integer = 5, fraction = 2, message = "Cena może mieć maksymalnie 5 cyfr przed przecinkiem i 2 po przecinku")
    private Double price;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @ManyToMany
    @JoinTable(
            name = "user_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBook> userBooks = new HashSet<>();

    // Constructors
    public Book() {}

    public Book(String title, String author, int publicationYear, String genre, double price) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.price = price;
        this.read = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

//    public Set<User> getUsers() {
//        return users;
//    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // Helper methods
    public void addUser(User user) {
        this.users.add(user);
        user.getBooks().add(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getBooks().remove(this);
    }

//    public void addUser(User user) {
//        UserBook userBook = new UserBook(user, this);
//        userBooks.add(userBook);
//        user.getUserBooks().add(userBook);
//    }

//    public void removeUser(User user) {
//        UserBook userBook = userBooks.stream()
//                .filter(ub -> ub.getUser().equals(user))
//                .findFirst()
//                .orElse(null);
//        if (userBook != null) {
//            userBooks.remove(userBook);
//            user.getUserBooks().remove(userBook);
//        }
//    }

    public Set<User> getUsers() {
        return userBooks.stream()
                .map(UserBook::getUser)
                .collect(Collectors.toSet());
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", read=" + read +
                '}';
    }
}