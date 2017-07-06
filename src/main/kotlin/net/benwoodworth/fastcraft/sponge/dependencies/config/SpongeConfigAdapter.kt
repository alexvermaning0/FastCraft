package net.benwoodworth.fastcraft.sponge.dependencies.config

import net.benwoodworth.fastcraft.core.dependencies.config.Config
import net.benwoodworth.fastcraft.core.dependencies.config.ConfigSection
import net.benwoodworth.fastcraft.core.util.Adapter
import ninja.leaping.configurate.ConfigurationOptions
import ninja.leaping.configurate.commented.CommentedConfigurationNode

/**
 * Sponge implementation of [Config].
 */
class SpongeConfigAdapter(baseNode: CommentedConfigurationNode) :
        ConfigSection by SpongeConfigSectionAdapter(baseNode),
        Adapter<CommentedConfigurationNode>(baseNode),
        Config {

    var configOptions = base.options
        private set

    override var header: List<String>
        get() {
            val lines = configOptions.header?.split('\n') ?: emptyList()

            return lines.map {
                if (it.isNotEmpty() && it[0] == ' ') {
                    it.substring(1)
                } else {
                    it
                }
            }
        }
        set(value) {
            configOptions = configOptions.setHeader(if (value.isEmpty()) {
                null
            } else {
                value.map { " $it" }.joinToString("\n")
            })
        }
}
