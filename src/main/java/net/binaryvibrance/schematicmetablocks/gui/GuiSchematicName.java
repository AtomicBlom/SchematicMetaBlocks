package net.binaryvibrance.schematicmetablocks.gui;

import net.binaryvibrance.schematicmetablocks.network.SetSchematicNameMessage;
import net.binaryvibrance.schematicmetablocks.tileentity.RegionTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

import static net.binaryvibrance.schematicmetablocks.TheMod.CHANNEL;

public class GuiSchematicName extends GuiScreen
{
    private final EntityPlayer player;
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final RegionTileEntity regionTileEntity;
    /**
     * Text field containing the command block's command.
     */
    private GuiTextField commandTextField;
    /**
     * "Done" button for the GUI.
     */
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    public GuiSchematicName(EntityPlayer player, World world, int x, int y, int z)
    {

        this.player = player;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        this.regionTileEntity = RegionTileEntity.tryGetTileEntity(world, new BlockPos(x, y, z));

    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Schematic Name", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, I18n.format(".schematic", new Object[0]), this.width / 2 + 100, 57, 10526880);
        this.commandTextField.drawTextBox();

        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char character, int p_73869_2_)
    {
        this.commandTextField.textboxKeyTyped(character, p_73869_2_);
        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;

        if (p_73869_2_ != 28 && p_73869_2_ != 156)
        {
            if (p_73869_2_ == 1)
            {
                this.actionPerformed(this.cancelBtn);
            }
        } else
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        this.commandTextField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 1)
            {
                this.mc.displayGuiScreen(null);
            } else if (button.id == 0)
            {
                final SetSchematicNameMessage message = new SetSchematicNameMessage(
                        this.regionTileEntity.getWorld().provider.getDimension(),
                        regionTileEntity.getPos(),
                        this.commandTextField.getText()
                );
                CHANNEL.sendToServer(message);

                /*/this.regionTileEntity.setSchematicName(this.commandTextField.getText());
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());

                try
                {
                    packetbuffer.writeInt(PacketType.SET_SCHEMATIC_NAME.ordinal());

                    packetbuffer.writeInt(this.regionTileEntity.xCoord);
                    packetbuffer.writeInt(this.regionTileEntity.yCoord);
                    packetbuffer.writeInt(this.regionTileEntity.zCoord);
                    packetbuffer.writeInt(this.regionTileEntity.getWorldObj().getWorldInfo().getVanillaDimension());
                    packetbuffer.writeStringToBuffer(this.commandTextField.getText());


                    CPacketCustomPayload packet = new CPacketCustomPayload(TheMod.RESOURCE_PREFIX + "Network", packetbuffer);
                    this.mc.getNetHandler().addToSendQueue(packet);
                } catch (Exception exception)
                {
                    Logger.severe("Couldn\'t send schematic name info", exception);
                } finally
                {
                    packetbuffer.release();
                }*/

                this.mc.displayGuiScreen(null);
            }
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.commandTextField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 150, 50, 245, 20);
        this.commandTextField.setMaxStringLength(128);
        this.commandTextField.setFocused(true);
        String currentSchematicName = regionTileEntity.getSchematicName();
        if (currentSchematicName != null)
        {
            this.commandTextField.setText(currentSchematicName);
        }

        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.commandTextField.updateCursorCounter();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
}
