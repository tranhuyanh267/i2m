package web.payload;

import javax.validation.constraints.NotBlank;

public class MyInfluencersRequest {
    @NotBlank
    private String name;

    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
