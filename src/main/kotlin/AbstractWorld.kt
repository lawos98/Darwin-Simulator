import java.util.concurrent.ConcurrentHashMap

abstract class AbstractWorld(val width: Int, val height: Int, jungleRatio: Int, private val starEnergy: Int, private val moveEnergy: Int, private val plantEnergy: Int, private val reproductionEnergy: Int) : IWorldMap {

    override var fieldList = ConcurrentHashMap<Vector2d, Field>()
    private val lowerCornerJungle = Vector2d(width * (100 - jungleRatio) / 200, height * (100 - jungleRatio) / 200)
    private val upperCornerJungle = Vector2d(width, height) - lowerCornerJungle

    //Lists that store unoccupied fields
    private val clearInJungle = mutableListOf<Vector2d>()
    private val clearOutJungle = mutableListOf<Vector2d>()

    private val stat=MapStats()
    private val grid =GuiGrid(this,stat)

    //filling the lists with unoccupied fields
    init {
        for (x in (0..width)) {
            for (y in (0..height)) {
                if (x in (lowerCornerJungle.x..upperCornerJungle.x) && y in (lowerCornerJungle.y..upperCornerJungle.y)) {
                    clearInJungle.add(Vector2d(x, y))
                } else {
                    clearOutJungle.add((Vector2d(x, y)))
                }
            }
        }
    }

    //generating random animals in the jungle
    override fun generateAnimalInJungle(count: Int) {
        fun randomGenotype(): String {
            val tab = mutableListOf<Int>()
            val path=(0..1).random()
            if(path==1){
                var type = 0
                for (i in (0..7)) {
                    tab.add((type..31).random())
                    type = tab.last()
                }
            }
            else{
                var type=31
                for (i in (0..7).reversed()) {
                    tab.add(0,(0..type).random())
                    type = tab.first()
                }
            }
            var currentValue = 0
            var result = ""
            for (i in (0..31)) {
                if (currentValue<tab.size && i >= tab[currentValue]) {
                    currentValue += 1
                }
                result += currentValue.toString()
            }
            return result
        }

        for (i in 0 until count) {
            val randomPosition = Vector2d(
                (lowerCornerJungle.x..upperCornerJungle.x).random(),
                (lowerCornerJungle.y..upperCornerJungle.y).random()
            )
            val genotype=randomGenotype()
            val genoTypeList=stat.getGenotypeList()
            if(genoTypeList.containsKey(genotype)){
                val temp=genoTypeList[genotype]
                if(temp!=null){
                    genoTypeList[genotype]=temp.toInt()+1
                }
            }
            else{
                genoTypeList[genotype]=1
            }
            val creature = Animal(this, randomPosition, genotype, starEnergy,null,null)
            creature.growUp()
            stat.changeNumberOfAnimal(1)
            place(creature)
        }
    }
    //generating copy of animals(we recognize that they are not real children like their prototypes)
    override fun doMagic(number:Int) {
        grid.doMagic(number)
        val animalList=getListAnimals()
        for(creature in animalList){
            if(clearInJungle.isNotEmpty() && clearOutJungle.isNotEmpty()) {
                val randomChose = (0..1).random()
                val randomPosition = if (randomChose == 1 && clearInJungle.isNotEmpty()) clearInJungle.random()
                else clearOutJungle.random()
                val genotypeList=stat.getGenotypeList()
                val temp = genotypeList[creature.getGenotype()]
                if (temp != null) genotypeList[creature.getGenotype()] = temp.toInt() + 1
                val newAnimal = Animal(this, randomPosition, creature.getGenotype(), starEnergy, null, null)
                place(newAnimal)
                newAnimal.growUp()
                stat.changeNumberOfAnimal(1)
            }
        }
    }
    //Positioning the animal on the map
    override fun place(animal: Animal) {
        val vAnimal = animal.returnPosition()
        if (fieldList.containsKey(vAnimal)) {
            val currentField = fieldList[vAnimal]
            currentField?.placeAnimal(animal)
        } else {
            val cell = Field()
            cell.placeAnimal(animal)
            fieldList[vAnimal] = cell
            grid.setBoxAnimal(animal.returnPosition(), animal)
            if (isJungle(animal.returnPosition())) {
                clearInJungle.remove(animal.returnPosition())
            } else {
                clearOutJungle.remove(animal.returnPosition())
            }
        }
    }

    //Updating the energy and dominant genotype
    override fun updatePosition(start: Vector2d, creature: Animal) {
        val field=fieldList[start]
        var check=true
        if(field!=null){
            if(stat.checkMainGenotype(creature.getGenotype())){
                grid.clearBox(start)
                grid.setBoxAnimal(start, creature)
            }
            else{
                val creatureList=field.getArrayAnimals()
                for(c in creatureList){
                    if(stat.checkMainGenotype(c.getGenotype())){
                        check=false
                        break
                    }
                }
                if(field.checkFirstAnimal()==creature && check){
                    grid.clearBox(start)
                    grid.setBoxAnimal(start, creature)
                }
            }
        }
    }

    //Updating the position of the animal
    override fun changePosition(start: Vector2d, end: Vector2d, creature: Animal) {
        val startField = fieldList[start]
        if (startField != null) {
            startField.removeAnimal(creature)
            if (!startField.isItAnimals()) {
                grid.clearBox(start)
                fieldList.remove(start)
                if (isJungle(start)) {
                    clearInJungle.add(start)
                } else {
                    clearOutJungle.add(start)
                }
            }
            if (fieldList.containsKey(end)) {
                val endField = fieldList[end]
                if (endField != null) {
                    endField.placeAnimal(creature)
                    updatePosition(end,creature)
                }
            } else {
                grid.setBoxAnimal(end, creature)
                val c = Field()
                c.placeAnimal(creature)
                fieldList[end] = c
                if (isJungle(end)) {
                    clearInJungle.remove(end)
                } else {
                    clearOutJungle.remove(end)
                }
            }
        }
    }

    //Checking whether a field is in the jungle
    override fun isJungle(position: Vector2d): Boolean {
        if (position.follows(lowerCornerJungle) && position.precedes(upperCornerJungle)) return true
        return false
    }


    //generation of grass in case of lack of space the functionality is omitted
    override fun generateGrass(number:Int) {
        for(i in 0 until number) {
            if (clearInJungle.isNotEmpty()) {
                val grassInJunglePosition = clearInJungle.random()
                grid.setBoxGrass(grassInJunglePosition)
                val cellIn = Field()
                cellIn.placeGrass(Grass(grassInJunglePosition))
                fieldList[grassInJunglePosition] = cellIn
                clearInJungle.remove(grassInJunglePosition)
                stat.changeNumberOfGrass(1)
            }
            if (clearOutJungle.isNotEmpty()) {
                val grassOutJunglePosition = clearOutJungle.random()
                grid.setBoxGrass(grassOutJunglePosition)
                val cellOut = Field()
                cellOut.placeGrass(Grass(grassOutJunglePosition))
                fieldList[grassOutJunglePosition] = cellOut
                clearOutJungle.remove(grassOutJunglePosition)
                stat.changeNumberOfGrass(1)
            }
        }
    }

    //Returning a List of All Animals
    override fun getListAnimals(): MutableList<Animal> {
        val listAnimals = mutableListOf<Animal>()
        for (key in fieldList.keys) {
            val currentField = fieldList[key]
            if (currentField != null) {
                if (currentField.isItAnimals()) {
                    listAnimals += currentField.getArrayAnimals()
                }
            }
        }
        return listAnimals
    }

    //Function that moves all animals
    override fun moveAnimals() {
        var numberOfEnergy=0
        val listAnimal = getListAnimals()
        for (creature in listAnimal) {
            creature.move()
            creature.feed(-moveEnergy)
            numberOfEnergy+=creature.getCurrentEnergy()
        }
        if(stat.animalIsEmpty())stat.setAvgEnergy(0)
        else stat.calculateAvgEnergy(numberOfEnergy)

    }

    //Cleaning function for animal carcasses with an energy equal to 1
    override fun clearDeadAnimals() {
        var listAnimal = getListAnimals()
        for (creature in listAnimal) {
            if (creature.getCurrentEnergy() <= 0) {
                if(stat.isItSelectAnimal(creature))stat.updateSelectAnimalDeath(getCurrentDay())
                stat.changeNumberOfDeadAnimals(1)
                stat.changeNumberOfDaysOfDeadAnimals(creature.getLifeDays())
                stat.changeNumberOfAnimal(-1)
                if (!creature.isGrowUp()){
                    stat.changeNumberOfKids(-1)
                }
                val currentField = fieldList[creature.returnPosition()]
                val genotypeList=stat.getGenotypeList()
                val gen=genotypeList[creature.getGenotype()]
                if(gen!=null) {
                    if (gen == 1) {
                        genotypeList.remove(creature.getGenotype())
                    } else {
                        val temp=genotypeList[creature.getGenotype()]
                        if(temp!=null){
                            genotypeList[creature.getGenotype()]=temp.toInt()-1
                        }
                    }
                }
                if (currentField != null) {
                    currentField.removeAnimal(creature)
                    if (!currentField.isItAnimals()) {
                        grid.clearBox(creature.returnPosition())
                        fieldList.remove(creature.returnPosition())
                        if (isJungle(creature.returnPosition())) {
                            clearInJungle.add(creature.returnPosition())
                        } else {
                            clearOutJungle.add(creature.returnPosition())
                        }
                    }
                }
            }
        }
        listAnimal = getListAnimals()
        for (creature in listAnimal) {
            if(creature.returnMom()!=null && creature.returnDad()!=null){
                if (!listAnimal.contains(creature.returnMom()) && !listAnimal.contains(creature.returnDad()) && !creature.isGrowUp()){
                    stat.changeNumberOfKids(-1)
                    creature.growUp()
                }
            }
        }
    }

    //Animal multiplication function
    override fun procreationAnimals() {
        for (key in fieldList.keys) {
            val currentField = fieldList[key]
            if (currentField != null) {
                var checkToProcreate = currentField.isItTwoAnimals()
                while (checkToProcreate) {
                    val creatureFirst = currentField.getFirstAnimal()
                    val creatureSecond = currentField.getFirstAnimal()
                    if (creatureFirst.getCurrentEnergy() >= reproductionEnergy && creatureSecond.getCurrentEnergy() >= reproductionEnergy) {
                        if(creatureFirst.isObservable() || creatureSecond.isObservable())stat.changeSelectAnimalProgeny(1)
                        if(stat.isItSelectAnimal(creatureFirst) || stat.isItSelectAnimal(creatureSecond)){
                            stat.changeSelectAnimalProgeny(1)
                            stat.changeSelectAnimalKid(1)
                        }
                        stat.changeNumberOfAnimal(1)
                        stat.changeNumberOfKids(1)
                        val kid=creatureFirst.reproduction(creatureSecond)
                        val genoTypeList=stat.getGenotypeList()
                        if(genoTypeList.containsKey(kid.getGenotype())){
                            val temp=genoTypeList[kid.getGenotype()]
                            if(temp!=null){
                                genoTypeList[kid.getGenotype()]=temp.toInt()+1
                            }
                        }
                        else{
                            genoTypeList[kid.getGenotype()]=1
                        }
                    } else {
                        checkToProcreate = false
                    }
                    currentField.placeAnimal(creatureFirst)
                    currentField.placeAnimal(creatureSecond)
                }
            }
        }
        grid.updateSelectAnimalBox(stat.returnSelectAnimalKids(),stat.returnSelectAnimalProgeny(),stat.returnSelectAnimalDeath())
    }

    //Animal feeding function
    override fun feedAnimals() {
        for (key in fieldList.keys) {
            val currentField = fieldList[key]
            if (currentField != null && currentField.isItAnimals() && currentField.isItGrass()) {
                stat.changeNumberOfGrass(-1)
                val creature = currentField.getFirstAnimal()
                val creatureList = arrayListOf(creature)
                while (currentField.isItAnimals() && creature.getCurrentEnergy() == currentField.checkFirstAnimal()
                        .getCurrentEnergy()
                ) {
                    creatureList.add(currentField.getFirstAnimal())
                }
                currentField.removeGrass()
                for (animal in creatureList) {
                    animal.feed(plantEnergy / creatureList.size)
                    currentField.placeAnimal(animal)
                }
            }
        }
    }

    override fun rightCorner(): Vector2d {
        return Vector2d(width, height)
    }

    override fun leftCorner(): Vector2d {
        return Vector2d(0, 0)
    }

    override fun returnMoveEnergy(): Int {
        return moveEnergy
    }

    override fun giveGrid(): GuiGrid {

        return grid
    }

    override fun getStats():MapStats{
        return stat
    }

    override fun getCurrentDay():Int{
        return stat.getDay()
    }

    override fun nextDay(){
        stat.nextDay()
    }
    //Function indicating a new selected animal
    override fun updateSelectAnimal(animal: Animal?){
        val animalList=getListAnimals()
        for(c in animalList){
            if(c.isObservable())c.changeObserver()
        }
        stat.setSelectAnimal(animal)
        if(animal!=null){
            animal.changeObserver()
        }
    }
}