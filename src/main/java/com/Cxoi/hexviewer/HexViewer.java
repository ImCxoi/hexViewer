package com.Cxoi.hexviewer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;



public class HexViewer implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            String hex = extractHexFromItem(stack);

            if (hex == null) return;

            try {
                int color = Integer.parseInt(hex, 16);

                lines.add(
                    Text.literal("Hex: #" + hex)
                        .styled(style -> style.withColor(color))
                );

            } catch (NumberFormatException e) {
                lines.add(
                    Text.literal("Hex: error")
                        .formatted(net.minecraft.util.Formatting.RED)
                );
            }
        });
    }


    private static String extractHexFromItem(ItemStack stack) {

        // Vanilla dyed color component
        var dyedColor = stack.get(DataComponentTypes.DYED_COLOR);
        if (dyedColor != null) {
            int rgb = dyedColor.rgb();
            return String.format("%02X%02X%02X",
                    (rgb >> 16) & 0xFF,
                    (rgb >> 8) & 0xFF,
                    rgb & 0xFF);
        }

        // Custom NBT via Optional API
        var nbtComp = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComp != null) {
            NbtCompound nbt = nbtComp.copyNbt();

            return nbt.getCompound("display")
                .flatMap(display -> display.getInt("color"))
                .map(rgb -> String.format("%02X%02X%02X",
                        (rgb >> 16) & 0xFF,
                        (rgb >> 8) & 0xFF,
                        rgb & 0xFF))
                .orElse(null);
        }

        return null;
    }


}
