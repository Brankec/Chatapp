import javafx.application.Platform;

public class Controller {
    private boolean isServer = false;

    public javafx.scene.control.TextArea ChatArea;
    public javafx.scene.control.TextField MessageArea;
    public javafx.scene.text.Text user;

    private NetworkConnection connection = isServer ? createServer() : createClient();

    private Server createServer() {
        return new Server (52049, data -> {
            Platform.runLater(() -> {
                ChatArea.appendText(data.toString() + "\n");
            });
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 52049, data -> {
            Platform.runLater(() -> {
                ChatArea.appendText(data.toString() + "\n");
            });
        });
    }

    public void initialize() throws Exception {
        if(isServer){
            user.setText("Server");
        } else {
            user.setText("Client");
        }

        connection.startConnection();

        MessageArea.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += MessageArea.getText();
            MessageArea.setText("");

            ChatArea.appendText(message + "\n");

            try {
                connection.send(message);
            } catch (Exception e) {
                ChatArea.appendText("Failed to send\n");
                e.printStackTrace();
            }
        });
    }
    public void stop() throws Exception {
        connection.closeConnection();
    }
}
