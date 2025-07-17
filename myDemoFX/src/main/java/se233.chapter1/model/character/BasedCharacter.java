package se233.chapter1.model.character;

import se233.chapter1.model.DamageType;
import se233.chapter1.model.item.Weapon;
import se233.chapter1.model.item.Armor;

public class BasedCharacter {
    protected String name, imgpath;
    protected DamageType type;
    protected int fullHp;
    protected Integer basedPow;
    protected Integer basedDef;
    protected Integer basedRes;
    protected Integer hp, power, defense, resistance;
    protected Weapon weapon;
    protected Armor armor;

    public String getName(){ return name; }
    public String getImagepath() { return imgpath; }
    public Integer getHp() { return hp; }
    public Integer getFullHp() { return fullHp; }
    public Integer getPower() { return power; }
    public Integer getDefense() { return defense; }
    public Integer getResistance() { return resistance; }

    public Integer getBasedPow() { return basedPow; }
    public Integer getBasedDef() { return basedDef; }
    public Integer getBasedRes() { return basedRes; }

    public void setPower(Integer power) { this.power = power; }
    public void setDefense(Integer defense) { this.defense = defense; }
    public void setResistance(Integer resistance) { this.resistance = resistance; }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
        if (weapon != null) {
            this.power = this.basedPow + weapon.getPower();
        } else {
            this.power = this.basedPow;
        }
    }

    public void equipArmor(Armor armor) {
        this.armor = armor;
        if (armor != null) {
            this.defense = this.basedDef + armor.getDefense();
            this.resistance = this.basedRes + armor.getResistance();
        } else {
            this.defense = this.basedDef;
            this.resistance = this.basedRes;
        }
    }

    @Override
    public String toString() { return name; }

    public DamageType getType() {
        return type;
    }
}