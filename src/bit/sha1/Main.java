package bit.sha1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Hlavní třída aplikace. Slouží pro vytvoření GUI
 * @author Lukáš Haringer
 */
public class Main extends Application {

    Label lHash1;
    Label lHash2;
    Label lInput;
    TextField input;
    TextField output1;
    TextField output2;
    private Stage primaryStage;

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("SHA-1");
        this.primaryStage.setScene(new Scene(getRoot(), 400, 500));
        this.primaryStage.show();
    }

    /**
     * Vytvoření GUI
     * @return BorderPane mainPane
     */
    private Parent getRoot() {

        BorderPane mainPane = new BorderPane();

        VBox vertLayout1 = new VBox();
        VBox vertLayout2 = new VBox();

        Label lTitul = new Label();
        lInput = new Label();
        lHash1 = new Label();
        lHash2 = new Label();
        input = new TextField();
        output1 = new TextField();
        output2 = new TextField();
        output1.setEditable(false);
        output2.setEditable(false);
        vertLayout1.getChildren().add(lTitul);
        vertLayout1.setAlignment(Pos.CENTER);
        lTitul.setText("SHA-1");
        lTitul.setFont(new Font("Arial", 50));
        lInput.setText("Zadejte řetězec pro hashování");
        lHash1.setText("Hash za pomoci vlastní funkce");
        lHash2.setText("Hash za pomoci knihovní funkce");
        lTitul.setPadding(new Insets(30));

        Button button = new Button("Hash");
        button.setOnAction(event -> click());

        vertLayout2.setSpacing(20);
        vertLayout2.setAlignment(Pos.CENTER);
        vertLayout2.getChildren().addAll(lInput, input, button, lHash1, output1, lHash2, output2);

        mainPane.setPadding(new Insets(20));
        mainPane.setTop(vertLayout1);
        mainPane.setCenter(vertLayout2);

        return mainPane;
    }

    /**
     * Metoda pro reakci na stisknutí tlačítka Hash
     */
    public void click() {
        if (input.getText() == null  || input.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nejdříve zadejte řetězec pro hashování");
            alert.setTitle("Nezadán řetězec");
            alert.showAndWait();
        } else {
            SHA1 hash = new SHA1();
            String result = hash.hashIt(input.getText().getBytes());
            output1.setText(result);
            String result2 = hash.hashItLibrary(input.getText().getBytes());
            output2.setText(result2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
