package com.coder.ffmpeg.model

data class CodecInfo(val id:Int, val name:String, val type:Int){
    override fun toString(): String {
        return "id = $id, name = $name, type = $type"
    }
}