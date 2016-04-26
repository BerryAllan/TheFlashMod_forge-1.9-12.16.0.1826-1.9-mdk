/* WARNING: The code that follows will make you cry:
 * 		a safety pig is provided below for your benefit.
 * 
 *                          _
 _._ _..._ .-',     _.._(`))
'-. `     '  /-._.-'    ',/
   )         \            '.
  / _    _    |             \
 |  a    a   /              |
 \   .-.                     ;  
  '-('' ).-'       ,'       ;
     '-;           |      .'
        \           \    /
        | 7  .__  _.-\   \
        | |  |  ``/  /`  /
       /,_|  |   /,_/   /
          /,_/      '`-'
 * 
 * Feel free to use the safety pig whenever it suits you best.
 */

package berryallan.theflashmod.handlers;

import berryallan.theflashmod.dimension.TeleporterSpeedForce;
import berryallan.theflashmod.entities.*;
import berryallan.theflashmod.items.FlashAbstract;
import berryallan.theflashmod.items.FlashItems;
import berryallan.theflashmod.main.MainRegistry;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.*;
import java.util.List;

import static berryallan.theflashmod.main.MainRegistry.fileName;

/*
 * features list so far:
 * running speed
 * running on water
 * running on air (traditional and new way)
 * jumping higher
 * stronger
 * stronger as speed increases
 * running up and down walls
 * fall damage not taken
 * phasing
 * slow time
 * step helper
 * tornadoes
 * vortexes
 * rapid regeneration
 * invisibility
 * lightning trail
 * the flash ring(s)
 * future flash lightning emanations (deprecated)
 * steal speed
 * glowing flash armor
 * speed digging
 * lightning throw
 * speed force dimension / interdimensional travel
 * becoming the flash through barry's lab
 * custom hand
 * cosmic treadmill
 *
 * need:
 * cosmic treadmill to upgrade speed in survival
 *
 * improve:
 * speed trail
 */

public class SpeedForceHandler {
    public static int flashFactor = 0;
    public static float jumpFactor = 0.04F;
    public static int lowerSpeedLimit = 0;
    public static float upperJumpMoveLimit = 0.64F;
    public static float lowerJumpMoveLimit = 0.04F;
    public static float gameSpeed = SlowTimeHandler.getGameSpeed();
    public static float slowMoFactor = 0.0F;
    public static float slowMoUpperLimit = 0.9F;
    public static float sloMoLowerLimit = 0.0F;
    public static boolean waterRunningUnlocked;
    public static boolean wallRunningUnlocked;
    public static boolean tornadoesAndVortexesUnlocked;
    public static boolean phasingUnlocked;
    public static boolean flyingUnlocked;
    public static boolean betterFlyingUnlocked;
    public static boolean lightningThrowUnlocked;
    public static boolean invisibilityUnlocked;
    public static boolean isFlying;
    public static boolean isFlyingNow;
    public static boolean vibrating;
    public static boolean vibratingInvisible;
    public static boolean interdimensionalTravelUnlocked;
    public static boolean speedStealUnlocked;
    public static boolean catchingArrowsUnlocked;
    public static boolean isFutureFlash;
    public static boolean isFlash;
    public static boolean onCosmicTreadmill;
    public static String speedkmh;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static int dimensionToTravelTo = MainRegistry.dimId;
    public static float cosmicTreadmillFloat = 0.0F;
    public static float cosmicTreadmillUpgradeSpeed;
    public static boolean cosmicTreadmillRunning = false;
    public static String keyToHit = "W";
    public static boolean goingDown;
    public static NBTTagCompound tag;

    public static String readFromTemp() {
        // This will reference one line at a time
        String line = null;

        try {
            File file = new File(fileName);
            BufferedReader output = new BufferedReader(new FileReader(file));

            // FileReader reads text files in the default encoding.
            //FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            //BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = output.readLine()) != null) {
                return line;
            }

            // Always close files.
            output.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" +
                    fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            // Or we could just do this:
            ex.printStackTrace();
        }

        return line;
    }

    public static void writeToFile(String stringToWrite) {
        try {
            // Assume default encoding.
            File file = new File(fileName);
            BufferedWriter output = new BufferedWriter(new FileWriter(file));

            //FileWriter fileWriter = new FileWriter("saves\\" + MainRegistry.fileName);

            // Always wrap FileWriter in BufferedWriter.
            //BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a newline character.
            output.write(stringToWrite);

            // Always close files.
            output.close();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + fileName + "'");
            // Or we could just do this:
            ex.printStackTrace();
        }
    }

    public static float setCosmicTreadmillFloat() {
        if (cosmicTreadmillFloat >= 0.0F && !(cosmicTreadmillFloat >= 1.0F) && !goingDown) {
            cosmicTreadmillFloat += 0.1;
            if (cosmicTreadmillFloat >= 1.0F) {
                goingDown = true;
                cosmicTreadmillFloat = 1.0F;
            }
        } else if (goingDown) {
            cosmicTreadmillFloat -= 0.1F;
            if (cosmicTreadmillFloat <= 0.0F) {
                goingDown = false;
                cosmicTreadmillFloat = 0.0F;
            }
        }
        return cosmicTreadmillFloat;
    }

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP thePlayer = (EntityPlayerMP) event.getEntity();

            if (flashFactor >= 65 && interdimensionalTravelUnlocked && !mc.thePlayer.isSneaking()
                    && mc.thePlayer.moveForward > 0 && mc.thePlayer.dimension != MainRegistry.dimId) {
                if (thePlayer.worldObj.isRemote) {
                    return;
                }

                mc.theWorld.addWeatherEffect(
                        new EntityLightningBolt(mc.theWorld, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                                true));

                BlockPos coords = thePlayer.getBedLocation(dimensionToTravelTo);
                if (coords == null) {
                    coords = thePlayer.mcServer.worldServerForDimension(dimensionToTravelTo).getSpawnPoint();
                }
                BlockPos realcoords = EntityPlayer
                        .getBedSpawnLocation(thePlayer.worldObj, coords, dimensionToTravelTo == 0);

                if (realcoords == null) {
                    thePlayer.playerNetServerHandler.sendPacket(new SPacketChangeGameState(0, 0.0F));
                }
                thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, dimensionToTravelTo,
                        new TeleporterSpeedForce(thePlayer.mcServer.worldServerForDimension(dimensionToTravelTo),
                                realcoords));

                if (tag.getInteger("upperSpeedLimit") == 65) {
                    flashFactor = tag.getInteger("upperSpeedLimit") - 1;
                } else {
                    flashFactor = tag.getInteger("upperSpeedLimit");
                }
            } else if (flashFactor >= 65 && interdimensionalTravelUnlocked && !mc.thePlayer.isSneaking()
                    && mc.thePlayer.moveForward > 0 && mc.thePlayer.dimension == MainRegistry.dimId) {
                dimensionToTravelTo = 0;
                if (thePlayer.worldObj.isRemote) {
                    return;
                }
                mc.theWorld.addWeatherEffect(
                        new EntityLightningBolt(mc.theWorld, mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ,
                                true));

                BlockPos coords = thePlayer.getBedLocation(dimensionToTravelTo);
                if (coords == null) {
                    coords = thePlayer.mcServer.worldServerForDimension(dimensionToTravelTo).getSpawnPoint();
                }
                BlockPos realcoords = EntityPlayer
                        .getBedSpawnLocation(thePlayer.worldObj, coords, dimensionToTravelTo == 0);

                if (realcoords == null) {
                    thePlayer.playerNetServerHandler.sendPacket(new SPacketChangeGameState(0, 0.0F));
                }
                thePlayer.mcServer.getPlayerList().transferPlayerToDimension(thePlayer, dimensionToTravelTo,
                        new TeleporterSpeedForce(thePlayer.mcServer.worldServerForDimension(dimensionToTravelTo),
                                realcoords));

                if (tag.getInteger("upperSpeedLimit") == 65) {
                    flashFactor = tag.getInteger("upperSpeedLimit") - 1;
                } else {
                    flashFactor = tag.getInteger("upperSpeedLimit");
                }
            } else {
                dimensionToTravelTo = MainRegistry.dimId;
            }
        }

        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            String PNT = EntityPlayer.PERSISTED_NBT_TAG;

            if (!event.getEntityLiving().getEntityData().hasKey(PNT)) {
                tag = new NBTTagCompound();
                event.getEntityLiving().getEntityData().setTag(PNT, tag);
            } else {
                tag = event.getEntityLiving().getEntityData().getCompoundTag(PNT);
            }

            if (tag.getBoolean("cosmicTreadmillBuilt")) {
                World world = player.getEntityWorld();
                double x = player.posX;
                double y = player.getEntityBoundingBox().minY;
                double z = player.posZ;

                if (world.getBlockState(new BlockPos(x, y, z))
                        .equals(Blocks.carpet.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.BLACK))
                        && world.getBlockState(new BlockPos(x, y + 6, z)).equals(Blocks.stonebrick.getDefaultState())
                        && flashFactor >= 1) {
                    onCosmicTreadmill = true;

                    setCosmicTreadmillFloat();

                    if (KeysHandler.C_KEY.isPressed()) {
                        if (!cosmicTreadmillRunning) {
                            cosmicTreadmillRunning = true;
                        } else {
                            cosmicTreadmillRunning = false;
                        }
                    }
                } else {
                    onCosmicTreadmill = false;
                    cosmicTreadmillRunning = false;
                }
            }

            if (cosmicTreadmillRunning) {
                System.out.println(readFromTemp());
                System.out.println("tag: " + tag.getInteger("upperSpeedLimit"));
                System.out.println("upgrades: " + tag.getInteger("cosmicUpgrades"));
                if (cosmicTreadmillFloat >= 0.85) {
                    if (keyToHit.equals("W")) {
                        if (player.moveForward > 0.0) {
                            if (!(cosmicTreadmillUpgradeSpeed >= 1.0)) {
                                cosmicTreadmillUpgradeSpeed += 0.25;
                            }
                            keyToHit = "S";
                        }
                    }
                    if (keyToHit.equals("S")) {
                        if (player.moveForward < 0.0) {
                            if (!(cosmicTreadmillUpgradeSpeed >= 1.0)) {
                                cosmicTreadmillUpgradeSpeed += 0.25;
                            }
                            keyToHit = "W";
                        }
                    }
                }
            } else {
                keyToHit = "W";
                cosmicTreadmillUpgradeSpeed = 0.0F;
            }

            if (cosmicTreadmillUpgradeSpeed >= 1) {
                cosmicTreadmillUpgradeSpeed = 0.0F;
                writeToFile(Integer.toString(Integer.parseInt(readFromTemp()) + 8));
            }

            if (player.capabilities.isCreativeMode) {
                tag.setInteger("upperSpeedLimit", 65);
            } else {
                tag.setInteger("upperSpeedLimit", Integer.parseInt(readFromTemp()));
            }

            if (flashFactor > tag.getInteger("upperSpeedLimit")) {
                flashFactor = tag.getInteger("upperSpeedLimit");
            }

            if (player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem()
                    .equals(FlashItems.helmetFlash) && player.inventory.armorItemInSlot(2) != null && player.inventory
                    .armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFlash)
                    && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem()
                    .equals(FlashItems.legsFlash) && player.inventory.armorItemInSlot(0) != null && player.inventory
                    .armorItemInSlot(0).getItem().equals(FlashItems.bootsFlash)) {
                player.addPotionEffect(new PotionEffect(MainRegistry.speedForce, 10, 10));
                isFlash = true;

                if ((player.getHeldItemMainhand() == null
                        || player.getHeldItemMainhand().getItem() == FlashItems.futureFlashHand)
                        && mc.gameSettings.thirdPersonView == 0 && Minecraft.getMinecraft().currentScreen == null) {
                    player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(FlashItems.flashHand));
                } else if (mc.gameSettings.thirdPersonView != 0 && player.getHeldItemMainhand() != null
                        && player.getHeldItemMainhand().getItem() == FlashItems.flashHand) {
                    player.setHeldItem(EnumHand.MAIN_HAND, null);
                }

                for (int i = 0; i < 36; ++i) {
                    if (player.inventory.mainInventory[i] != null
                            && player.inventory.mainInventory[i].getItem() == FlashItems.flashHand) {
                        if (mc.currentScreen != null) {
                            player.inventory.mainInventory[i] = null;
                        }

                        if (player.inventory.mainInventory[player.inventory.currentItem]
                                != player.inventory.mainInventory[i]) {
                            player.inventory.mainInventory[i] = null;
                        }
                    }
                }

                if (player.inventory.mainInventory[player.inventory.currentItem + 1] != null) {
                    if (player.getHeldItemMainhand() != null
                            && player.getHeldItemMainhand().getItem() == FlashItems.flashHand) {
                        player.inventory.mainInventory[player.inventory.currentItem] = player.inventory.mainInventory[
                                player.inventory.currentItem + 1];
                        player.inventory.mainInventory[player.inventory.currentItem + 1] = null;
                    }
                }

                if (flashFactor >= 1) {
                    player.capabilities.setFlySpeed((float) (flashFactor * 0.0333333333333));
                    player.fallDistance = 0.0F;
                    player.jumpMovementFactor = jumpFactor;

                    //player.addPotionEffect(
                    //		new PotionEffect(Potion.getPotionById(3), 10, (int) (flashFactor * 5.6), false, false));
                    //player.addPotionEffect(
                    //		new PotionEffect(MainRegistry.speedStrength, 10, (int) (flashFactor / 1.5), false, false));
                    player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                            .setBaseValue(flashFactor == 1 && flashFactor != 0 ? 2 * 35 / 32 : flashFactor * 35 / 32);
                    mc.thePlayer.getFovModifier();

                    if (player.moveForward > 0.0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) (MathHelper.sin(f) * (flashFactor / 2F));
                        player.motionZ += (double) (MathHelper.cos(f) * (flashFactor / 2F));
                    } else if (player.moveForward < 0.0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) -(MathHelper.sin(f) * (flashFactor / 2.4F));
                        player.motionZ += (double) -(MathHelper.cos(f) * (flashFactor / 2.4F));
                    }

                    if (player.moveStrafing > 0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) -(MathHelper.cos(f) * (flashFactor / 2.4F));
                        player.motionZ -= (double) -(MathHelper.sin(f) * (flashFactor / 2.4F));
                    } else if (player.moveStrafing < 0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) (MathHelper.cos(f) * (flashFactor / 2.4F));
                        player.motionZ -= (double) (MathHelper.sin(f) * (flashFactor / 2.4F));
                    }
                } else {
                    player.capabilities.isFlying = false;
                    player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
                }
            } else {
                isFlash = false;

                if (player.getHeldItemMainhand() != null
                        && player.getHeldItemMainhand().getItem() == FlashItems.flashHand) {
                    player.setHeldItem(EnumHand.MAIN_HAND, null);
                }
            }

            if (player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem()
                    .equals(FlashItems.helmetFutureFlash) && player.inventory.armorItemInSlot(2) != null
                    && player.inventory.armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFutureFlash)
                    && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem()
                    .equals(FlashItems.legsFutureFlash) && player.inventory.armorItemInSlot(0) != null
                    && player.inventory.armorItemInSlot(0).getItem().equals(FlashItems.bootsFutureFlash)) {
                player.addPotionEffect(new PotionEffect(MainRegistry.speedForce, 10, 10));
                isFutureFlash = true;

                if ((player.getHeldItemMainhand() == null
                        || player.getHeldItemMainhand().getItem() == FlashItems.flashHand)
                        && mc.gameSettings.thirdPersonView == 0 && mc.currentScreen == null) {
                    player.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(FlashItems.futureFlashHand));
                } else if (mc.gameSettings.thirdPersonView != 0 && player.getHeldItemMainhand() != null
                        && player.getHeldItemMainhand().getItem() == FlashItems.futureFlashHand) {
                    player.setHeldItem(EnumHand.MAIN_HAND, null);
                }

                for (int i = 0; i < 36; ++i) {
                    if (player.inventory.mainInventory[i] != null
                            && player.inventory.mainInventory[i].getItem() == FlashItems.futureFlashHand) {
                        if (mc.currentScreen != null) {
                            player.inventory.mainInventory[i] = null;
                        }

                        if (player.inventory.mainInventory[player.inventory.currentItem]
                                != player.inventory.mainInventory[i]) {
                            player.inventory.mainInventory[i] = null;
                        }
                    }
                }

                if (player.inventory.mainInventory[player.inventory.currentItem + 1] != null) {
                    if (player.getHeldItemMainhand() != null
                            && player.getHeldItemMainhand().getItem() == FlashItems.futureFlashHand) {
                        player.inventory.mainInventory[player.inventory.currentItem] = player.inventory.mainInventory[
                                player.inventory.currentItem + 1];
                        player.inventory.mainInventory[player.inventory.currentItem + 1] = null;
                    }
                }

                if (flashFactor >= 1) {
                    player.capabilities.setFlySpeed((float) (flashFactor * 0.0433));
                    player.fallDistance = 0.0F;
                    player.jumpMovementFactor = jumpFactor;

                    //player.addPotionEffect(
                    //new PotionEffect(Potion.getPotionById(3), 10, flashFactor * 8, false, false));
                    //player.addPotionEffect(new PotionEffect(MobEffects.digSpeed, 10, (flashFactor / 2), false, false));
                    player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)
                            .setBaseValue(flashFactor == 1 && flashFactor != 0 ? 2 * 25 / 16 : flashFactor * 25 / 16);

                    if (player.moveForward > 0.0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) (MathHelper.sin(f) * (flashFactor / 1.4F));
                        player.motionZ += (double) (MathHelper.cos(f) * (flashFactor / 1.4F));
                    } else if (player.moveForward < 0.0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) -(MathHelper.sin(f) * (flashFactor / 1.68F));
                        player.motionZ += (double) -(MathHelper.cos(f) * (flashFactor / 1.68F));
                    }

                    if (player.moveStrafing > 0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) -(MathHelper.cos(f) * (flashFactor / 1.68F));
                        player.motionZ -= (double) -(MathHelper.sin(f) * (flashFactor / 1.68F));
                    } else if (player.moveStrafing < 0 && player.onGround) {
                        float f = player.rotationYaw * 0.017453292f;
                        player.motionX -= (double) (MathHelper.cos(f) * (flashFactor / 1.68F));
                        player.motionZ -= (double) (MathHelper.sin(f) * (flashFactor / 1.68F));
                    }
                } else {
                    player.capabilities.isFlying = false;
                    player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
                }
            } else {
                isFutureFlash = false;

                if (player.getHeldItemMainhand() != null
                        && player.getHeldItemMainhand().getItem() == FlashItems.futureFlashHand) {
                    player.setHeldItem(EnumHand.MAIN_HAND, null);
                }
            }

            if (KeysHandler.SPACEBAR.isKeyDown() && player.onGround && !player.isInWater()) {
                int jump = 4;
                player.motionY = 0.42F;
                if (flashFactor >= 1) {
                    player.motionY += (double) ((float) (jump + 1) * 0.1F);
                }
                if (player.isSprinting()) {
                    float f = player.rotationYaw * 0.017453292F;
                    player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
                    player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
                }
                player.isAirBorne = true;
                ForgeHooks.onLivingJump(player);
            } else {
                player.isAirBorne = false;
            }
            if (KeysHandler.SPACEBAR.isKeyDown() && player.isInWater() && flashFactor < 1) {
                player.motionY += 0.03999999910593033D;
            }

            if (tag.getBoolean("barryAllenLabBuilt")) {
                World world = player.getEntityWorld();
                double x = player.posX;
                double y = player.getEntityBoundingBox().minY;
                double z = player.posZ;

                if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockPressurePlateWeighted && world
                        .getBlockState(new BlockPos(x, y + 5, z)).equals(Blocks.brick_block.getDefaultState())) {
                    world.addWeatherEffect(new EntityLightningBolt(world, x, y, z, true));

                    world.setBlockToAir(new BlockPos(x - 3, y + 1, z));
                    world.setBlockToAir(new BlockPos(x - 3, y + 1, z - 1));
                    world.setBlockToAir(new BlockPos(x - 3, y + 1, z - 2));

                    world.setBlockToAir(new BlockPos(x - 3, y + 2, z));
                    world.setBlockToAir(new BlockPos(x - 3, y + 2, z + 1));
                    world.setBlockToAir(new BlockPos(x - 3, y + 2, z - 1));

                    world.setBlockToAir(new BlockPos(x - 3, y + 3, z));
                    world.setBlockToAir(new BlockPos(x - 3, y + 3, z - 1));

                    world.setBlockToAir(new BlockPos(x - 3, y + 4, z));

                    world.setBlockToAir(new BlockPos(x, y + 1, z + 2));
                    world.setBlockToAir(new BlockPos(x - 1, y + 1, z + 2));
                    world.setBlockToAir(new BlockPos(x + 1, y + 1, z + 2));
                    world.setBlockToAir(new BlockPos(x + 2, y + 1, z + 2));

                    world.setBlockToAir(new BlockPos(x, y + 3, z + 2));
                    world.setBlockToAir(new BlockPos(x - 1, y + 3, z + 2));
                    world.setBlockToAir(new BlockPos(x + 1, y + 3, z + 2));
                    world.setBlockToAir(new BlockPos(x + 2, y + 3, z + 2));

                    player.playSound(SoundEvents.block_glass_break, 1.0F, 1.7F);

                    player.inventory.addItemStackToInventory(new ItemStack(FlashItems.helmetFlash));
                    player.inventory.addItemStackToInventory(new ItemStack(FlashItems.chestPlateFlash));
                    player.inventory.addItemStackToInventory(new ItemStack(FlashItems.legsFlash));
                    player.inventory.addItemStackToInventory(new ItemStack(FlashItems.bootsFlash));

                    mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(
                            "You, Barry Allen, have just been struck by lightning and doused in chemicals. "
                                    + "This turns you into the generator of the Speed Force,"
                                    + " an enigmatic energy that reverberates throughout all of time and space with every step you take. "
                                    + "Any users of this energy"
                                    + " gain the ability to move at superhuman-speeds and utilize other various abilities."
                                    + " Your infinite speed potential must be used to"
                                    + " serve a fine sense of justice throughout the world, and protect it from any threats that"
                                    + " may arise. Using your new-found abilities and your knowledge of science, you work quickly to"
                                    + " create a friction "
                                    + "resistant suit you can use to run at extreme speeds without damaging side effects to yourself. "
                                    + "However, you feel like there must be a better way to store and deploy your suit..."));

                    tag.setBoolean("barryAllenLabBuilt", false);
                }
            }

            if (player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(2) != null
                    && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(0) != null
                    && player.inventory.armorItemInSlot(3).getItem() instanceof FlashAbstract && player.inventory
                    .armorItemInSlot(2).getItem() instanceof FlashAbstract && player.inventory.armorItemInSlot(1)
                    .getItem() instanceof FlashAbstract && player.inventory.armorItemInSlot(0)
                    .getItem() instanceof FlashAbstract) {
                //unlocked?
                if (tag.getInteger("upperSpeedLimit") <= 8)    //level 1
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;

                    catchingArrowsUnlocked = false;
                    phasingUnlocked = false;
                    invisibilityUnlocked = false;
                    flyingUnlocked = false;
                    betterFlyingUnlocked = false;
                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 8 && tag.getInteger("upperSpeedLimit") <= 16)    //level 2
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;

                    phasingUnlocked = false;
                    invisibilityUnlocked = false;
                    flyingUnlocked = false;
                    betterFlyingUnlocked = false;
                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 16 && tag.getInteger("upperSpeedLimit") <= 24)    //level 3
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;
                    phasingUnlocked = true;

                    invisibilityUnlocked = false;
                    flyingUnlocked = false;
                    betterFlyingUnlocked = false;
                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 24 && tag.getInteger("upperSpeedLimit") <= 32)    //level 4
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;
                    phasingUnlocked = true;
                    tornadoesAndVortexesUnlocked = true;
                    invisibilityUnlocked = true;

                    flyingUnlocked = false;
                    betterFlyingUnlocked = false;
                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 32 && tag.getInteger("upperSpeedLimit") <= 48)    //level 5
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;
                    phasingUnlocked = true;
                    tornadoesAndVortexesUnlocked = true;
                    invisibilityUnlocked = true;
                    flyingUnlocked = true;

                    betterFlyingUnlocked = false;
                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 48 && tag.getInteger("upperSpeedLimit") <= 56)    //level 6
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;
                    phasingUnlocked = true;
                    tornadoesAndVortexesUnlocked = true;
                    invisibilityUnlocked = true;
                    flyingUnlocked = true;
                    betterFlyingUnlocked = true;

                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 56 && tag.getInteger("upperSpeedLimit") <= 64)    //level 7
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;
                    phasingUnlocked = true;
                    tornadoesAndVortexesUnlocked = true;
                    invisibilityUnlocked = true;
                    flyingUnlocked = true;
                    betterFlyingUnlocked = true;
                    lightningThrowUnlocked = true;
                    speedStealUnlocked = true;

                    interdimensionalTravelUnlocked = false;
                } else if (tag.getInteger("upperSpeedLimit") > 64)    //level 8
                {
                    waterRunningUnlocked = true;
                    wallRunningUnlocked = true;
                    catchingArrowsUnlocked = true;
                    phasingUnlocked = true;
                    tornadoesAndVortexesUnlocked = true;
                    invisibilityUnlocked = true;
                    flyingUnlocked = true;
                    betterFlyingUnlocked = true;
                    lightningThrowUnlocked = true;
                    speedStealUnlocked = true;
                    interdimensionalTravelUnlocked = true;
                } else    //none
                {
                    waterRunningUnlocked = false;
                    wallRunningUnlocked = false;
                    catchingArrowsUnlocked = false;
                    phasingUnlocked = false;
                    tornadoesAndVortexesUnlocked = false;
                    invisibilityUnlocked = false;
                    flyingUnlocked = false;
                    betterFlyingUnlocked = false;
                    lightningThrowUnlocked = false;
                    speedStealUnlocked = false;
                    interdimensionalTravelUnlocked = false;
                }

                // speed
                if (flashFactor >= 65) {
                    speedkmh = "FTL";
                } else {
                    speedkmh = Double.toString(MathHelper.sqrt_double(
                            (player.posX + player.lastTickPosX) * (player.posX + player.lastTickPosX)
                                    + (player.posZ + player.lastTickPosZ) * (player.posZ + player.lastTickPosZ)) / (
                            player.ticksExisted - (player.ticksExisted - 1)));
                }

                if (player.moveForward > 0 && flashFactor >= 1 && !player.isSneaking()) {
                    player.setSprinting(true);
                }

                World world1 = player.getEntityWorld();
                int x1 = MathHelper.floor_double(player.posX);
                int z1 = MathHelper.floor_double(player.posZ);

                // water running
                if (flashFactor >= 1 && waterRunningUnlocked) {
                    if (world1.getBlockState(new BlockPos(x1, (int) (player.getEntityBoundingBox().minY - 1), z1))
                            .getBlock().getDefaultState().getMaterial() == Material.water || player.isInWater()) {
                        player.motionY = 0.0D;
                        player.capabilities.isFlying = true;
                        player.playSound(SoundEvents.block_water_ambient, 1.0F, 1.7F);

                        if (player.isSneaking() && !Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()
                                && Minecraft.getMinecraft().currentScreen == null) {
                            player.motionY = -0.42;
                        }

                        if (KeysHandler.SPACEBAR.isKeyDown()) {
                            player.motionY = 0.42;

                            if (flashFactor >= 1) {
                                player.motionY += (double) ((float) (4 + 1) * 0.1F);
                            }
                            if (player.isSprinting()) {
                                float f = player.rotationYaw * 0.017453292F;
                                player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
                                player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
                            }

                            player.isAirBorne = true;
                            ForgeHooks.onLivingJump(player);
                        } else {
                            player.isAirBorne = false;
                        }

                    } else {
                        player.capabilities.isFlying = false;
                    }
                }
                // speed falling while in slo-mo
                if (slowMoFactor > 0 && player.isSneaking()) {
                    player.motionY -= slowMoFactor * 1.16;
                }
                // rapid regeneration
                if (!player.isDead) {
                    if (player.getHealth() < player.getMaxHealth()) {
                        if (player.inventory.armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFlash)) {
                            player.heal((float) ((tag.getInteger("upperSpeedLimit") / 2) * 0.0125));
                            // player.setHealth(player.getHealth() + (float) ((FlashArmor.flashFactor / 1.5) * 0.0125));
                        } else if (player.inventory.armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFutureFlash)) {
                            player.heal((float) ((tag.getInteger("upperSpeedLimit") / 1.5) * 0.0125));
                            // player.setHealth(player.getHealth() + (float) ((FlashArmor.flashFactor / 1.5) * 0.0125));
                        }
                        player.hurtTime = 0;
                        player.maxHurtTime = 0;
                    }
                } else if (player.isDead) {
                    player.setDead();
                }

                // step helper
                if (flashFactor >= 1) {
                    player.stepHeight = player.isCollidedHorizontally && player.onGround ? 1.5f : 0.5f;
                }

                // increase speed
                if (KeysHandler.UP_KEY.isKeyDown()) {
                    if (flashFactor < tag.getInteger("upperSpeedLimit")) {
                        flashFactor += 1;
                    }

                    if (jumpFactor < upperJumpMoveLimit) {
                        jumpFactor += 0.025F;
                    }

                    if (jumpFactor >= upperJumpMoveLimit) {
                        jumpFactor = upperJumpMoveLimit;
                    }
                }
                // decrease speed
                if (KeysHandler.DOWN_KEY.isKeyDown()) {
                    if (flashFactor >= lowerSpeedLimit) {
                        flashFactor -= 1;
                    }
                    if (flashFactor <= lowerSpeedLimit || flashFactor < 1 || flashFactor == -1) {
                        flashFactor = lowerSpeedLimit;
                    }

                    if (jumpFactor > lowerJumpMoveLimit) {
                        jumpFactor -= 0.025F;
                    }

                    if (jumpFactor <= lowerJumpMoveLimit) {
                        jumpFactor = lowerJumpMoveLimit;
                    }
                }
                // speed max
                if (KeysHandler.PGUP_KEY.isKeyDown()) {
                    if (tag.getInteger("upperSpeedLimit") == 65) {
                        flashFactor = tag.getInteger("upperSpeedLimit") - 1;
                    } else {
                        flashFactor = tag.getInteger("upperSpeedLimit");
                    }
                    jumpFactor = upperJumpMoveLimit;
                }
                // speed min
                if (KeysHandler.PGDOWN_KEY.isKeyDown()) {
                    flashFactor = lowerSpeedLimit + 1;
                    jumpFactor = lowerJumpMoveLimit + 0.025F;
                }

                // wall running
                if (flashFactor >= 1 && player.isCollidedHorizontally && KeysHandler.R_KEY.isKeyDown()
                        && wallRunningUnlocked) {
                    player.motionY += 0.25;
                }
                if (flashFactor >= 1 && player.isCollidedHorizontally && player.isSneaking() && wallRunningUnlocked) {
                    player.motionY -= 0.25;
                }

                //extinguish all fires by vibration
                if (flashFactor >= 1 && player.isBurning() && vibrating) {
                    player.extinguish();
                }

                // slow time perspective
                float speed = Float.valueOf(Float.toString(this.gameSpeed - this.slowMoFactor).substring(0, 3))
                        .floatValue();
                if (KeysHandler.C_KEY.isKeyDown() && !onCosmicTreadmill) {
                    slowMoFactor += 0.04F;
                    if (slowMoFactor >= slowMoUpperLimit) {
                        slowMoFactor = slowMoUpperLimit;
                    }
                    SlowTimeHandler.setGameSpeed(speed);
                    Minecraft.getMinecraft().currentScreen = null;
                    player.arrowHitTimer = (int) (SlowTimeHandler.getGameSpeed() * 500.0F);
                }
                if (KeysHandler.X_KEY.isKeyDown() && !Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatOpen()
                        && Minecraft.getMinecraft().currentScreen == null) {
                    slowMoFactor -= 0.04F;
                    if (slowMoFactor <= sloMoLowerLimit) {
                        slowMoFactor = sloMoLowerLimit;
                    }

                    SlowTimeHandler.setGameSpeed(speed);
                    Minecraft.getMinecraft().currentScreen = null;
                }
                // phase
                if (KeysHandler.F_KEY.isKeyDown() && phasingUnlocked && flashFactor >= 1) {
                    player.noClip = true;
                    player.isAirBorne = true;
                    vibrating = true;

                    if (invisibilityUnlocked) {
                        vibratingInvisible = true;
                    } else {
                        vibratingInvisible = false;
                    }

                    mc.renderChunksMany = false;
                    player.capabilities.isFlying = true;
                    player.onGround = false;
                    player.capabilities.disableDamage = true;

                    if (KeysHandler.SPACEBAR.isKeyDown()) {
                        player.motionY += 0.42F;
                    } else if (player.isSneaking()) {
                        player.motionY += (player.capabilities.getFlySpeed() * 3.0F) - 0.42F;
                    }

                    if (!isFlying) {
                        if (world1.getBlockState(new BlockPos(x1, player.getEntityBoundingBox().minY - 1, z1))
                                .getBlock().getDefaultState().getMaterial() == Material.air) {
                            player.motionY -= 0.42;
                        }
                    }
                        /*
                        if (world1.getBlockState(new BlockPos(x1, player.getEntityBoundingBox().minY - 1, z1))
								.getBlock().getMaterial() != Material.air &&
								world1.getBlockState(new BlockPos(x1, player.getEntityBoundingBox().minY - 1, z1))
										.getBlock().getMaterial() != Material.water && player.isInWater())
						{
							player.onGround = true;
						}
						else
						{
							player.onGround = false;
						}
						*/
                } else {
                    vibrating = false;
                    vibratingInvisible = false;
                    player.isAirBorne = false;
                }

                // flying
                if (KeysHandler.R_KEY.isKeyDown() && flashFactor >= 1 && flyingUnlocked
                        && !player.isCollidedHorizontally) {
                    player.isAirBorne = true;
                    isFlyingNow = true;
                    if (betterFlyingUnlocked) {
                        player.capabilities.isFlying = true;
                        isFlying = true;

                        if (KeysHandler.SPACEBAR.isKeyDown()) {
                            player.motionY += 0.42F;
                        } else if (player.isSneaking()) {
                            player.motionY += (player.capabilities.getFlySpeed() * 3.0F) - 0.42F;
                        }
                    } else if (!player.isCollidedHorizontally && KeysHandler.SPACEBAR.isKeyDown()) {
                        player.motionY += 0.175D;
                        isFlying = true;
                    } else {
                        isFlying = false;
                    }
                } else {
                    player.isAirBorne = false;
                    isFlyingNow = false;
                }

                // steal speed
                if (speedStealUnlocked) {
                    List<Entity> entities3 = player.getEntityWorld().loadedEntityList;

                    if (entities3.size() > 0) {
                        for (int i = 0; i < entities3.size(); i++) {
                            Entity entity = entities3.get(i);

                            if (KeysHandler.G_KEY.isKeyDown() && flashFactor >= 1) {
                                if (entity instanceof EntityPlayer) {

                                } else {
                                    if (mc.objectMouseOver.entityHit == entity) {
                                        entity.performHurtAnimation();

                                        entity.motionX = 0;
                                        entity.motionY = 0;
                                        entity.motionZ = 0;

                                        entity.setPosition(entity.lastTickPosX, entity.lastTickPosY,
                                                entity.lastTickPosZ);

                                        entity.rotationYaw = entity.prevRotationYaw;
                                        entity.rotationPitch = entity.prevRotationPitch;
                                    }
                                }
                            }
                        }
                    }
                }

                // catching arrows
                if (KeysHandler.G_KEY.isKeyDown() && catchingArrowsUnlocked) {
                    World world = player.getEntityWorld();
                    int x = 3;
                    int y = 3;
                    int z = 3;

                    List<Entity> entityList = world
                            .getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().expandXyz(16));

                    if (entityList.size() > 0) {
                        for (int i = 0; i < entityList.size(); i++) {
                            Entity entity = entityList.get(i);

                            if (entity instanceof EntityPlayer) {

                            } else if (entity instanceof EntityArrow) {
                                entity.motionX = 0;
                                entity.motionY = 0;
                                entity.motionZ = 0;

                                entity.setPosition(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);

                                entity.rotationYaw = entity.prevRotationYaw;
                                entity.rotationPitch = entity.prevRotationPitch;

                                entity.setDead();
                                player.inventory.addItemStackToInventory(new ItemStack(Items.arrow));
                            } else {

                            }
                        }
                    }
                }

                // tornado blowback and vortexes
                if (KeysHandler.V_KEY.isKeyDown() && flashFactor >= 1 && tornadoesAndVortexesUnlocked) {
                    World world = player.getEntityWorld();

                    List<Entity> entityList = world
                            .getEntitiesWithinAABB(Entity.class, player.getEntityBoundingBox().expandXyz(24));

                    if (player.isSneaking()) {
                        if (entityList.size() > 0) {
                            for (int i = 0; i < entityList.size(); i++) {
                                Entity entity = entityList.get(i);

                                int j = 1;
                                if (entity instanceof EntityPlayer) {

                                } else {
                                    float f = player.rotationYaw * 0.017453292f;
                                    entity.motionX += (double) (MathHelper.sin(f)) / 3;
                                    entity.motionZ -= (double) (MathHelper.cos(f)) / 3;
                                }
                            }
                        }
                    } else {
                        if (entityList.size() > 0) {
                            for (int i = 0; i < entityList.size(); i++) {
                                Entity entity = entityList.get(i);

                                double j = 1;
                                if (entity instanceof EntityPlayer) {

                                } else {
                                    float f = player.rotationYaw * 0.017453292f;
                                    entity.motionX -= (double) (MathHelper.sin(f)) / 3;
                                    entity.motionZ += (double) (MathHelper.cos(f)) / 3;
                                    entity.motionY += j / 6;
                                }
                            }
                        }
                    }
                }

                // speed force lightning throw future flash
                if (KeysHandler.T_KEY.isKeyDown() && !player.isSneaking() && player.moveForward > 0
                        && player.getHeldItemMainhand() != null
                        && player.getHeldItemMainhand().getItem() == FlashItems.futureFlashHand && flashFactor >= 1
                        && player.inventory.armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFutureFlash)
                        && lightningThrowUnlocked) {
                    world1.playSound(player.posX, player.posY, player.posZ, SoundEvents.entity_creeper_hurt,
                            SoundCategory.HOSTILE, 4F, 1.7F, false);

                    EntitySpeedForceLightningThrowBlue entitySpeedForceLightningThrowBlue = new EntitySpeedForceLightningThrowBlue(
                            world1, player);
                    entitySpeedForceLightningThrowBlue
                            .func_184538_a(player, player.rotationPitch, player.rotationYaw, 0.0F, 5.0F, 1.0F);
                    world1.spawnEntityInWorld(entitySpeedForceLightningThrowBlue);
                }

                // speed force lightning throw flash
                if (KeysHandler.T_KEY.isKeyDown() && !player.isSneaking() && player.moveForward > 0
                        && player.getHeldItemMainhand() != null
                        && player.getHeldItemMainhand().getItem() == FlashItems.futureFlashHand && flashFactor >= 1
                        && player.inventory.armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFlash)
                        && lightningThrowUnlocked) {
                    world1.playSound(player.posX, player.posY, player.posZ, SoundEvents.entity_creeper_hurt,
                            SoundCategory.HOSTILE, 4F, 1.7F, false);

                    EntitySpeedForceLightningThrowYellow entitySpeedForceLightningThrowYellow = new EntitySpeedForceLightningThrowYellow(
                            world1, player);
                    entitySpeedForceLightningThrowYellow
                            .func_184538_a(player, player.rotationPitch, player.rotationYaw, 0.0F, 5.0F, 1.0F);
                    world1.spawnEntityInWorld(entitySpeedForceLightningThrowYellow);
                }

                // speed trail blue
                if (flashFactor >= 1 && !player.isSneaking() && player.moveForward > 0 && player.inventory
                        .armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFutureFlash) && !vibratingInvisible
                        && !(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
                    for (int i = 0; i < 1; ++i) {
                        Entity entity = EntityList.createEntityByName("speed_force_trail_blue", world1);

                        if (entity instanceof EntityLivingBase) {
                            EntityLiving entityliving = (EntityLiving) entity;
                            entity.setLocationAndAngles(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ,
                                    player.rotationYaw, player.rotationPitch);
                            entityliving.rotationYawHead = entityliving.rotationYaw;
                            entityliving.renderYawOffset = entityliving.rotationYaw;
                            entityliving.onInitialSpawn(world1.getDifficultyForLocation(new BlockPos(entityliving)),
                                    (IEntityLivingData) null);
                            world1.spawnEntityInWorld(entity);
                        }
                    }
                }

                List<Entity> entities = world1.getLoadedEntityList();

                if (entities.size() > 0) {
                    for (int i = 0; i < entities.size(); i++) {
                        Entity entity = entities.get(i);
                        double speedTrailTime = 0;
                        if (flashFactor > 0 && flashFactor <= 16) {
                            speedTrailTime = 2.5;
                        } else if (flashFactor > 16 && flashFactor <= 32) {
                            speedTrailTime = 5;
                        } else if (flashFactor > 32 && flashFactor <= 48) {
                            speedTrailTime = 7.5;
                        } else if (flashFactor > 48 && flashFactor <= 64) {
                            speedTrailTime = 10;
                        }

                        if (entity instanceof EntitySpeedForceTrailBlue && entity.ticksExisted >= speedTrailTime) {
                            entity.setDead();
                        }
                    }
                }

                // speed trail yellow
                if (flashFactor >= 1 && !player.isSneaking() && player.moveForward > 0 && player.inventory
                        .armorItemInSlot(2).getItem().equals(FlashItems.chestPlateFlash) && !vibratingInvisible && !(
                        Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
                    for (int i = 0; i < 1; ++i) {
                        Entity entity = EntityList.createEntityByName("speed_force_trail_yellow", world1);

                        if (entity instanceof EntityLivingBase) {
                            EntityLiving entityliving = (EntityLiving) entity;
                            entity.setLocationAndAngles(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ,
                                    player.rotationYaw, player.rotationPitch);
                            entityliving.rotationYawHead = entityliving.rotationYaw;
                            entityliving.renderYawOffset = entityliving.rotationYaw;
                            entityliving.onInitialSpawn(world1.getDifficultyForLocation(new BlockPos(entityliving)),
                                    (IEntityLivingData) null);
                            world1.spawnEntityInWorld(entity);
                        }
                    }
                }

                List<Entity> entities2 = world1.getLoadedEntityList();

                if (entities2.size() > 0) {
                    for (int i = 0; i < entities2.size(); i++) {
                        Entity entity = entities2.get(i);
                        double speedTrailTime = 0;
                        if (flashFactor > 0 && flashFactor <= 16) {
                            speedTrailTime = 2.0;
                        } else if (flashFactor > 16 && flashFactor <= 32) {
                            speedTrailTime = 4.0;
                        } else if (flashFactor > 32 && flashFactor <= 48) {
                            speedTrailTime = 6.0;
                        } else if (flashFactor > 48 && flashFactor <= 64) {
                            speedTrailTime = 8.0;
                        }

                        if (entity instanceof EntitySpeedForceTrailYellow && entity.ticksExisted >= speedTrailTime) {
                            entity.setDead();
                        }
                    }
                }

                //future flash lightning
                if (player.moveForward == 0) {
                    if (flashFactor >= 1 && player.inventory.armorItemInSlot(2).getItem()
                            .equals(FlashItems.chestPlateFutureFlash) && !vibratingInvisible && !(
                            Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)) {
                        for (int i = 0; i < 1; ++i) {
                            Entity entity = EntityList.createEntityByName("future_flash_lightning", world1);

                            if (entity instanceof EntityLivingBase) {
                                EntityLiving entityliving = (EntityLiving) entity;
                                entity.setLocationAndAngles(player.lastTickPosX, player.lastTickPosY,
                                        player.lastTickPosZ, player.rotationYaw, player.rotationPitch);
                                entityliving.rotationYawHead = entityliving.rotationYaw;
                                entityliving.renderYawOffset = entityliving.rotationYaw;
                                entityliving.onInitialSpawn(world1.getDifficultyForLocation(new BlockPos(entityliving)),
                                        (IEntityLivingData) null);
                                // world1.spawnEntityInWorld(entity);
                            }
                        }
                    }
                }
                if (entities2.size() > 0) {
                    for (int i = 0; i < entities2.size(); i++) {
                        Entity entity = entities2.get(i);
                        double speedTrailTime = 1;

                        if (entity instanceof EntityFutureFlashLightning && entity.ticksExisted >= speedTrailTime) {
                            entity.setDead();
                        } else if (entity instanceof EntityFutureFlashLightning && player.moveForward > 0) {
                            entity.setDead();
                            entity.preventEntitySpawning = true;
                        } else {
                            entity.preventEntitySpawning = false;
                        }
                    }
                }
            } else {
                flashFactor = 0;
                SlowTimeHandler.setGameSpeed(1.0F);
                slowMoFactor = 0.0F;
                waterRunningUnlocked = false;
                wallRunningUnlocked = false;
                tornadoesAndVortexesUnlocked = false;
                phasingUnlocked = false;
                flyingUnlocked = false;
                betterFlyingUnlocked = false;
                lightningThrowUnlocked = false;
                invisibilityUnlocked = false;
                vibrating = false;
                vibratingInvisible = false;
                player.capabilities.isFlying = false;
            }
        } else {
            if (slowMoFactor == slowMoUpperLimit) {
                event.setCanceled(true);
            } else {
                event.setCanceled(false);
            }
        }
    }

    @SubscribeEvent
    public void pre(RenderPlayerEvent.Pre event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (vibratingInvisible) {
                event.setCanceled(true);
                player.setInvisible(true);
                player.setSilent(true);
            } else {
                event.setCanceled(false);
                player.setInvisible(false);
            }
        }
    }

    @SubscribeEvent
    public void breakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (flashFactor >= 1 && isFlash) {
                event.setNewSpeed(flashFactor * 1.5F);
            }
            if (flashFactor >= 1 && isFutureFlash) {
                event.setNewSpeed(flashFactor * 2F);
            }
        }
    }

    @SubscribeEvent
    public void fovUpdate(FOVUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (flashFactor >= 1) {
                event.setNewfov(2);
            }
        }
    }

    @SubscribeEvent
    public void playerHandEvent(RenderHandEvent event) {
        if (isFlash || isFutureFlash) {
            event.setCanceled(true);
            if (mc.thePlayer.getHeldItemMainhand() != null) {
                mc.getItemRenderer().renderItemInFirstPerson(event.getPartialTicks());
            }
        } else {
            event.setCanceled(false);
        }
    }

    //eventually perhaps
    /*
    @SubscribeEvent public void playerJumpEvent(LivingEvent.LivingJumpEvent event)
	{
		if (event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();

			if (isFutureFlash || isFlash)
			{
				if (flashFactor >= 1)
				{
					//stuff
				}
			}
		}
	}
	*/

    @SubscribeEvent
    public void playerFallEvent(LivingFallEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (flashFactor >= 1) {
                event.setCanceled(true);
            } else {
                event.setCanceled(false);
            }
        }
    }
}
