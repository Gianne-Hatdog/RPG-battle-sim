import java.util.Scanner;

public class BattleSimulator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean playing = true;

        while (playing) {
            showStartMenu(scanner);              // Show title and start/exit menu
            Player player = createPlayer(scanner);  // Let player choose name and class

            exploreDungeon(player, scanner);    // Player explores dungeon, fights normal enemies

            if (player.isAlive()) {
                fightBoss(player, scanner);     // Player fights final boss if alive
            } else {
                System.out.println("\nYou died in the dungeon...");
            }

            playing = playAgain(scanner);       // Ask player if they want to play again
        }

        scanner.close();
        System.out.println("Thanks for playing!");
    }

    // ----------- START MENU -----------
    private static void showStartMenu(Scanner scanner) {
        // Display game title and options
        System.out.println("=== CTRL + ALT + Dungeon ===");
        System.out.println("1. Start Game");
        System.out.println("2. Exit");

        // Keep asking until a valid option is selected
        int menuChoice = 0;
        while (true) {
            System.out.print("Choose: ");
            try {
                menuChoice = scanner.nextInt();
                scanner.nextLine();

                if (menuChoice == 1) break;     // Start game
                if (menuChoice == 2) {          // Exit game
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                }

                System.out.println("Invalid choice!");
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }
    }

    // ----------- PLAYER CREATION -----------
    private static Player createPlayer(Scanner scanner) {
        // Let player enter a name or default to "Player"
        System.out.println("You slowly open your eyes... huh, where are you?");
        System.out.println("The place is dark, cold, and kind of creepy...");
        System.out.println("You have forgotten everything even your name...");
        System.out.println("Enter a name (leave blank for 'Player'): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Player";

        // Let player pick a class
        System.out.println("\nChoose your class:");
        System.out.println("1. Staff (Mage)");
        System.out.println("2. Sword (Warrior)");

        int choice = 0;
        while (true) {
            try {
                System.out.print("Enter choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1 || choice == 2) break;
                System.out.println("Invalid choice!");
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }

        // Create the chosen class object
        if (choice == 1) {
            System.out.println("\nYou have become a Mage!");
            return new Mage(name);
        } else {
            System.out.println("\nYou have become a Warrior!");
            return new Warrior(name);
        }
    }

    // ----------- DUNGEON EXPLORATION -----------
    // Player explores 3 normal rooms (north/east/west) and then goes to boss room
    private static void exploreDungeon(Player player, Scanner scanner) {
        boolean northDone = false, eastDone = false, westDone = false;

        while (player.isAlive()) {

            // Ask direction using numbers (1-4)
            int dirChoice = 0;
            while (true) {
                System.out.println("\n===== Choose a Path =====");
                System.out.println("1. North  - " + (northDone ? "Cleared" : "Enemy inside"));
                System.out.println("2. East   - " + (eastDone ? "Cleared" : "Enemy inside"));
                System.out.println("3. West   - " + (westDone ? "Cleared" : "Enemy inside"));
                System.out.println("4. South  - Boss Room " + (player.getLevel() >= 3 ? "(Unlocked)" : "(Locked)"));
                System.out.print("Enter (1-4): ");

                try {
                    dirChoice = scanner.nextInt();
                    scanner.nextLine();
                    if (dirChoice >= 1 && dirChoice <= 4) break;
                    System.out.println("Invalid number!");
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                    scanner.nextLine();
                }
            }

            // Convert numeric choice to direction string
            String dir = switch (dirChoice) {
                case 1 -> "north";
                case 2 -> "east";
                case 3 -> "west";
                case 4 -> "south";
                default -> "";
            };

            // Boss room requires level 3
            if (dir.equals("south")) {
                if (player.getLevel() < 3) {
                    System.out.println("A powerful force blocks your way. You must reach level 3 first.");
                    continue;
                }
                break; // Proceed to boss
            }

            // Fight corresponding enemy if room not cleared
            switch (dir) {
                case "north" -> {
                    if (!northDone) northDone = fightEnemy(player, "Skeleton", scanner);
                    else System.out.println("Room already cleared!");
                }
                case "east" -> {
                    if (!eastDone) eastDone = fightEnemy(player, "Goblin", scanner);
                    else System.out.println("Room already cleared!");
                }
                case "west" -> {
                    if (!westDone) westDone = fightEnemy(player, "Zombie", scanner);
                    else System.out.println("Room already cleared!");
                }
            }

            // Level up after clearing any room
            if ((dir.equals("north") && northDone) ||
                (dir.equals("east") && eastDone) ||
                (dir.equals("west") && westDone)) {
                player.levelUp();
            }
        }
    }

    // ----------- NORMAL ENEMY FIGHT -----------
    private static boolean fightEnemy(Player player, String enemyName, Scanner scanner) {
        // Create a normal monster and start the battle
        Monster enemy = new Monster(enemyName, 80, 15, 5);
        System.out.println("\nA " + enemyName + " appears!");
        return runBattle(player, enemy, scanner);
    }

    // ----------- BOSS FIGHT -----------
    private static void fightBoss(Player player, Scanner scanner) {
        System.out.println("\nYou enter the boss chamber...");
        Monster boss = new Monster("Dungeon Lord", 250, 35, 10); // Boss stats

        boolean won = runBattle(player, boss, scanner);

        if (won) {
            System.out.println("\n===== VICTORY! =====");
            System.out.println("You have defeated the Dungeon Lord and escaped!");
        } else {
            System.out.println("\n===== GAME OVER =====");
        }
    }

    // ----------- MAIN BATTLE LOOP -----------
    private static boolean runBattle(Player player, Monster monster, Scanner scanner) {
        int consecutiveHits = 0; // Tracks when special attack is ready

        while (player.isAlive() && monster.isAlive()) {

            // Display current HP and resource (Mana/Energy)
            System.out.println("\n------------------------------");
            System.out.println(player.getName() + " HP: " + player.getHealth());
            if (player instanceof Mage) System.out.println("Mana: " + ((Mage) player).getMana());
            if (player instanceof Warrior) System.out.println("Energy: " + ((Warrior) player).getEnergy());
            System.out.println(monster.getName() + " HP: " + monster.getHealth());
            System.out.println("------------------------------");

            int action = 0;

            // Ask player for action (Attack / Defend / Special)
            while (true) {
                System.out.println("Choose your action:");
                System.out.println("1. Attack");
                System.out.println("2. Defend");
                if (consecutiveHits >= 3) System.out.println("3. Special Attack (Ready!)");
                System.out.print("Enter: ");

                try {
                    action = scanner.nextInt();
                    scanner.nextLine();
                    if (action >= 1 && action <= 3) break;
                    System.out.println("Invalid action!");
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                    scanner.nextLine();
                }
            }

            boolean defended = false;

            // Perform chosen action
            switch (action) {
                case 1 -> { player.attack(monster); consecutiveHits++; } // Normal attack
                case 2 -> { 
                    System.out.println(player.getName() + " braces for the attack!");
                    defended = true; consecutiveHits = 0;               // Defend reduces damage
                }
                case 3 -> {                                           // Special attack
                    if (consecutiveHits >= 3) {
                        if (player.specialAttack(monster)) consecutiveHits = 0;
                    } else {
                        System.out.println("You need 3 consecutive attacks first!");
                    }
                }
            }

            if (!monster.isAlive()) break;

            // Monster attacks or reduced damage if defended
            if (defended) {
                int reduced = Math.max(0, monster.getAttackDamage() - player.getDefense() * 2);
                System.out.println("The monster attacks but the damage is reduced!");
                player.takeDamage(reduced);
            } else {
                monster.attack(player);
            }
        }

        return player.isAlive(); // Return true if player won, false if dead
    }

    // ----------- PLAY AGAIN -----------
    private static boolean playAgain(Scanner scanner) {
        // Ask if player wants to restart or exit
        while (true) {
            System.out.println("\n1. Play Again");
            System.out.println("2. Exit");
            try {
                int again = scanner.nextInt();
                scanner.nextLine();
                return again == 1;
            } catch (Exception e) {
                System.out.println("Invalid input!");
                scanner.nextLine();
            }
        }
    }
                        }
