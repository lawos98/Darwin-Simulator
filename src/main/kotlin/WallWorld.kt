class WallWorld(width: Int, height: Int, jungleRatio: Int, startEnergy:Int, moveEnergy:Int, plantEnergy:Int, reproductionEnergy:Int) :AbstractWorld(width, height, jungleRatio, startEnergy,moveEnergy, plantEnergy,reproductionEnergy) {
    override fun canMoveTo(position: Vector2d): Boolean {
        return (position.follows(Vector2d(0,0)) && position.precedes(Vector2d(width,height)))
    }
    override fun convertToGoodVector(v: Vector2d): Vector2d {
        return v
    }
}