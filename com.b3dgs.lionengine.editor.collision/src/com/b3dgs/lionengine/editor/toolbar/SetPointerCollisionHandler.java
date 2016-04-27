/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.editor.toolbar;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.editor.utility.UtilToolbar;
import com.b3dgs.lionengine.editor.utility.dialog.UtilDialog;
import com.b3dgs.lionengine.editor.world.PaletteModel;
import com.b3dgs.lionengine.editor.world.PaletteType;
import com.b3dgs.lionengine.editor.world.WorldModel;
import com.b3dgs.lionengine.editor.world.view.WorldPart;
import com.b3dgs.lionengine.game.collision.tile.MapTileCollision;
import com.b3dgs.lionengine.game.object.Services;

/**
 * Set pointer collision handler.
 */
public final class SetPointerCollisionHandler
{
    /** Element ID. */
    public static final String ID = PaletteModel.ID_PREFIX + "pointer-collision";

    /**
     * Update the tool bar.
     * 
     * @param shell The shell parent.
     * @param toolBar The tool bar.
     */
    private static void updateToolbar(Shell shell, MToolBar toolBar)
    {
        if (WorldModel.INSTANCE.getMap().hasFeature(MapTileCollision.class))
        {
            UtilToolbar.setToolItemSelectionPrefix(toolBar, false, PaletteModel.ID_PREFIX);
            UtilToolbar.setToolItemSelection(toolBar, true, ID);

            final PaletteType type = PaletteType.POINTER_COLLISION;
            final Services services = WorldModel.INSTANCE.getServices();
            services.get(PaletteModel.class).setSelectedPalette(type);

            final WorldPart view = services.get(WorldPart.class);
            view.setCursor(type.getCursor());
        }
        else
        {
            UtilToolbar.setToolItemSelection(toolBar, false, ID);
            UtilDialog.error(shell, Messages.ErrorTitle, Messages.ErrorMessage);
        }
    }

    /**
     * Create handler.
     */
    public SetPointerCollisionHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     * 
     * @param shell The shell parent.
     * @param partService The part service reference.
     */
    @Execute
    public void execute(Shell shell, EPartService partService)
    {
        final MPart part = partService.findPart(WorldPart.ID);
        if (part != null)
        {
            final MToolBar toolBar = part.getToolbar();
            if (toolBar != null)
            {
                updateToolbar(shell, toolBar);
            }
        }
    }
}