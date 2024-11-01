package ourstory.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.FoodComponent.FoodEffect;
import org.bukkit.plugin.Plugin;
import ourstory.utils.EnchantItem;

public class onPlayerInteract implements Listener {

	private static final int EatDuration = 32;

	private Plugin p = Bukkit.getPluginManager().getPlugin("OurStory");

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
	public void fastEaterEnchantment(PlayerInteractEvent event) {
		if (event == null)
			return;
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		Player player = event.getPlayer();
		if (!(player.getInventory().getItemInMainHand().getType().isEdible() || player.getInventory().getItemInOffHand().getType().isEdible()))
			return;
		ItemStack item = player.getInventory().getItemInMainHand().getType().isEdible() ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
		int FastEaterLevel = EnchantItem.getEnchantAmount(player.getInventory().getHelmet(), "fast_eater");

		if (FastEaterLevel > 0 && item.getType().isEdible()) {
			double duration_reduction_factor = (1.0 - (0.1 * FastEaterLevel));
			long new_duration = (long) (EatDuration * duration_reduction_factor);
			FoodComponent Food = item.getItemMeta().getFood();
			int Nutri = Food.getNutrition();
			float Satu = Food.getSaturation();

			Bukkit.getScheduler().runTaskLater(p, () -> {
				if (Food != null) {
					event.setCancelled(true);
					player.setFoodLevel(Math.min(player.getFoodLevel() + Nutri, 20));
					player.setSaturation((float) Math.min(player.getSaturation() + Satu, 20.0));
					for (FoodEffect fe : Food.getEffects()) {
						player.addPotionEffect(fe.getEffect());
					}
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
				}
			}, new_duration);
		}
	}
}
