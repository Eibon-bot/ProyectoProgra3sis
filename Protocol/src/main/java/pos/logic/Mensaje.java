package pos.logic;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String from;
    private String to;
    private String text;


    public Mensaje(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }


    @Override
    public String toString() {
        return String.format("%s → %s: %s", from, to, text);
    }

}
