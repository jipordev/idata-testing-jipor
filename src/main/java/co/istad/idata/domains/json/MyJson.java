package co.istad.idata.domains.json;

import java.io.Serializable;

public class MyJson implements Serializable {

    private String stringProp;
    private Long longProp;

    public void setLongProp(Long longProp) {
        this.longProp = longProp;
    }

    public void setStringProp(String stringProp) {
        this.stringProp = stringProp;
    }

    public Long getLongProp() {
        return longProp;
    }

    public String getStringProp() {
        return stringProp;
    }
}
