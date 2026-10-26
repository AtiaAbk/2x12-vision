package bd.july2024.level;

/**
 * Lightweight Player Inventory for Project July 2024.
 * Tracks essential mission items (rally leaflets) and campus supplies (drinking water, food, first aid).
 */
public class PlayerInventory {

    private int waterCount = 2;
    private int foodCount = 1;
    private int leafletCount = 0;
    private int firstAidCount = 1;

    public PlayerInventory() {}

    public PlayerInventory(int water, int food, int leaflets, int firstAid) {
        this.waterCount = Math.max(0, water);
        this.foodCount = Math.max(0, food);
        this.leafletCount = Math.max(0, leaflets);
        this.firstAidCount = Math.max(0, firstAid);
    }

    /** Uses one water bottle to recover stamina. Returns true if consumed. */
    public boolean useWater() {
        if (waterCount > 0) {
            waterCount--;
            return true;
        }
        return false;
    }

    /** Uses one food item to restore stamina. Returns true if consumed. */
    public boolean useFood() {
        if (foodCount > 0) {
            foodCount--;
            return true;
        }
        return false;
    }

    public int getWaterCount() {
        return waterCount;
    }

    public void setWaterCount(int count) {
        this.waterCount = Math.max(0, count);
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int count) {
        this.foodCount = Math.max(0, count);
    }

    public int getLeafletCount() {
        return leafletCount;
    }

    public void setLeafletCount(int count) {
        this.leafletCount = Math.max(0, count);
    }

    public int getFirstAidCount() {
        return firstAidCount;
    }

    public void setFirstAidCount(int count) {
        this.firstAidCount = Math.max(0, count);
    }

    public boolean hasLeaflets() {
        return leafletCount > 0;
    }

    public void reset() {
        waterCount = 2;
        foodCount = 1;
        leafletCount = 0;
        firstAidCount = 1;
    }
}
