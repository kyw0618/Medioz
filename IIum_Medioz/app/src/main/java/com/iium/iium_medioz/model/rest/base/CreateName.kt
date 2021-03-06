package com.iium.iium_medioz.model.rest.base

data class CreateName(
    val result : List<DataList>? =null
)

data class DataList(
    val DataList : List<DataListSecond>? = null,
    val title : String? = null,
    val keyword  : String? = null,
    val timestamp : String? =null,
    val defaultcode : String? = null,
    val sensitivity : String? = null,
    val sendcode : String? = null,
    val userId : String? =null,
    val id : String? =null
)

data class DataListSecond(
    val textImg : List<TextList>,
    val Img : List<NormalList>,
    val video : List<VideoList>
)

data class TextList(
    var fieldname: String? = null,
    var originalname: String? = null,
    var encoding: String? = null,
    var mimetype: String? = null,
    var destination: String? = null,
    var filename: String? = null,
    var path: String? = null,
    var size: Int? = null
)


data class NormalList(
    var fieldname: String? = null,
    var originalname: String? = null,
    var encoding: String? = null,
    var mimetype: String? = null,
    var destination: String? = null,
    var filename: String? = null,
    var path: String? = null,
    var size: Int? = null
)

data class VideoList(
    var fieldname: String? = null,
    var originalname: String? = null,
    var encoding: String? = null,
    var mimetype: String? = null,
    var destination: String? = null,
    var filename: String? = null,
    var path: String? = null,
    var size: Int? = null
)