package com.serilum.manure.mixin;

import com.serilum.manure.events.ManureDropEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractHorse.class, priority = 1001)
public class AbstractHorseMixin {
	@Inject(method = "fedFood(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "RETURN"))
	public void fedFood(Player player, ItemStack itemStack, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() == InteractionResult.PASS) {
			return;
		}

		ManureDropEvent.onAnimalEat((AbstractHorse)(Object)this);
	}
}
