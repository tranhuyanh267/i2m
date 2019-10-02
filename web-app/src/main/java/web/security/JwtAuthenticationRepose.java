package web.security;

public class JwtAuthenticationRepose {
    private String accessToken;
    private String typeToken="Bearer ";

    public JwtAuthenticationRepose(String accessToken){
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(String typeToken) {
        this.typeToken = typeToken;
    }
}
