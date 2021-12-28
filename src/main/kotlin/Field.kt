import java.util.*

class Field {
    //A class representing one field on the map in which there may be animals or plants

    private var inAnimals=PriorityQueue(ComparatorsAnimal.Comparator)
    private var inGrass: Grass? =null

    override fun toString(): String {
        var result=""
        result += if(isItGrass()) "G"
        else "-"
        result+="|"
        result+=inAnimals.size.toString()
        return result
    }
    fun placeAnimal(creature: Animal){
        inAnimals.add(creature)
    }
    fun placeGrass(grass: Grass){
        inGrass=grass
    }
    fun removeAnimal(creature: Animal){
        inAnimals.remove(creature)
    }
    fun isItGrass():Boolean{
        return inGrass!=null
    }
    fun removeGrass(){
        inGrass=null
    }
    fun isItAnimals():Boolean{
        return inAnimals.isNotEmpty()
    }
    fun getFirstAnimal():Animal{
        return inAnimals.remove()
    }
    fun checkFirstAnimal():Animal{
        return inAnimals.peek()
    }
    fun getArrayAnimals():Array<Animal>{
        return inAnimals.toTypedArray()
    }
    fun isItTwoAnimals():Boolean{
        return inAnimals.size>=2
    }
}