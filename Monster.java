public class Monster extends Character {

    public Monster(String name, int health, int attackDamage, int defense) {
        super(name, health, attackDamage, defense);  // Basic monster setup
    }

    @Override
    public void attack(Character target) {
        System.out.println(this.getName() + " attacks " + target.getName());
        target.takeDamage(this.getAttackDamage());   // Normal attack
    }

    @Override
    public boolean specialAttack(Character target) {
        System.out.println(this.getName() + " uses Ferocious Bite!");
        target.takeDamage(this.getAttackDamage() + 10);  // Stronger bite
        return true; // Monster always succeeds
    }
}
