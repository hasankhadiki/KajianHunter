package tehhutan.app.kajianhunter;

/**
 * Created by 5215100071 on 4/9/2018.
 */

public class KajianList {
    private String Departemen;
    private String Kegiatan;
    private String Nama;
    private String JamMulai;
    private String JamAkhir;
    private String Deskripsi;
    private String Pict;

    public KajianList() {
    }

    public KajianList(String departemen, String kegiatan, String nama, String jamMulai, String jamAkhir, String deskripsi, String pict) {
        Departemen = departemen;
        Kegiatan = kegiatan;
        Nama = nama;
        JamMulai = jamMulai;
        JamAkhir = jamAkhir;
        Deskripsi = deskripsi;
        Pict = pict;
    }

    public String getDepartemen() {
        return Departemen;
    }

    public void setDepartemen(String departemen) {
        Departemen = departemen;
    }

    public String getKegiatan() {
        return Kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        Kegiatan = kegiatan;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getJamMulai() {
        return JamMulai;
    }

    public void setJamMulai(String jamMulai) {
        JamMulai = jamMulai;
    }

    public String getJamAkhir() {
        return JamAkhir;
    }

    public void setJamAkhir(String jamAkhir) {
        JamAkhir = jamAkhir;
    }

    public String getDeskripsi() {
        return Deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        Deskripsi = deskripsi;
    }

    public String getPict() {
        return Pict;
    }

    public void setPict(String pict) {
        Pict = pict;
    }
}
