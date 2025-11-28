public class Mage extends Player {
    private int mana;
    private int maxMana;

    public Mage(String name) {
        super(name, 100, 50, 10); // Base stats
        this.mana = 100;
        this.maxMana = 100;
    }

    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }

    @Override
    public void attack(Character target) {
        // Fireball if enough mana
        if (mana >= 10) {
            System.out.println(this.getName() + " casts Fireball at " + target.getName());
            target.takeDamage(this.getAttackDamage());
            mana -= 10;
        } else {
            // Weak attack when no mana
            System.out.println(this.getName() + " has no mana! Uses weak attack.");
            int weakDamage = Math.max(0, this.getAttackDamage() - 30);
            target.takeDamage(weakDamage);
        }

        // Mana regen after each attack
        if (mana < maxMana) {
            mana += 20;
            if (mana > maxMana) mana = maxMana;
        }
    }

    @Override
    public boolean specialAttack(Character target) {
        // Lightning Strike requires 25 mana
        if (mana >= 25) {
            System.out.println(this.getName() + " casts Lightning Strike!");
            target.takeDamage(this.getAttackDamage() + 25);
            mana -= 25;
            return true;
        } else {
            System.out.println(this.getName() + " doesn't have enough mana for special attack!");
            return false;
        }
    }

    // Called during level up
    public void levelUpMana() {
        maxMana += 20;   // Increase maximum mana
        mana = maxMana;  // Restore to full
    }
}
