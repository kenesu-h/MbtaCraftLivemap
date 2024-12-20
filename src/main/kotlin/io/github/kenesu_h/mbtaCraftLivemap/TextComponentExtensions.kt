package io.github.kenesu_h.mbtaCraftLivemap

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

fun TextComponent.Builder.text(text: String): TextComponent.Builder {
    return this.append(Component.text(text))
}

fun TextComponent.Builder.decoratedText(text: String, decoration: TextDecoration): TextComponent.Builder {
    return this.append(Component.text(text).decorate(decoration))
}

fun TextComponent.Builder.coloredText(text: String, color: NamedTextColor): TextComponent.Builder {
    return this.append(Component.text(text).color(color))
}

fun TextComponent.Builder.subtext(text: String): TextComponent.Builder {
    return this.append(Component.text(text).color(NamedTextColor.GRAY))
}

fun TextComponent.Builder.decoratedSubtext(text: String, decoration: TextDecoration): TextComponent.Builder {
    return this.append(Component.text(text).color(NamedTextColor.GRAY).decorate(decoration))
}

fun TextComponent.Builder.newline(): TextComponent.Builder {
    return this.appendNewline()
}