package uk.ac.ncl.j_carlton.comichub;

/**
 * Object representation of a Comic Book.
 *
 * The generally usage of this class will be inline with the
 * data that is stored on the AWS database.
 *
 * @author Jonathan Carlton - 130266400
 */
public class ComicBook {

    /*
        Object variables.
     */
    private int id;
    private String name;
    private String volume;
    private int issue;
    private String publisher;
    private String published_date;
    private String image_ref;
    private boolean in_library;

    /**
     * Class constructor.
     * Used to build a Comic Book object.
     * @param id                id of the comic book
     * @param name              name of the comic book
     * @param volume            the volume of which the comic book is a part of
     * @param issue             the issue number
     * @param publisher         the comic book publisher
     * @param published_date    when the comic book was published
     * @param image_ref         a reference to the cover art work
     * @param in_library        is the object in the library?
     */
    public ComicBook(int id, String name, String volume, int issue, String publisher, String published_date, String image_ref, boolean in_library) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.issue = issue;
        this.publisher = publisher;
        this.published_date = published_date;
        this.image_ref = image_ref;
        this.in_library = in_library;
    }

    /**
     * String representation of the object, used for printing to
     * console in a formatted way.
     * @return  the objects variables as a formatted string.
     */
    @Override
    public String toString() {
        return "ComicBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", volume='" + volume + '\'' +
                ", issue=" + issue +
                ", publisher='" + publisher + '\'' +
                ", published_date='" + published_date + '\'' +
                ", image_ref='" + image_ref + '\'' +
                ", in_library=" + in_library +
                '}';
    }

    /**
     * Compares the current current object with another, passed as
     * a parameter
     * @param o     the other object to compare
     * @return      true, if they are equal and false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComicBook comicBook = (ComicBook) o;

        if (getId() != comicBook.getId()) return false;
        if (getIssue() != comicBook.getIssue()) return false;
        if (isIn_library() != comicBook.isIn_library()) return false;
        if (getName() != null ? !getName().equals(comicBook.getName()) : comicBook.getName() != null)
            return false;
        if (getVolume() != null ? !getVolume().equals(comicBook.getVolume()) : comicBook.getVolume() != null)
            return false;
        if (getPublisher() != null ? !getPublisher().equals(comicBook.getPublisher()) : comicBook.getPublisher() != null)
            return false;
        return getPublished_date() != null ? getPublished_date().equals(comicBook.getPublished_date()) : comicBook.getPublished_date() == null;

    }

    /**
     * Generates the hash code for the object, using its variables
     * @return  the hash code
     */
    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getVolume() != null ? getVolume().hashCode() : 0);
        result = 31 * result + getIssue();
        result = 31 * result + (getPublisher() != null ? getPublisher().hashCode() : 0);
        result = 31 * result + (getPublished_date() != null ? getPublished_date().hashCode() : 0);
        return result;
    }

    /**
     * Getter methods
     * @return return the object variable that is requested
     */
    public int getId() { return id; }
    public String getName() {
        return name;
    }
    public String getVolume() {
        return volume;
    }
    public int getIssue() {
        return issue;
    }
    public String getPublisher() {
        return publisher;
    }
    public String getPublished_date() {
        return published_date;
    }
    public String getImage_ref() {
        return image_ref;
    }
    public boolean isIn_library() {
        return in_library;
    }

    /**
     * Setter methods.
     * Used to set the object variables in the class
     */
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
    public void setIssue(int issue) {
        this.issue = issue;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }
    public void setImage_ref(String image_ref) {
        this.image_ref = image_ref;
    }
    public void setIn_library(boolean in_library) {
        this.in_library = in_library;
    }

}
