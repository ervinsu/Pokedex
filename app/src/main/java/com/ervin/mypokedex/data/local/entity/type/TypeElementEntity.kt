package com.ervin.mypokedex.data.local.entity.type

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ervin.mypokedex.utils.TableTypeElement


@Entity
//    (indices = [Index("typeId")])
     (tableName = TableTypeElement.TypeTable,
    indices = [Index(TableTypeElement.TypeID)])
data class TypeElementEntity (

    @ColumnInfo(name = TableTypeElement.TypeID)
    @PrimaryKey
    val typeId:Int,

    @ColumnInfo(name = TableTypeElement.TypeName)
    val typeName:String,

    @ColumnInfo(name = TableTypeElement.TypeColor)
    val typeColor:String,

    @ColumnInfo(name = TableTypeElement.TypeUrl)
    var typeUrl:String
)