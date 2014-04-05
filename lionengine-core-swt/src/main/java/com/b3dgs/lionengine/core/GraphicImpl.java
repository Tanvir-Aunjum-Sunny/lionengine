/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import com.b3dgs.lionengine.ColorRgba;
import com.b3dgs.lionengine.GradientColor;
import com.b3dgs.lionengine.Graphic;

/**
 * Main interface with the graphic output, representing the screen buffer.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
final class GraphicImpl
        implements Graphic
{
    /**
     * Get the image buffer.
     * 
     * @param imageBuffer The image buffer.
     * @return The buffer.
     */
    private static Image getBuffer(ImageBuffer imageBuffer)
    {
        return ((ImageBufferImpl) imageBuffer).getBuffer();
    }

    /** The graphic output. */
    private GC gc;
    /** Gradient paint. */
    private Color gradientColor1;
    /** Gradient paint. */
    private Color gradientColor2;
    /** Last transform. */
    private Transform lastTransform;
    /** Last color. */
    private Color lastColor;
    /** Last swt transform. */
    private org.eclipse.swt.graphics.Transform lastSwtTransform;

    /**
     * Constructor.
     */
    GraphicImpl()
    {
        // Nothing to do
    }

    /**
     * Constructor.
     * 
     * @param g The graphics output.
     */
    GraphicImpl(GC g)
    {
        gc = g;
    }

    /*
     * Graphic
     */

    @Override
    public void clear(int x, int y, int width, int height)
    {
        gc.setBackground(ScreenImpl.display.getSystemColor(SWT.COLOR_BLACK));
        gc.fillRectangle(0, 0, width, height);
    }

    @Override
    public void dispose()
    {
        gc.dispose();
    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy)
    {
        gc.copyArea(x, y, width, height, dx, dy);
    }

    @Override
    public void drawImage(ImageBuffer image, int x, int y)
    {
        gc.drawImage(GraphicImpl.getBuffer(image), x, y);
    }

    @Override
    public void drawImage(ImageBuffer image, Transform transform, int x, int y)
    {
        if (lastTransform != transform)
        {
            lastTransform = transform;
            if (lastSwtTransform != null)
            {
                lastSwtTransform.dispose();
            }
            lastSwtTransform = new org.eclipse.swt.graphics.Transform(GraphicImpl.getBuffer(image).getDevice());
            lastSwtTransform.scale((float) transform.getScaleX(), (float) transform.getScaleY());
            gc.setTransform(lastSwtTransform);
        }
        gc.drawImage(GraphicImpl.getBuffer(image), x, y);
    }

    @Override
    public void drawImage(ImageBuffer image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2)
    {
        if (sx2 < sx1)
        {
            final org.eclipse.swt.graphics.Transform old = new org.eclipse.swt.graphics.Transform(ScreenImpl.display);
            gc.getTransform(old);

            final org.eclipse.swt.graphics.Transform transform = new org.eclipse.swt.graphics.Transform(
                    ScreenImpl.display);
            transform.setElements(1, 0, 0, -1, 0, 0);
            gc.setTransform(transform);
            gc.drawImage(GraphicImpl.getBuffer(image), dx1, dy1);
            transform.dispose();
            gc.setTransform(old);
        }
        else
        {
            gc.drawImage(GraphicImpl.getBuffer(image), sx1, sy1, sx2 - sx1, sy2 - sy1, dx1, dy1, dx2 - dx1, dy2 - dy1);
        }
    }

    @Override
    public void drawRect(int x, int y, int width, int height, boolean fill)
    {
        if (fill)
        {
            gc.drawRectangle(x, y, width, height);
        }
        else
        {
            gc.drawRectangle(x, y, width, height);
        }
    }

    @Override
    public void drawGradient(int x, int y, int width, int height)
    {
        gc.setBackground(gradientColor1);
        gc.setForeground(gradientColor2);
        gc.fillGradientRectangle(x, y, width, height, false);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2)
    {
        gc.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawOval(int x, int y, int width, int height, boolean fill)
    {
        if (fill)
        {
            gc.fillOval(x, y, width, height);
        }
        else
        {
            gc.drawOval(x, y, width, height);
        }
    }

    @Override
    public void setColor(ColorRgba color)
    {
        if (lastColor != null)
        {
            lastColor.dispose();
        }
        lastColor = new Color(gc.getDevice(), color.getRed(), color.getGreen(), color.getBlue());
        gc.setForeground(lastColor);
    }

    @Override
    public void setColorGradient(GradientColor gc)
    {
        final ColorRgba color1 = gc.getColor1();
        final ColorRgba color2 = gc.getColor2();
        if (gradientColor1 != null)
        {
            gradientColor1.dispose();
        }
        if (gradientColor2 != null)
        {
            gradientColor1.dispose();
        }
        gradientColor1 = new Color(this.gc.getDevice(), color1.getRed(), color1.getGreen(), color1.getBlue());
        gradientColor2 = new Color(this.gc.getDevice(), color2.getRed(), color2.getGreen(), color2.getBlue());
    }

    @Override
    public <G> void setGraphic(G graphic)
    {
        if (gc != null)
        {
            gc.dispose();
        }
        gc = (GC) graphic;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <G> G getGraphic()
    {
        return (G) gc;
    }

    @Override
    public ColorRgba getColor()
    {
        final Color color = gc.getForeground();
        return new ColorRgba(color.getRed(), color.getGreen(), color.getBlue());
    }
}