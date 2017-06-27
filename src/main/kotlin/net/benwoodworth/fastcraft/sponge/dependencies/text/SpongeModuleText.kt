package net.benwoodworth.fastcraft.sponge.dependencies.text

import dagger.Module
import dagger.Provides
import net.benwoodworth.fastcraft.core.dependencies.text.ModuleText
import net.benwoodworth.fastcraft.core.dependencies.text.TextBuilder
import net.benwoodworth.fastcraft.core.dependencies.text.TextColorRegistry
import javax.inject.Singleton
import org.spongepowered.api.text.Text as SpongeText

/**
 * Sponge implementation of [ModuleText].
 */
@Module
class SpongeModuleText : ModuleText {

    @Provides
    override fun textBuilder(): TextBuilder {
        return SpongeTextBuilderAdapter(SpongeText.builder(""))
    }

    @Provides @Singleton
    override fun textColorRegistry(): TextColorRegistry {
        return SpongeTextColorRegistry()
    }
}
