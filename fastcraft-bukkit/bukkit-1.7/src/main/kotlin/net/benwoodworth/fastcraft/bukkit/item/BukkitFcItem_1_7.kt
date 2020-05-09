package net.benwoodworth.fastcraft.bukkit.item

import net.benwoodworth.fastcraft.platform.item.FcItem
import net.benwoodworth.fastcraft.platform.item.FcItemType
import net.benwoodworth.fastcraft.platform.text.FcText
import org.bukkit.inventory.ItemStack
import javax.inject.Inject
import javax.inject.Singleton

open class BukkitFcItem_1_7(
    override val itemStack: ItemStack,
    protected val itemTypes: FcItemType.Factory,
    protected val textFactory: FcText.Factory,
) : BukkitFcItem {
    override val type: FcItemType
        get() = itemTypes.fromMaterialData(itemStack.data)

    override val amount: Int
        get() = itemStack.amount

    override val name: FcText
        get() = itemStack
            .takeIf { it.hasItemMeta() }
            ?.itemMeta
            ?.takeIf { it.hasDisplayName() }
            ?.displayName
            ?.let { textFactory.createFcText(it) }
            ?: type.blockName

    override val lore: List<FcText>
        get() = itemStack
            .takeIf { it.hasItemMeta() }
            ?.itemMeta
            ?.takeIf { it.hasLore() }
            ?.lore
            ?.map { textFactory.createFcText(it ?: "") }
            ?: emptyList()

    override val hasMetadata: Boolean
        get() = itemStack.hasItemMeta()

    override fun toItemStack(): ItemStack {
        return itemStack.clone()
    }

    override fun equals(other: Any?): Boolean {
        return other is FcItem && itemStack == other.itemStack
    }

    override fun hashCode(): Int {
        return itemStack.hashCode()
    }

    @Singleton
    open class Factory @Inject constructor(
        protected val itemTypes: FcItemType.Factory,
        protected val textFactory: FcText.Factory,
    ) : BukkitFcItem.Factory {
        override fun copyItem(item: FcItem, amount: Int): FcItem {
            try {
                if (amount == item.amount) {
                    return item
                }

                val itemStack = item.toItemStack()
                itemStack.amount = amount

                return createFcItem(itemStack)
            } catch (e: AssertionError) {
                System.err.println("""
                    Share this with Kepler:
                    - item = ${item.itemStack}
                    - amount = $amount
                """.trimIndent())
                throw e
            }
        }

        override fun createFcItem(itemStack: ItemStack): FcItem {
            return BukkitFcItem_1_7(
                itemStack = itemStack,
                itemTypes = itemTypes,
                textFactory = textFactory
            )
        }
    }
}
