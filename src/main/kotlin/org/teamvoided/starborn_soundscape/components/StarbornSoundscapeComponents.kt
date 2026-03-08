package org.teamvoided.starborn_soundscape.components

import com.mojang.serialization.Codec

data class OverarchieverData(val charge: Int) : SimpleStorageComponent {

    // this part fixes the constant ticking caused by this data
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is OverarchieverData) super.equals(other)
        else true
    }
    // this is the end of it

    companion object {
        val DEFAULT: OverarchieverData = OverarchieverData(0)
        val CODEC = Codec.INT.xmap(
            { int -> OverarchieverData(int) },
            { component -> component.charge }
        )
    }

}

data class OverarchieverDatav2(val passivelyDraining: Boolean) : SimpleStorageComponent {

    // this part fixes the constant ticking caused by this data
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is OverarchieverDatav2) super.equals(other)
        else true
    }
    // this is the end of it

    companion object {
        val DEFAULT: OverarchieverDatav2 = OverarchieverDatav2(false)
        val CODEC = Codec.BOOL.xmap(
            { bool -> OverarchieverDatav2(bool) },
            { component -> component.passivelyDraining }
        )
    }
}

data class CurrentUseTime(val useTime: Int) : SimpleStorageComponent {

    // this part fixes the constant ticking caused by this data
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is CurrentUseTime) super.equals(other)
        else true
    }
    // this is the end of it

    companion object {
        val DEFAULT: CurrentUseTime = CurrentUseTime(0)
        val CODEC = Codec.INT.xmap(
            { int -> CurrentUseTime(int) },
            { component -> component.useTime }
        )
    }
}