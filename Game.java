import javax.swing.JOptionPane;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class Game {
    /**
     * Variables used for game logic
     * */
    static Scanner sc = new Scanner(System.in);
    static int scorePlayer = 0;
    static int scorePlayer2 = 0;
    static int scoreMachine = 0;
    static final int rounds = 5;
    static boolean newGamePlus = false;
    static String[] hand = {"Paper", "Scissors", "Rock"};


    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Init game");
        while (true) {
            String states =  newGamePlus?JOptionPane.showInputDialog("Delete last score?\nyes\nno"):null;

            if (states != null && states.equals("yes")) {resetScores();}

            String mode = JOptionPane.showInputDialog("Select mode: \n1-PvE\n2-PvP\n3-Challenge");
            try {
                int value = Integer.parseInt(mode);
                switch (value) {
                    case 1:
                        playerVsMachine();
                        break;
                    case 2:
                        playerVsPlayer();
                        break;
                    case 3:
                        challengeMode();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid mode. Exiting game.");
                        break;
                }
            }catch (NumberFormatException e){
                Logger.getLogger("Error").warning(e.getMessage());
            }finally {
                String playAgain = JOptionPane.showInputDialog("Do you want to play again? (yes/no)");
                if (playAgain.equalsIgnoreCase("no")) {
                    break;
                }
            }
        }
        sc.close();
    }
    /**
     * method for player versus machine match
     * */
    public static void playerVsMachine() {
        Random rand = new Random();
        for (int i = 0; i < rounds; i++) {
            JOptionPane.showMessageDialog(null, "Round " + (i + 1));
            do {
            try {
                String playerMoveInput = JOptionPane.showInputDialog("Select your move: 1-Paper\n2-Scissors\n3-Rock");
                int playerMove = Integer.parseInt(playerMoveInput) - 1;
                int machineMove = rand.nextInt(3);

                JOptionPane.showMessageDialog(null, "You chose " + hand[playerMove]);
                JOptionPane.showMessageDialog(null, "Machine chose " + hand[machineMove]);

                resultBattle(playerMove,machineMove,false);
                break;
            }catch (RuntimeException e){
                Logger.getLogger("Error").warning(e.getMessage());
                JOptionPane.showMessageDialog(null, "You chose invalid move!");
            }
            }while (true);

        }
        JOptionPane.showMessageDialog(null, "Final Score: Player: " + scorePlayer + " - Machine: " + scoreMachine);
        newGamePlus = true;
    }
    /**
     * method for player versus player match
     * */
    public static void playerVsPlayer() {
        int player1Move = 0;
        int player2Move = 0;
        for (int i = 0; i < rounds; i++) {
            JOptionPane.showMessageDialog(null, "Round " + (i + 1));
            do {
                try {
                    String player1MoveInput = JOptionPane.showInputDialog("Player 1, select your move: 1-Paper\n2-Scissors\n3-Rock");
                    player1Move = Integer.parseInt(player1MoveInput) - 1;
                    if (player1Move >2) {throw new RuntimeException("Invalid move");}
                    break;
                }catch (RuntimeException e){
                    Logger.getLogger("Error").warning(e.getMessage());
                    JOptionPane.showMessageDialog(null, "You chose invalid move!");
                }
            }while (true);

            do {
                try {
                    String player2MoveInput = JOptionPane.showInputDialog("Player 2, select your move: 1-Paper\n2-Scissors\n3-Rock");
                    player2Move = Integer.parseInt(player2MoveInput) - 1;
                    if (player2Move >2) {throw new RuntimeException("Invalid move");}
                    break;
                }catch (RuntimeException e){
                    Logger.getLogger("Error").warning(e.getMessage());
                    JOptionPane.showMessageDialog(null, "You chose invalid move!");
                }
            }while (true);




            JOptionPane.showMessageDialog(null, "Player 1 chose " + hand[player1Move]);
            JOptionPane.showMessageDialog(null, "Player 2 chose " + hand[player2Move]);

            resultBattle(player1Move,player2Move,true);
        }
        JOptionPane.showMessageDialog(null, "Final Score: Player 1: " + scorePlayer + " - Player 2: " + scorePlayer2);
        newGamePlus = true;
    }
    /**
     * method for challenge using the best of luck rule
     * */
    public static void challengeMode() {
        int rounds = getRounds();
        Random rand = new Random();
        for (int i = 0; i < rounds; i++) {
            JOptionPane.showMessageDialog(null, "Round " + (i + 1));
            String player1MoveInput = JOptionPane.showInputDialog("Player 1, select your move: 1-Paper\n2-Scissors\n3-Rock");
            int playerMove = Integer.parseInt(player1MoveInput) - 1;
            int machineMove = rand.nextInt(3);

            JOptionPane.showMessageDialog(null, "Player 1 chose " + hand[playerMove]);
            JOptionPane.showMessageDialog(null, "Machine chose " + hand[machineMove]);

            resultBattle(playerMove,machineMove,false);

            if (hasWinner(scorePlayer, scoreMachine, rounds)) break;

        }
        JOptionPane.showMessageDialog(null, "Final Score: Player: " + scorePlayer + " - Machine: " + scoreMachine);
        resetScores();
    }
    /**
     * method that to receive the players' response as a parameter and the type of match
     * */
    public static void resultBattle(int player1, int player2, boolean type){
        if (player1 == player2) {
            JOptionPane.showMessageDialog(null, "It's a tie!");
        }
        /**
         * a single if that only checks the probability of player 1 winning, if it is not in that probability, player 2 or machine wins
         * */
        else if ((player1 == 0 && player2 == 2) || (player1 == 1 && player2 == 0) || (player1 == 2 && player2 == 1)) {
            JOptionPane.showMessageDialog(null, "You player 1 win this round!");
            scorePlayer++;
        } else {
            JOptionPane.showMessageDialog(null, (type?"Player 2":"Machine") +"wins this round!");
            scoreMachine++;
        }
    }
    public static int getRounds() {
        String roundsInput = JOptionPane.showInputDialog("Select number of rounds: Best of 3, 5, 7, etc.");
        do {
            try {
                int rounds = Integer.parseInt(roundsInput);
                if (rounds % 2 == 0) {
                    JOptionPane.showMessageDialog(null, "Number of rounds must be odd for a 'Best of' series. Adjusting to " + (rounds + 1) + " rounds.");
                    rounds++;
                }
                return rounds;
            }catch (RuntimeException e){
                Logger.getLogger("Error").warning(e.getMessage());
                JOptionPane.showMessageDialog(null, "You chose invalid move!");
            }
        }while (true);

    }

    public static boolean hasWinner(int score1, int score2, int rounds) {
        int requiredWins = (rounds / 2) + 1;
        if (score1 == requiredWins || score2 == requiredWins) {
            return true;
        }
        return false;
    }
    public static void resetScores() {
        scorePlayer = 0;
        scorePlayer2 = 0;
        scoreMachine = 0;
    }
}
