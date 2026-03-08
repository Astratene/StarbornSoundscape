@file:Suppress("PropertyName", "ClassName", "HasPlatformType", "unused")

package org.teamvoided.starborn_soundscape.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

interface SimpleStorageComponent

//   _______                              __
//  |    ___|.--.--.---.-.--------.-----.|  |.-----.-----.
//  |    ___||_   _|  _  |        |  _  ||  ||  -__|__ --|
//  |_______||__.__|___._|__|__|__|   __||__||_____|_____|
//                                |__|

data class ONE_VALUE(val VALUE: Int) : SimpleStorageComponent {
    companion object {
        val DEFAULT: ONE_VALUE = ONE_VALUE(0)
        val CODEC = Codec.INT.xmap({ int -> ONE_VALUE(int) }, { component -> component.VALUE })
    }
}

// For Multi Value Components you can always add more values to the end,
// just look at commented out boolean for how to do it.
// Be careful to not miss the commas!
data class MULTI_VALUE(val INT_VALUE: Int, val STRING_VALUE: String/*, val BOOL_VALUE: Boolean*/) :
    SimpleStorageComponent {
    companion object {
        val DEFAULT: MULTI_VALUE = MULTI_VALUE(0, ""/*, false*/)
        val CODEC = RecordCodecBuilder.create<MULTI_VALUE> { builder ->
            builder.group(
                Codec.INT.fieldOf("INT_VALUE").forGetter { it.INT_VALUE },
                Codec.STRING.fieldOf("STRING_VALUE").forGetter { it.STRING_VALUE }
//                ,
//                Codec.BOOL.fieldOf("BOOL_VALUE").forGetter { it.BOOL_VALUE }
            ).apply(builder, ::MULTI_VALUE)
        }

    }
}

// Examples on getting and setting data values


// This will 'getOrDefault' the data value, which means it will either take the current value
// stored in the data component, or will 'default' it, taking the default value. for example,
// the 'ONE_VALUE' component in this class will default to 0.
// val data = stack.getOrDefault(StarbornSoundscapeDataComponents.DATA_COMPONENT, DataComponent.DEFAULT)
// When you want to get a specific value out of the data component, you should do this:
// val dataComponent = data.dataComponent

// This will set the data values. It is important to note that it will set EVERY value for multi
// components.
// stack.set(StarbornSoundscapeDataComponents.DATA_COMPONENT, DataComponent(value))
// stack.set(StarbornSoundscapeDataComponents.MULTI_DATA_COMPONENT, DataComponent(value1, value2))

// Example code: adding +1 to one value
// val data = stack.getOrDefault(StarbornSoundscapeDataComponents.MULTI_DATA_COMPONENT, MultiDataComponent.DEFAULT)
// val value1 = data.value1 + 1
// stack.set(StarbornSoundscapeDataComponents.MULTI_DATA_COMPONENT, DataComponent(value1, data.value2))

// This example code will keep value2 the same, while modifying value1. It is important to note however, if this
// value changes while another thread is actively changing it(processing parallel), the value could change in unexpected ways.