// New UserBook entity
package bp.books.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_books")
public class UserBook {
    @EmbeddedId
    private UserBookId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "is_read")
    private boolean read = false;

    public UserBook() {}

    public UserBook(User user, Book book) {
        this.user = user;
        this.book = book;
        this.id = new UserBookId(user.getUserid(), book.getId());
    }

    // Getters and setters
    public UserBookId getId() {
        return id;
    }

    public void setId(UserBookId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}