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
package com.b3dgs.lionengine.game.object;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.core.Medias;
import com.b3dgs.lionengine.game.object.trait.mirrorable.Mirrorable;
import com.b3dgs.lionengine.game.object.trait.mirrorable.MirrorableModel;
import com.b3dgs.lionengine.game.object.trait.transformable.Transformable;

/**
 * Test the handled objects.
 */
public class HandledObjectsImplTest
{
    /** Test configuration. */
    private static Media config;

    /**
     * Prepare test.
     */
    @BeforeClass
    public static void setUp()
    {
        Medias.setResourcesDirectory(System.getProperty("java.io.tmpdir"));
        config = UtilSetup.createConfig();
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        Assert.assertTrue(config.getFile().delete());
        Medias.setResourcesDirectory(Constant.EMPTY_STRING);
    }

    /** Handled objects test. */
    private final HandledObjectsImpl handled = new HandledObjectsImpl();
    /** Object test. */
    private final ObjectGame object = new ObjectGame(new Setup(config), new Services());

    /**
     * Clean test.
     */
    @After
    public void after()
    {
        object.freeId();
    }

    /**
     * Test ID manipulation.
     */
    @Test
    public void testId()
    {
        handled.add(object);

        Assert.assertEquals(object, handled.get(object.getId()));
        Assert.assertEquals(object, handled.values().iterator().next());
        Assert.assertEquals(1, handled.getIds().size());

        handled.remove(object.getId());

        Assert.assertTrue(handled.getIds().isEmpty());
        Assert.assertFalse(handled.values().iterator().hasNext());
        try
        {
            Assert.assertNull(handled.get(object.getId()));
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }
    }

    /**
     * Test trait manipulation.
     */
    @Test
    public void testTrait()
    {
        final Mirrorable mirrorable = new MirrorableModel();
        object.addTrait(mirrorable);
        object.prepareTraits(new Services());
        handled.add(object);

        Assert.assertEquals(mirrorable, handled.get(Mirrorable.class).iterator().next());
        Assert.assertFalse(handled.get(Transformable.class).iterator().hasNext());

        handled.remove(object.getId());

        Assert.assertFalse(handled.get(Mirrorable.class).iterator().hasNext());
    }

    /**
     * Test trait manipulation.
     */
    @Test
    public void testType()
    {
        final Mirrorable mirrorable = new MirrorableModel();
        object.addType(mirrorable);
        handled.add(object);

        Assert.assertEquals(mirrorable, handled.get(Mirrorable.class).iterator().next());

        handled.remove(object.getId());

        Assert.assertFalse(handled.get(Mirrorable.class).iterator().hasNext());
    }
}
