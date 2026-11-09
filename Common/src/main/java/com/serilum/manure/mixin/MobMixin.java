package com.serilum.manure.mixin;

import com.serilum.manure.events.ManureDropEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class, priority = 1001)
public class MobMixin {
	@Inject(method = "ate()V", at = @At(value = "HEAD"))
	public void ate(CallbackInfo ci) {
		if ((Object)this instanceof Animal animal) {
			ManureDropEvent.onAnimalEat(animal);
		}
	}
}
