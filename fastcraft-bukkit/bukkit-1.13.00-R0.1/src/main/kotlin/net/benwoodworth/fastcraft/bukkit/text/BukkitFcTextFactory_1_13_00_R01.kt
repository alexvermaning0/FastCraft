package net.benwoodworth.fastcraft.bukkit.text

import net.benwoodworth.fastcraft.platform.text.FcLegacyText
import net.benwoodworth.fastcraft.platform.text.FcText
import net.benwoodworth.fastcraft.platform.text.FcTextColor
import net.benwoodworth.fastcraft.platform.text.FcTranslatable
import javax.inject.Inject

class BukkitFcTextFactory_1_13_00_R01 @Inject constructor(
) : BukkitFcTextFactory {

    override fun createFcText(
        text: String,
        color: FcTextColor?,
        bold: Boolean?,
        italic: Boolean?,
        underline: Boolean?,
        strikethrough: Boolean?,
        obfuscate: Boolean?,
        extra: List<FcText>?
    ): FcText {
        return BukkitFcText_1_13_00_R01(
            text = text,
            translate = null,
            color = color,
            bold = bold,
            italic = italic,
            underline = underline,
            strikethrough = strikethrough,
            obfuscate = obfuscate,
            extra = extra
        )
    }

    override fun createFcText(
        translate: FcTranslatable,
        color: FcTextColor?,
        bold: Boolean?,
        italic: Boolean?,
        underline: Boolean?,
        strikethrough: Boolean?,
        obfuscate: Boolean?,
        extra: List<FcText>?
    ): FcText {
        return BukkitFcText_1_13_00_R01(
            text = null,
            translate = translate,
            color = color,
            bold = bold,
            italic = italic,
            underline = underline,
            strikethrough = strikethrough,
            obfuscate = obfuscate,
            extra = extra
        )
    }

    override fun createFcText(legacyText: FcLegacyText): FcText {
        return createFcText(legacyText)
    }
}
