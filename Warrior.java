public class Warrior extends Player {
    private int energy;

    public Warrior(String name) {
        super(name, 75, 30, 15);
        this.energy = 100;
    }

    public int getEnergy() { return energy; }

    @Override
    public void attack(Character target) {
        if (energy >= 10) {
            System.out.println(this.getName() + " slashes " + target.getName());
            target.takeDamage(this.getAttackDamage());
            energy -= 10;
        } else {
            System.out.println(this.getName() + " has no energy! Uses weak attack.");
            int weakDamage = Math.max(0, this.getAttackDamage() - 10);
            target.takeDamage(weakDamage);
        }

        // Energy regeneration
        if (energy < 100) {
            energy += 10;
            if (energy > 100) energy = 100;
        }
    }

    @Override
    public boolean specialAttack(Character target) {
        if (energy >= 20) {
            System.out.println(this.getName() + " performs Whirlwind Attack!");
            target.takeDamage(this.getAttackDamage() + 15);
            energy -= 20;
            return true;
        } else {
            System.out.println(this.getName() + " doesn't have enough energy for a special attack!");
            return false;
        }
    }
}
