import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class MapStats {
    //a class containing statistics about maps
    private var day:Int=0
    private var genoTypeList= ConcurrentHashMap<String,Int>()
    private var numberOfGrass=0
    private var numberOfAnimals=0
    private var averageEnergy=0
    private var numberDeadAnimals=0
    private var numberOfDaysDeadAnimals=0
    private var numberOfKids=0
    private var mainGenoType=""

    private var selectAnimal:Animal?=null
    private var selectAnimalKids=0
    private var selectAnimalProgeny=0
    private var selectAnimalDeath:Int?=null

    private var listData= mutableListOf<Data>()

    private fun numberOfDaysDeadAnimalsPerAnimal():Int{
        if(numberDeadAnimals==0)return 0
        return numberOfDaysDeadAnimals/numberDeadAnimals
    }

    private fun numberOfKidsPerAnimal():Double{
        if(numberOfAnimals==0)return 0.0
        return (numberOfKids*100/numberOfAnimals).toDouble()/100
    }
    fun getDataStats():Data{
        val data=Data(numberOfAnimals,numberOfGrass,averageEnergy,numberOfDaysDeadAnimalsPerAnimal(),numberOfKidsPerAnimal())
        listData.add(data)
        return data
    }

    fun getGenotypePriorityQueue(): PriorityQueue<GenoType> {
        val result= PriorityQueue(ComparatorsGenotype.Comparator)
        for(key in genoTypeList.keys){
            val number=genoTypeList[key]
            if(number!=null){
                val currentGenotype=GenoType(key,number)
                result.add(currentGenotype)
            }
        }
        if(result.isNotEmpty())mainGenoType=result.peek().string
        return result
    }
    fun getGenotypeList():ConcurrentHashMap<String,Int>{
        return genoTypeList
    }
    fun changeNumberOfAnimal(v:Int){
        numberOfAnimals+=v
    }
    fun changeNumberOfGrass(v:Int){
        numberOfGrass+=v
    }
    fun checkMainGenotype(gen:String):Boolean{
        return gen==mainGenoType
    }
    fun animalIsEmpty():Boolean{
        return numberOfAnimals==0
    }
    fun setAvgEnergy(v:Int){
        averageEnergy=v
    }
    fun calculateAvgEnergy(v:Int){
        averageEnergy=v/numberOfAnimals
    }
    fun isItSelectAnimal(creature:Animal):Boolean{
        return creature==selectAnimal
    }
    fun updateSelectAnimalDeath(v:Int){
        selectAnimalDeath=v
    }
    fun changeNumberOfDeadAnimals(v:Int){
        numberDeadAnimals+=v
    }
    fun changeNumberOfDaysOfDeadAnimals(v:Int){
        numberOfDaysDeadAnimals+=v
    }
    fun changeNumberOfKids(v:Int){
        numberOfKids+=v
    }
    fun changeSelectAnimalProgeny(v:Int){
        selectAnimalProgeny+=v
    }
    fun changeSelectAnimalKid(v:Int){
        selectAnimalKids+=v
    }
    fun returnSelectAnimalKids():String{
        return selectAnimalKids.toString()
    }
    fun returnSelectAnimalProgeny():String{
        return selectAnimalProgeny.toString()
    }
    fun returnSelectAnimalDeath():String{
        return selectAnimalDeath.toString()
    }
    fun isItMainGenotype(creature: Animal):Boolean{
        return creature.getGenotype()==mainGenoType
    }
    fun getDay():Int{
        return day
    }
    fun nextDay(){
        day+=1
    }
    fun setSelectAnimal(animal: Animal?){
        selectAnimalKids=0
        selectAnimalProgeny=0
        selectAnimalDeath=null
        selectAnimal=animal
    }
    fun export(path:Boolean) {
        val file = if(path) File("reportInfWorld.csv")
        else File("reportWallWorld.csv")

        val printWriter = file.bufferedWriter()
        val csvPrinter=CSVPrinter(printWriter, CSVFormat.DEFAULT.withHeader("animal,grass,energy,life,kids"))

        var avgAnimal=0
        var avgGrass=0
        var avgEnergy=0
        var avgLife=0
        var avgKids=0.0
        var count=0

        for(data in listData){
            csvPrinter.printRecord(data.toCSVRow())
            avgAnimal+=data.animal
            avgGrass+=data.grass
            avgEnergy+=data.energy
            avgLife+=data.life
            avgKids+=data.kids
            count+=1
        }
        csvPrinter.printRecord("AVG-animal,AVG-grass,AVG-energy,AVG-life,AVG-kids")
        if(count==0)csvPrinter.printRecord("none,none,none,none,none")
        else {
            val data=Data(avgAnimal/count,avgGrass/count,avgEnergy/count,avgLife/count,avgKids/count)
            csvPrinter.print(data.toCSVRow())
        }
        csvPrinter.flush()
    }
}
