public class Mage extends Player {
    private int mana;

    public Mage(String name) {
        super(name, 50, 50, 10);
        this.mana = 100;
    }

    public int getMana() { return mana; }

    @Override
    public void attack(Character target) {
        if (mana >= 10) {
            System.out.println(this.getName() + " casts Fireball at " + target.getName());
            target.takeDamage(this.getAttackDamage());
            mana -= 10;
        } else {
            System.out.println(this.getName() + " has no mana! Uses weak attack.");
            int weakDamage = Math.max(0, this.getAttackDamage() - 30);
            target.takeDamage(weakDamage);
        }

        // Mana regeneration
        if (mana < 100) {
            mana += 20;
            if (mana > 100) mana = 100;
        }
    }

    @Override
    public boolean specialAttack(Character target) {
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
}
