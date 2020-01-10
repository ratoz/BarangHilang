package Model;

public class UserUpdate {
    private String nickname;
    private String nohp;
    private String email;

    public UserUpdate() {}

    public UserUpdate(String nickname, String nohp, String email) {
        this.nickname = nickname;
        this.nohp = nohp;
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNohp() {
        return nohp;
    }

    public String getEmail() {
        return email;
    }
}

