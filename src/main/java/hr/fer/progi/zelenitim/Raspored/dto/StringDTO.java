package hr.fer.progi.zelenitim.Raspored.dto;

public class StringDTO {
    private String text;

    public StringDTO() {}

    public StringDTO(String assignToUsername) {
        this.text = assignToUsername;
    }

    public String getText() {
        return text;
    }

    public void setText(String assignToUsername) {
        this.text = assignToUsername;
    }
}
