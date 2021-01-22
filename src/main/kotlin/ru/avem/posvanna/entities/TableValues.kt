package ru.avem.posvanna.entities

import javafx.beans.property.StringProperty

data class TableValuesTest0(
        var descriptor: StringProperty,
        var descriptor2: StringProperty,
        var descriptor3: StringProperty
)

data class TableValuesTest1(
        var descriptor: StringProperty,
        var section1t: StringProperty
)

data class TableValuesTest2(
        var descriptor: StringProperty,
        var section21t: StringProperty
)

data class TableValuesTest3(
        var descriptor: StringProperty,
        var section31t: StringProperty
)

data class TableValuesTest4(
        var descriptor: StringProperty,
        var start: StringProperty,
        var pause: StringProperty
)