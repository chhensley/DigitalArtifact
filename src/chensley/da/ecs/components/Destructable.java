/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

/**
 * Defines a destructable component
 */
public class Destructable {
	private final int hp;
	private final int impactArmor;
	private final String onDestroy;
	
	//Damage received by entity
	private int dmg = 0;
	
	/**
	 * Constructor
	 * @param hp
	 * 		Total damage an entity can take
	 * @param impactArmor
	 * 		Resistance to melee attacks
	 * @param onDestroy
	 * 		Message sent when the entity is destroyed
	 */
	public Destructable(int hp, int impactArmor, String onDestroy) {
		this.hp = hp;
		this.impactArmor = impactArmor;
		this.onDestroy = onDestroy;
	}
	
	public Destructable(Destructable destructable) {
		this.hp = destructable.hp;
		this.impactArmor = destructable.impactArmor;
		this.onDestroy = destructable.onDestroy;
	}
	
	public int hitPoints() { return hp; }
	public int currentHitPoints() { return hp - dmg; }
	public int impactArmor() { return impactArmor; }
	public String onDestroy() { return onDestroy; }
	public int damage() { return dmg; }
	public int addDamage(int dmg) { return this.dmg += dmg; }
	public void setDamage(int dmg) { this.dmg = dmg; }
}
