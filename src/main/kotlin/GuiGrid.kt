import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.shape.Circle

class GuiGrid(private var map: IWorldMap,private var stat:MapStats) {

    //Parameters of the whole grid and individual cells
    private val mWidth=800
    private val mHeight=800
    private val cellSize= minOf(mWidth/(map.rightCorner().x+1),mHeight/(map.rightCorner().y + 1)).toDouble()
    private var grid = GridPane()

    private var mainBox=HBox()
    private var box=VBox()

    //Field containing information about the current day
    private var daysLabel=readyLabel("day :0")

    //An array containing all the current fields in the grid
    private var itemArray = Array(map.rightCorner().x + 1) { Array(map.rightCorner().y + 1) { readyBox() } }

    //Section responsible for displaying graphs
    private var chartBox=VBox()
    private var chartContainer=HBox()
    //    Graph showing the amount of animals on the map
    private var liveAnimalChart= Chart("Animals")
    //    Graph showing the amount of grass on the map
    private var grassChart=Chart("Grass")
    //    Graph showing the average energy possessed by each living animal
    private var energyChart=Chart("Energy")
    //    Graph showing average life expectancy of animals
    private var lifeTimeChart=Chart("Life")
    //    Graph showing the average number of children for living individuals, but if both parents die, the child becomes an adult and is not counted as a child
    private var kidsChart=Chart("Kids")

    //    Section for the management of dominant animal genotypes
    private var genotypeGrid=GridPane()
    private var showMainGenotype=false

    //    Section responsible for tracking the value of the selected animal
    private var selectBox=VBox()
    private var selectAnimal:Animal?=null
    private var selectAnimalBox=GridPane()
    private var selectAnimalGenotype=readyLabel("none")
    private var selectAnimalKids=readyLabel("none")
    private var selectAnimalProgeny=readyLabel("none")
    private var selectAnimalDeath=readyLabel("none")

    //    Field responsible if and which time magic happened
    private var lDoMagic=readyLabel("")

    init {
        //setting the initial values for the selected animal
        selectBox.alignment=Pos.CENTER
        val buttonSelect=Button("Disable Select")
        buttonSelect.setOnAction {
            map.updateSelectAnimal(null)
            selectAnimal=null
            updateSelectAnimalBox(" - "," - "," - ")
            selectAnimalGenotype.text=" - "
        }
        selectBox.children.addAll(buttonSelect,selectAnimalBox)
        selectAnimalBox.alignment=Pos.CENTER
        selectAnimalBox.add(readyLabel("genotype:"),0,0)
        selectAnimalBox.add(selectAnimalGenotype,1,0)
        selectAnimalBox.add(readyLabel("kids:"),0,1)
        selectAnimalBox.add(selectAnimalKids,1,1)
        selectAnimalBox.add(readyLabel("progeny:"),0,2)
        selectAnimalBox.add(selectAnimalProgeny,1,2)
        selectAnimalBox.add(readyLabel("day of dead:"),0,3)
        selectAnimalBox.add(selectAnimalDeath,1,3)

        //setting the initial values for the genotype
        genotypeGrid.alignment=Pos.CENTER
        val mainGenotypeButton=Button("Show/Hide Main Genotype")
        mainGenotypeButton.alignment=Pos.CENTER
        mainGenotypeButton.setOnAction {
            showMainGenotype=!showMainGenotype
            updateMainGenotype()
        }

        //setting the buttons and initial values for the graphs
        val buttonAnimal=Button("Animal")
        val buttonGrass=Button("Grass")
        val buttonEnergy=Button("Energy")
        val buttonLife=Button("Life")
        val buttonKids=Button("Kids")
        val chartButtons=HBox()
        chartContainer.children.add(liveAnimalChart.returnChart())
        chartButtons.alignment=Pos.CENTER
        chartButtons.children.addAll(buttonAnimal,buttonGrass,buttonEnergy,buttonLife,buttonKids)

        buttonAnimal.setOnAction{
            chartContainer.children.clear()
            chartContainer.children.add(liveAnimalChart.returnChart())
        }
        buttonGrass.setOnAction{
            chartContainer.children.clear()
            chartContainer.children.add(grassChart.returnChart())
        }
        buttonEnergy.setOnAction{
            chartContainer.children.clear()
            chartContainer.children.add(energyChart.returnChart())
        }
        buttonLife.setOnAction{
            chartContainer.children.clear()
            chartContainer.children.add(lifeTimeChart.returnChart())
        }
        buttonKids.setOnAction{
            chartContainer.children.clear()
            chartContainer.children.add(kidsChart.returnChart())
        }

        chartBox.children.addAll(chartButtons,chartContainer,genotypeGrid,mainGenotypeButton)
        chartBox.alignment=Pos.CENTER

        mainBox.children.addAll(chartBox,grid,selectBox)
        mainBox.alignment=Pos.CENTER

        box.spacing=20.0
        box.alignment=Pos.CENTER
        box.children.addAll(daysLabel,lDoMagic,mainBox)

        daysLabel.style=("-fx-border-color:black;")

        //setting styles for a grid where green fields are jungles and brown fields are steppes
        grid.alignment = Pos.CENTER
        grid.isGridLinesVisible = true
        for (i in itemArray.indices) {
            for (j in itemArray[i].indices) {
                if (map.isJungle(Vector2d(i, j))) {
                    itemArray[i][j].style = ("-fx-border-color:green; -fx-background-color: darkgreen;")
                }
                else{
                    itemArray[i][j].style = ("-fx-border-color:brown; -fx-background-color: DARKGOLDENROD;")
                }
                grid.add(itemArray[i][j], i, j)
            }
        }
    }

    //functions returning stylised containers
    private fun readyBox(): VBox {
        val box = VBox()
        box.alignment = Pos.CENTER
        box.minHeight = cellSize
        box.minWidth = cellSize
        return box
    }
    private fun readyLabel(text:String):Label{
        val l=Label(text)
        l.alignment=Pos.CENTER
        l.minWidth=100.0
        l.minHeight=20.0
        return l
    }

    //function that styles the field if there is an animal in it, depending on the position on the map and the
    // amount of energy (if the circle is full the animal can survive for more than 100 days,
    // otherwise it starts to feel hungry and feels empty)
    fun setBoxAnimal(v:Vector2d,animal:Animal) {
        val energyPercent=animal.getEnergyPercent()
        val circleBox=StackPane()
        val circle = Circle(cellSize/4)
        if(stat.isItMainGenotype(animal) && showMainGenotype){
            circle.fill=javafx.scene.paint.Color.SKYBLUE
        }
        else circle.fill=javafx.scene.paint.Color.BLACK
        val energyCircle=Circle((energyPercent*cellSize)/4)
        if(map.isJungle(v)){
            energyCircle.fill=javafx.scene.paint.Color.DARKGREEN
        }
        else{
            energyCircle.fill=javafx.scene.paint.Color.DARKGOLDENROD
        }
        circleBox.children.addAll(circle,energyCircle)
        itemArray[v.x][v.y].children.add(circleBox)
        circleBox.setOnMouseClicked {
            selectAnimalGenotype.text=animal.getGenotype()
            map.updateSelectAnimal(animal)
            selectAnimalKids.text=" - "
            selectAnimalProgeny.text=" - "
            selectAnimalDeath.text=" - "
        }
    }

    //function that styles a field if there is a plant in it
    fun setBoxGrass(v:Vector2d) {
        val circle = Circle(cellSize/6)
        circle.fill=javafx.scene.paint.Color.GREENYELLOW
        itemArray[v.x][v.y].children.add(circle)
    }

    fun returnGrid(): VBox {
        return box
    }
    fun clearBox(v:Vector2d){
        itemArray[v.x][v.y].children.clear()
    }
    fun updateCharts(day:Int){
        val data=stat.getDataStats()
        liveAnimalChart.update(day,data.animal)
        grassChart.update(day,data.grass)
        energyChart.update(day,data.energy)
        lifeTimeChart.update(day,data.life)
        kidsChart.update(day,data.kids)
        setGenotypeGrid()
    }

    //a function displaying the 10 most common animal genotypes
    private fun setGenotypeGrid(){
        var index=0
        genotypeGrid.children.clear()
        val tab=stat.getGenotypePriorityQueue()
        while(tab.isNotEmpty() && index<10){
            genotypeGrid.add(readyLabel(tab.peek().string),0,index)
            genotypeGrid.add(readyLabel(tab.remove().number.toString()),1,index)
            index+=1
        }
    }
    fun updateDays(day:Int){
        daysLabel.text="day :$day"
    }

    //function to style all fields containing animals with the dominant genotype
    private fun updateMainGenotype(){
        val fieldList=map.fieldList
        for(key in fieldList.keys){
            val field=fieldList[key]
            if(field!=null && field.isItAnimals()){
                val animalList=field.getArrayAnimals()
                for(creature in animalList){
                    if(stat.isItMainGenotype(creature)){
                        clearBox(creature.position)
                        setBoxAnimal(creature.position,creature)
                        break
                    }
                }
            }
        }
    }
    //function updating data on the selected animal
    fun updateSelectAnimalBox(kids:String, progeny:String, death:String){
        selectAnimalKids.text=kids
        selectAnimalProgeny.text=progeny
        selectAnimalDeath.text=death
    }
    fun doMagic(number:Int){
        lDoMagic.text="Magic $number is dome!"
    }
}

