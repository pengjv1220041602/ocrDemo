package ocr;

import java.util.List;

/**
 * @Author ZhPJ
 * @Date 2019/1/2 00028:54
 * @Version 1.0
 * @Description:
 */

public class OCRResponse {
    private Integer code;
    private String message;
    private Data data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

class Item {
    private Itemcoord itemcoord;
    private List<Word> words;
    private String itemstring;

    public Itemcoord getItemcoord() {
        return itemcoord;
    }

    public void setItemcoord(Itemcoord itemcoord) {
        this.itemcoord = itemcoord;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public String getItemstring() {
        return itemstring;
    }

    public void setItemstring(String itemstring) {
        this.itemstring = itemstring;
    }
}

class Itemcoord {
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}

class Word {
    private String character;
    private Double confidence;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}

class Data {
    private Integer angle;
    private List<Item> items;
    private String session_id;

    public Integer getAngle() {
        return angle;
    }

    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}

