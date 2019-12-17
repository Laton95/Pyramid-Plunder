package com.laton95.pyramidplunder.client.gui.screen.inventory;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.inventory.container.UrnContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class UrnScreen extends ContainerScreen<UrnContainer> {
	
	private final ResourceLocation guiTexture = new ResourceLocation(PyramidPlunder.MOD_ID, "textures/gui/container/urn.png");
	
	public UrnScreen(UrnContainer container, PlayerInventory playerInventory, ITextComponent name) {
		super(container, playerInventory, name);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString(this.title.getFormattedText(), 8.0F, 23.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96), 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(guiTexture);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		this.blit(i, j + 15, 0, 0, xSize, ySize);
	}
}
