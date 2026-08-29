package chess;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Chess extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        var textArea = new TextArea();
        ChessGame game = new ChessGame();
        ChessBoardGUI board = new ChessBoardGUI(8, 8, game, textArea);
        ChessBoard chessBoard = game.getBoard();

        // add piece icons to the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);

                if (piece != null) {
                    board.addPieceIcon(piece);
                }

            }
        }

        // Creating UI Buttons
        var resignBtn = new Button("Resign");
        var drawBtn = new Button("Offer Draw");

        // Button functionality
        resignBtn.setOnAction((event) -> {
            if (game.getGameOver() == true) {
                return;
            }
            System.out.println("Resign button pressed");
            board.resigned();
            game.setGameOver(true);

        });
        drawBtn.setOnAction((event) -> {
            if (game.getGameOver() == true) {
                return;
            }
            System.out.println("Draw button pressed");
            board.openDrawChoice();
        });
        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });

        // Arranging UI Elements.
        var toolbar = new ToolBar();
        toolbar.getItems().addAll(resignBtn, drawBtn);

        var movesMadeLabel = new Label("MOVES MADE");
        movesMadeLabel.setStyle("-fx-font-size: 18 px; -fx-font-weight: bold;");
        movesMadeLabel.setMaxWidth(Double.MAX_VALUE);
        movesMadeLabel.setAlignment(Pos.CENTER);

        textArea.setEditable(false);
        var rightSide = new VBox();
        rightSide.getChildren().addAll(movesMadeLabel, textArea, toolbar);
        VBox.setVgrow(textArea, Priority.ALWAYS);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(board, rightSide);
        splitPane.setDividerPositions(0.75);

        stage.setTitle("Chess");
        var contentPane = new BorderPane();
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        stage.setScene(scene);
        stage.show();
    }
}
