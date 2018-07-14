package tehhutan.app.kajianhunter.model;

/**
 * Created by tehhutan on 27/09/17.
 */

public class User {

    private String Email;
    private String Nama;
    private String Password;
    private String Photo;
    public User() {
    }

    public User(String email, String nama, String password, String photo) {
        Email = email;
        Nama = nama;
        Password = password;
        Photo = photo;
    }
    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
