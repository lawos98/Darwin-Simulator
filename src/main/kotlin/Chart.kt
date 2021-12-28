import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart

class Chart(private val title:String) {
    //Class responsible for the graph and its display

    private var chart= LineChart(NumberAxis(), NumberAxis())
    private var series= XYChart.Series<Number,Number>()
    init {
        chart.createSymbols=false
        chart.data.add(series)
        chart.style = ".default-color0.chart-series-line { -fx-stroke: #f0e68c; }"
    }
    fun update(x:Int,y:Number){
        series.name= "$title :$y"
        series.data.add(XYChart.Data(x,y))
    }
    fun returnChart():LineChart<Number,Number>{
        return chart
    }
}