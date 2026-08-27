package chess;

import javafx.application.Application;
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

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);

                if (piece != null) {
                    addPieceIcon(board, piece);
                }

            }
        }

        // Creating UI Buttons
        var rematchBtn = new Button("Rematch");
        var resignBtn = new Button("Resign");
        var drawBtn = new Button("Offer Draw");

        rematchBtn.setOnAction((event) -> {
            System.out.println("Rematch button pressed");
            game.reset();
            board.getIcons().clear();
            ChessBoard newChessBoard = game.getBoard();

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece piece = newChessBoard.getPiece(row, col);

                    if (piece != null) {
                        addPieceIcon(board, piece);
                    }

                }

            }
            board.clearResignedMessage();
            board.clearStateMessage();
            board.requestLayout();
            game.setCurrentTurn("white");
            board.clearMovesMade();
            textArea.clear();
        });
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
        toolbar.getItems().addAll(rematchBtn, resignBtn, drawBtn);

        textArea.setEditable(false);
        var rightSide = new VBox();
        rightSide.getChildren().addAll(textArea, toolbar);
        VBox.setVgrow(textArea, Priority.ALWAYS);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(board, rightSide);
        splitPane.setDividerPositions(0.75);

        stage.setTitle("Chess");
        var contentPane = new BorderPane();
        // contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        stage.setScene(scene);
        stage.show();
    }

    private void addPieceIcon(ChessBoardGUI board, Piece piece) {

        var image = Chess.class.getClassLoader().getResourceAsStream(piece.getImageName());

        if (image != null) {
            board.getIcons().add(new PieceIcon(
                    piece.getCol(),
                    piece.getRow(),
                    image,
                    piece));
        } else {
            System.out.println("Could not load image: " + piece.getImageName());
        }

    }
}
