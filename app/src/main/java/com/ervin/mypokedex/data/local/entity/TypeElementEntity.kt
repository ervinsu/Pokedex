package com.ervin.mypokedex.data.local.entity

import androidx.room.*
import com.ervin.mypokedex.utils.TablePokemonTypeElement
import com.ervin.mypokedex.utils.TableTypeElement


@Entity
//    (indices = [Index("typeId")])
     (tableName = TableTypeElement.TypeTable)
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