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