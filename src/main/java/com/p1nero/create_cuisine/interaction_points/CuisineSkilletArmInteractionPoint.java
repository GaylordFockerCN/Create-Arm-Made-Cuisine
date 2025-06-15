package com.p1nero.create_cuisine.interaction_points;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import com.simibubi.create.content.logistics.box.PackageItem;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.item.PlateItem;
import dev.xkmc.cuisinedelight.content.item.SpatulaItem;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.data.CDConfig;
import dev.xkmc.l2core.init.reg.ench.EnchHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModSounds;

public class CuisineSkilletArmInteractionPoint extends ArmInteractionPoint {
    public CuisineSkilletArmInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
        super(type, level, pos, state);
    }

    @Override
    protected Vec3 getInteractionPositionVector() {
        return Vec3.upFromBottomCenterOf(pos, .125);
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    /**
     * 往锅里加菜或拿着盘子的时候取菜
     * @return 剩余物品
     */
    @Override
    public ItemStack insert(ItemStack stack, boolean simulate) {
        if (level.getBlockEntity(pos) instanceof CuisineSkilletBlockEntity be) {
            ItemStack input = stack.copy();

            //翻炒
            if(input.getItem() instanceof SpatulaItem){
                if(be.cookingData.contents.isEmpty()) {
                    return stack;
                }
                if (!level.isClientSide() && !simulate) {
                    be.stir(level.getGameTime(), 0);
                    level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.9F);
                    return stack.copy();
                }
                input.shrink(1);
                return input;
            }

            //装走
            if(input.getItem() instanceof PlateItem){
                if (!be.cookingData.contents.isEmpty()) {
                    input.shrink(1);
                    if(simulate) {
                        return input;
                    } else {
                        CookingData data = be.cookingData;
                        data.stir(level.getGameTime(), 0);
                        CookedFoodData food = CookedFoodData.of(data);
                        ItemStack foodStack = BaseCuisineRecipe.findBestMatch(level, food);
                        be.cookingData = new CookingData();
                        be.sync();
                        ExperienceOrb.award(((ServerLevel) level), Vec3.atCenterOf(pos), food.score() * food.size() / 100);
                        return foodStack;
                    }
                }
                return stack;
            }

            //自动拆包
            if(input.getItem() instanceof PackageItem) {
                if(!simulate) {
                    ItemStackHandler contents = PackageItem.getContents(input);
                    for (int i = 0; i < contents.getSlots(); i++) {
                        ItemStack itemstack = contents.getStackInSlot(i);
                        int size = itemstack.getCount();
                        //炒锅一次只能接受一个，需要拆开交互
                        for(int j = 0; j < size; j++) {
                            tryCook(itemstack, false, be);
                        }
                        if(!itemstack.isEmpty()) {
                            ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemstack);
                            level.addFreshEntity(itemEntity);
                        }
                    }
                }
                return ItemStack.EMPTY;
            }

            //下菜
            return tryCook(input, simulate, be);
        }
        return stack;
    }

    public ItemStack tryCook(ItemStack stack, boolean simulate, CuisineSkilletBlockEntity be) {
        //下菜
        IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(stack);
        if (config != null) {
            if (!be.canCook()) {
                return stack;
            }

            if (be.cookingData.contents.size() >= CDConfig.SERVER.maxIngredient.get()) {
                return stack;
            }

            int count = 1 + EnchHelper.getLv(be.baseItem, Enchantments.EFFICIENCY);
            if (be.slowCook()) {
                be.cookingData.setSpeed(0.5F);
            }
            ItemStack add = stack.split(count);
            if(!simulate) {
                ItemStack remain = add.getCraftingRemainingItem();
                remain.setCount(add.getCount());
                be.cookingData.addItem(add, level.getGameTime());
                be.sync();
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.9F);
                return remain;
            }
            return stack;
        }
        return stack;
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    public static class Type extends ArmInteractionPointType {
        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return level.getBlockEntity(pos) instanceof CuisineSkilletBlockEntity be && be.canCook() && be.cookingData.contents.size() < CDConfig.SERVER.maxIngredient.get();
        }

        @Nullable
        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new CuisineSkilletArmInteractionPoint(this, level, pos, state);
        }

    }
}
