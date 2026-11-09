package com.serilum.manure.mixin;

import com.serilum.manure.events.ManureDropEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Animal.class, priority = 1001)
public class AnimalMixin {
	@Inject(method = "usePlayerItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "HEAD"))
	public void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack, CallbackInfo ci) {
		Animal animal = (Animal)(Object)this;
		if (animal.isFood(itemStack)) {
			ManureDropEvent.onAnimalEat(animal);
		}
	}
}
