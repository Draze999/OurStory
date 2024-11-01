package ourstory.events;

import java.lang.annotation.Target;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import ourstory.utils.EnchantItem;
import org.bukkit.util.Vector;


public class onPlayerInteract implements Listener {
	/*
	 * Removes player that breaks farmland if jumped on top
	 */
	@EventHandler
	public void cancelPlayerJumpOnFarmland(PlayerInteractEvent e) {
		if (e == null)
			return;

		// Disable jumping on farmlands
		if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType().equals(Material.FARMLAND))
			e.setCancelled(true);
	}

	@EventHandler
	public void ApplyEffectWithSpyglass(PlayerInteractEvent event) {
		if (event == null)
			return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Player player = event.getPlayer();
		if (!(player.getInventory().getItemInMainHand().getType().equals(Material.SPYGLASS) || player.getInventory().getItemInOffHand().getType().equals(Material.SPYGLASS)))
			return;
		ItemStack item = player.getInventory().getItemInMainHand().getType().equals(Material.SPYGLASS) ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
		int VulneSeekLevel = EnchantItem.getEnchantAmount(item, "vulnerability_seeker");

		if (VulneSeekLevel > 0) {
			Vector direction = player.getEyeLocation().getDirection();
			RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), direction, 50, (entity) -> entity instanceof Entity && entity != player);
			if (result != null && result.getHitEntity() instanceof LivingEntity) {
				((LivingEntity) result.getHitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 1, false, false, true));
			}
		}
	}
}
