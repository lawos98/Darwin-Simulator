import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class Animal(private val map: IWorldMap,position: Vector2d, private val genotype:String, private var energy:Int,private val mom:Animal?,private val dad:Animal?):AbstractWorldMapElement(position){
    //The values of the mum and dad fields specify both parents of the animal

    private var orientation: MapDirection = MapDirection.values()[Random.nextInt(MapDirection.values().size)]
    private var daysOfLife:Int=0
    //The value of the adulthood field determines whether the animal is an adult, which means that both of its parents are dead
    private var mature:Boolean=false

    //Value indicates whether the animal is currently being tracked as a child or descendant of the observed animal
    private var selectObserver:Boolean=false

    fun feed(energy: Int){
        this.energy+=energy
    }

    fun getCurrentEnergy(): Int {
        return this.energy
    }

    fun getEnergyPercent():Double{
        return min(max(1-energy.toDouble()/(100*map.returnMoveEnergy()),0.01),0.9)
    }

    fun getGenotype():String{
        return genotype
    }

    fun getLifeDays():Int{
        return daysOfLife
    }
    fun growUp(){
        mature=true
    }
    fun isGrowUp():Boolean{
        return mature
    }
    fun returnMom():Animal?{
        return mom
    }
    fun returnDad():Animal?{
        return dad
    }
    fun changeObserver(){
        selectObserver=!selectObserver
    }
    fun isObservable():Boolean{
        return selectObserver
    }

    fun move() {
        daysOfLife+=1
        when (genotype.random()) {
            '0' -> {
                if (map.canMoveTo(position + orientation.toUnitVector())) {
                    map.changePosition(position,map.convertToGoodVector(position + orientation.toUnitVector()),this)
                    position =map.convertToGoodVector(position+orientation.toUnitVector())
                }
            }
            '1' -> {orientation.moveUntilRight(1);map.updatePosition(position,this)}
            '2' -> {orientation.moveUntilRight(2);map.updatePosition(position,this)}
            '3' -> {orientation.moveUntilRight(3);map.updatePosition(position,this)}
            '4' -> {
                if (map.canMoveTo(position - orientation.toUnitVector())) {
                    map.changePosition(position,map.convertToGoodVector(position - orientation.toUnitVector()),this)
                    position =map.convertToGoodVector(position-orientation.toUnitVector())
                }
            }
            '5' -> {orientation.moveUntilLeft(3);map.updatePosition(position,this)}
            '6' -> {orientation.moveUntilLeft(2);map.updatePosition(position,this)}
            '7' -> {orientation.moveUntilLeft(1);map.updatePosition(position,this)}
        }
    }
    fun reproduction(secondAnimal:Animal):Animal{
        val indexToCut=(this.energy*this.genotype.length/(this.energy+secondAnimal.energy))
        val r:Int=(0..1).random()
        val genoFirst:String
        val genoSecond:String
        if(r==0){
            genoFirst=genotype.substring(0,indexToCut)
            genoSecond=secondAnimal.getGenotype().substring(indexToCut,secondAnimal.genotype.length)
        }
        else{
            genoSecond=secondAnimal.getGenotype().substring(0,indexToCut)
            genoFirst=genotype.substring(indexToCut,this.genotype.length)
        }
        var indexFirst=0
        var indexSecond=0
        var genoKid=""
        while(indexFirst<genoFirst.length && indexSecond<genoSecond.length){
            if(genoFirst[indexFirst]<genoSecond[indexSecond]){
                genoKid+=genoFirst[indexFirst]
                indexFirst+=1
            }
            else{
                genoKid+=genoSecond[indexSecond]
                indexSecond+=1
            }
        }
        genoKid += if(indexFirst<genoFirst.length){
            genoFirst.substring(indexFirst,genoFirst.length)
        } else{
            genoSecond.substring(indexSecond,genoSecond.length)
        }
        val kid=Animal(map,this.position,genoKid,(this.energy+secondAnimal.energy)/4,this,secondAnimal)
        if(this.isObservable() || secondAnimal.isObservable())kid.selectObserver=true
        map.place(kid)
        this.energy=this.energy*3/4
        secondAnimal.energy=secondAnimal.energy*3/4
        return kid
    }
}