public abstract class Player extends Character {
    protected int level;
    protected int maxHealth;

    public Player(String name, int health, int attackDamage, int defense) {
        super(name, health, attackDamage, defense);
        this.maxHealth = health;  // Save original HP to scale
        this.level = 1;
    }

    public int getLevel() { return level; }
    public int getMaxHealth() { return maxHealth; }

    public void restoreFullHP() {
        this.health = maxHealth; // Heal to full
    }

    // Leveling system for all players
    public void levelUp() {
        level++;  // Increase level

        // Stat increases
        maxHealth += 20;
        health = maxHealth; // Restore full HP
        attackDamage += 5;
        defense += 2;

        // Increase class-specific stats
        if (this instanceof Mage) {
            ((Mage)this).levelUpMana();
        } else if (this instanceof Warrior) {
            ((Warrior)this).levelUpEnergy();
        }

        // Level-up message
        System.out.println("\n=== LEVEL UP! ===");
        System.out.println("Level: " + level);
        System.out.println("Max HP increased to " + maxHealth);

        if (this instanceof Mage) {
            System.out.println("Max Mana increased to " + ((Mage)this).getMaxMana());
        } else if (this instanceof Warrior) {
            System.out.println("Max Energy increased to " + ((Warrior)this).getMaxEnergy());
        }

        System.out.println("Attack +5, Defense +2");
        System.out.println("HP and class resource fully restored!");
        System.out.println("=================\n");
    }
}
