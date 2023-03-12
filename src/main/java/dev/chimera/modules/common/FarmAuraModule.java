package dev.chimera.modules.common;

import dev.chimera.ChimeraClient;
import dev.chimera.amalthea.events.misc.TickEvent;
import dev.chimera.modules.Module;
import dev.chimera.modules.ModuleCategory;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class FarmAuraModule extends Module {
    private static MinecraftClient client;
    private static final Item[] usableItems = {
            Items.WHEAT_SEEDS,
            Items.BEETROOT_SEEDS,
            Items.POTATO,
            Items.CARROT
    };

    private static final int RANGE = 5;

    private static List<BlockPos> availableFarmland;

    public FarmAuraModule() {
        super(ModuleCategory.PLAYER, "Farm Aura", GLFW.GLFW_KEY_0);
    }

    @Override
    public void init() {
        client = MinecraftClient.getInstance();
        ChimeraClient.LOGGER.info("Initialized module '" + getModuleName() + "'");
    }

    @Override
    public void onEnable() {
        ClientWorld world = client.world;
        if (client.player == null || world == null) return;

        availableFarmland = getFarmlandBlockPosAroundPlayer(world);

        if(!availableFarmland.isEmpty()) {
            ChimeraClient.LOGGER.info(availableFarmland.toString());
        }
    }

    private static ArrayList<BlockPos> getFarmlandBlockPosAroundPlayer(ClientWorld world) {
        BlockPos p1 = client.player.getBlockPos().add(RANGE, RANGE, RANGE);
        BlockPos p2 = client.player.getBlockPos().add(-RANGE, -RANGE, -RANGE);

        BlockPos max = new BlockPos(
                Math.max(p1.getX(), p2.getX()),
                Math.max(p1.getY(), p2.getY()),
                Math.max(p1.getZ(), p2.getZ())
        );

        BlockPos min = new BlockPos(
                Math.min(p1.getX(), p2.getX()),
                Math.min(p1.getY(), p2.getY()),
                Math.min(p1.getZ(), p2.getZ())
        );

        ArrayList<BlockPos> blocks = new ArrayList<>();

        IntStream.range(min.getX(), max.getX()).forEachOrdered(x-> {
            IntStream.range(min.getY(), max.getY()).forEachOrdered(y-> {
                IntStream.range(min.getZ(), max.getZ()).forEachOrdered(z->{
                    BlockPos targetPos = new BlockPos(x, y, z);

                    if(world.getBlockState(targetPos).getBlock().equals(Blocks.FARMLAND)) {
                        blocks.add(targetPos);
                    }
                });
            });
        });

        return blocks;
    }

    @Override
    public void onDisable() {}

    @Override
    public void onTickStart(TickEvent.Start event) {
        ClientPlayerEntity player = client.player;
        if (!getModuleEnabled() || player == null
                || client.world == null
                || player.getItemCooldownManager().isCoolingDown(player.getMainHandStack().getItem())
                || client.interactionManager == null) return;

        if (!isItemUsable() || availableFarmland.isEmpty()) {
            setModuleState(false);
            return;
        }
        BlockPos farmland = availableFarmland.remove(0);

        replantForBlockPos(player, farmland);
    }

    private static void replantForBlockPos(ClientPlayerEntity player, BlockPos farmland) {
        client.interactionManager.interactBlock(
                player,
                Hand.MAIN_HAND,
                new BlockHitResult(
                        new Vec3d(farmland.getX(), farmland.getY(), farmland.getZ()),
                        Direction.UP,
                        farmland,
                        false
                )
        );
    }

    @Override
    public void onTickEnd(TickEvent.End event) {
    }

    private static boolean isItemUsable() {
        ItemStack mainHandStack = client.player.getMainHandStack();
        return Arrays.stream(usableItems).anyMatch(item ->
                item.equals(mainHandStack.getItem())
        );
    }
}
