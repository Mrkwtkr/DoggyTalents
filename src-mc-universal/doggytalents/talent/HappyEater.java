package doggytalents.talent;

import doggytalents.api.inferface.ITalent;
import doggytalents.entity.EntityDog;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

/**
 * @author ProPercivalalb
 */
public class HappyEater extends ITalent {

	@Override
	public int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) {
		int level = dog.talents.getLevel(this);
		if(foodValue == 0) {
	        if ((stack.getItem() == Items.FISH || stack.getItem() == Items.COOKED_FISH) && level == 5)
	        	foodValue = 30 + 3 * level;
	
	        if (stack.getItem() == Items.ROTTEN_FLESH && level >= 3)
	        	foodValue = 30 + 3 * level;
		}
		else {
			if(stack.getItem() != Items.ROTTEN_FLESH && stack.getItem() instanceof ItemFood) {
	            ItemFood itemfood = (ItemFood)stack.getItem();
	
	            if (itemfood.isWolfsFavoriteMeat())
	            	foodValue += 4 * level;
	        }
		}
		return foodValue; 
	}
	
	@Override
	public String getKey() {
		return "happyeater";
	}

}
