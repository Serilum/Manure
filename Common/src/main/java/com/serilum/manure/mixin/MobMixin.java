package com.serilum.manure.mixin;

import com.serilum.manure.events.ManureDropEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class, priority = 1001)
public class MobMixin {
	@Inject(method = "usePlayerItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "HEAD"))
	public void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack, CallbackInfo ci) {
		if ((Object)this instanceof Animal animal && animal.isFood(itemStack)) {
			ManureDropEvent.onAnimalEat(animal);
		}
	}

	@Inject(method = "ate()V", at = @At(value = "HEAD"))
	public void ate(CallbackInfo ci) {
		if ((Object)this instanceof Animal animal) {
			ManureDropEvent.onAnimalEat(animal);
		}
	}
}
