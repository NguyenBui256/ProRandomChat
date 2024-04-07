module ProRandomChat {
    requires javafx.controls;
    requires javafx.fxml;

    opens ProRandomChat.Controller to javafx.fxml;
    exports ProRandomChat.Controller;
    exports ProRandomChat.Model;
    exports ProRandomChat;
    opens ProRandomChat to javafx.fxml;
    opens ProRandomChat.Model to javafx.fxml;

}