package com.coder.ffmpeg.utils

open class CommandParams{

    private var data: MutableList<String?> = mutableListOf()
    init {
        data.add("ffmpeg")
        data.add("-y")
    }

    /**
     * 添加命令参数
     */
    fun append(param:String?):CommandParams{
        data.add(param)
        return this
    }
    /**
     * 添加命令参数
     */
    fun append(param:Int?):CommandParams{
        data.add(param.toString())
        return this
    }
    /**
     * 添加命令参数
     */
    fun append(param:Long?):CommandParams{
        data.add(param.toString())
        return this
    }

    /**
     * 移除某个命令
     */
    fun remove(param: String?):CommandParams{
        for (index in (data.size-1) downTo 0){
            if (param == data[index]){
                data.removeAt(index);
            }
        }
        return this
    }

    /**
     * 情况参数
     */
    fun clear():CommandParams{
        data.clear()
        return this;
    }

    fun get():Array<String?>{
        return data.toTypedArray()
    }
}