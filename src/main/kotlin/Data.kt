class Data(val animal:Int,val grass:Int,val energy:Int,val life:Int,val kids:Double) {
    fun toCSVRow():String{
        return "$animal,$grass,$energy,$life,$kids"
    }
}