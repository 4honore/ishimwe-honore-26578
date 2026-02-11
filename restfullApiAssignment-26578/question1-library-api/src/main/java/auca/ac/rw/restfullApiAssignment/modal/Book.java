package auca.ac.rw.restfullApiAssignment.modal;

public class Book {
    private long id;
    private String title;
    private String author;
    private String isbn;
    private int publishedYear;

public Book() {}

public Book(long id, String title, String author, String isbn, int publishedYear) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.isbn = isbn;
    this.publishedYear = publishedYear;
}
//getter and setter
   public long getId() {
    return id;
    }
    public void setId(long id) {
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
        public String getIsbn() {
            return isbn;
        }
        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }
        public int getPublishedYear() {
            return publishedYear;
        }
        public void setPublishedYear(int publishedYear) {
            this.publishedYear = publishedYear;
        }
    }
