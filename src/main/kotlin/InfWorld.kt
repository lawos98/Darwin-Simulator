class InfWorld(width: Int, height: Int, jungleRatio: Int, startEnergy:Int, moveEnergy:Int, plantEnergy:Int, reproductionEnergy:Int) :AbstractWorld(width, height, jungleRatio, startEnergy,moveEnergy, plantEnergy,reproductionEnergy) {
    override fun canMoveTo(position: Vector2d): Boolean {
        return true
    }

    override fun convertToGoodVector(v: Vector2d): Vector2d {
        return Vector2d((width+1+v.x)%(width+1),(height+1+v.y)%(height+1))
    }
}
