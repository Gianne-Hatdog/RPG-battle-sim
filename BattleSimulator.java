import java.util.Scanner;

public class BattleSimulator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean playing = true;

        while (playing) {

            System.out.println("You slowly open your eyes...");
            System.out.println("Your head hurts. You don't remember anything.");
            System.out.println("You seem to be inside a dim dungeon chamber.");
            System.out.println("A voice echoes in your mind: \"Defeat the boss and you will be free...\"");

            //NAME SELECTION
            System.out.print("\nYou have forgotten your name...\nEnter a name (leave blank for 'Player'): ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                name = "Player";
            }

            // CLASS CHOICE
            System.out.println("\nIn front of you lies a STAFF and a SWORD.");
            System.out.println("Which will you pick?");
            System.out.println("1. Staff (Mage)");
            System.out.println("2. Sword (Warrior)");

            int choice = 0;
            while (true) {
                System.out.print("Enter choice (1 or 2): ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // cleanup
                    if (choice == 1 || choice == 2) break;
                    else System.out.println("Invalid input! Choose 1 or 2.");
                } catch (Exception e) {
                    System.out.println("Invalid input! Enter a number.");
                    scanner.nextLine(); // clear invalid input
                }
            }

            Player player;
            if (choice == 1) {
                player = new Mage(name);
                System.out.println("\nYou have become a Mage!");
            } else {
                player = new Warrior(name);
                System.out.println("\nYou have become a Warrior!");
            }

            int level = 1;
            boolean northDone = false, eastDone = false, westDone = false;

            // ROOM EXPLORATION
            while (player.isAlive()) {

                System.out.println("\n===== Dungeon Paths =====");
                System.out.println("North - " + (northDone ? "Cleared" : "Enemy inside"));
                System.out.println("East  - " + (eastDone ? "Cleared" : "Enemy inside"));
                System.out.println("West  - " + (westDone ? "Cleared" : "Enemy inside"));
                System.out.println("South - Boss Room " + (level >= 3 ? "(Unlocked)" : "(Locked until level 3)"));

                String dir = "";
                while (true) {
                    System.out.print("\nWhere do you go? (north/east/west/south): ");
                    dir = scanner.nextLine().toLowerCase();
                    if (dir.equals("north") || dir.equals("east") || dir.equals("west") || dir.equals("south")) break;
                    else System.out.println("Invalid direction! Type north, east, west, or south.");
                }

                if (dir.equals("south")) {
                    if (level < 3) {
                        System.out.println("A powerful force blocks your way. You must reach level 3 first.");
                        continue;
                    }
                    break; // go to boss
                }

                // NORMAL ROOMS
                if (dir.equals("north") && !northDone) {
                    northDone = fightEnemy(player, "Skeleton", scanner);
                    if (northDone) levelUp(player, ++level);
                }
                else if (dir.equals("east") && !eastDone) {
                    eastDone = fightEnemy(player, "Goblin", scanner);
                    if (eastDone) levelUp(player, ++level);
                }
                else if (dir.equals("west") && !westDone) {
                    westDone = fightEnemy(player, "Zombie", scanner);
                    if (westDone) levelUp(player, ++level);
                }
                else {
                    System.out.println("You can't go there or the room is already cleared.");
                }
            }

            if (!player.isAlive()) {
                System.out.println("\nYou died in the dungeon...");
            }

            // BOSS FIGHT
            if (player.isAlive()) {
                System.out.println("\nYou enter the boss chamber...");
                Monster boss = new Monster("Dungeon Lord", 250, 35, 10);

                boolean won = runBattle(player, boss, scanner);

                if (won) {
                    System.out.println("\n===== VICTORY! =====");
                    System.out.println("You have defeated the Dungeon Lord and escaped!");
                } else {
                    System.out.println("\n===== GAME OVER =====");
                }
            }

            // PLAY AGAIN?
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

            playing = (again == 1);
        }

        scanner.close();
    }


    // Normal Enemy
    private static boolean fightEnemy(Player player, String enemyName, Scanner scanner) {
        Monster m = new Monster(enemyName, 80, 15, 5);
        System.out.println("\nA " + enemyName + " appears!");

        return runBattle(player, m, scanner);
    }

    //Battle System
    private static boolean runBattle(Player player, Monster monster, Scanner scanner) {

        int consecutiveHits = 0;

        while (player.isAlive() && monster.isAlive()) {

            System.out.println("\n------------------------------");
            System.out.println(player.getName() + " HP: " + player.getHealth());
            if (player instanceof Mage) System.out.println("Mana: " + ((Mage) player).getMana());
            if (player instanceof Warrior) System.out.println("Energy: " + ((Warrior) player).getEnergy());
            System.out.println(monster.getName() + " HP: " + monster.getHealth());
            System.out.println("------------------------------");

            int action = 0;
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

            switch (action) {
                case 1:
                    player.attack(monster);
                    consecutiveHits++;
                    break;
                case 2:
                    System.out.println(player.getName() + " braces for the attack!");
                    defended = true;
                    consecutiveHits = 0;
                    break;
                case 3:
                    if (consecutiveHits >= 3) {
                        boolean ok = player.specialAttack(monster);
                        if (ok) consecutiveHits = 0;
                    } else {
                        System.out.println("You need 3 consecutive attacks first!");
                    }
                    break;
            }

            if (!monster.isAlive()) break;

            if (defended) {
                int reduced = Math.max(0, monster.getAttackDamage() - player.getDefense() * 2);
                System.out.println("The monster attacks but the damage is reduced!");
                player.takeDamage(reduced);
            } else {
                monster.attack(player);
            }
        }

        return player.isAlive();
    }


    // Level Up System
    private static void levelUp(Player player, int level) {
        System.out.println("\n*** LEVEL UP! You are now leve
        if (player instanceof Warrior) {
            player.setHealth(player.getHealth() + 10);
            player.setAttackDamage(player.getAttackDamage() + 3);
            player.setDefense(player.getDefense() + 2);
        } else if (player instanceof Mage) {
            player.setHealth(player.getHealth() + 5);
            player.setAttackDamage(player.getAttackDamage() + 5);
            player.setDefense(player.getDefense() + 1);
        }

        System.out.println("Stats increased!");
    }
}
