package Model;

public class User {
    private String name;
    private String prodi;
    private String tahunmasuk;
    // private String Phone;


    public User() {
    }

    public User(String name, String prodi, String tahunmasuk) {
        this.name = name;
        this.prodi = prodi;
        this.tahunmasuk = tahunmasuk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getTahunmasuk() {
        return tahunmasuk;
    }

    public void setTahunmasuk(String tahunmasuk) {
        this.tahunmasuk = tahunmasuk;
    }
}