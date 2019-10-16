package web.model;

public class UpdatePasswordModel {
    private String oldPassword;
    private String password;

    public UpdatePasswordModel() {
    }

    public UpdatePasswordModel(String oldPassword, String password) {
        this.oldPassword = oldPassword;
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
