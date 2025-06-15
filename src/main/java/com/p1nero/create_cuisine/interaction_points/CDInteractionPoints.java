package com.p1nero.create_cuisine.interaction_points;

import com.p1nero.create_cuisine.CreateCuisineMod;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.api.registry.CreateRegistries;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = CreateCuisineMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CDInteractionPoints {

    private static final DeferredRegister<ArmInteractionPointType> REGISTER = DeferredRegister
            .create(CreateRegistries.ARM_INTERACTION_POINT_TYPE, CreateCuisineMod.MODID);

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        register(SKILLET, CuisineSkilletArmInteractionPoint.Type::new);
    }

    //spotless:off
    public static final DeferredHolder<ArmInteractionPointType, ArmInteractionPointType> SKILLET = holder("cuisine_skillet");
    //spotless:on

    public static void register(IEventBus modBus) {
        REGISTER.register(modBus);
    }

    public static void register(DeferredHolder<ArmInteractionPointType, ArmInteractionPointType> holder, Supplier<? extends ArmInteractionPointType> supplier) {
        REGISTER.register(holder.getId().getPath(), supplier);
    }

    private static DeferredHolder<ArmInteractionPointType, ArmInteractionPointType> holder(String name) {
        return DeferredHolder.create(CreateRegistries.ARM_INTERACTION_POINT_TYPE, ResourceLocation.fromNamespaceAndPath(CreateCuisineMod.MODID, name));
    }
}
