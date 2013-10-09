/*
 * Copyright (C) 2013 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.example.warcraft.weapon;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.example.warcraft.Context;
import com.b3dgs.lionengine.example.warcraft.ResourcesLoader;
import com.b3dgs.lionengine.example.warcraft.entity.Attacker;
import com.b3dgs.lionengine.example.warcraft.entity.Entity;
import com.b3dgs.lionengine.game.SetupGame;
import com.b3dgs.lionengine.game.rts.ability.attacker.FactoryWeaponRts;

/**
 * Weapons factory.
 */
public final class FactoryWeapon
        extends FactoryWeaponRts<WeaponType, Entity, Weapon, Attacker>
{
    /** Context reference. */
    private Context context;

    /**
     * Constructor.
     */
    public FactoryWeapon()
    {
        super(WeaponType.class);
        loadAll(WeaponType.values());
    }

    /**
     * Set the context.
     * 
     * @param context The context
     */
    public void setContext(Context context)
    {
        this.context = context;
    }

    /*
     * FactoryWeaponRts
     */

    @Override
    public Weapon createWeapon(WeaponType id, Attacker user)
    {
        switch (id)
        {
            case axe:
                return new Axe(user, context);
            case sword:
                return new Sword(user, context);
            case spear:
                return new Spear(user, context);
            case bow:
                return new Bow(user, context);
            default:
                throw new LionEngineException("Weapon not found: " + id.name());
        }
    }

    @Override
    protected SetupGame createSetup(WeaponType id)
    {
        return new SetupGame(Media.get(ResourcesLoader.WEAPONS_DIR, id + ".xml"));
    }
}
