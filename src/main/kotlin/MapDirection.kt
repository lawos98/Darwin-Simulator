enum class MapDirection {
    //Class specifying the direction of the animal

    North{
        override fun convertToString():String{
            return "North"
        }
        override fun next(): MapDirection {
            return NorthEast
        }
        override fun previous(): MapDirection {
            return NorthWest
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(0, 1)
        }
    },
    NorthEast{
        override fun convertToString():String {
            return "NorthEast"
        }
        override fun next(): MapDirection {
            return East
        }
        override fun previous(): MapDirection {
            return North
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(1, 1)
        }
    },
    East{
        override fun convertToString():String{
            return "East"
        }
        override fun next(): MapDirection {
            return SouthEast
        }
        override fun previous(): MapDirection {
            return NorthEast
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(1, 0)
        }
    },
    SouthEast{
        override fun convertToString():String{
            return "SouthEast"
        }
        override fun next(): MapDirection {
            return South
        }
        override fun previous(): MapDirection {
            return East
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(1, -1)
        }
    },
    South{
        override fun convertToString():String{
            return "South"
        }
        override fun next(): MapDirection {
            return SouthWest
        }
        override fun previous(): MapDirection {
            return SouthEast
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(0, -1)
        }
    },
    SouthWest{
        override fun convertToString():String{
            return "SouthWest"
        }
        override fun next(): MapDirection {
            return West
        }
        override fun previous(): MapDirection {
            return South
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(-1, -1)
        }
    },
    West{
        override fun convertToString():String{
            return "West"
        }
        override fun next(): MapDirection {
            return NorthWest
        }
        override fun previous(): MapDirection {
            return SouthWest
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(-1, 0)
        }
    },
    NorthWest{
        override fun convertToString():String{
            return "NorthWest"
        }
        override fun next(): MapDirection {
            return North
        }
        override fun previous(): MapDirection {
            return West
        }
        override fun toUnitVector(): Vector2d {
            return Vector2d(-1, 1)
        }
    };
    abstract fun convertToString():String
    abstract fun next(): MapDirection
    abstract fun previous(): MapDirection
    abstract fun toUnitVector(): Vector2d

    fun moveUntilRight(count:Int):MapDirection{
        var result=this
        for(i in 0 until count){
            result=result.next()
        }
        return result
    }
    fun moveUntilLeft(count:Int):MapDirection{
        var result=this
        for(i in 0 until count){
            result=result.previous()
        }
        return result
    }
}