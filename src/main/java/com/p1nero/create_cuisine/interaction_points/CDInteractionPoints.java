package com.p1nero.create_cuisine.interaction_points;

import com.p1nero.create_cuisine.CreateCuisineMod;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.createmod.catnip.platform.ForgeRegisteredObjectsHelper;
import net.createmod.ponder.api.registration.MultiTagBuilder;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = CreateCuisineMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CDInteractionPoints {
    @SubscribeEvent
    public static void register(FMLCommonSetupEvent event) {
        Registry.register(CreateBuiltInRegistries.ARM_INTERACTION_POINT_TYPE, ResourceLocation.fromNamespaceAndPath(CreateCuisineMod.MODID, "cuisine_skillet"), new CuisineSkilletArmInteractionPoint.Type());
    }

}
