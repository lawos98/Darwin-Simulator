import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx


class Engine(private var map: IWorldMap, private var grid: GuiGrid,startAnimals:Int, private var numberOfGrass:Int, private var numberOfMagic:Int) {
    private var checkRun=true
    private var day:Int=0
    private var magicTry=0
    init {
        map.generateAnimalInJungle(startAnimals)
    }
    fun run() {
        GlobalScope.launch(Dispatchers.JavaFx) {
            while(checkRun){
                delay(1)
                day=map.getCurrentDay()
                map.generateGrass(numberOfGrass)
                map.moveAnimals()
                map.feedAnimals()
                map.clearDeadAnimals()
                map.procreationAnimals()
                grid.updateCharts(day)
                map.nextDay()
                grid.updateDays(day)
                if(map.getListAnimals().size==5 && magicTry<numberOfMagic){
                    magicTry+=1
                    map.doMagic(magicTry)
                }
            }

        }
    }
    fun block(){
        checkRun=false
    }
    fun unblock(){
        checkRun=true
    }
}
