package ourstory.events;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class onDepthStrider implements Listener {

	private static final float DEFAULT_WALK_SPEED = 0.2f;
	private static final float DEFAULT_WALK_DIST = 4.317f;
	private static final float DEFAULT_WATER_DIST = 1.957f;
	private static final float DEFAULT_WATER_SPEED = DEFAULT_WATER_DIST * DEFAULT_WALK_SPEED / DEFAULT_WALK_DIST;
	private static final float DEFAULT_LAVA_DIST = 0.784f;
	private static final float DEFAULT_LAVA_SPEED = DEFAULT_LAVA_DIST * DEFAULT_WALK_SPEED / DEFAULT_WALK_DIST;

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ItemStack[] armors = player.getInventory().getArmorContents();
		System.out.println(player.getWalkSpeed());
		int level = 0;
		for (var armorcontent : armors) {
			if (armorcontent != null && armorcontent.containsEnchantment(Enchantment.DEPTH_STRIDER)) {
				level = Math.max(armorcontent.getEnchantmentLevel(Enchantment.DEPTH_STRIDER), level);
			}
		}
		if (player.isInWater()) {
			if (level >= 9) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 0));
			}
		}
		// if (player.isSwimming() || player.isFlying())
		// return;
		else if (player.getLocation().getBlock().getType() == Material.LAVA) {
			// Niveaux 4 à 8 : applique la vitesse de DS8 dans la lave
			if (level >= 4) {
				double newspeed = Math.min((level - 3) * 0.2, 1);
				player.setWalkSpeed((float) (newspeed * DEFAULT_WALK_SPEED + (1 - newspeed) * DEFAULT_LAVA_SPEED));
				player.setFlySpeed((float) (newspeed * DEFAULT_WALK_SPEED + (1 - newspeed) * DEFAULT_LAVA_SPEED));
				// player.setVelocity(player.getVelocity().multiply(newspeed * DEFAULT_WALK_SPEED + (1 - newspeed) *
				// DEFAULT_LAVA_SPEED).setY(player.getVelocity().getY()));
			}
			if (level >= 10) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 0));
			}
		} else {
			// Réinitialise la vitesse lorsque le joueur n'est ni dans l'eau ni dans la lave
			player.setWalkSpeed(DEFAULT_WALK_SPEED);
		}
	}
}
