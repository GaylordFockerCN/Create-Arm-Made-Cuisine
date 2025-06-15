package com.p1nero.create_cuisine;

import com.mojang.logging.LogUtils;
import com.p1nero.create_cuisine.interaction_points.CDInteractionPoints;
import com.p1nero.create_cuisine.ponder.scene.CuisineSkilletScene;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(CreateCuisineMod.MODID)
public class CreateCuisineMod {

    public static final String MODID = "create_cuisine";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateCuisineMod(IEventBus modBus, ModContainer modContainer) {
        CDInteractionPoints.register(modBus);
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    private static class ClientEvents {

        @SubscribeEvent
        public static void registerClient(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new PonderPlugin() {
                @Override
                public @NotNull String getModId() {
                    return CreateCuisineMod.MODID;
                }

                @Override
                public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
                    PonderSceneRegistrationHelper<RegistryEntry<?, ?>> ENTRY_HELPER = helper.withKeyFunction(RegistryEntry::getId);
                    ENTRY_HELPER.forComponents(CDItems.SKILLET).addStoryBoard("cuisine_skillet_ponder", CuisineSkilletScene::addFood, AllCreatePonderTags.ARM_TARGETS);
                }

                @Override
                public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
                    PonderTagRegistrationHelper<RegistryEntry<?, ?>> ENTRY_HELPER = helper.withKeyFunction(RegistryEntry::getId);
                    ENTRY_HELPER.addToTag(AllCreatePonderTags.ARM_TARGETS)
                            .add(CDItems.SKILLET);
                }
            });

        }
    }

}
