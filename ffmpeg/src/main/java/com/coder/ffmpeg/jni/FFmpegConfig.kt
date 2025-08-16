package com.coder.ffmpeg.jni

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Collections
import java.util.concurrent.atomic.AtomicReference


class FFmpegConfig {

    companion object {

        init {
            System.loadLibrary("ffmpeg-org")
            System.loadLibrary("ffmpeg-command")
        }

        /**
         * Whether to enable debugging mode
         * @param debug true or false
         */
        external fun setDebug(debug: Boolean)

        /**
         * Set the env of native
         * @param name env name
         * @param value env value
         */
        private external fun setNativeEnv(name: String, value: String)
        /**
         * Set font config dir for fontconfig
         * Note：It's a config dir not font dir
         * @param configPath the font config dir
         */
        fun setFontConfigPath(configPath: String) {
            setNativeEnv("FONTCONFIG_PATH", configPath)
        }

        /**
         * Set font config file for fontconfig
         * Note：It's a config file not font file
         * @param configFile the font config file
         */
        fun setFontConfigFile(configFile: String) {
            setNativeEnv("FONTCONFIG_FILE", configFile)
        }

        /**
         * Set font dir for fontconfig
         * @param context context for application
         * @param fontDir the font dir contain fonts (.ttf and .otf files)
         * @param fontNameMapping
         */
        fun setFontDir(context: Context, fontDir:String, fontNameMapping: Map<String, String>){
            setFontDirList(context, Collections.singletonList(fontDir),fontNameMapping)
        }
        /**
         * Set font dir for fontconfig
         * @param context context for application
         * @param fontDirList list of directories that contain fonts (.ttf and .otf files)
         */
        fun setFontDirList(context: Context, fontDirList: List<String>, fontNameMapping: Map<String, String>) {
            var validFontNameMappingCount = 0
            val cacheDir = context.cacheDir
            val fontConfigDir = File(cacheDir, "fontconfig")
            if (!fontConfigDir.exists()) {
                fontConfigDir.mkdirs()
            }
            // Create font config
            val fontConfigFile = File(fontConfigDir, "fonts.conf")
            if (fontConfigFile.exists() && fontConfigFile.isFile) {
                fontConfigFile.delete()
            }
            fontConfigFile.createNewFile()
            val fontNameMappingBlock = StringBuilder()
            if (fontNameMapping.isNotEmpty()){
                for (entry in fontNameMapping.entries) {
                    val fontName: String = entry.key
                    val mappedFontName: String = entry.value

                    if ((fontName.trim().isNotEmpty()) && (mappedFontName.trim().isNotEmpty())) {
                        fontNameMappingBlock.append("    <match target=\"pattern\">\n");
                        fontNameMappingBlock.append("        <test qual=\"any\" name=\"family\">\n");
                        fontNameMappingBlock.append(String.format("            <string>%s</string>\n", fontName));
                        fontNameMappingBlock.append("        </test>\n");
                        fontNameMappingBlock.append("        <edit name=\"family\" mode=\"assign\" binding=\"same\">\n");
                        fontNameMappingBlock.append(String.format("            <string>%s</string>\n", mappedFontName));
                        fontNameMappingBlock.append("        </edit>\n");
                        fontNameMappingBlock.append("    </match>\n");

                        validFontNameMappingCount++
                    }
                }
            }
            val fontConfigBuilder = StringBuilder()
            fontConfigBuilder.append("<?xml version=\"1.0\"?>\n")
            fontConfigBuilder.append("<!DOCTYPE fontconfig SYSTEM \"fonts.dtd\">\n")
            fontConfigBuilder.append("<fontconfig>\n")
            fontConfigBuilder.append("    <dir prefix=\"cwd\">.</dir>\n")
            for (fontDirectoryPath in fontDirList) {
                fontConfigBuilder.append("    <dir>")
                fontConfigBuilder.append(fontDirectoryPath)
                fontConfigBuilder.append("</dir>\n")
            }
            fontConfigBuilder.append(fontNameMappingBlock)
            fontConfigBuilder.append("</fontconfig>\n")
            val reference = AtomicReference<FileOutputStream>()
            try {
                val outputStream = FileOutputStream(fontConfigFile)
                reference.set(outputStream)

                outputStream.write(fontConfigBuilder.toString().toByteArray())
                outputStream.flush()
                setFontConfigPath(fontConfigDir.absolutePath)
            }catch (e:IOException){
                e.printStackTrace()
            }finally {
                reference.get().close()
            }
        }

    }
}