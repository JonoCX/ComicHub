package uk.ac.ncl.j_carlton.comichub;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object representation of a Comic Book.
 *
 * The generally usage of this class will be inline with the
 * data that is stored on the AWS database.
 *
 * Implements Parcelable as the de-facto method for passing
 * objects between activities.
 *
 * @author Jonathan Carlton - 130266400
 */

public class ComicBook implements Parcelable {

    /*
        Object variables.
     */
    private int id;
    private String name;
    private String volume;
    private int issue;
    private String publisher;
    private String publishedDate;
    private String imageRef;
    private boolean inLibrary;

    /**
     * Class constructor.
     * Used to build a Comic Book object.
     * @param id                id of the comic book
     * @param name              name of the comic book
     * @param volume            the volume of which the comic book is a part of
     * @param issue             the issue number
     * @param publisher         the comic book publisher
     * @param publishedDate    when the comic book was published
     * @param imageRef         a reference to the cover art work
     * @param inLibrary        is the object in the library?
     */
    public ComicBook(int id, String name, String volume, int issue, String publisher, String publishedDate, String imageRef, boolean inLibrary) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.issue = issue;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.imageRef = imageRef;
        this.inLibrary = inLibrary;
    }

    /**
     * Create a ComicBook object from a Parcel
     * @param in    Parcel containing the parameters for the object.
     */
    public ComicBook(Parcel in) {
        String[] data = new String[8];
        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.volume = data[2];
        this.issue = Integer.parseInt(data[3]);
        this.publisher = data[4];
        this.publishedDate = data[5];
        this.imageRef = data[6];
        this.inLibrary = Boolean.parseBoolean(data[7]);
    }

    /**
     * String representation of the object, used for printing to
     * console in a formatted way.
     * @return  the objects variables as a formatted string.
     */
    @Override
    public String toString() {
        if (name == null) {
            return volume + "\n" + issue;
        }
        return name + "\n" + issue;
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
        if (isInLibrary() != comicBook.isInLibrary()) return false;
        if (getName() != null ? !getName().equals(comicBook.getName()) : comicBook.getName() != null)
            return false;
        if (getVolume() != null ? !getVolume().equals(comicBook.getVolume()) : comicBook.getVolume() != null)
            return false;
        if (getPublisher() != null ? !getPublisher().equals(comicBook.getPublisher()) : comicBook.getPublisher() != null)
            return false;
        return getPublishedDate() != null ? getPublishedDate().equals(comicBook.getPublishedDate()) : comicBook.getPublishedDate() == null;

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
        result = 31 * result + (getPublishedDate() != null ? getPublishedDate().hashCode() : 0);
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
    public String getPublishedDate() {
        return publishedDate;
    }
    public String getImageRef() {
        return imageRef;
    }
    public boolean isInLibrary() {
        return inLibrary;
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
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }
    public void setInLibrary(boolean inLibrary) {
        this.inLibrary = inLibrary;
    }

    public static final Parcelable.Creator<ComicBook> CREATOR = new Parcelable.Creator<ComicBook>() {
        /**
         * Take a parcel and convert it into a comic book object
         * @param source    the parcel to be converted
         * @return the new object created from the contents of the parcel
         */
        @Override
        public ComicBook createFromParcel(Parcel source) {
            return new ComicBook(source);
        }

        /**
         * Create a comic book array
         * @param size      of a particular size
         * @return the array
         */
        @Override
        public ComicBook[] newArray(int size) {
            return new ComicBook[size];
        }
    };

    /**
     * Has to be overridden as part of the implementation of the class
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write the current object to a parcel object ready to be
     * transferred between activities.
     * @param dest      the destination parcel
     * @param flags     any flags needed
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                Integer.toString(this.getId()),
                this.getName(),
                this.getVolume(),
                Integer.toString(this.getIssue()),
                this.getPublisher(),
                this.getPublishedDate(),
                this.getImageRef(),
                Boolean.toString(this.isInLibrary())
        });
    }
}
