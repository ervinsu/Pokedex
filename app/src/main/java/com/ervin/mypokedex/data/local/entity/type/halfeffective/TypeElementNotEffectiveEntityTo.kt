package com.ervin.mypokedex.data.local.entity.type.halfeffective

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementNotEffectiveTo


@Entity(
    tableName = TableTypeElementNotEffectiveTo.TypeNotEffectiveTableTo,
    primaryKeys = [TableTypeElementNotEffectiveTo.TypeID,
        TableTypeElementNotEffectiveTo.TypeIDNotEffectiveTo],
    indices = [Index(
        TableTypeElementNotEffectiveTo.TypeIDNotEffectiveTo,
        TableTypeElementNotEffectiveTo.TypeID
    )]
)
data class TypeElementNotEffectiveEntityTo(
    @ColumnInfo(name = TableTypeElementNotEffectiveTo.TypeID)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementNotEffectiveTo.TypeIDNotEffectiveTo)
    val ck_typeNotEffectiveId: Int
)