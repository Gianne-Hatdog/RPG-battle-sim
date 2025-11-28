public abstract class Character {
    // Store the stats of the character
    protected String name;
    protected int health;
    protected int attackDamage;
    protected int defense;

    public Character(String name, int health, int attackDamage, int defense) {
        this.name = name;
        this.health = Math.max(0, health);          // Prevent negative values
        this.attackDamage = Math.max(0, attackDamage);
        this.defense = Math.max(0, defense);
    }

    // Basic getters
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getAttackDamage() { return attackDamage; }
    public int getDefense() { return defense; }

    // Setters used inside subclasses
    public void setName(String name) { this.name = name; }
    protected void setHealth(int health) { this.health = Math.max(0, health); }
    protected void setAttackDamage(int attackDamage) { this.attackDamage = Math.max(0, attackDamage); }
    protected void setDefense(int defense) { this.defense = Math.max(0, defense); }

    public boolean isAlive() { return health > 0; }   // Checks if character is still alive

    public void takeDamage(int rawDamage) {
        // Calculate final damage after defense
        int net = rawDamage - this.defense;
        if (net < 0) net = 0;

        this.health -= net;    // Deduct HP
        if (this.health < 0) this.health = 0;

        System.out.println(this.name + " takes " + net +
                " damage. HP: " + this.health);
    }

    // All characters must implement these methods
    public abstract void attack(Character target);
    public abstract boolean specialAttack(Character target);
}
