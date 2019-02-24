package com.laton95.pyramidplunder.client.gui;

import com.laton95.pyramidplunder.inventory.ContainerUrn;
import com.laton95.pyramidplunder.reference.ModReference;
import com.laton95.pyramidplunder.util.LogHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiUrn extends GuiContainer
{
	
	private final ResourceLocation guiTexture = new ResourceLocation(ModReference.MOD_ID, "textures/gui/urn.png");
	
	private final IInventory playerInventory;
	
	private final IInventory urnInventory;
	
	public GuiUrn(InventoryPlayer playerInventory, Container container)
	{
		super(container);
		this.playerInventory = playerInventory;
		this.urnInventory = ((ContainerUrn) container).getUrnInventory();
		allowUserInput = false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRenderer.drawString(urnInventory.getDisplayName().getUnformattedText(), 8, 23, 4210752);
		fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 96, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTexture);
		int i = (width - xSize) / 2;
		int j = (height - ySize) / 2;
		this.drawTexturedModalRect(i, j + 15, 0, 0, xSize, ySize);
	}
}
