package konhaiii.beds_arent_tnt.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BedBlock.class)
public class ExampleMixin {
	@Inject(
			method = "useWithoutItem",
			at = @At("HEAD"),
			cancellable = true
	)
	private void onUse(BlockState state, Level level, BlockPos pos,
	                   Player player, BlockHitResult hitResult,
	                   CallbackInfoReturnable<InteractionResult> cir) {

		if (!level.isClientSide()) {
			BedRule bedRule = level.environmentAttributes()
					.getValue(EnvironmentAttributes.BED_RULE, pos);

			if (bedRule.explodes()) {
				player.displayClientMessage(
						Component.translatable("block.minecraft.bed.no_sleep_dimension"),
						true
				);

				cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
				cir.cancel();
			}
		}
	}
}