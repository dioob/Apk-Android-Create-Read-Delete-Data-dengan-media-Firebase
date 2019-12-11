package dioobanu.yahoo.employeeku;

public class DataPegawai {

    public String nama;
    public String alamat;
    public String tempattgllahir;
    public String jeniskelamin;
    public String email;
    public String nohp;
    public String gambarprofil;

    public DataPegawai(String nama, String alamat, String tempattgllahir, String jeniskelamin, String email, String nohp, String gambarprofil) {
        this.nama = nama;
        this.alamat = alamat;
        this.tempattgllahir = tempattgllahir;
        this.jeniskelamin = jeniskelamin;
        this.email = email;
        this.nohp = nohp;
        this.gambarprofil = gambarprofil;
    }

    public DataPegawai(){

    }

    public String getGambarprofil() {
        return gambarprofil;
    }

    public void setGambarprofil(String gambarprofil) {
        this.gambarprofil = gambarprofil;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTempattgllahir() {
        return tempattgllahir;
    }

    public void setTempattgllahir(String tempattgllahir) {
        this.tempattgllahir = tempattgllahir;
    }

    public String getJeniskelamin() {
        return jeniskelamin;
    }

    public void setJeniskelamin(String jeniskelamin) {
        this.jeniskelamin = jeniskelamin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}
