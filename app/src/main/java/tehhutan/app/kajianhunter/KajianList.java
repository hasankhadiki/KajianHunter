package tehhutan.app.kajianhunter;

/**
 * Created by 5215100071 on 4/9/2018.
 */

public class KajianList {
    private String departemen;
    private String kegiatan;
    private String nama;
    private String jamMulai;
    private String jamAkhir;
    private String deskripsi;
    private String pict;

    public KajianList() {
    }

    public KajianList(String departemen, String kegiatan, String nama, String jamMulai, String jamAkhir, String deskripsi, String pict) {
        this.departemen = departemen;
        this.kegiatan = kegiatan;
        this.nama = nama;
        this.jamMulai = jamMulai;
        this.jamAkhir = jamAkhir;
        this.deskripsi = deskripsi;
        this.pict = pict;
    }

    public String getDepartemen() {
        return departemen;
    }

    public void setDepartemen(String departemen) {
        this.departemen = departemen;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamAkhir() {
        return jamAkhir;
    }

    public void setJamAkhir(String jamAkhir) {
        this.jamAkhir = jamAkhir;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict;
    }
}
