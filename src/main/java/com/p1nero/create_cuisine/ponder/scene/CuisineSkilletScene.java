package com.p1nero.create_cuisine.ponder.scene;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import dev.xkmc.cuisinedelight.init.registrate.PlateFood;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Objects;

public class CuisineSkilletScene {
    public static void addFood(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("add_food", "Use Mechanical Arm to add food");

        ItemStack armItem = AllBlocks.MECHANICAL_ARM.asStack();
        BlockPos armPos = util.grid().at(2, 1, 3);
        Selection armSel = util.select().position(armPos);
        BlockPos inputDepot = util.grid().at(0, 1, 1);
        Selection input = util.select().position(inputDepot);
        BlockPos outputDepot = util.grid().at(0, 1, 3);
        BlockPos skilletPos = util.grid().at(4, 2, 1);
        Selection output = util.select().position(outputDepot);
        Vec3 skilletSurface = util.vector().blockSurface(skilletPos, Direction.NORTH);
        Vec3 inputDepotSurface = util.vector().blockSurface(inputDepot, Direction.NORTH);
        Vec3 outputDepotSurface = util.vector().blockSurface(outputDepot, Direction.NORTH);
        Vec3 armSurface = util.vector().blockSurface(armPos, Direction.WEST);

        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();

        scene.idle(20);

        scene.world().setKineticSpeed(armSel, 0);
        scene.world().showSection(armSel, Direction.DOWN);
        scene.world().showSection(util.select().fromTo(4, 1, 1, 4, 2, 1), Direction.DOWN);
        scene.world().showSection(input, Direction.DOWN);
        scene.idle(20);

        ItemStack cabbage = new ItemStack(ModItems.CABBAGE_LEAF.get());
        ItemStack mutton = new ItemStack(ModItems.MUTTON_CHOPS.get());
        scene.world().createItemOnBeltLike(inputDepot, Direction.NORTH, cabbage);
        scene.idle(20);
        scene.world().setKineticSpeed(armSel, -48);
        scene.idle(20);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
        scene.idle(24);
        scene.world().removeItemsFromBelt(inputDepot);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, cabbage, 0);
        scene.idle(24);
        scene.world().modifyBlockEntity(skilletPos, CuisineSkilletBlockEntity.class, (be -> be.cookingData.addItem(cabbage, be.getLevel() == null ? 1145 : be.getLevel().getGameTime())));
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Mechanical Arm can put foods into heated cuisine skillet.")
                .pointAt(inputDepotSurface)
                .placeNearTarget();
        scene.idle(80);

        scene.world().hideSection(input, Direction.UP);
        scene.idle(20);
        scene.world().showSection(input, Direction.DOWN);
        scene.idle(20);

        ItemStack pack = new ItemStack(Objects.requireNonNull(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(Create.ID, "cardboard_package_12x12"))));
        scene.world().createItemOnBeltLike(inputDepot, Direction.NORTH, pack);
        scene.idle(20);
        scene.world().setKineticSpeed(armSel, -48);
        scene.idle(20);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
        scene.idle(24);
        scene.world().removeItemsFromBelt(inputDepot);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, pack, 0);
        scene.idle(24);
        scene.world().modifyBlockEntity(skilletPos, CuisineSkilletBlockEntity.class, (be -> be.cookingData.addItem(mutton, be.getLevel() == null ? 1145 : be.getLevel().getGameTime())));
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("If the input is packages, it will unwrapped it and try to put all foods inside into the cuisine skillet.")
                .pointAt(inputDepotSurface)
                .placeNearTarget();
        scene.idle(80);

        scene.world().hideSection(input, Direction.UP);
        scene.idle(20);
        scene.world().showSection(input, Direction.DOWN);
        scene.idle(20);

        ItemStack spatula = new ItemStack(CDItems.SPATULA.get());
        scene.world().createItemOnBeltLike(inputDepot, Direction.NORTH, spatula);
        scene.idle(20);
        scene.world().setKineticSpeed(armSel, -48);
        scene.idle(20);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
        scene.idle(24);
        scene.world().removeItemsFromBelt(inputDepot);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, spatula, 0);
        scene.idle(24);
        scene.world().modifyBlockEntity(skilletPos, CuisineSkilletBlockEntity.class, (be -> be.stir(be.getLevel() == null ? 1145 : be.getLevel().getGameTime(), 0)));
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_OUTPUTS, spatula, -1);
        scene.idle(24);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, spatula, 0);
        scene.world().modifyBlockEntity(skilletPos, CuisineSkilletBlockEntity.class, (be -> be.stir(be.getLevel() == null ? 1145 : be.getLevel().getGameTime(), 0)));
        scene.idle(24);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_OUTPUTS, spatula, -1);
        scene.idle(24);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, spatula, 0);
        scene.world().modifyBlockEntity(skilletPos, CuisineSkilletBlockEntity.class, (be -> be.stir(be.getLevel() == null ? 1145 : be.getLevel().getGameTime(), 0)));
        scene.idle(24);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_OUTPUTS, spatula, -1);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("If the input is spatula, it will stir the cuisine skillet until the cuisine skillet is empty.")
                .pointAt(armSurface)
                .placeNearTarget();
        scene.idle(80);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);

        scene.world().hideSection(input, Direction.UP);
        scene.idle(20);
        scene.world().showSection(input, Direction.DOWN);
        scene.world().showSection(output, Direction.DOWN);
        scene.idle(20);

        scene.overlay().showControls(inputDepotSurface, Pointing.RIGHT, 10)
                .rightClick()
                .withItem(armItem);
        AABB depotBounds = AllShapes.CASING_13PX.get(Direction.UP)
                .bounds();
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.INPUT, new Object(), depotBounds.move(0, 1, 1), 110);
        scene.idle(20);
        Object second = new Object();
        scene.overlay().showControls(skilletSurface, Pointing.RIGHT, 10)
                .rightClick()
                .withItem(armItem);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.INPUT, second, depotBounds.move(4, 2, 1), 20);
        scene.idle(20);
        scene.overlay().showControls(skilletSurface, Pointing.RIGHT, 10)
                .rightClick()
                .withItem(armItem);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, second, depotBounds.move(4, 2, 1), 50);
        scene.idle(20);
        scene.overlay().showControls(outputDepotSurface, Pointing.RIGHT, 10)
                .rightClick()
                .withItem(armItem);
        Object third = new Object();
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.INPUT, third, depotBounds.move(0, 1, 3), 20);
        scene.idle(20);
        scene.overlay().showControls(outputDepotSurface, Pointing.RIGHT, 10)
                .rightClick()
                .withItem(armItem);
        scene.overlay().chaseBoundingBoxOutline(PonderPalette.OUTPUT, third, depotBounds.move(0, 1, 3), 30);
        scene.idle(30);

        ItemStack plate = new ItemStack(CDItems.PLATE.get());
        ItemStack plateFood = new ItemStack(PlateFood.MEAT_WITH_VEGETABLES.item);
        scene.world().createItemOnBeltLike(inputDepot, Direction.NORTH, plate);
        scene.idle(20);
        scene.world().setKineticSpeed(armSel, -48);
        scene.idle(20);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_INPUT, ItemStack.EMPTY, 0);
        scene.idle(24);
        scene.world().removeItemsFromBelt(inputDepot);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, plate, 0);
        scene.idle(24);
        scene.world().modifyBlockEntity(skilletPos, CuisineSkilletBlockEntity.class, (be -> be.cookingData = new CookingData()));
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_INPUTS, plateFood, -1);
        scene.idle(24);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.MOVE_TO_OUTPUT, plateFood, 1);
        scene.idle(24);
        scene.world().createItemOnBeltLike(outputDepot, Direction.UP, plateFood);
        scene.world().instructArm(armPos, ArmBlockEntity.Phase.SEARCH_INPUTS, ItemStack.EMPTY, -1);
        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("If the input is plate and there are two outputs, the arm will take the cooked food from cuisine skillet and put it to output 2.")
                .pointAt(inputDepotSurface)
                .placeNearTarget();
        scene.idle(80);

    }
}
