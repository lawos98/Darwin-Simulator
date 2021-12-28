class ComparatorsAnimal {
    class Comparator{
        companion object : java.util.Comparator<Animal> {
            override fun compare(V: Animal, U: Animal): Int {
                return U.getCurrentEnergy()-V.getCurrentEnergy()
            }
        }
    }
}
