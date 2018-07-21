package tehhutan.app.kajianhunter.model;

/**
 * Created by 5215100071 on 7/21/2018.
 */

public class SavedList {
    private String Nama;
    private String Tempat;
    private String Tema;

    public SavedList() {
    }

    public SavedList(String nama, String tempat, String tema) {
        Nama = nama;
        Tempat = tempat;
        Tema = tema;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTempat() {
        return Tempat;
    }

    public void setTempat(String tempat) {
        Tempat = tempat;
    }

    public String getTema() {
        return Tema;
    }

    public void setTema(String tema) {
        Tema = tema;
    }
}