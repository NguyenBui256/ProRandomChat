package ProRandomChat.Model;

import java.io.Serializable;

public class ResponseFromServer implements Serializable {
    public String type, content;
    public Object object;
    public ResponseFromServer(String type, String content) {
        this.type = type;
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public String getContent() {
        return content;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Object getObject() {return object;}

    public void setObject(Object object) {
        this.object = object;
    }
}
