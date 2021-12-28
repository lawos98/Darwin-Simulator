class ComparatorsGenotype {
    class Comparator{
        companion object : java.util.Comparator<GenoType> {
            override fun compare(V: GenoType, U: GenoType): Int {
                return U.number-V.number
            }
        }
    }
}