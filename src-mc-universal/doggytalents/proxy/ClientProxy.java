package doggytalents.proxy;

import java.util.Random;

import doggytalents.base.ObjectLib;
import doggytalents.base.ObjectLibClient;
import doggytalents.base.VersionControl;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.entity.EntityDog;
import doggytalents.handler.KeyState;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ObjectLibClient.INITIALIZATION.preInit(event);
		
		ClientRegistry.registerKeyBinding(KeyState.come);
		ClientRegistry.registerKeyBinding(KeyState.stay);
		ClientRegistry.registerKeyBinding(KeyState.ok);
		ClientRegistry.registerKeyBinding(KeyState.heel);
		
		RenderingRegistry.registerEntityRenderingHandler(ObjectLib.ENTITY_DOG_CLASS, RenderDog::new);
		RenderingRegistry.registerEntityRenderingHandler(ObjectLib.ENTITY_DOGGY_BEAM_CLASS, RenderDogBeam::new);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		ObjectLibClient.INITIALIZATION.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		ObjectLibClient.INITIALIZATION.postInit(event);
	}
	
	@Override
    protected void registerEventHandlers() {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(VersionControl.createObject("WorldRender"));
		MinecraftForge.EVENT_BUS.register(new KeyState());
    }
	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		if(ID == GUI_ID_DOGGY) {
			Entity target = player.world.getEntityByID(x);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			return new GuiDogInfo(dog, player);
		}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.world.getEntityByID(x);
            if(!(target instanceof EntityDog)) 
            	return null;
			EntityDog dog = (EntityDog)target;
			return ObjectLibClient.createGuiPackPuppy(player, dog);
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			TileEntity target = ObjectLib.BRIDGE.getTileEntity(world, x, y, z);
			if(!(target instanceof TileEntityFoodBowl))
				return null;
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
			return ObjectLibClient.createGuiFoodBowl(player.inventory, foodBowl);
		}
		else if(ID == GUI_ID_FOOD_BAG) {
			return ObjectLibClient.createGuiTreatBag(player, x, player.inventory.getStackInSlot(x));
		}
		return null;
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
	
	@Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}
	
	@Override
	public void spawnCrit(World world, Entity entity) {
		FMLClientHandler.instance().getClient().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
	}
	
	@Override
	public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {
		ObjectLibClient.METHODS.spawnCustomParticle(player, pos, rand, posX, posY, posZ, numberOfParticles, particleSpeed);
	}
}
