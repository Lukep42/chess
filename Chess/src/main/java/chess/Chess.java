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

        // ChessBoard chessBoard = new ChessBoard();
        // ChessBoardGUI board = new ChessBoardGUI(8, 8, chessBoard, textArea);

        ChessGame game = new ChessGame();
        ChessBoardGUI board = new ChessBoardGUI(8, 8, game, textArea);
        ChessBoard chessBoard = game.getBoard();

        // String[] backRank = { "Rook", "Knight", "Bishop", "Queen", "King", "Bishop",
        // "Knight", "Rook" };

        // for (int i = 0; i < 8; i++) {
        // // black back rank
        // addPieceIcon(board, i, 0, backRank[i] + "-b.png");
        // // black pawns
        // addPieceIcon(board, i, 1, "pawn-b.png");

        // // white pawns
        // addPieceIcon(board, i, 6, "pawn-w.png");
        // // white back rank
        // addPieceIcon(board, i, 7, backRank[i] + "-w.png");

        // }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);

                if (piece != null) {
                    addPieceIcon(board, piece);
                }

            }
        }

        // Creating UI Buttons
        var startBtn = new Button("Rematch");
        var endBtn = new Button("Resign");

        startBtn.setOnAction((event) -> {
            System.out.println("Rematch button pressed");
        });
        endBtn.setOnAction((event) -> {
            System.out.println("Resign button pressed");
        });
        stage.setOnCloseRequest((event) -> {
            System.out.println("Close button pressed");
        });
        // var textArea = new TextArea();
        // textArea.appendText(board.getMoves().toString());
        // textArea.appendText("Sidebar\n");
        // textArea.appendText("Text\n");

        // Arranging UI Elements.
        var toolbar = new ToolBar();
        toolbar.getItems().addAll(startBtn, endBtn);

        var splitPane = new SplitPane();
        splitPane.getItems().addAll(board, textArea);
        splitPane.setDividerPositions(0.75);

        stage.setTitle("Chess");
        var contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);

        var scene = new Scene(contentPane, 1200, 1000);
        stage.setScene(scene);
        stage.show();
    }

    // private void addPieceIcon(ChessBoardGUI board, int x, int y, String
    // imageName) {
    // var image = Chess.class.getClassLoader().getResourceAsStream(imageName);

    // if (image != null) {
    // board.getIcons().add(new PieceIcon(
    // x, // x
    // y, // y
    // image));
    // } else {
    // System.out.println("Could not load image: " + imageName);
    // }
    // }

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
