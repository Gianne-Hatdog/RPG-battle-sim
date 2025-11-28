import java.util.Scanner;

public class BattleSimulator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean playing = true;

        while (playing) {
            showStartMenu(scanner);              // Shows title screen
            Player player = createPlayer(scanner);  // Player chooses class & name

            exploreDungeon(player, scanner);    // Explore 3 enemy rooms

            if (player.isAlive()) {
                fightBoss(player, scanner);     // If still alive, fight boss
            } else {
                System.out.println("\nYou died in the dungeon...");
            }

            playing = playAgain(scanner);       // Ask if they want to replay
        }

        scanner.close();
        System.out.println("Thanks for playing!");
    }

    // --------------------- START MENU ---------------------
    private static void showStartMenu(Scanner scanner) {
        System.out.println("=== CTRL + ALT + Dungeon ===");
        System.out.println("1. Start Game");
        System.out.println("2. Exit");

        int menuChoice = 0;
        while (true) {
            System.out.print("Choose: ");
            try {
                menuChoice = scanner.nextInt();
                scanner.nextLine();
                if (menuChoice == 1) break;     // Start game
                else if (menuChoice == 2) {
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);             // End program
                } else {
                    System.out.println("Invalid choice! Enter 1 or 2.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
                scanner.nextLine();
            }
        }
    }

    // --------------------- PLAYER CREATION ---------------------
    private static Player createPlayer(Scanner scanner) {
        System.out.print("\nYou have forgotten your name...\nEnter a name (leave blank for 'Player'): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Player";    // Default name

        System.out.println("\nIn front of you lies a STAFF and a SWORD.");
        System.out.println("Which will you pick?");
        System.out.println("1. Staff (Mage)");
        System.out.println("2. Sword (Warrior)");

        int choice = 0;
        while (true) {
            System.out.print("Enter choice (1 or 2): ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1 || choice == 2) break;
                else System.out.println("Invalid input! Choose 1 or 2.");
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
                scanner.nextLine();
            }
        }

        // Create chosen class
        Player player;
        if (choice == 1) {
            player = new Mage(name);
            System.out.println("\nYou have become a Mage!");
        } else {
            player = new Warrior(name);
            System.out.println("\nYou have become a Warrior!");
        }

        return player;
    }

    // --------------------- DUNGEON EXPLORATION ---------------------
    private static void exploreDungeon(Player player, Scanner scanner) {
        boolean northDone = false, eastDone = false, westDone = false;

        while (player.isAlive()) {
            // Show available rooms
            System.out.println("\n===== Dungeon Paths =====");
            System.out.println("North - " + (northDone ? "Cleared" : "Enemy inside"));
            System.out.println("East  - " + (eastDone ? "Cleared" : "Enemy inside"));
            System.out.println("West  - " + (westDone ? "Cleared" : "Enemy inside"));
            System.out.println("South - Boss Room " + (player.getLevel() >= 3 ? "(Unlocked)" : "(Locked until level 3)"));

            // Ask for direction
            String dir = "";
            while (true) {
                System.out.print("\nWhere do you go? (north/east/west/south): ");
                dir = scanner.nextLine().trim().toLowerCase();
                if (dir.equals("north") || dir.equals("east") || dir.equals("west") || dir.equals("south")) break;
                else System.out.println("Invalid direction! Type north, east, west, or south.");
            }

            // South = boss room (requires level 3)
            if (dir.equals("south")) {
                if (player.getLevel() < 3) {
                    System.out.println("A powerful force blocks your way. You must reach level 3 first.");
                    continue;
                }
                break; // Continue to boss fight
            }

            // Fight enemies in each room
            switch (dir) {
                case "north":
                    if (!northDone) northDone = fightEnemy(player, "Skeleton", scanner);
                    else System.out.println("Room already cleared!");
                    break;
                case "east":
                    if (!eastDone) eastDone = fightEnemy(player, "Goblin", scanner);
                    else System.out.println("Room already cleared!");
                    break;
                case "west":
                    if (!westDone) westDone = fightEnemy(player, "Zombie", scanner);
                    else System.out.println("Room already cleared!");
                    break;
            }

            // Player levels up after clearing each room
            if ((dir.equals("north") && northDone) || (dir.equals("east") && eastDone) || (dir.equals("west") && westDone)) {
                player.levelUp(); // Adds stat boosts + full heal + full mana/energy
            }
        }
    }

    // --------------------- NORMAL ENEMY ---------------------
    private static boolean fightEnemy(Player player, String enemyName, Scanner scanner) {
        Monster enemy = new Monster(enemyName, 80, 15, 5); // Normal enemy stats
        System.out.println("\nA " + enemyName + " appears!");
        return runBattle(player, enemy, scanner);
    }

    // --------------------- BOSS FIGHT ---------------------
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

    // --------------------- BATTLE SYSTEM ---------------------
    private static boolean runBattle(Player player, Monster monster, Scanner scanner) {
        int consecutiveHits = 0; // Tracks consecutive attacks for special attack

        while (player.isAlive() && monster.isAlive()) {

            // Show combat stats
            System.out.println("\n------------------------------");
            System.out.println(player.getName() + " HP: " + player.getHealth());
            if (player instanceof Mage) System.out.println("Mana: " + ((Mage) player).getMana());
            if (player instanceof Warrior) System.out.println("Energy: " + ((Warrior) player).getEnergy());
            System.out.println(monster.getName() + " HP: " + monster.getHealth());
            System.out.println("------------------------------");

            int action = 0;

            // Player chooses action
            while (true) {
                System.out.println("Choose your action:");
                System.out.println("1. Attack");
                System.out.println("2. Defend");
                if (consecutiveHits >= 3) System.out.println("3. Special Attack (Ready!)");
                System.out.print("Enter: ");
                try {
                    action = scanner.nextInt();
                    scanner.nextLine();
                    if (action == 1 || action == 2 || action == 3) break;
                    else System.out.println("Invalid action! Enter 1, 2, or 3.");
                } catch (Exception e) {
                    System.out.println("Invalid input! Enter a number.");
                    scanner.nextLine();
                }
            }

            boolean defended = false;

            // ACTION HANDLER
            switch (action) {
                case 1 -> {
                    player.attack(monster);   // Normal attack
                    consecutiveHits++;
                }
                case 2 -> {
                    System.out.println(player.getName() + " braces for the attack!");
                    defended = true;         // Reduces incoming damage
                    consecutiveHits = 0;
                }
                case 3 -> {
                    if (consecutiveHits >= 3) {
                        boolean ok = player.specialAttack(monster); // Use class special attack
                        if (ok) consecutiveHits = 0;
                    } else {
                        System.out.println("You need 3 consecutive attacks first!");
                    }
                }
            }

            if (!monster.isAlive()) break;

            // Monster attacks
            if (defended) {
                int reduced = Math.max(0, monster.getAttackDamage() - player.getDefense() * 2);
                System.out.println("The monster attacks but the damage is reduced!");
                player.takeDamage(reduced);
            } else {
                monster.attack(player);
            }
        }

        return player.isAlive(); // True = victory
    }

    // --------------------- PLAY AGAIN ---------------------
    private static boolean playAgain(Scanner scanner) {
        int again = 0;
        while (true) {
            System.out.println("\n1. Play Again");
            System.out.println("2. Exit");
            System.out.print("Choose: ");
            try {
                again = scanner.nextInt();
                scanner.nextLine();
                if (again == 1 || again == 2) break;
                else System.out.println("Invalid choice! Type 1 or 2.");
            } catch (Exception e) {
                System.out.println("Invalid input! Enter a number.");
                scanner.nextLine();
            }
        }
        return again == 1; // True = replay
    }
}
