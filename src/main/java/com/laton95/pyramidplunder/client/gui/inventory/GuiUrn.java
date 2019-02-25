package com.laton95.pyramidplunder.client.gui.inventory;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.inventory.ContainerUrn;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiUrn extends GuiContainer {
	
	public static final ResourceLocation URN_GUI_ID = new ResourceLocation(PyramidPlunder.MOD_ID, "gui_urn");
	
	private final ResourceLocation guiTexture = new ResourceLocation(PyramidPlunder.MOD_ID, "textures/gui/container/urn.png");
	
	private final IInventory playerInventory;
	
	private final IInventory urnInventory;
	
	public GuiUrn(InventoryPlayer playerInventory, IInventory urnInventory) {
		super(new ContainerUrn(playerInventory, urnInventory));
		this.playerInventory = playerInventory;
		this.urnInventory = urnInventory;
		allowUserInput = false;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(urnInventory.getDisplayName().getFormattedText(), 8, 23, 4210752);
		fontRenderer.drawString(playerInventory.getDisplayName().getFormattedText(), 8, ySize - 96, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTexture);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		this.drawTexturedModalRect(i, j + 15, 0, 0, xSize, ySize);
	}
}
