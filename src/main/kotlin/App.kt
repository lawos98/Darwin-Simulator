import javafx.application.Application
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class App: Application() {
    private var choseMap=true
    private var blockInf=true
    private var blockWall=true
    override fun start(stage:Stage){

        //Section containing all elements of the initial view

        val mainBox=VBox()
        mainBox.alignment=Pos.CENTER

        val buttonStart=Button("Start")
        buttonStart.minWidth=300.0
        buttonStart.minHeight=20.0

        val lStartAnimal=Label("Please set start Animal")
        val inputStartAnimal=TextField("50")
        inputStartAnimal.maxWidth=300.0

        val lGrass=Label("Please set Grass generation per day")
        val inputGrass=TextField("1")
        inputGrass.maxWidth=300.0

        val lWidth=Label("Please set width")
        val inputWidth=TextField("50")
        inputWidth.maxWidth=300.0

        val lHeight=Label("Please set Height")
        val inputHeight=TextField("50")
        inputHeight.maxWidth=300.0

        val lJungleRatio=Label("Please set Jungle Ration [0-100]")
        val inputJungleRatio=TextField("50")
        inputJungleRatio.maxWidth=300.0

        val lStartEnergy=Label("Please set Start Energy")
        val inputStartEnergy=TextField("5000")
        inputStartEnergy.maxWidth=300.0

        val lMoveEnergy=Label("Please set Move Energy")
        val inputMoveEnergy=TextField("10")
        inputMoveEnergy.maxWidth=300.0

        val lPlantEnergy=Label("Please set Plant Energy")
        val inputPlantEnergy=TextField("1000")
        inputPlantEnergy.maxWidth=300.0

        val lReproductionEnergy=Label("Please set minimal energy to Reproduction")
        val inputReproductionEnergy=TextField("1000")
        inputReproductionEnergy.maxWidth=300.0

        val buttonMagic=Button("Do magic:false")
        var doMagic=false
        buttonMagic.setOnAction {
            doMagic=!doMagic
            buttonMagic.text="Do magic:$doMagic"
        }

        mainBox.children.addAll(buttonStart,lStartAnimal,inputStartAnimal,lGrass,inputGrass,lWidth,inputWidth,lHeight,inputHeight,lJungleRatio,inputJungleRatio,lStartEnergy,inputStartEnergy,lMoveEnergy,inputMoveEnergy,lPlantEnergy,inputPlantEnergy,lReproductionEnergy,inputReproductionEnergy,buttonMagic)


        buttonStart.setOnAction{

            //Map and engine launch section

            val mapInf=InfWorld(inputWidth.text.toInt()-1,inputHeight.text.toInt()-1,inputJungleRatio.text.toInt(),inputStartEnergy.text.toInt(),inputMoveEnergy.text.toInt(),inputPlantEnergy.text.toInt(),inputReproductionEnergy.text.toInt())
            val gridInf=mapInf.giveGrid()
            val mapWall=WallWorld(inputWidth.text.toInt()-1,inputHeight.text.toInt()-1,inputJungleRatio.text.toInt(),inputStartEnergy.text.toInt(),inputMoveEnergy.text.toInt(),inputPlantEnergy.text.toInt(),inputReproductionEnergy.text.toInt())
            val gridWall=mapWall.giveGrid()
            var numberOfMagic=0
            if(doMagic)numberOfMagic=3
            val engineInf=Engine(mapInf,gridInf,inputStartAnimal.text.toInt(),inputGrass.text.toInt(),numberOfMagic)
            val engineWall=Engine(mapWall,gridWall,inputStartAnimal.text.toInt(),inputGrass.text.toInt(),numberOfMagic)
            engineInf.run()
            engineWall.run()

            //Section containing all elements of the main view
            val simulationBox=VBox()
            val buttonBox=HBox()
            buttonBox.alignment=Pos.CENTER
            val buttonChangeMap=Button("Change Map to Inf")
            val buttonStop=Button("Stop/Start")
            val buttonExport=Button("Export Data to CSV")
            buttonBox.children.addAll(buttonChangeMap,buttonStop,buttonExport)
            simulationBox.children.addAll(buttonBox,gridWall.returnGrid())
            mainBox.children.clear()
            mainBox.children.add(simulationBox)
            mainBox.alignment=Pos.TOP_CENTER

            //Button functionality management section

            buttonChangeMap.setOnAction {
                simulationBox.children.clear()
                if(choseMap){
                    simulationBox.children.addAll(buttonBox,gridInf.returnGrid())
                    buttonChangeMap.text="Change Map to Wall"
                }
                else {
                    simulationBox.children.addAll(buttonBox,gridWall.returnGrid())
                    buttonChangeMap.text="Change Map to Inf"
                }
                choseMap=!choseMap
            }

            buttonStop.setOnAction{
                if(choseMap){
                    if(blockWall)engineWall.block()
                    else{
                        engineWall.unblock()
                        engineWall.run()
                    }
                    blockWall=!blockWall
                }
                else{
                    if(blockInf)engineInf.block()
                    else{
                        engineInf.unblock()
                        engineInf.run()
                    }
                    blockInf=!blockInf
                }
            }

            buttonExport.setOnAction {
                if(choseMap)mapInf.getStats().export(false)
                else mapWall.getStats().export(true)
            }
        }

        //Section activating the scene view
        val startScene=Scene(mainBox,1800.0,1000.0)
        stage.scene=startScene
        stage.isFullScreen=true
        stage.show()
    }
}