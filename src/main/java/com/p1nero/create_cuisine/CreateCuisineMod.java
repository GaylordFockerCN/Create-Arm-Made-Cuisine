package com.p1nero.create_cuisine;

import com.mojang.logging.LogUtils;
import com.p1nero.create_cuisine.ponder.CCPonderScenes;
import com.p1nero.create_cuisine.ponder.scene.CuisineSkilletScene;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import dev.xkmc.cuisinedelight.content.item.CuisineSkilletItem;
import net.createmod.catnip.platform.ForgeRegisteredObjectsHelper;
import net.createmod.ponder.api.registration.MultiTagBuilder;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateCuisineMod.MODID)
public class CreateCuisineMod {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "create_cuisine";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateCuisineMod() {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    private static class ClientEvents{

        @SubscribeEvent
        public static void registerClient(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new PonderPlugin() {
                @Override
                public @NotNull String getModId() {
                    return CreateCuisineMod.MODID;
                }

                @Override
                public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
                    ForgeRegisteredObjectsHelper forgeRegisteredObjectsHelper = new ForgeRegisteredObjectsHelper();
                    PonderSceneRegistrationHelper<Item> entryHelper = helper.withKeyFunction(forgeRegisteredObjectsHelper::getKeyOrThrow);
                    ForgeRegistries.ITEMS
                            .getValues()
                            .stream()
                            .filter(item -> item instanceof CuisineSkilletItem)
                            .forEach(item -> entryHelper.forComponents(item).addStoryBoard("cuisine_skillet_ponder", CuisineSkilletScene::addFood, AllCreatePonderTags.ARM_TARGETS));
                }

                @Override
                public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
                    ForgeRegisteredObjectsHelper forgeRegisteredObjectsHelper = new ForgeRegisteredObjectsHelper();
                    MultiTagBuilder.Tag<Item> builder = helper.withKeyFunction((Item s) -> forgeRegisteredObjectsHelper.getKeyOrThrow(s)).addToTag(AllCreatePonderTags.ARM_TARGETS);
                    ForgeRegistries.ITEMS
                            .getValues()
                            .stream()
                            .filter(item -> item instanceof CuisineSkilletItem)
                            .forEach(builder::add);
                }
            });

        }
    }

}
