package com.ervin.mypokedex.data.local.entity.type.effective

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.ervin.mypokedex.utils.TableTypeElementSuperEffectiveFrom

@Entity(
    tableName = TableTypeElementSuperEffectiveFrom.TypeSuperEffectiveTableFrom,
    primaryKeys = [TableTypeElementSuperEffectiveFrom.TypeIDSuperEffectiveFrom,
        TableTypeElementSuperEffectiveFrom.TypeID],
    indices = [Index(
        TableTypeElementSuperEffectiveFrom.TypeIDSuperEffectiveFrom,
        TableTypeElementSuperEffectiveFrom.TypeID
    )]
)
data class TypeElementSuperEffectiveEntityFrom(
    @ColumnInfo(name = TableTypeElementSuperEffectiveFrom.TypeID, index = true)
    val ck_typeId: Int,
    @ColumnInfo(name = TableTypeElementSuperEffectiveFrom.TypeIDSuperEffectiveFrom, index = true)
    val ck_typeEffectiveId: Int
)