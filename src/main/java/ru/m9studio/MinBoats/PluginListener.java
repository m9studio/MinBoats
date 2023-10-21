package ru.m9studio.MinBoats;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PluginListener implements Listener
{
	static Map<Material, Boat.Type> TypeBoat = new HashMap<>();
	static
	{
		TypeBoat.put(Material.ACACIA_BOAT, Boat.Type.ACACIA);
		TypeBoat.put(Material.ACACIA_CHEST_BOAT, Boat.Type.ACACIA);

		TypeBoat.put(Material.JUNGLE_BOAT, Boat.Type.JUNGLE);
		TypeBoat.put(Material.JUNGLE_CHEST_BOAT, Boat.Type.JUNGLE);

		TypeBoat.put(Material.DARK_OAK_BOAT, Boat.Type.DARK_OAK);
		TypeBoat.put(Material.DARK_OAK_CHEST_BOAT, Boat.Type.DARK_OAK);

		TypeBoat.put(Material.OAK_BOAT, Boat.Type.OAK);
		TypeBoat.put(Material.OAK_CHEST_BOAT, Boat.Type.OAK);

		TypeBoat.put(Material.BIRCH_BOAT, Boat.Type.BIRCH);
		TypeBoat.put(Material.BIRCH_CHEST_BOAT, Boat.Type.BAMBOO);

		TypeBoat.put(Material.SPRUCE_BOAT, Boat.Type.SPRUCE);
		TypeBoat.put(Material.SPRUCE_CHEST_BOAT, Boat.Type.SPRUCE);

		TypeBoat.put(Material.MANGROVE_BOAT, Boat.Type.MANGROVE);
		TypeBoat.put(Material.MANGROVE_CHEST_BOAT, Boat.Type.MANGROVE);

		TypeBoat.put(Material.CHERRY_BOAT, Boat.Type.CHERRY);
		TypeBoat.put(Material.CHERRY_CHEST_BOAT, Boat.Type.CHERRY);

		TypeBoat.put(Material.BAMBOO_RAFT, Boat.Type.BAMBOO);
		TypeBoat.put(Material.BAMBOO_CHEST_RAFT, Boat.Type.BAMBOO);
	}
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEvent e)
	{
		ItemStack I = e.getPlayer().getInventory().getItemInMainHand();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && isBoat(I))
		{
			Player P = e.getPlayer();
			if(P.getVehicle() == null)
			{
				Boat B;
				Location L = e.getClickedBlock().getLocation().clone();
				boolean isWater = L.getBlock().getType() == Material.WATER;
				boolean isSpawn = false;
				for(int i = 0; i <= 2; i++) //2 -кол-во блоков между блоком на который тыкнули, и воздухом над поверхностью воды, где должна быть лодка
				{
					L.add(0, 1, 0);
					if(L.getBlock().getType() == Material.WATER)
					{
						isWater = true;
					}
					else if (L.getBlock().getType() == Material.AIR)
					{
						isSpawn = true;
						break;
					}
					else
					{
						isWater = false;
						break;
					}
				}
				if(isSpawn && isWater)
				{
					L.add(0, -0.25, 0);
					L.setYaw(P.getLocation().getYaw());
					if(isBoatChest(I))
					{
						B = L.getWorld().spawn(L, ChestBoat.class);
						((ChestBoat)B).getInventory().setContents(P.getEnderChest().getContents());
						P.getEnderChest().setContents(new ItemStack[0]);
					}
					else
					{
						B = L.getWorld().spawn(L, Boat.class);
					}
					B.setBoatType(getType(I));
					B.addPassenger(P);
				}
			}
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onVehicleDamageEvent(VehicleDamageEvent e)
	{
		EntityType E = e.getVehicle().getType();
		if(E == EntityType.BOAT || E == EntityType.CHEST_BOAT)
		{
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onVehicleExitEvent(VehicleExitEvent e)
	{
		EntityType E = e.getVehicle().getType();
		if(E == EntityType.BOAT)
		{
			Boat B = (Boat)e.getVehicle();
			if(B.getPassengers().get(0) == e.getExited())
			{
				B.remove();
			}
		}
		else if(E == EntityType.CHEST_BOAT)
		{
			ChestBoat B = (ChestBoat)e.getVehicle();
			Player P = (Player) e.getExited();
			Inventory I = B.getInventory();
			P.getEnderChest().setContents(I.getContents());
			I.setContents(new ItemStack[0]);
			B.remove();
		}
	}

	Boat.Type getType(ItemStack I)
	{
		return getType(I.getType());
	}
	boolean isBoatWithoutChest(ItemStack I)
	{
		return isBoatWithoutChest(I.getType());
	}
	boolean isBoatChest(ItemStack I)
	{
		return isBoatChest(I.getType());
	}
	boolean isBoat(ItemStack I)
	{
		return isBoat(I.getType());
	}

	Boat.Type getType(Material M)
	{
		if(TypeBoat.containsKey(M))
		{
			return TypeBoat.get(M);
		}
		return Boat.Type.OAK;
	}
	boolean isBoatWithoutChest(Material M)
	{
		if(TypeBoat.containsKey(M))
		{
			return !isBoatChest(M);
		}
		return false;
	}
	boolean isBoatChest(Material M)
	{
		if(isBoat(M))
		{
			return M.toString().split("CHEST").length == 2;
		}
		return false;
	}
	boolean isBoat(Material M)
	{
		return TypeBoat.containsKey(M);
	}
}
