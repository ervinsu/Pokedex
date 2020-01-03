package com.ervin.mypokedex.data.local.entity.type.halfeffective

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementNotEffectiveFrom


@Entity(
    tableName = TableTypeElementNotEffectiveFrom.TypeNotEffectiveTableFrom,
    primaryKeys = [TableTypeElementNotEffectiveFrom.TypeID,
        TableTypeElementNotEffectiveFrom.TypeIDNotEffectiveFrom],
    indices = [Index(
        TableTypeElementNotEffectiveFrom.TypeIDNotEffectiveFrom,
        TableTypeElementNotEffectiveFrom.TypeID
    )]
)
data class TypeElementNotEffectiveEntityFrom (
    @ColumnInfo(name = TableTypeElementNotEffectiveFrom.TypeID)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementNotEffectiveFrom.TypeIDNotEffectiveFrom)
    val ck_typeNoDamageId: Int
)