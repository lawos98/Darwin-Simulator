import java.util.concurrent.ConcurrentHashMap


interface IWorldMap {
    var fieldList:ConcurrentHashMap<Vector2d, Field>

    fun place(animal: Animal)

    fun canMoveTo(position: Vector2d): Boolean

    fun generateGrass(number:Int)

    fun updatePosition(start:Vector2d,creature: Animal)
    fun changePosition(start: Vector2d, end: Vector2d, creature: Animal)

    fun isJungle(position:Vector2d):Boolean

    fun convertToGoodVector(v:Vector2d):Vector2d

    fun getListAnimals():MutableList<Animal>

    fun moveAnimals()

    fun clearDeadAnimals()

    fun feedAnimals()

    fun procreationAnimals()

    fun generateAnimalInJungle(count:Int)

    fun rightCorner():Vector2d

    fun leftCorner():Vector2d

    fun returnMoveEnergy():Int

    fun giveGrid():GuiGrid

    fun getStats():MapStats

    fun getCurrentDay():Int

    fun nextDay()

    fun updateSelectAnimal(animal: Animal?)

    fun doMagic(number:Int)
}